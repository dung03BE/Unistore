package com.dung.UniStore.repository;

import com.dung.UniStore.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<InventoryItem,Integer>{
        InventoryItem findByProductId(int product_id);
}
