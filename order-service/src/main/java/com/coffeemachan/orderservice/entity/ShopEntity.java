package com.coffeemachan.orderservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "shops")
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "shop_id")
    private UUID shopId;
    @Column(name = "address")
    private String address;
    @Column(name = "contact_number")
    private String contactNumber;
    @Column(name = "opening_time")
    private String openingTime;
    @Column(name = "closing_time")
    private String closingTime;
    @Column(name = "num_of_queues")
    private Integer numberOfQueues;
    @Column(name = "size_of_queue")
    private Integer maxQueueSize;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "shop")
    private Set<ShopMenuItemEntity> shopMenuItems;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shop")
    private Set<OrderEntity> shopOrders;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "shop")
    private Set<ShopMenuItemOrderEntity> shopMenuItemOrders;

}
