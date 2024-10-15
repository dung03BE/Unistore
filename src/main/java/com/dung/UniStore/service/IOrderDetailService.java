package com.dung.UniStore.service;

import com.dung.UniStore.dto.request.OrderDetailCreationRequest;
import com.dung.UniStore.dto.response.OrderDetailsResponse;
import com.dung.UniStore.dto.response.OrderResponse;

public interface IOrderDetailService {
    OrderDetailsResponse createOrderDetail(OrderDetailCreationRequest request) throws Exception;
}
