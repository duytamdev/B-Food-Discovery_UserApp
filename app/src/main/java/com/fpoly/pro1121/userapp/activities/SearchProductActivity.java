package com.fpoly.pro1121.userapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.adapter.ProductAdapter;
import com.fpoly.pro1121.userapp.model.Product;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.core.OrderBy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchProductActivity extends AppCompatActivity {

    Toolbar toolbar;
    SearchView searchView;
    RecyclerView rvProduct;
    ProductAdapter productAdapter;
    List<Product> list;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        initUI();
        initRecycler();
        searchProduct();
        readDataRealTime();
        initToolbar();
    }

    private void searchProduct() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                productAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                productAdapter.filter(newText);
                return true;
            }
        });
    }

    private void readDataRealTime() {
        ProgressDialog progressDialog = new ProgressDialog(SearchProductActivity.this);
        progressDialog.setMessage("loading....");
        progressDialog.show();
        db.collection("products")
                .orderBy("price", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Product> clones = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            String id = (String) data.get("id");
                            String name = (String) data.get("name");
                            int price = ((Long) Objects.requireNonNull(data.get("price"))).intValue();
                            String categoryID = (String) data.get("categoryID");
                            String urlImage = (String) data.get("urlImage");
                            String description = (String) data.get("description");
                            Product product = new Product(id, urlImage, name, price, description, categoryID);
                            clones.add(product);
                        }
                        list = new ArrayList<>();
                        list.addAll(clones);
                        productAdapter.setData(list);
                        progressDialog.dismiss();
                    } else {
                        Log.w("-->", "Error getting documents.", task.getException());
                    }
                });
    }

    private void initRecycler() {
        productAdapter = new ProductAdapter(product -> {
            Intent intent = new Intent(SearchProductActivity.this, ProductDetailsActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        });
        productAdapter.setData(list);
        rvProduct.setAdapter(productAdapter);
        rvProduct.setLayoutManager(new GridLayoutManager(this, 2));
    }


    private void initUI() {
        searchView = findViewById(R.id.search_product);
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Search");
        rvProduct = findViewById(R.id.rv_product_search);
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar_search);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_baseline_keyboard_backspace_24);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}