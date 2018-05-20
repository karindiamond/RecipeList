package com.kld2.recipelist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Direction implements Serializable {

    private static final long serialVersionUID = 2000L;

    private String text;
    private List<Ingredient> ingredients;

    Direction(String text) {
        this.text = text;
        this.ingredients = new ArrayList<>();
    }

    public String getText() {
        return text;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setText(String text) {
        this.text = text;
    }
}
