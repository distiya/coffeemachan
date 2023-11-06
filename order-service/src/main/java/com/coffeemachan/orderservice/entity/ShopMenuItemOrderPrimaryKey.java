package com.coffeemachan.orderservice.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.util.UUID;

@Data
@Embeddable
public class ShopMenuItemOrderPrimaryKey{

    private UUID shopId;
    private String itemCode;
    private UUID orderId;

}
