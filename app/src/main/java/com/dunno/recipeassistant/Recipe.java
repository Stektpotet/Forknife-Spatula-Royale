package com.dunno.recipeassistant;

import android.provider.BaseColumns;

public class Recipe {
    public int id = 0;
    public String title = "";
    public String time = "";
    public String description = "";
    public String instructions = "";
    public float hasPercentage = 0;


    public static class Entry implements BaseColumns {
        public static final String TABLE_NAME = "recipe";
        public static final String COLUMN_NAME_ID = "_ID";
        public static final String COLUMN_NAME_Title = "title";
        public static final String COLUMN_NAME_Time = "cookTime";
        public static final String COLUMN_NAME_Description = "description";
        public static final String COLUMN_NAME_Instructions = "instructions";
        // Image path.
    }


}
