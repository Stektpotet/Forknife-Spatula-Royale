package com.dunno.recipeassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CommitRecipeActivity extends AppCompatActivity {

    private Button doneButton;
    private View.OnClickListener doneButtonListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_recipe);


        doneButton = findViewById(R.id.commit_button_done);
        View.OnClickListener doneButtonListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        };doneButton.setOnClickListener(doneButtonListener);
    }
}
