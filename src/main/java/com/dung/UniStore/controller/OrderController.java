package com.dung.UniStore.controller;

import com.dung.UniStore.dto.request.OrderCreationRequest;
import com.dung.UniStore.dto.response.ApiResponse;
import com.dung.UniStore.dto.response.OrderResponse;
import com.dung.UniStore.form.OrderFilterForm;
import com.dung.UniStore.service.IOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@Validated
@RequiredArgsConstructor
public class OrderController {
    private final IOrderService orderService;
    @GetMapping
    public ApiResponse<List<OrderResponse>> getAllOrders(OrderFilterForm form)
    {
        return ApiResponse.<List<OrderResponse>>builder()
                .result(orderService.getAllOrders(form))
                .build();
    }
    @PostMapping
    public ApiResponse<OrderResponse> createOrder(@Valid @RequestBody OrderCreationRequest request) throws Exception {
        return ApiResponse.<OrderResponse>builder()
                .result(orderService.createOrder(request))
                .build();
    }
//    @PutMapping("{id}")
//    public ResponseEntity<String> updateOrder(@PathVariable int id,  @RequestBody UpdateOrderForm form) throws DataNotFoundException {
//        form.setId(id);
//        Order existingOrder = orderService.updateOrder(form);
//        return new ResponseEntity<>("Update OK", HttpStatus.OK);
//    }
//    @DeleteMapping("{id}")
//    public ResponseEntity<String> deleteOrder(@PathVariable int id) throws DataNotFoundException {
//        orderService.deleteOrder(id);
//        return new ResponseEntity<>("OK", HttpStatus.OK);
//    }
//    @GetMapping("{id}")
//    public OrderDTO getOrderById(@PathVariable int id) throws DataNotFoundException {
//        return modelMapper.map(orderService.getOrderById(id),OrderDTO.class);
//    }
//    @GetMapping("/user/{user_id}")
//    public OrderDTO getOrderByUserId(@PathVariable("user_id") int userId) throws DataNotFoundException {
//        Order orderByUserId = orderService.getOrderByUserId(userId);
//        return modelMapper.map(orderByUserId,OrderDTO.class);
//    }
}
