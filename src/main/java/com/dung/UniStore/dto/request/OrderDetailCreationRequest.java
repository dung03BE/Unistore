package com.dung.UniStore.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Builder
public class OrderDetailCreationRequest {
    @NotBlank(message = "Order_Id is required!")
    @JsonProperty("order_id")
    private int orderId;
    @NotBlank(message = "ProductId is required!")
    @JsonProperty("product_id")
    private int productId;
    @Min(value = 0,message = "Quantity must be >=0")
    @JsonProperty("quantity")
    private int quantity;
    private BigDecimal price;
    private String color;
}
