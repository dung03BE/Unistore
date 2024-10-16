package com.dung.UniStore.service;

import com.dung.UniStore.dto.request.OrderDetailCreationRequest;
import com.dung.UniStore.dto.response.OrderDetailsResponse;
import com.dung.UniStore.entity.InventoryItem;
import com.dung.UniStore.entity.Order;
import com.dung.UniStore.entity.OrderDetail;
import com.dung.UniStore.entity.Product;
import com.dung.UniStore.exception.AppException;
import com.dung.UniStore.exception.ErrorCode;
import com.dung.UniStore.mapper.OrderDetailMapper;
import com.dung.UniStore.repository.IOrderDetailRepository;
import com.dung.UniStore.repository.IOrderRepository;
import com.dung.UniStore.repository.IProductRepository;
import com.dung.UniStore.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final IOrderDetailRepository orderDetailRepository;
    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final OrderDetailMapper orderDetailMapper;
    private final RedisTemplate<String,Object> redisTemplate;
    private final InventoryRepository inventoryRepository;
    @Autowired
    private RedissonClient redissonClient;

    @Override
    public OrderDetailsResponse createOrderDetail(OrderDetailCreationRequest request) {
        // Lấy thông tin Order và Product
        Product existingProduct = productRepository.findById(request.getProductId()).orElseThrow(
                ()-> new AppException(ErrorCode.PRODUCT_NOT_EXISTED)
        );
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_EXISTS));
        InventoryItem inventoryItem = inventoryRepository.findByProductId(request.getProductId());


        // Tạo khóa duy nhất cho sản phẩm
        RLock lock = redissonClient.getLock("lock:product:" + request.getProductId());

        System.out.println("Hàng kho: "+ inventoryItem.getQuantity());
        System.out.println("Hàng yêu cầu: "+ request.getQuantity());
        try {
            // Thử lấy lock trong 10 giây, thời gian giữ lock là 5 giây
            if (lock.tryLock(10, 30, TimeUnit.SECONDS)) {
                System.out.println("Lock acquired!");
//                // Dừng lại để kiểm tra khóa trong Redis
//                Thread.sleep(15000); // Dừng lại trong 15 giây
                try {
                    // Kiểm tra số lượng sản phẩm có đủ để đặt hàng không
                    if (inventoryItem.getQuantity() < request.getQuantity()) {
                        System.out.println("Out of stock!!!");
                        throw new AppException(ErrorCode.OutofStock);
                    }
                    // Tạo OrderDetail
                    OrderDetail orderDetail = OrderDetail.builder()
                            .order(order)
                            .product(existingProduct)
                            .quantity(request.getQuantity())
                            .color(request.getColor())
                            .price(request.getPrice())
                            .build();

                    // Lưu OrderDetail
                    orderDetailRepository.save(orderDetail);

                    // Giảm số lượng sản phẩm trong kho
                    inventoryItem.setQuantity(inventoryItem.getQuantity() - request.getQuantity());
                    inventoryRepository.save(inventoryItem); // Cập nhật số lượng sản phẩm

                    // Chuyển đổi thành DTO để trả về
                    return orderDetailMapper.toOrderDetailsResponse(orderDetail);
                } finally {
                    // Giải phóng lock
                    lock.unlock();
                }
            } else {
                throw new RuntimeException("Vui lòng thử lại sau khi sản phẩm đang được xử lý.");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Khôi phục trạng thái gián đoạn
            throw new RuntimeException("Có lỗi xảy ra khi đặt hàng", e);
        }
    }
}

