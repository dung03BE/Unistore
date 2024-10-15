package com.dung.UniStore.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL) //khai baos khi co null thi no k xuat hien nua, vi du : messsage  = null thi se k dc tra ra
public class ApiResponse<T>{
    @Builder.Default
    private int code = 1000;

    private String message;
    private T result;
}
