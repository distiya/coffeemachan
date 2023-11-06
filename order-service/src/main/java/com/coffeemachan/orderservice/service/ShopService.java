package com.coffeemachan.orderservice.service;

import com.coffeemachan.orderservice.data.ShopInfo;

import java.util.UUID;

public interface ShopService {

    void persistShop(ShopInfo shopInfo);
    void deleteShop(UUID shopId);

}
