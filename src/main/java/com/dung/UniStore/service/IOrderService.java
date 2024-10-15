package com.dung.UniStore.service;

import com.dung.UniStore.dto.request.OrderCreationRequest;
import com.dung.UniStore.dto.response.OrderResponse;
import com.dung.UniStore.form.OrderFilterForm;

import java.util.List;

public interface IOrderService {
    List<OrderResponse> getAllOrders(OrderFilterForm form);

    OrderResponse createOrder(OrderCreationRequest request) throws Exception;
}
