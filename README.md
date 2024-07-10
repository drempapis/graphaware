The client also has some discount rules already in place (buy a pineapple pizza, get 10% off
the others) and he wants to have a new discount rule ASAP: if the customer buys 3 pizzas,
the cheapest of the 3 is free. We expect you to implement this. No need to fix the whole
application, just make sure the new rule is working correctly (the client has a limited
budget).


To implement it, I assumed that the discounts are chained and that the three-pizza discount is applied only if the number of pizzas is equal to three.
In any case, I implemented it in a way that makes it easy to modify the code by only using unit tests.

The PurchaseService:computeAmount method changes to

    private Double computeAmount(List<Pizza> pizzas) {
        Discount discount = new PineapplePizzaDiscount(new ThreePizzasOneFreeDiscount(new NoDiscount()));
        return discount.getCost(pizzas);
    }