package com.fpoly.pro1121.userapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.fpoly.pro1121.userapp.R;
import com.fpoly.pro1121.userapp.adapter.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

public class HomeFragment extends Fragment {

    View mView;
    SearchView mSearch;
    TextView tvHelloUser;
    SliderAdapter sliderAdapter;
    SliderView sliderView;
    int[] imagesSlide ={R.drawable.banner,R.drawable.banner,R.drawable.banner,R.drawable.banner};
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home,container,false);
        initUI();
        initSlider();
        return mView;

    }
    private void initUI() {
        mSearch = mView.findViewById(R.id.search_main_fragment);
        mSearch.setIconifiedByDefault(false);
        mSearch.setQueryHint("Search");
        tvHelloUser = mView.findViewById(R.id.tv_hello_user);
    }

    private void initSlider() {
        sliderView = mView.findViewById(R.id.sliderView);
        sliderAdapter = new SliderAdapter(requireContext(),imagesSlide);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();
    }

}
