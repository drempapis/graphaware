package com.graphaware.pizzeria.service.discountrules;

import com.graphaware.pizzeria.model.Pizza;

import java.util.List;

public interface Discount {

    double getCost(List<Pizza> pizzas);
}
