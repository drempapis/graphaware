package com.graphaware.pizzeria.service.discountrules;

import com.graphaware.pizzeria.model.Pizza;

import java.util.List;

public abstract class DiscountDecorator implements Discount {
	
	private Discount discount;
	
	public DiscountDecorator(Discount beverage){
		this.discount = beverage;
	}

	@Override
	public double getCost(List<Pizza> pizzaList) {
		return this.discount.getCost(pizzaList);
	}
}
