package com.coffeemachan.orderservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "shop_menu_items")
public class ShopMenuItemEntity {

    @EmbeddedId
    private ShopMenuItemPrimaryKey primaryKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id")
    @MapsId("shopId")
    private ShopEntity shop;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_item_id")
    @MapsId("itemCode")
    private MenuItemEntity menuItem;

    @Column(name = "item_price")
    private Double itemPrice;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<OrderEntity> orders;

}
