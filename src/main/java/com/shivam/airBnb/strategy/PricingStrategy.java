package com.shivam.airBnb.strategy;

import com.shivam.airBnb.entity.Inventory;

import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);

}
