package com.dunno.recipeassistant;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dunno.recipeassistant.R;
import com.dunno.recipeassistant.RecipeListFragment;
import com.dunno.recipeassistant.ShoppingListFragment;

public class IngredientListAdapter extends RecyclerView.Adapter<IngredientListAdapter.ViewHolder> {
    private String[] mDataSet;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ingredient_item_image);
            textView = itemView.findViewById(R.id.ingredient_item_title);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public IngredientListAdapter(String[] dataSet) {
        this.mDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public IngredientListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // crate a new view

        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_ingredient_item, parent, false);

        return  new IngredientListAdapter.ViewHolder(item);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.textView.setText(this.mDataSet[position]);
    }

    @Override
    public int getItemCount() {
        return this.mDataSet.length;
    }

}
