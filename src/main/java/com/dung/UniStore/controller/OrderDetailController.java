package com.dung.UniStore.controller;

import com.dung.UniStore.dto.request.OrderDetailCreationRequest;
import com.dung.UniStore.dto.response.ApiResponse;
import com.dung.UniStore.dto.response.OrderDetailsResponse;
import com.dung.UniStore.dto.response.OrderResponse;
import com.dung.UniStore.service.IOrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/orderdetails")
@RequiredArgsConstructor
public class OrderDetailController {
    private final IOrderDetailService orderDetailService;
    @PostMapping
    public ApiResponse<OrderDetailsResponse> createOrderDetail(@RequestBody OrderDetailCreationRequest request) throws Exception {

        return ApiResponse.<OrderDetailsResponse>builder()
                .result(orderDetailService.createOrderDetail(request))
                .build();
    }
//    @DeleteMapping("{id}")
//    public ResponseEntity<?> deleteOrderDetail(@PathVariable int id) throws DataNotFoundException {
//        orderDetailService.deleteOrderDetail(id);
//        return new ResponseEntity<>("OK",HttpStatus.OK);
//    }
}
