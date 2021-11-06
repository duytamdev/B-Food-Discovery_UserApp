package com.fpoly.pro1121.userapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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
import com.fpoly.pro1121.userapp.activities.LoginActivity;
import com.fpoly.pro1121.userapp.activities.ProductDetailsActivity;
import com.fpoly.pro1121.userapp.activities.SearchProductActivity;
import com.fpoly.pro1121.userapp.adapter.CategoryAdapter;
import com.fpoly.pro1121.userapp.adapter.ProductAdapter;
import com.fpoly.pro1121.userapp.adapter.SliderAdapter;
import com.fpoly.pro1121.userapp.model.Category;
import com.fpoly.pro1121.userapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    RecyclerView rvCategory,rvProduct;
    CategoryAdapter categoryAdapter;
    ProductAdapter productAdapter;

    List<Category> listCategories;
    List<Product> listProducts;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    int[] imagesSlide ={R.drawable.banner,R.drawable.banner,R.drawable.banner,R.drawable.banner};
    String idCategoryHamburger = "914981de-654f-47ea-a2ff-17b116f52719";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home,container,false);
        initUI();
        initSlider();
        initRecyclerCategory();
        initRecyclerProducts();
        actionSearch();
        realTimeDataBase();
        getNameUser();
        getListProductOfCategory(idCategoryHamburger);// default show list of hamburger
        return mView;

    }

    private void realTimeDataBase() {
        db.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value!=null){
                            try {
                                List<Category> clones = new ArrayList<>();
                                List<DocumentSnapshot> snapshotsList = value.getDocuments();
                                for(DocumentSnapshot snapshot:  snapshotsList){
                                    Map<String, Object> data = snapshot.getData();
                                    assert data != null;
                                    String id = Objects.requireNonNull(data.get("id")).toString();
                                    String name = Objects.requireNonNull(data.get("name")).toString();
                                    String urlImage = Objects.requireNonNull(data.get("urlImage")).toString();
                                    Category category = new Category(id,name,urlImage);
                                    clones.add(category);
                                }
                                listCategories = new ArrayList<>();
                                listCategories.addAll(clones);
                                categoryAdapter.setData(listCategories);
                            }catch(Exception e) {
                                e.printStackTrace();
                            }
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
        sliderAdapter = new SliderAdapter(requireContext(),imagesSlide);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }

    private void initRecyclerCategory() {
        categoryAdapter = new CategoryAdapter(new CategoryAdapter.IClickCategoryListener() {
            @Override
            public void clickCategory(String idCategory) {
                getListProductOfCategory(idCategory);
            }
        });
        categoryAdapter.setData(listCategories);
        rvCategory.setAdapter(categoryAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false);
        rvCategory.setLayoutManager(linearLayoutManager);

    }

    private void getListProductOfCategory(String idCategory) {
        ProgressDialog progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("loading....");
        progressDialog.show();
        db.collection("products")
                .whereEqualTo("categoryID",idCategory)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Product> clones = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String,Object> data = document.getData();
                                String id = (String) data.get("id");
                                String name = (String) data.get("name");
                                int price =( (Long) data.get("price")).intValue();
                                String categoryID = idCategory;
                                String urlImage = (String) data.get("urlImage");
                                String description = (String) data.get("description");
                                Product product = new Product(id,urlImage,name,price,description,categoryID);
                                clones.add(product);
                            }
                            listProducts = new ArrayList<>();
                            listProducts.addAll(clones);
                            productAdapter.setData(listProducts);
                            progressDialog.dismiss();
                        } else {
                            Log.w("-->", "Error getting documents.", task.getException());
                        }
                    }
                });

    }

    private void initRecyclerProducts() {
        productAdapter = new ProductAdapter(new ProductAdapter.IClickProductListener() {
            @Override
            public void clickShowDetail(Product product) {
                Intent intent= new Intent(requireContext(), ProductDetailsActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });
        productAdapter.setData(listProducts);
        rvProduct.setAdapter(productAdapter);
        rvProduct.setLayoutManager(new GridLayoutManager(requireContext(),2));
    }
    private void getNameUser(){
        DocumentReference ref = db.collection("users").document(Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tvHelloUser.setText("Hello "+document.get("name").toString());
                    } else {
                        Log.d("--->", "No such document");
                    }
                } else {
                    Log.d("--->", "get failed with ", task.getException());
                }

            }
        });

    }
    private void actionSearch() {
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(requireContext(), SearchProductActivity.class));
                requireActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_left);
            }
        });
    }

}
