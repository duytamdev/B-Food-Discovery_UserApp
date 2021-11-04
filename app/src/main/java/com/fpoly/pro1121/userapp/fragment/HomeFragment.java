package com.fpoly.pro1121.userapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.activities.SearchProductActivity;
import com.fpoly.pro1121.userapp.adapter.CategoryAdapter;
import com.fpoly.pro1121.userapp.adapter.SliderAdapter;
import com.fpoly.pro1121.userapp.model.Category;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
    RecyclerView rvCategory;
    CategoryAdapter categoryAdapter;
    List<Category> listCategories;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    int[] imagesSlide ={R.drawable.banner,R.drawable.banner,R.drawable.banner,R.drawable.banner};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home,container,false);
        initUI();
        initSlider();
        initRecyclerCategory();
        actionSearch();
        realTimeDataBase();
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
        categoryAdapter = new CategoryAdapter();
        categoryAdapter.setData(listCategories);
        rvCategory.setAdapter(categoryAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext(),RecyclerView.HORIZONTAL,false);
        rvCategory.setLayoutManager(linearLayoutManager);

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
