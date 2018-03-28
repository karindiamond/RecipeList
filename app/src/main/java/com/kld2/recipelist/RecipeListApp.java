package com.kld2.recipelist;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karin on 7/13/2017.
 */

public class RecipeListApp extends Application {

    //TODO move loading of this list to this class? (with a getter containing a null check)
    //TODO add static add to list method
    public static List<Recipe> globalRecipeList = new ArrayList<>();

}
