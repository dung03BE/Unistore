package com.dung.UniStore.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name="user_id" )
    private User user;
    @Column(name="fullname")
    private String fullName;
    @Column(name = "email", length = 100)
    private String email;
    @Column(name="phone_number",length=100,nullable = false)
    private String phoneNumber;
    @Column(name = "address", length = 100)
    private String address;
    @Column(name = "note", length = 100)
    private String note;
    @Column(name="order_date")
    private Date orderDate;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
    @Column(name="total_money")
    private BigDecimal totalMoney;
    @Column(name="shipping_method")
    private String shippingMethod;
    @Column(name="shipping_address")
    private String shippingAddress;
    @Column(name="shipping_date")
    private LocalDate shippingDate;
    @Column(name="tracking_number")
    private String trackingNumber ;
    @Column(name="payment_method")
    private String paymentMethod;
    @Column(name="active")
    private boolean active; //thuoc ve admin
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderDetail> orderDetailList;

}
