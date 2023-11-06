package com.coffeemachan.orderservice.repository;

import com.coffeemachan.orderservice.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShopRepository extends JpaRepository<ShopEntity, UUID> {
}
