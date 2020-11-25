package com.pucmm.sqlite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.pucmm.sqlite.Services.DatabaseService;

public class ProductsListFragment extends Fragment {
    DatabaseService databaseService;
    SimpleCursorAdapter simpleCursorAdapter;
    ListView listView;

    public ProductsListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (container != null) container.removeAllViews();

        View view = inflater.inflate(R.layout.fragment_products_list, container, false);

        listView = view.findViewById(R.id.products_list_view);

        databaseService = new DatabaseService(getContext());
        databaseService.openConnection();

        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.item_list, databaseService.getProducts(), new String[]{"_id", "name", "price", "categoryName"}, new int[]{R.id.product_id, R.id.product_name, R.id.product_price, R.id.product_category}, 0);
        simpleCursorAdapter.notifyDataSetChanged();

        listView.setAdapter(simpleCursorAdapter);

        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            TextView productId = view.findViewById(R.id.product_id);
            TextView productName = view.findViewById(R.id.product_name);
            TextView productPrice = view.findViewById(R.id.product_price);
            TextView productCategoryName = view.findViewById(R.id.product_category);

            ProductsFragment productsFragment = new ProductsFragment();

            Bundle bundle = new Bundle();
            bundle.putString("id", productId.getText().toString());
            bundle.putString("name", productName.getText().toString());
            bundle.putString("categoryName", productCategoryName.getText().toString());
            bundle.putString("price", productPrice.getText().toString());

            productsFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.main_fragment, productsFragment, "products").addToBackStack("products").commit();
        });

        return view;
    }

    @Override
    public void onDestroy() {
        databaseService.closeConnection();

        super.onDestroy();
    }
}