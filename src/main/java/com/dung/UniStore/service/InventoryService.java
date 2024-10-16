package com.dung.UniStore.service;

import com.dung.UniStore.dto.request.InventoryItemRequest;
import com.dung.UniStore.entity.InventoryItem;
import com.dung.UniStore.entity.Product;
import com.dung.UniStore.exception.AppException;
import com.dung.UniStore.exception.ErrorCode;
import com.dung.UniStore.repository.IProductRepository;
import com.dung.UniStore.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService implements IInventoryService {
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final IProductRepository productRepository;
    private final RedisTemplate redisTemplate;
    @Override
    public List<InventoryItem> getAllInventory() {
        return inventoryRepository.findAll();
    }

    @Override
    public InventoryItem getInventory(int id) {
        InventoryItem inventoryItem = inventoryRepository.findById(id).orElseThrow(
                ()-> new AppException(ErrorCode.INVENTORY_ITEM_NOT_EXISTS)
        );
        return inventoryItem;
    }

    @Override
    public InventoryItem createInventoryItem(InventoryItemRequest itemRequest) {
        // Kiểm tra xem sản phẩm có tồn tại hay không
        Product existingProduct = productRepository.findById(itemRequest.getProduct_id()).orElseThrow(
                ()-> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)
        );
        // Tìm mục inventory với productId tương ứng
        InventoryItem existingInventoryItem = (InventoryItem) inventoryRepository.findByProductId(itemRequest.getProduct_id());

        if (existingInventoryItem != null) {
            // Nếu mục inventory đã tồn tại, cộng thêm quantity
            existingInventoryItem.setQuantity(existingInventoryItem.getQuantity() + itemRequest.getQuantity());
            inventoryRepository.save(existingInventoryItem);

            return existingInventoryItem;
        } else {
            // Nếu mục inventory chưa tồn tại, tạo mới
            modelMapper.typeMap(InventoryItemRequest.class,InventoryItem.class)
                    .addMappings(mapper->mapper.skip(InventoryItem::setId));
            InventoryItem inventoryItem = new InventoryItem();

            modelMapper.map(itemRequest, inventoryItem);
            inventoryItem.setQuantity(itemRequest.getQuantity());
            inventoryItem.setProductId(itemRequest.getProduct_id());
            inventoryRepository.save(inventoryItem);

            return inventoryItem;
        }
    }

    @Override
    public InventoryItem updateInventoryItem(int id, InventoryItemRequest itemRequest) {
        InventoryItem existingInventoryItem = (InventoryItem) inventoryRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.INVENTORY_ITEM_NOT_EXISTS));
        existingInventoryItem.setId(id);
        modelMapper.typeMap(InventoryItemRequest.class,InventoryItem.class)
                .addMappings(mapper->mapper.skip(InventoryItem::setProductId));
        existingInventoryItem.setQuantity(itemRequest.getQuantity());

        return inventoryRepository.save(existingInventoryItem);
    }

    @Override
    public void deleteInventoryItem(int id) {
        InventoryItem existingInventoryItem = (InventoryItem) inventoryRepository.findById(id)
                .orElseThrow(()->new AppException(ErrorCode.INVENTORY_ITEM_NOT_EXISTS));
        inventoryRepository.delete(existingInventoryItem);
    }
}
