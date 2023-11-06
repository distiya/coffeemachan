package com.coffeemachan.orderservice.entity;

import com.coffeemachan.orderservice.constants.OrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "shop_orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;
    @Column(name = "customer_name")
    private String customerName;
    @Column(name = "customer_loyalty_points")
    private String customerLoyaltyPoints;
    @Column(name = "customer_user_name")
    private String customerUserName;
    @Column(name = "assigned_queue")
    private Integer assignedQueue;
    @Column(name = "total_preparation_time")
    private Integer totalPreparationTime;
    @Column(name = "order_status")
    private OrderStatus orderStatus;
    @Column(name = "order_total")
    private Double orderTotal;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    private ShopEntity shop;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    private Set<ShopMenuItemOrderEntity> shopMenuItemOrders;

}
