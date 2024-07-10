package com.graphaware.pizzeria.service.discountrules;

import com.graphaware.pizzeria.model.Pizza;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

public class DiscountRulesTest {

    private static Pizza pizza1, pizza2, pizza3;

    @BeforeAll
    static void setup() {
        pizza1 = new Pizza();
        pizza1.setToppings(Arrays.asList("mozzarella", "pineapple"));
        pizza1.setPrice(12.0);
        pizza2 = new Pizza();
        pizza2.setToppings(Arrays.asList("mozzarella", "tomato"));
        pizza2.setPrice(7.0);
        pizza3 = new Pizza();
        pizza3.setToppings(Arrays.asList("mozzarella", "ham"));
        pizza3.setPrice(6.0);
    }

    @Test
    public void noDiscountEmptyList() {
        Discount discount = new NoDiscount();
        assertThat(discount.getCost(Collections.emptyList())).isEqualTo(0.0);
    }

    @Test
    public void noDiscount() {
        Discount discount = new NoDiscount();
        assertThat(discount.getCost(Arrays.asList(pizza1, pizza2, pizza3))).isEqualTo(25.0);
    }

    @Test
    public void threePizzasDiscountEmptyList() {
        Discount discount = new ThreePizzasOneFreeDiscount(new NoDiscount());
        assertThat(discount.getCost(Collections.emptyList())).isEqualTo(0.0);
    }

    @Test
    public void threePizzasDiscountOtherThanThreePizzas() {
        Discount discount = new ThreePizzasOneFreeDiscount(new NoDiscount());
        assertThat(discount.getCost(Arrays.asList(pizza1))).isEqualTo(12.0);
    }

    @Test
    public void threePizzasDiscount() {
        Discount discount = new ThreePizzasOneFreeDiscount(new NoDiscount());
        assertThat(discount.getCost(Arrays.asList(pizza1, pizza2, pizza3))).isEqualTo(19.0);
    }

    @Test
    public void pineapplePizzasDiscountEmptyList() {
        Discount discount = new PineapplePizzaDiscount(new NoDiscount());
        assertThat(discount.getCost(Collections.emptyList())).isEqualTo(0.0);
    }

    @Test
    public void pineapplePizzasDiscountPizzaWithoutPineapple() {
        Discount discount = new PineapplePizzaDiscount(new NoDiscount());
        assertThat(discount.getCost(Arrays.asList(pizza2, pizza3))).isEqualTo(13.0);
    }

    @Test
    public void pineapplePizzasDiscountPizzaWithPineapple() {
        Discount discount = new PineapplePizzaDiscount(new NoDiscount());
        assertThat(discount.getCost(Arrays.asList(pizza1, pizza2, pizza3))).isEqualTo(23.7);
    }

    @Test
    public void AllDiscountsEmptyList() {
        Discount discount = new PineapplePizzaDiscount(new ThreePizzasOneFreeDiscount(new NoDiscount()));
        assertThat(discount.getCost(Collections.emptyList())).isEqualTo(0.0);
    }

    @Test
    public void AllDiscountsThreePizzasDiscountNotApplied() {
        Discount discount = new PineapplePizzaDiscount(new ThreePizzasOneFreeDiscount(new NoDiscount()));
        assertThat(discount.getCost(Arrays.asList(pizza1, pizza2))).isEqualTo(18.3);
    }

    @Test
    public void AllDiscountsPineappleDiscountNotApplied() {
        Discount discount = new PineapplePizzaDiscount(new ThreePizzasOneFreeDiscount(new NoDiscount()));
        assertThat(discount.getCost(Arrays.asList(pizza2, pizza3))).isEqualTo(13.0);
    }

    @Test
    public void AllDiscountsApplied() {
        Discount discount = new PineapplePizzaDiscount(new ThreePizzasOneFreeDiscount(new NoDiscount()));
        assertThat(discount.getCost(Arrays.asList(pizza1, pizza2, pizza3))).isEqualTo(17.7);
    }
}
