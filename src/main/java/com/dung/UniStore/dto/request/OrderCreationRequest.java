package com.dung.UniStore.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreationRequest {

    @Min(value = 1, message = "User's ID must be > 0")
    private int userId;
    private String fullName;
    private String email;
    @NotBlank(message = "Phone number is required")
    @Size(min = 5, message = "Phone number must be at least 5 characters")
    private String phoneNumber;
    private String address;
    private String note;
    @Min(value = 0, message = "Total money must be >= 0")
    private Float totalMoney;
    private String shippingMethod;
    private String shippingAddress;
    private LocalDate shippingDate;
    private String paymentMethod;
}
