package com.dung.UniStore.repository;

import com.dung.UniStore.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IProductRepository extends JpaRepository<Product,Integer>, JpaSpecificationExecutor<Product> {
    @Query("SELECT p FROM Product p WHERE TRIM(p.name) = TRIM(:name)")
    Optional<Product> findByName(String name);

    boolean existsByName(String name);
}
