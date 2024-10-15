package com.dung.UniStore.repository;


import com.dung.UniStore.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProductImageRepository extends JpaRepository<ProductImage,Integer> {
    List<ProductImage> findByProductId(int productId);
}
