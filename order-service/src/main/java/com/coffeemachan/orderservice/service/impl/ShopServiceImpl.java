package com.coffeemachan.orderservice.service.impl;

import com.coffeemachan.orderservice.data.ShopInfo;
import com.coffeemachan.orderservice.repository.ShopRepository;
import com.coffeemachan.orderservice.service.ShopService;
import com.coffeemachan.orderservice.util.DomainConvertUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class ShopServiceImpl implements ShopService {

    private final ShopRepository repository;

    @Override
    public void persistShop(ShopInfo shopInfo) {
        repository.saveAndFlush(DomainConvertUtil.toShopEntity(shopInfo));
    }

    @Override
    public void deleteShop(UUID shopId) {

    }
}
