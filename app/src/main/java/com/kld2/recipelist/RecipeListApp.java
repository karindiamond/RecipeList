package com.kld2.recipelist;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karin on 7/13/2017.
 */

public class RecipeListApp extends Application {

    public List<Recipe> globalRecipeList = new ArrayList<>();

}
