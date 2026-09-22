package com.shivam.airBnb.strategy;


import com.shivam.airBnb.entity.Inventory;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class OccupationPricingStrategy implements PricingStrategy{

    private final PricingStrategy wrapped ;

    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        BigDecimal price = wrapped.calculatePrice(inventory);

        if(((double) inventory.getBookedCount() /inventory.getTotalCount()) >= 0.8){
            price = price.multiply(BigDecimal.valueOf(1.25)) ;
        }

        return price ;
    }

}
