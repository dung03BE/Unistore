package com.dung.UniStore.mapper;

import com.dung.UniStore.dto.request.OrderCreationRequest;
import com.dung.UniStore.dto.response.OrderResponse;
import com.dung.UniStore.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderMapper {
    @Mapping(target = "user.id", source = "userId")
    Order toOrder(OrderCreationRequest request);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "orderDetailList", source = "order.orderDetailList")
    OrderResponse toOrderResponse(Order order);
}
