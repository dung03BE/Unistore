package com.dung.UniStore.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ProductFilterForm {
    private String search; //search by name
    private Float minPrice;
    private Float maxPrice;
}
