package com.pucmm.sqlite;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.pucmm.sqlite.Services.DatabaseService;

import java.util.List;


public class ProductsFragment extends Fragment {
    EditText txtName, txtPrice;
    Spinner spnCategory;
    ArrayAdapter<String> arrayAdapter;
    List<String> lsCategories;
    Button btnAdd, btnDelete, btnUpdate, btnSave;
    DatabaseService databaseService;
    long productId;

    public ProductsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }

        View view = inflater.inflate(R.layout.fragment_products, container, false);

        txtName = view.findViewById(R.id.txt_name);
        txtPrice = view.findViewById(R.id.txt_price);
        btnAdd = view.findViewById(R.id.btn_add);
        btnUpdate = view.findViewById(R.id.btn_update);
        btnDelete = view.findViewById(R.id.btn_delete);
        btnSave = view.findViewById(R.id.btn_save);

        spnCategory = view.findViewById(R.id.spn_category);

        DatabaseService databaseService = new DatabaseService(getContext());

        // Setting up categories spinner
        lsCategories = databaseService.getCategories();
        arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, lsCategories);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spnCategory.setAdapter(arrayAdapter);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            productId = Long.parseLong(bundle.getString("id"));

            txtName.setText(bundle.getString("name"));
            txtPrice.setText(bundle.getString("price"));
            spnCategory.post(() -> spnCategory.setSelection(arrayAdapter.getPosition(bundle.getString("categoryName"))));

            btnSave.setVisibility(View.GONE);

            // Now delete and update are visible
            btnDelete.setVisibility(View.VISIBLE);
            btnUpdate.setVisibility(View.VISIBLE);
        }

        btnAdd.setOnClickListener(view1 -> {
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, new CategoriesFragment()).addToBackStack("categories").commit();
        });

        btnSave.setOnClickListener(view1 -> {
            String name = txtName.getText().toString();
            String categoryName = lsCategories.size() > 0 ? spnCategory.getSelectedItem().toString() : "";

            if (name.trim().length() == 0 || categoryName == "" || txtPrice.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
            } else {
                float price = Float.parseFloat(txtPrice.getText().toString());
                databaseService.createProduct(name, price, categoryName);
                Toast.makeText(getContext(), "Product created", Toast.LENGTH_SHORT).show();

                getFragmentManager().beginTransaction().replace(R.id.main_fragment, new ProductsListFragment(), "list").addToBackStack("list").commit();
            }
        });

        btnUpdate.setOnClickListener(view1 -> {
            String name = txtName.getText().toString();
            String categoryName = lsCategories.size() > 0 ? spnCategory.getSelectedItem().toString() : "";

            if (name.trim().length() == 0 || categoryName == "" || txtPrice.getText().toString().trim().length() == 0) {
                Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
            } else {
                float price = Float.parseFloat(txtPrice.getText().toString());
                databaseService.updateProduct(productId, name, price, categoryName);
                Toast.makeText(getContext(), "Product updated", Toast.LENGTH_SHORT).show();

                getFragmentManager().beginTransaction().replace(R.id.main_fragment, new ProductsListFragment(), "list").addToBackStack("list").commit();
            }
        });

        btnDelete.setOnClickListener(view1 -> {
            new AlertDialog.Builder(getContext()).setTitle("Delete Product").setMessage("Do you want to delete this product?").setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                databaseService.deleteProduct(productId);
                Toast.makeText(getContext(), "Product deleted", Toast.LENGTH_SHORT).show();
                getFragmentManager().beginTransaction().replace(R.id.main_fragment, new ProductsListFragment(), "list").addToBackStack("list").commit();
            })
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();
        });

        return view;
    }
}