package com.kld2.recipelist;

import android.text.Html;
import android.text.Spanned;

import java.io.Serializable;

public class Ingredient implements Serializable {

    private static final long serialVersionUID = 1000L;

    private int quantityNumerator;
    private int quantityDenominator;
    private String unit;
    private String name;
    private String note;

    Ingredient(int quantityNumerator, int quantityDenominator, String unit, String name, String note) {
        this.quantityNumerator = quantityNumerator;
        this.quantityDenominator = quantityDenominator;
        this.unit = unit;
        this.name = name;
        this.note = note;
    }

    public int getQuantityNumerator() {
        return quantityNumerator;
    }

    public int getQuantityDenominator() {
        return quantityDenominator;
    }

    public String getUnit() {
        return unit;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public void setQuantityNumerator(int quantityNumerator) {
        this.quantityNumerator = quantityNumerator;
    }

    public void setQuantityDenominator(int quantityDenominator) {
        this.quantityDenominator = quantityDenominator;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Spanned getIngredientString() {
        String ingredient = "";
        if (quantityDenominator == 1) {
            ingredient += quantityNumerator;
        } else if (quantityNumerator % quantityDenominator == 0) {
            ingredient += (quantityNumerator / quantityDenominator);
        } else {
            if (quantityNumerator > quantityDenominator) {
                ingredient += (quantityNumerator / quantityDenominator);
            }
            ingredient += "<sup>" + (quantityNumerator % quantityDenominator) + "</sup>/<sub>" + quantityDenominator + "</sub>";
        }
        if (!unit.isEmpty()) {
            ingredient += " " + unit;
        }
        ingredient += " " + name;
        if (!note.isEmpty()) {
            ingredient += " (" + note + ")";
        }
        return Html.fromHtml(ingredient, Html.FROM_HTML_MODE_LEGACY);
    }
}
