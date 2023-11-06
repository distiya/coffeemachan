package com.coffeemachan.orderservice.util;

import com.coffeemachan.orderservice.data.ShopInfo;
import com.coffeemachan.orderservice.data.ShopMenuItem;
import com.coffeemachan.orderservice.entity.ShopEntity;
import com.coffeemachan.orderservice.entity.ShopMenuItemEntity;

import java.util.stream.Collectors;

public class DomainConvertUtil {

    public static ShopEntity toShopEntity(ShopInfo shopInfo){
        ShopEntity entity = new ShopEntity();
        entity.setShopId(shopInfo.getShopId());
        entity.setAddress(shopInfo.getAddress());
        entity.setContactNumber(shopInfo.getContactNumber());
        entity.setOpeningTime(shopInfo.getOpeningTime());
        entity.setClosingTime(shopInfo.getClosingTime());
        entity.setNumberOfQueues(shopInfo.getNumberOfQueues());
        entity.setMaxQueueSize(shopInfo.getMaxQueueSize());
        return entity;
    }

    public static ShopInfo toShopInfo(ShopEntity entity){
        ShopInfo info = new ShopInfo();
        info.setShopId(entity.getShopId());
        info.setAddress(entity.getAddress());
        info.setContactNumber(entity.getContactNumber());
        info.setOpeningTime(entity.getOpeningTime());
        info.setClosingTime(entity.getClosingTime());
        info.setNumberOfQueues(entity.getNumberOfQueues());
        info.setMaxQueueSize(entity.getMaxQueueSize());
        info.setMenu(entity.getShopMenuItems().stream().map(DomainConvertUtil::toShopMenuItem).collect(Collectors.toSet()));
        return info;
    }

    public static ShopMenuItem toShopMenuItem(ShopMenuItemEntity entity){
        ShopMenuItem menuItem = new ShopMenuItem();
        menuItem.setItemCode(entity.getMenuItem().getItemCode());
        menuItem.setItemDescription(entity.getMenuItem().getItemDescription());
        menuItem.setItemName(entity.getMenuItem().getItemName());
        menuItem.setItemPrice(entity.getItemPrice());
        return menuItem;
    }

}
