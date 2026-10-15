package com.sky.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class ShoppingCartDTO implements Serializable {

    private Long dishId;    // 菜品Id
    private Long setmealId; // 套餐Id
    private String dishFlavor; // 菜品口味

}
