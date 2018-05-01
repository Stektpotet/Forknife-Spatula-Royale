package com.dunno.recipeassistant;

import android.provider.BaseColumns;

public class Ingredient {

    public int id = 0;
    public String name = "";

    public static class Entry implements BaseColumns {
        public static final String TABLE_NAME = "ingredient";
        public static final String COLUMN_NAME_NAME = "name";
    }
}
