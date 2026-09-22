package com.shivam.airBnb.strategy;


import com.shivam.airBnb.entity.Inventory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class HolidaysPricingStrategy implements PricingStrategy {

    private final PricingStrategy wrapped;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);

        boolean isTodayHoliday = true; // call an API or check with local data

        if(isTodayHoliday){
            price = price.multiply(BigDecimal.valueOf(1.5)) ;
        }
        return price ;
    }
}
