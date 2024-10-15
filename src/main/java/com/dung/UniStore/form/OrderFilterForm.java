package com.dung.UniStore.form;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderFilterForm {
    private String fullName;
    private Float totalMoney;
    private Boolean active;

}
