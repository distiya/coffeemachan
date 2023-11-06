package com.coffeemachan.orderservice.repository;

import com.coffeemachan.orderservice.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItemEntity, String> {
}
