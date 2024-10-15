package com.dung.UniStore.repository;

import com.dung.UniStore.entity.InventoryTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryTranRepository extends JpaRepository<InventoryTransaction,Integer> {
}
