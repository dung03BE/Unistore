package com.dung.UniStore.service;

import com.dung.UniStore.dto.request.ProductCreationRequest;
import com.dung.UniStore.dto.request.ProductUpdateRequest;
import com.dung.UniStore.dto.response.ProductImageResponse;
import com.dung.UniStore.dto.response.ProductResponse;
import com.dung.UniStore.entity.Category;
import com.dung.UniStore.entity.Product;
import com.dung.UniStore.entity.ProductImage;
import com.dung.UniStore.exception.AppException;
import com.dung.UniStore.exception.ErrorCode;
import com.dung.UniStore.form.ProductFilterForm;
import com.dung.UniStore.mapper.ProductMapper;
import com.dung.UniStore.repository.ICategoryRepository;
import com.dung.UniStore.repository.IProductImageRepository;
import com.dung.UniStore.repository.IProductRepository;
import com.dung.UniStore.specification.ProductSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService implements IProductService{
    final IProductRepository productRepository;
    final ProductMapper productMapper;
    final ICategoryRepository categoryRepository;
    final IProductImageRepository productImageRepository;
    @Override
    public Page<Product> getAllProducts(Pageable pageable, ProductFilterForm form) {
        Specification<Product> where = ProductSpecification.builtWhere(form);
        return productRepository.findAll(where, pageable);
    }

    @Override
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProductById(int id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)
        );
        return  productMapper.toProductResponse(product);
    }

    @Override
    @CachePut(value = "products", key = "#id")
    public ProductResponse updateProduct(int id, ProductUpdateRequest request) {
        Product existingProduct = productRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)
        );
        productMapper.updateProduct(existingProduct, request);
        Category category =categoryRepository.findById(request.getCategoryId()).orElseThrow(
                ()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED)
        );
        existingProduct.setCategory(category);
        return productMapper.toProductResponse( productRepository.save(existingProduct));
    }

    @Override
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(int id) {
        Product existingProduct = productRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)
        );
        productRepository.delete(existingProduct);
    }

    @Override
    public ProductResponse getProductByName(String name) {
        Product existingProduct = productRepository.findByName(name).orElseThrow(
                () -> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)
        );
        return productMapper.toProductResponse(existingProduct);
    }

    @Override
    public ProductResponse createProduct(ProductCreationRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(
                ()->new AppException(ErrorCode.CATEGORY_NOT_EXISTED)
        );
        Product product= productMapper.toProduct(request);


        productRepository.save(product);
        return productMapper.toProductResponse(product);
    }

    @Override
    public ProductImage createProductImage(int id, ProductImageResponse build) throws Exception {
        Product existingProduct = productRepository
                .findById(id)
                .orElseThrow(()-> new AppException(ErrorCode.PRODUCT_NOT_EXISTED));


        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageUrl(build.getImageUrl())
                .build();
        //k cho insert qua 5 anh
        int size = productImageRepository.findByProductId(id).size();
        if(size>=ProductImage.Maximum_Images_Per_Product)
        {
            throw  new Exception("Number of images must be <="+
                    ProductImage.Maximum_Images_Per_Product);
        }
        return  productImageRepository.save(newProductImage);
    }
}
