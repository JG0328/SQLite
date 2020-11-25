package com.pucmm.sqlite;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pucmm.sqlite.Services.DatabaseService;

public class CategoriesFragment extends Fragment {
    EditText txtCategoryName;
    Button btnSave;
    DatabaseService databaseService;

    public CategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container != null) container.removeAllViews();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        txtCategoryName = view.findViewById(R.id.txt_category_name);

        btnSave = view.findViewById(R.id.btn_create_category);
        btnSave.setOnClickListener(view1 -> {
            String name = txtCategoryName.getText().toString();

            if (name.trim().length() == 0)
                Toast.makeText(getContext(), "A name must be provided", Toast.LENGTH_SHORT).show();
            else {
                databaseService = new DatabaseService(getContext());
                databaseService.createCategory(name);
                Toast.makeText(getContext(), "Category created", Toast.LENGTH_SHORT).show();

                getFragmentManager().beginTransaction().replace(R.id.main_fragment, new ProductsFragment(), "products").commit();
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        databaseService.closeConnection();
        super.onDestroy();
    }
}