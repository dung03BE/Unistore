package com.dung.UniStore.controller;

import com.dung.UniStore.dto.request.InventoryTranRequest;
import com.dung.UniStore.entity.InventoryTransaction;
import com.dung.UniStore.service.InventoryTranService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/invtransaction")
@RequiredArgsConstructor
public class InventoryTranController {
    private final InventoryTranService inventoryTranService;
    @GetMapping
    public Page<InventoryTransaction> getAllInvTransaction(@PageableDefault(page = 0,size =5,  sort = "createdAt", direction = Sort.Direction.DESC) Pageable page)
    {
        Page<InventoryTransaction> inventoryTransactions = inventoryTranService.getAllInvTransaction(page);
        return inventoryTransactions;
    }
    @PostMapping
    public ResponseEntity<?> createInvTransaction(@RequestBody InventoryTranRequest request)  {
        InventoryTransaction inventoryTransaction = inventoryTranService.createInvTransaction(request);
        return ResponseEntity.ok(inventoryTransaction);
    }
}
