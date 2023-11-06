package com.coffeemachan.orderservice.data;

import lombok.Data;

@Data
public class MenuItem {

    private String itemCode;
    private String itemName;
    private String itemDescription;
    private Integer itemPreparationTime;

}
