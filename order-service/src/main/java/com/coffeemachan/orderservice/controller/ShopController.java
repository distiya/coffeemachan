package com.coffeemachan.orderservice.controller;

import com.coffeemachan.orderservice.data.ShopInfo;
import com.coffeemachan.orderservice.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/shops")
public class ShopController {

    private final ShopService shopService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    void createShop(@RequestBody ShopInfo shopInfo){
        shopService.persistShop(shopInfo);
    }
}
