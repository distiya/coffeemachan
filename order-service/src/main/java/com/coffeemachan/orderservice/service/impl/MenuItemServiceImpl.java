package com.coffeemachan.orderservice.service.impl;

import com.coffeemachan.orderservice.data.MenuItem;
import com.coffeemachan.orderservice.entity.MenuItemEntity;
import com.coffeemachan.orderservice.repository.MenuItemRepository;
import com.coffeemachan.orderservice.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class MenuItemServiceImpl implements MenuItemService {

    private final MenuItemRepository repository;

    @Override
    public void persistMenuItems(List<MenuItem> menuItems) {
        repository.saveAllAndFlush(menuItems.stream().map(this::toMenuItemEntity).collect(Collectors.toList()));
    }

    @Override
    public List<MenuItem> listMenuItems() {
        return repository.findAll().stream().map(this::toMenuItem).collect(Collectors.toList());
    }

    @Override
    public void deleteMenuItems(List<String> itemCodes) {
        repository.deleteAllById(itemCodes);
    }

    private MenuItemEntity toMenuItemEntity(MenuItem menuItem){
        MenuItemEntity entity = new MenuItemEntity();
        entity.setItemCode(menuItem.getItemCode());
        entity.setItemName(menuItem.getItemName());
        entity.setItemDescription(menuItem.getItemDescription());
        entity.setItemPreparationTime(menuItem.getItemPreparationTime());
        return entity;
    }

    private MenuItem toMenuItem(MenuItemEntity entity){
        MenuItem item = new MenuItem();
        item.setItemCode(entity.getItemCode());
        item.setItemName(entity.getItemName());
        item.setItemDescription(entity.getItemDescription());
        item.setItemPreparationTime(entity.getItemPreparationTime());
        return item;
    }
}
