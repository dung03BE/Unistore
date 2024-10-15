package com.dung.UniStore.dto.request;

import com.dung.UniStore.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryTranRequest {
    private int inventoryItemId;
    private int quantityChange ;
    private TransactionType transactionType;
    private String reason;
    private String referenceId;
    //  @Size(min = 5, message = "CreatedBy must be at least 5 characters")
    private String createdBy;
    private LocalDateTime createdAt;
}
