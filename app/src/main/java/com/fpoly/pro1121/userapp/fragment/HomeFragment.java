package com.fpoly.pro1121.userapp.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.activities.ProductDetailsActivity;
import com.fpoly.pro1121.userapp.activities.SearchProductActivity;
import com.fpoly.pro1121.userapp.adapter.CategoryAdapter;
import com.fpoly.pro1121.userapp.adapter.ProductAdapter;
import com.fpoly.pro1121.userapp.adapter.SliderAdapter;
import com.fpoly.pro1121.userapp.model.Category;
import com.fpoly.pro1121.userapp.model.Product;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HomeFragment extends Fragment {

    View mView;
    SearchView mSearch;
    TextView tvHelloUser;
    SliderAdapter sliderAdapter;
    SliderView sliderView;
    RecyclerView rvCategory, rvProduct;
    CategoryAdapter categoryAdapter;
    ProductAdapter productAdapter;

    List<Category> listCategories;
    List<Product> listProducts;
    int[] imagesSlide = {R.drawable.banner1, R.drawable.banner2, R.drawable.banner3, R.drawable.banner};
    String idCategoryDefault = "86b7a60c-3043-4805-9a49-761697b88740";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        initUI();
        initSlider();
        initRecyclerCategory();
        initRecyclerProducts();
        actionSearch();
        realTimeDataBase();
        getNameUser();
        getListProductOfCategory(idCategoryDefault);// default show list of hamburger
        return mView;

    }
    // đọc dữ liệu categories realtime
    private void realTimeDataBase() {
        db.collection("categories")
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        try {
                            // tạo 1 list category để lưu dữ liệu
                            List<Category> clones = new ArrayList<>();
                            // danh sách document
                            List<DocumentSnapshot> snapshotsList = value.getDocuments();
                            for (DocumentSnapshot snapshot : snapshotsList) {
                                // document dạng map<key,value> nên  khởi tạo 1 map để chứa dữ liệu document
                                Map<String, Object> data = snapshot.getData();
                                assert data != null;
                                String id = Objects.requireNonNull(data.get("id")).toString();
                                String name = Objects.requireNonNull(data.get("name")).toString();
                                String urlImage = Objects.requireNonNull(data.get("urlImage")).toString();
                                Category category = new Category(id, name, urlImage);
                                // thêm đối tượng vào danh sách đã tạo
                                clones.add(category);
                            }
                            // làm mới listCategories
                            listCategories = new ArrayList<>();
                            // thêm tất cả các category trong clone vào listCategories
                            listCategories.addAll(clones);
                            // đỗ dữ liệu lên adapter
                            categoryAdapter.setData(listCategories);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    private void initUI() {
        mSearch = mView.findViewById(R.id.search_main_fragment);
        mSearch.setQueryHint("Search");
        tvHelloUser = mView.findViewById(R.id.tv_hello_user);
        rvCategory = mView.findViewById(R.id.rv_category);
        rvProduct = mView.findViewById(R.id.rv_product);

    }

    private void initSlider() {
        sliderView = mView.findViewById(R.id.sliderView);
        sliderAdapter = new SliderAdapter(requireContext(), imagesSlide);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }

    private void initRecyclerCategory() {
        categoryAdapter = new CategoryAdapter(this::getListProductOfCategory);
        categoryAdapter.setData(listCategories);
        rvCategory.setAdapter(categoryAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false);
        rvCategory.setLayoutManager(linearLayoutManager);

    }

    private void getListProductOfCategory(String idCategory) {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("loading....");
        progressDialog.show();
        db.collection("products")
                // select các product có categoryID = ifCategory truyền vào
                .whereEqualTo("categoryID", idCategory)
                .get()
                .addOnCompleteListener(task -> {
                    // trạng thái thành công
                    if (task.isSuccessful()) {
                        List<Product> clones = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            String id = (String) data.get("id");
                            String name = (String) data.get("name");
                            int price = ((Long) Objects.requireNonNull(data.get("price"))).intValue();
                            String categoryID;
                            categoryID = idCategory;
                            String urlImage = (String) data.get("urlImage");
                            String description = (String) data.get("description");
                            Product product = new Product(id, urlImage, name, price, description, categoryID);
                            clones.add(product);
                        }
                        listProducts = new ArrayList<>();
                        listProducts.addAll(clones);
                        // đỗ dữ liệu lên adapter
                        productAdapter.setData(listProducts);
                        progressDialog.dismiss();
                    } else {
                        Log.w("-->", "Error getting documents.", task.getException());
                    }
                });

    }

    private void initRecyclerProducts() {
        productAdapter = new ProductAdapter(product -> {
            Intent intent = new Intent(requireContext(), ProductDetailsActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        });
        productAdapter.setData(listProducts);
        rvProduct.setAdapter(productAdapter);
        rvProduct.setLayoutManager(new GridLayoutManager(requireContext(), 2));
    }

    @SuppressLint("SetTextI18n")
    private void getNameUser() {
        // select name current user đổ lên view
        DocumentReference ref = db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        ref.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    tvHelloUser.setText("Hello " + Objects.requireNonNull(document.get("name")).toString());
                } else {
                    Log.d("--->", "No such document");
                }
            } else {
                Log.d("--->", "get failed with ", task.getException());
            }

        });

    }

    private void actionSearch() {
        // khi click vào thanh tìm kiếm start activity Search
        mSearch.setOnClickListener(view -> {
            startActivity(new Intent(requireContext(), SearchProductActivity.class));
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
        });
    }

}
