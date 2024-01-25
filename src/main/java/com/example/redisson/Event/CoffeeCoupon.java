package com.example.redisson.Event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CoffeeCoupon {
	
    private String Menu;
    private String coffee_code;
    private int quantity;
}
