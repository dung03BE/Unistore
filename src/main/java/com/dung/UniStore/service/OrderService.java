package com.dung.UniStore.service;

import com.dung.UniStore.dto.request.OrderConfirmationMessage;
import com.dung.UniStore.dto.request.OrderCreationRequest;
import com.dung.UniStore.dto.response.OrderResponse;
import com.dung.UniStore.entity.Order;
import com.dung.UniStore.entity.Status;
import com.dung.UniStore.entity.User;
import com.dung.UniStore.exception.AppException;
import com.dung.UniStore.exception.ErrorCode;
import com.dung.UniStore.form.OrderFilterForm;
import com.dung.UniStore.mapper.OrderMapper;
import com.dung.UniStore.repository.IOrderRepository;
import com.dung.UniStore.repository.IUserRepository;
import com.dung.UniStore.specification.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final IOrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final IUserRepository userRepository;

    @Autowired
    private EmailService emailService;
    @Override
    public List<OrderResponse> getAllOrders(OrderFilterForm form) {
        Specification<Order> where = OrderSpecification.buildWhere(form);
        return orderRepository.findAll(where).stream().map(orderMapper::toOrderResponse).toList() ;
    }

    @Override
    public OrderResponse createOrder(OrderCreationRequest request) throws Exception {
        User existingUser =userRepository.findById(request.getUserId()).orElseThrow(
                ()->new AppException(ErrorCode.USER_NOT_EXISTED)
        );
        Order order = orderMapper.toOrder(request);
//        order.setUser();
        order.setOrderDate(new Date());//time now
        order.setStatus(Status.pending);
        LocalDate shippingDate =request.getShippingDate()==null?
                LocalDate.now():request.getShippingDate();
        //shippingDate phải là từ ngày hôm nay
        if(shippingDate.isBefore(LocalDate.now()))
        {
            throw new Exception("Data must be at least today");
        }
        order.setShippingDate(shippingDate);
        order.setActive(true);
        orderRepository.save(order);
        OrderResponse orderResponse = orderMapper.toOrderResponse(order);
        OrderConfirmationMessage message = new OrderConfirmationMessage();
        message.setId(order.getId());
        message.setCustomerEmail(order.getEmail());

        // Gửi email xác nhận
        emailService.sendOrderConfirmationEmail(message);
        return orderResponse;
    }
}
