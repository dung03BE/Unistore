package com.dung.UniStore.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageResponse {
    @JsonProperty("product_id")
    @Min(value = 1,message = "ProductId must be >0")
    private int productId;
    @Size(min=5,max=200,message = "Image's name")
    @JsonProperty("image_url")
    private String imageUrl;
}
