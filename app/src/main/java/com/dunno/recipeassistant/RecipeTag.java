package com.dunno.recipeassistant;

import android.graphics.Color;
import android.provider.BaseColumns;

import com.yalantis.filter.model.FilterModel;

import org.jetbrains.annotations.NotNull;

public class RecipeTag implements FilterModel {

    public static final float SATURATION_BLEACH = 0.4f;
    public static final float SATURATION_HARD = 1.0f;


    public static int GetColor(int i, int count, float saturation) {

        float hue = ((float)i*360/count);
        return Color.HSVToColor(new float[]{hue, saturation, 0.8f});
    }

    public RecipeTag(int id, String tag) {
        this.mTag = tag;
        this.id = id;
    }

    private int id;
    private String mTag;

    @NotNull
    @Override
    public String getText() {
        return mTag;
    }

    public int getId() {
        return id;
    }

    public static class Entry implements BaseColumns {
        public static final String TABLE_NAME = "recipeTag";
        public static final String COLUMN_NAME_recipeId = "recipeId";
        public static final String COLUMN_NAME_tagId = "tagId";
    }
}
