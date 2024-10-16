package com.dung.UniStore.exception;


import com.dung.UniStore.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.Map;
import java.util.Objects;


import jakarta.validation.ConstraintViolation;


@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";

    @ExceptionHandler(value = Exception.class)
    ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception) {
        log.error("Exception: ", exception);
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse> handlingAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = new ApiResponse();

        apiResponse.setCode(errorCode.getCode());
        apiResponse.setMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getStatusCode()).body(apiResponse);
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        return ResponseEntity.status(errorCode.getStatusCode())
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingValidation(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String message = (fieldError != null) ? fieldError.getDefaultMessage() : null;

        ErrorCode errorCode = null;
        Map<String, Object> attributes = null;

        try {
            // Kiểm tra xem thông báo có phải là mã lỗi tùy chỉnh hay không
            errorCode = ErrorCode.valueOf(message);
            if (fieldError != null) {
                var constraintViolation = fieldError.unwrap(ConstraintViolation.class);
                if (constraintViolation != null) {
                    attributes = constraintViolation.getConstraintDescriptor().getAttributes();
                    log.info(attributes.toString());
                }
            }
        } catch (IllegalArgumentException e) {
            // Nếu thông báo không phải mã lỗi tùy chỉnh, sử dụng thông báo gốc
            log.info("Không phải mã lỗi tùy chỉnh, sử dụng thông báo validation thông thường.");
        }

        ApiResponse apiResponse = new ApiResponse();

        if (errorCode != null) {
            apiResponse.setCode(errorCode.getCode());
            apiResponse.setMessage(
                    Objects.nonNull(attributes)
                            ? mapAttribute(errorCode.getMessage(), attributes)
                            : errorCode.getMessage());
        } else {
            // Trả về thông báo validation thông thường nếu không phải mã lỗi tùy chỉnh
            apiResponse.setCode(ErrorCode.INVALID_KEY.getCode());
            apiResponse.setMessage(message);
        }

        return ResponseEntity.badRequest().body(apiResponse);
//        String enumKey = exception.getFieldError().getDefaultMessage();
//
//        ErrorCode errorCode = ErrorCode.INVALID_KEY;
//
//        try {
//            errorCode = ErrorCode.valueOf(enumKey);
//        } catch (IllegalArgumentException e){
//
//        }
//
//        ApiResponse apiResponse = new ApiResponse();
//
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(errorCode.getMessage());
//
//        return ResponseEntity.badRequest().body(apiResponse);
    }


    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));

        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }
}
