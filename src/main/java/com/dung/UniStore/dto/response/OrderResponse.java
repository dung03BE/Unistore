package com.dung.UniStore.dto.response;

import com.dung.UniStore.entity.Status;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class OrderResponse {

    @JsonProperty("user_id")
    private int userId;
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;
    private String note;
    @JsonProperty("order_date")
    private Date orderDate;
    private Status status;
    @JsonProperty("total_money")
    private Float totalMoney;
    @JsonProperty("shipping_method")
    private String shippingMethod;
    @JsonProperty("shipping_address")
    private String shippingAddress;
    @JsonProperty("shipping_date")
    private LocalDate shippingDate;
    @JsonProperty("tracking_number")
    private String trackingNumber ;
    @JsonProperty("payment_method")
    private String paymentMethod;
    @JsonProperty("is_active")
    private Boolean active;
    @JsonProperty("order_details")
    private List<OrderDetailsResponse> orderDetailList;

}
