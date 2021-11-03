package com.fpoly.pro1121.userapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fpoly.pro1121.userapp.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderViewHolder>{

    int[] imagesSlide;
    Context context;
    public SliderAdapter(Context context, int[] imagesSlide) {
        this.context = context;
        this.imagesSlide = imagesSlide;

    }
    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.slider_item,parent,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        viewHolder.ivImageSlider.setImageResource(imagesSlide[position]);
    }

    @Override
    public int getCount() {
        if(imagesSlide!=null) return imagesSlide.length;
        return 0;
    }

    public class SliderViewHolder extends SliderViewAdapter.ViewHolder {
        public ImageView ivImageSlider;
        public SliderViewHolder(View itemView) {
            super(itemView);
            ivImageSlider = itemView.findViewById(R.id.iv_slider);
        }
    }
}
