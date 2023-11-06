package com.coffeemachan.orderservice.controller;

import com.coffeemachan.orderservice.data.MenuItem;
import com.coffeemachan.orderservice.service.MenuItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final MenuItemService menuItemService;

    @PostMapping(path = "/menu-items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    void saveMenuItems(@RequestBody List<MenuItem> menuItems){
        menuItemService.persistMenuItems(menuItems);
    }

    @GetMapping(path = "/menu-items", consumes = MediaType.APPLICATION_JSON_VALUE)
    List<MenuItem> listMenuItems(){
        return menuItemService.listMenuItems();
    }

    @DeleteMapping(path = "/menu-items", consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteMenuItems(@RequestBody List<String> itemCodes){
        menuItemService.deleteMenuItems(itemCodes);
    }

}
