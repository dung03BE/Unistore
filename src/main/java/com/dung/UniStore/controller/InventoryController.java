package com.dung.UniStore.controller;
import com.dung.UniStore.dto.request.InventoryItemRequest;
import com.dung.UniStore.dto.response.ApiResponse;
import com.dung.UniStore.entity.InventoryItem;
import com.dung.UniStore.service.IInventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/v1/inventories")
@RequiredArgsConstructor
@Validated
public class InventoryController {
    private final IInventoryService inventoryService;
    @GetMapping
    public ApiResponse<List<InventoryItem>> getAllInventoryItem()
    {
        return ApiResponse.<List<InventoryItem>>builder()
            .result(inventoryService.getAllInventory())
            .build();
    }

    @GetMapping("{id}")
    public ApiResponse<InventoryItem> getInventory(@PathVariable int id) {
        return ApiResponse.<InventoryItem>builder()
                .result(inventoryService.getInventory(id))
                .build();
    }
    @PostMapping
    public ApiResponse<?> createInventoryItem(@Valid @RequestBody InventoryItemRequest itemRequest) {


        return  ApiResponse.<InventoryItem>builder()
                .result(inventoryService.createInventoryItem(itemRequest))
                .build();

    }
    @PutMapping("{id}")
    public ResponseEntity<?> updateInventoryItem(@PathVariable int id, @RequestBody InventoryItemRequest itemRequest)  {
        InventoryItem inventoryItem = inventoryService.updateInventoryItem(id,itemRequest);
        return ResponseEntity.ok().body(inventoryItem);
    }
    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteInventoryItem(@PathVariable int id) {
        inventoryService.deleteInventoryItem(id);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

}
