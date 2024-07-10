package com.graphaware.pizzeria.service.discountrules;

import com.graphaware.pizzeria.model.Pizza;

import java.util.List;

public class NoDiscount implements Discount {

    @Override
    public double getCost(List<Pizza> pizzas) {
        return pizzas.stream().mapToDouble(Pizza::getPrice).sum();
    }
}
