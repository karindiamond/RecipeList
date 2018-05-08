package com.kld2.recipelist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karin on 7/11/2017.
 */

public class Recipe implements Serializable {

    private static final long serialVersionUID = 0L;

    private String name, link;
    private int prepTime;
    private int cookTime;
    private List<Ingredient> ingredients;
    private List<Direction> directions;

    Recipe(String name, String link, int prepTime, int cookTime) {
        this.name = name;
        this.link = link;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.ingredients = new ArrayList<>();
        this.directions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public int getPrepHours() {
        return prepTime / 60;
    }

    public int getPrepMinutes() {
        return prepTime % 60;
    }

    public int getCookTime() {
        return cookTime;
    }

    public int getCookHours() {
        return cookTime / 60;
    }

    public int getCookMinutes() {
        return cookTime % 60;
    }

    private int getTotalTime() {
        return prepTime + cookTime;
    }

    public String getTimes() {
        String result = Utils.timeToString(getTotalTime());
        if (prepTime != 0 && cookTime != 0) {
            result = "Prep Time: " + Utils.timeToString(prepTime)
                    + "\nCook Time: " + Utils.timeToString(cookTime)
                    + "\nTotal Time: " + result;
        }
        return result;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<Direction> getDirections() {
        return directions;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

}
