package com.kld2.recipelist;

import java.io.Serializable;

/**
 * Created by Karin on 7/11/2017.
 */

public class Recipe implements Serializable {

    private String name, link;
    private int prepTime;
    private int cookTime;

    public Recipe(String name, String link, int prepTime, int cookTime) {
        this.name = name;
        this.link = link;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public Integer getTotalTime() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

}
