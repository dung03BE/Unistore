package com.dung.UniStore.service;

import com.dung.UniStore.dto.request.InventoryItemRequest;
import com.dung.UniStore.entity.InventoryItem;

import java.util.List;

public interface IInventoryService {
    List<InventoryItem> getAllInventory();

   InventoryItem getInventory(int id);

    InventoryItem createInventoryItem(InventoryItemRequest itemRequest);

    InventoryItem updateInventoryItem(int id, InventoryItemRequest itemRequest);

    void deleteInventoryItem(int id);
}
