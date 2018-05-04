package com.dunno.recipeassistant;

import android.graphics.Color;
import android.provider.BaseColumns;

import com.yalantis.filter.model.FilterModel;

import org.jetbrains.annotations.NotNull;

public class RecipeTag implements FilterModel {

    static final float SATURATION_HARD = 1.0f;


    static int GetColor(int i, int count, float saturation) {

        float hue = ((float)i*360/count);
        return Color.HSVToColor(new float[]{hue, saturation, 0.8f});
    }

    RecipeTag(int id, String tag) {
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

    static class Entry implements BaseColumns {
        static final String TABLE_NAME = "recipeTag";
        static final String COLUMN_NAME_recipeId = "recipeId";
        static final String COLUMN_NAME_tagId = "tagId";
    }
}
