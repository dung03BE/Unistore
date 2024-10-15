package com.dung.UniStore.service;

import com.dung.UniStore.dto.request.InventoryTranRequest;
import com.dung.UniStore.entity.InventoryItem;
import com.dung.UniStore.entity.InventoryTransaction;
import com.dung.UniStore.exception.AppException;
import com.dung.UniStore.exception.ErrorCode;
import com.dung.UniStore.repository.InventoryRepository;
import com.dung.UniStore.repository.InventoryTranRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryTranService {
    private final InventoryTranRepository invTransactionRepository;
    private final ModelMapper modelMapper;
    private final InventoryRepository inventoryRepository;

    public Page<InventoryTransaction> getAllInvTransaction(Pageable page) {
        return invTransactionRepository.findAll(page);
    }

    public InventoryTransaction createInvTransaction(InventoryTranRequest request) {
        InventoryItem inventoryItem=inventoryRepository.findById(request.getInventoryItemId())
                .orElseThrow(()->new AppException(ErrorCode.INVENTORY_ITEM_NOT_EXISTS));

        modelMapper.typeMap(InventoryTranRequest.class,InventoryTransaction.class)
                .addMappings(mapper->mapper.skip(InventoryTransaction::setId));
        InventoryTransaction inventoryTransaction = new InventoryTransaction();
        modelMapper.map(request,inventoryTransaction);
        inventoryTransaction.setInventoryItem(inventoryItem);
        inventoryTransaction.setQuantityChange(request.getQuantityChange());
        inventoryTransaction.setTransactionType(request.getTransactionType());
        inventoryTransaction.setReason(request.getReason());
        inventoryTransaction.setCreatedBy(request.getCreatedBy());
        inventoryTransaction.setCreatedAt(request.getCreatedAt());
        return invTransactionRepository.save(inventoryTransaction);
    }
}
