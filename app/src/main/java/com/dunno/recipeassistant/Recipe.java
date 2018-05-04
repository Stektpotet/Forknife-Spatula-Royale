package com.dunno.recipeassistant;

import android.provider.BaseColumns;
import android.support.annotation.NonNull;

import java.text.DateFormat;
import java.util.Comparator;

public class Recipe implements Comparable<Recipe>{
    public int id = 0;
    public String title = "";
    int timeInMinutes;
    String description = "";
    String instructions = "";
    public String image = "";

    float hasPercentage = 0;

    @Override //default sorting
    public int compareTo(@NonNull Recipe recipe) {
        return title.compareTo(recipe.title);
    }


    static class Entry implements BaseColumns {
        static final String TABLE_NAME = "recipe";
        static final String COLUMN_NAME_ID = "_ID";
        static final String COLUMN_NAME_Image = "image";
        static final String COLUMN_NAME_Title = "title";
        static final String COLUMN_NAME_Time = "cookTime";
        static final String COLUMN_NAME_Description = "description";
        static final String COLUMN_NAME_Instructions = "instructions";

    }

    static final Comparator<Recipe> BY_NAME_ALPHABETICAL = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe recipe, Recipe t1) {
            return recipe.compareTo(t1);
        }
    };

    static final Comparator<Recipe> BY_NAME_ALPHABETICAL_DESCENDING = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe recipe, Recipe t1) {
            return -BY_NAME_ALPHABETICAL.compare(recipe, t1);
        }
    };

    static final Comparator<Recipe> BY_TIME_ASCENDING = new Comparator<Recipe>() {

        @Override
        public int compare(Recipe recipe, Recipe t1) {
            return Integer.compare(recipe.timeInMinutes, t1.timeInMinutes);
        }
    };

    static final Comparator<Recipe> BY_TIME_DESCENDING = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe recipe, Recipe t1) {
            return -BY_TIME_ASCENDING.compare(recipe, t1);
        }
    };

    static final Comparator<Recipe> BY_PERCENTAGE_ASCENDING = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe recipe, Recipe t1) {
            return Float.compare(recipe.hasPercentage, t1.hasPercentage);
        }
    };

    static final Comparator<Recipe> BY_PERCENTAGE_DESCENDING = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe recipe, Recipe t1) {
            return -BY_PERCENTAGE_ASCENDING.compare(recipe, t1);
        }
    };
}
