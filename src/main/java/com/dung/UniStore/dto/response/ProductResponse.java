package com.dung.UniStore.dto.response;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private int id;
    private String name;
    private float price;
    private String thumbnail ;
    private String description;
    private int  categoryId;
}
