package com.shubhamwithcode.mymall.Adapters;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shubhamwithcode.mymall.Models.sliderModel;
import com.shubhamwithcode.mymall.R;

import java.util.List;

public class sliderAdapter extends PagerAdapter {

    List<sliderModel> sliderModelList;

    public sliderAdapter(List<sliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View  view = LayoutInflater.from(container.getContext()).inflate(R.layout.sider_layout,container,false);
        ConstraintLayout banner_contenor = view.findViewById(R.id.banner_slider_Container);
        banner_contenor.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(sliderModelList.get(position).getBackground())));
        ImageView banner = view.findViewById(R.id.banner_slider);
        Glide.with(container.getContext()).load(sliderModelList.get(position).getBanner()).apply(new RequestOptions().placeholder(R.drawable.blank_big)).into(banner);
        container.addView(view,0);
        return view ;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return sliderModelList.size();
    }

}
