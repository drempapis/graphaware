package com.graphaware.pizzeria.service.discountrules;

import com.graphaware.pizzeria.model.Pizza;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ThreePizzasOneFreeDiscount extends DiscountDecorator {

    private Discount discount;

    public ThreePizzasOneFreeDiscount(Discount discount) {
        super(discount);
        this.discount = discount;
    }

    @Override
    public double getCost(List<Pizza> pizzaList) {
        Optional<Double> discount = Optional.empty();

        if(pizzaList.size() == 3) {
            discount = pizzaList.stream()
                    .sorted(Comparator.comparing(Pizza::getPrice))
                    .map(Pizza::getPrice)
                    .findFirst();
        }

        return this.discount.getCost(pizzaList) - discount.orElse(0.0);
    }
}
