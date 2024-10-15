package com.dung.UniStore.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderConfirmationMessage {
    private int id; // ID của đơn hàng
    private String customerEmail; // Địa chỉ email của khách hàng
    private String emailBody; // Nội dung email
}
