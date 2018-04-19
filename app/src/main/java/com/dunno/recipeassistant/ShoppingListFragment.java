package com.dunno.recipeassistant;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ShoppingListFragment extends Fragment {
    public ShoppingListFragment() {}

    public static ShoppingListFragment newInstance(int entry) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        //SET ARGS
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_shoppinglist, container, false);
        MainActivity activity = (MainActivity) getActivity();

        return rootView;
    }

}
