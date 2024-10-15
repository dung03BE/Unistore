package com.dung.UniStore.dto.request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryItemRequest {
    private int id;

    @Min(value = 1, message = "Product_Id must be > 0")
    private int product_id;
    @Min(value = 0, message = "Quantity must be>=0")
    private int quantity;
}