package com.dung.UniStore.controller;
import com.dung.UniStore.dto.request.ProductCreationRequest;
import com.dung.UniStore.dto.request.ProductUpdateRequest;
import com.dung.UniStore.dto.response.*;
import com.dung.UniStore.entity.Product;
import com.dung.UniStore.entity.ProductImage;
import com.dung.UniStore.form.ProductFilterForm;
import com.dung.UniStore.mapper.ProductMapper;
import com.dung.UniStore.service.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
@Validated
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    private final  ModelMapper modelMapper;
    private final ProductMapper productMapper;

    @PostMapping()
    public ApiResponse<ProductResponse> createProduct(@Valid @RequestBody ProductCreationRequest request)
    {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.createProduct(request))
                .build();
    }

    @PostMapping(value = "uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<ProductImage>> uploadImages(@PathVariable int id,@ModelAttribute("files")  List<MultipartFile> files)
    {

        try {
            ProductResponse existingProduct = productService.getProductById(id);
            files = files ==null ? new ArrayList<MultipartFile>():files;
            if(files.size()> ProductImage.Maximum_Images_Per_Product)
            {
                return ApiResponse.<List<ProductImage>>builder()
                        .result(null)
                        .message(String.format("Chỉ được upload tối đa %d ảnh", ProductImage.Maximum_Images_Per_Product))
                        .build();
            }
            List<ProductImage> productImages = new ArrayList<>();
            for (MultipartFile file:files) {
                if(file.getSize()==0)
                {
                    continue;
                }
                //kiem tra kich thuoc va dịnh dang file
                if (file.getSize() > 30 * 1024 * 1024) {
                    return ApiResponse.<List<ProductImage>>builder()
                            .result(null)
                            .message("File quá lớn! (Tối đa 30MB)")
                            .build();
                }
                String contentType = file.getContentType();//check dinh dang phải file ảnh k
                if (contentType == null || !contentType.startsWith("image/")) {
                    return ApiResponse.<List<ProductImage>>builder()
                            .result(null)
                            .message("File không phải là hình ảnh hợp lệ")
                            .build();
                }
                //luu file và update thumbnail
                String filename = storeFile(file);
                //luu vao productimage
                ProductImage productImage = productService.createProductImage(
                        existingProduct.getId(),
                        ProductImageResponse.builder()
                                .imageUrl(filename)
                                .build()
                );
                productImages.add(productImage);
            }
            return ApiResponse.<List<ProductImage>>builder()
                    .result(productImages)
                    .message("Tải lên hình ảnh thành công")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<List<ProductImage>>builder()
                    .result(null)
                    .message(e.getMessage())
                    .build();
        }

    }
    private String storeFile(MultipartFile file ) throws IOException {
        if(!isImageFile(file)||file.getOriginalFilename()==null)
        {
            throw  new IOException("Invalid image file format");
        }
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //thêm uuid vào trc tên file để đảm bảo tên file là duy nhất
        String uniqueFilename = UUID.randomUUID().toString() +"_"+filename;
        //đường dẫn đến thư mục muốn lưu file
        Path upLoadDir = Paths.get("uploads");
        //kiemtra và tạo thưc mục nếu k tồn tại
        if(!Files.exists(upLoadDir))
        {
            Files.createDirectories(upLoadDir);
        }
        //đường dẫn đầy đủ tên file
        Path destination = Paths.get(upLoadDir.toString(),uniqueFilename);
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        return uniqueFilename;
    }
    private boolean isImageFile(MultipartFile file)
    {
        String contentType = file.getContentType();
        return contentType!=null && contentType.startsWith("image/");
    }

    @GetMapping
    public ApiResponse<Page<ProductResponse>> getAllProducts(@PageableDefault(page = 0, size = 5) Pageable pageable, ProductFilterForm form)
    {
        //page of products
        Page<Product> productPage = productService.getAllProducts(pageable,form);
        // get data of productPage map with ProductResponse
        List<ProductResponse> productResponses = modelMapper.map(productPage.getContent(),new TypeToken<List<ProductResponse>>(){}.getType());
        //create a new productresponse Page
        Page<ProductResponse> productResponsePage = new PageImpl<>(productResponses,pageable,productPage.getTotalElements());
        return ApiResponse.<Page<ProductResponse>>builder()
                .result(productResponsePage)
                .build();
    }
    @GetMapping("{id}")
    public ApiResponse<ProductResponse> getProductById(@PathVariable int id)
    {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductById(id))
                .build();
    }
    @PutMapping("{id}")
    public ApiResponse<ProductResponse> updateProduct(@PathVariable int id, @Valid @RequestBody ProductUpdateRequest request)
    {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.updateProduct(id, request))
                .build();
    }
    @DeleteMapping("{id}")
    public ApiResponse<String> deleteProduct(@PathVariable int id)
    {
        productService.deleteProduct(id);
        return ApiResponse.<String>builder()
                .result("Product has been deleted!")
                .build();
    }
    @GetMapping("/name/{name}")
    public ApiResponse<ProductResponse> getProductByName(@PathVariable String name)
    {

        return ApiResponse.<ProductResponse>builder()
                .result(productService.getProductByName(name))
                .build();
    }
}
