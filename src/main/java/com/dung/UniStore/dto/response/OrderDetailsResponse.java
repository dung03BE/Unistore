package com.dung.UniStore.dto.response;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsResponse {
    private int id;
    private int productId;
    private int orderId;
    private int quantity;
    private BigDecimal price;
    private String color;
}
