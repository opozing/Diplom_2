package org.example;

import java.util.List;

public class CreateOrderPOJO {
    private List<String> ingredients;

    public CreateOrderPOJO(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
