package com.dung.UniStore.service;

import com.dung.UniStore.dto.request.OrderDetailCreationRequest;
import com.dung.UniStore.dto.response.OrderDetailsResponse;
import com.dung.UniStore.entity.Order;
import com.dung.UniStore.entity.OrderDetail;
import com.dung.UniStore.entity.Product;
import com.dung.UniStore.exception.AppException;
import com.dung.UniStore.exception.ErrorCode;
import com.dung.UniStore.mapper.OrderDetailMapper;
import com.dung.UniStore.repository.IOrderDetailRepository;
import com.dung.UniStore.repository.IOrderRepository;
import com.dung.UniStore.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final IOrderDetailRepository orderDetailRepository;
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final RedisTemplate<String,Object> redisTemplate;
//    @Autowired
//    private RedissonClient redissonClient;
    @Override
    public OrderDetailsResponse createOrderDetail(OrderDetailCreationRequest request) throws Exception {
        Integer pQuantityStock = getIntegerValue(request.getProductId() + "_quantity_stock");

        System.out.println("Quantity stock **************:" + pQuantityStock);

        if (pQuantityStock == null) {
            System.out.println("Quantity stock is null for product ID: " + request.getProductId());
            throw new Exception("Số lượng tồn kho không tồn tại trong Redis cho Product ID: " + request.getProductId());
        }
        // Can key lu tren redis luu so dien thoai da ban
        String productSoldKey = request.getProductId()  + "_sold";
        redisTemplate.opsForValue().setIfAbsent(productSoldKey, 0);
        Object totalSold = redisTemplate.opsForValue().increment(productSoldKey, request.getQuantity());
        System.out.println("Total sold:********************:" + totalSold);
        System.out.println("OrderQuantity:********************:" + request.getQuantity());
        if (Integer.parseInt(totalSold.toString())  > pQuantityStock) {
            System.out.println("Out of Stock!!!!!!!!!!!!!!!!!");
            throw new AppException(ErrorCode.OutofStock);

        }

        Product existingProduct = productRepository.findById(request.getProductId()).orElseThrow(
                ()-> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)
        );
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTS));
        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(existingProduct)
                .quantity(request.getQuantity())
                .color(request.getColor())
                .price(request.getPrice())
                .build();
        orderDetail = orderDetailRepository.save(orderDetail);
        order.setTotalMoney(calculateTotalForOrder(order.getId()));
        orderRepository.save(order);
        return orderDetailMapper.toOrderDetailsResponse(orderDetail);
    }
    private Integer getIntegerValue(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            try {
                return Integer.parseInt(value.toString());
            } catch (NumberFormatException ex) {
                throw new IllegalArgumentException("Value for key " + key + "is not a valid integer");
            }
        }
        return null;
    }
    public BigDecimal calculateTotalForOrder(int orderId) {
        List<OrderDetail> orderDetails = getOrderDetailsByOrderId(orderId);
        BigDecimal total = BigDecimal.ZERO;

        for (OrderDetail detail : orderDetails) {
            BigDecimal quantity = new BigDecimal(detail.getQuantity());
            BigDecimal itemTotal = detail.getPrice().multiply(quantity);
            total = total.add(itemTotal);
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }
    public List<OrderDetail> getOrderDetailsByOrderId(int orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }
}

