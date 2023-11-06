package com.coffeemachan.orderservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "menu_items")
public class MenuItemEntity {

    @Id
    @Column(name = "item_code")
    private String itemCode;
    @Column(name = "item_name")
    private String itemName;
    @Column(name = "item_description")
    private String itemDescription;
    @Column(name = "item_preparation_time")
    private Integer itemPreparationTime;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menuItem")
    private Set<ShopMenuItemEntity> shopMenuItems;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "menuItem")
    private Set<ShopMenuItemOrderEntity> shopMenuItemOrders;

}
