package com.kld2.recipelist;

import java.io.Serializable;

/**
 * Created by Karin on 7/11/2017.
 */

public class Recipe implements Serializable {

    private String name, link;

    public Recipe() {
    }

    public Recipe(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

}
