package com.dung.UniStore.mapper;

import com.dung.UniStore.dto.request.ProductCreationRequest;
import com.dung.UniStore.dto.request.ProductUpdateRequest;
import com.dung.UniStore.dto.request.UserCreationRequest;
import com.dung.UniStore.dto.request.UserUpdateRequest;
import com.dung.UniStore.dto.response.ProductResponse;
import com.dung.UniStore.dto.response.UserResponse;
import com.dung.UniStore.entity.Product;
import com.dung.UniStore.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    @Mapping(target = "category.id", source = "categoryId")
    Product toProduct(ProductCreationRequest request);


    @Mapping(target = "categoryId", source = "category.id")
    ProductResponse toProductResponse(Product product);

    void updateProduct(@MappingTarget Product product, ProductUpdateRequest request);

}
