package com.coffeemachan.orderservice.service;

import com.coffeemachan.orderservice.data.MenuItem;

import java.util.List;

public interface MenuItemService {

    void persistMenuItems(List<MenuItem> menuItems);
    List<MenuItem> listMenuItems();
    void deleteMenuItems(List<String> itemCodes);

}
