package com.coffeemachan.orderservice.data;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class ShopInfo {

    private UUID shopId;
    private String address;
    private String contactNumber;
    private String openingTime;
    private String closingTime;
    private Double latitude;
    private Double longitude;
    private Integer numberOfQueues;
    private Integer maxQueueSize;
    private Set<ShopMenuItem> menu;

}
