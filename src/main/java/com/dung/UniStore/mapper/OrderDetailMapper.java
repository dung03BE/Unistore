package com.dung.UniStore.mapper;

import com.dung.UniStore.dto.request.OrderCreationRequest;
import com.dung.UniStore.dto.request.OrderDetailCreationRequest;
import com.dung.UniStore.dto.response.OrderDetailsResponse;
import com.dung.UniStore.dto.response.OrderResponse;
import com.dung.UniStore.entity.Order;
import com.dung.UniStore.entity.OrderDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderDetailMapper {
    @Mapping(target = "product.id", source = "productId")
    @Mapping(target = "order.id", source = "orderId")
    OrderDetail toOrder(OrderDetailCreationRequest request);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "orderId", source = "order.id")
    OrderDetailsResponse toOrderDetailsResponse(OrderDetail orderDetail);
}
