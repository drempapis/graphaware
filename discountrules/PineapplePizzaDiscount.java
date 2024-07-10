package com.graphaware.pizzeria.service.discountrules;

import com.graphaware.pizzeria.model.Pizza;

import java.util.List;

public class PineapplePizzaDiscount extends DiscountDecorator {

    private Discount discount;

    public PineapplePizzaDiscount(Discount discount) {
        super(discount);
        this.discount = discount;
    }

    @Override
    public double getCost(List<Pizza> pizzaList) {
        boolean applyDiscount = pizzaList.stream()
                .anyMatch(p -> p.getToppings().contains("pineapple"));

        double discount = 0.0;
        if (applyDiscount) {
            for (Pizza pizza : pizzaList) {
                if(!pizza.getToppings().contains("pineapple")) {
                    discount += pizza.getPrice() * 0.1;
                }
            }
        }
        return this.discount.getCost(pizzaList) - discount;
    }

}
