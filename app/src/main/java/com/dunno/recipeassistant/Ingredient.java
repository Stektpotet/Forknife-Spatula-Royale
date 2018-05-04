package com.dunno.recipeassistant;

import android.provider.BaseColumns;

public class Ingredient {

    public int id = 0;
    public String name = "";
    String unit = "units";
    float amount = 0;

    @Override
    public String toString() {
        return name;
    }

    static class Entry implements BaseColumns {
        static final String TABLE_NAME = "ingredient";
        static final String COLUMN_NAME_ID = "_ID";
        static final String COLUMN_NAME_NAME = "name";
        static final String COLUMN_NAME_UNIT = "unit";

    }
}
