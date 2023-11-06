package com.coffeemachan.orderservice.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.UUID;

@Data
@Embeddable
public class ShopMenuItemPrimaryKey {

    private UUID shopId;
    private String itemCode;

}
