package com.kld2.recipelist;

import android.app.Application;
import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Karin on 7/13/2017.
 */

public class RecipeListApp extends Application {

    private List<Recipe> recipeList;
    private final String fileName = "RecipeList.txt";

    public List<Recipe> getRecipeList() {
        if (recipeList == null) {
            loadRecipeData();
        }
        return recipeList;
    }

    private void loadRecipeData() {
        // Handle case where the file does not yet exist (new install)
        //File file = new File(getFilesDir() + File.separator + fileName);
        //if (!this.getFileStreamPath(fileName).exists()) {
//        if (!file.exists()) {
//            recipeList = new ArrayList<>();
//            return;
//        }

        try {
            FileInputStream fileInputStream = openFileInput(fileName);
            ObjectInputStream is = new ObjectInputStream(fileInputStream);
            //noinspection unchecked -- the below case should be safe so we suppress the warning due to type erasure
            recipeList = (List<Recipe>)is.readObject();
            is.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // TODO this is a fix for the commented code above. Determine why file.exists() was always true, even directly after uninstall
        if (recipeList == null) {
            recipeList = new ArrayList<>();
        }
    }

    public void saveRecipeData() {
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fileOutputStream);
            os.writeObject(recipeList);
            os.flush();
            os.close();
            fileOutputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
