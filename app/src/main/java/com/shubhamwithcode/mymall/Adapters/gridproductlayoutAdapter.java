package com.shubhamwithcode.mymall.Adapters;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shubhamwithcode.mymall.Models.horizontalscrollproductModel;
import com.shubhamwithcode.mymall.Product_Detail_Activity;
import com.shubhamwithcode.mymall.R;

import java.util.List;

public class gridproductlayoutAdapter extends BaseAdapter {

    List<horizontalscrollproductModel> horizontalscrollproductModelList;

    public gridproductlayoutAdapter(List<horizontalscrollproductModel> horizontalscrollproductModelList) {
        this.horizontalscrollproductModelList = horizontalscrollproductModelList;
    }

    @Override
    public int getCount() {
        return horizontalscrollproductModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       View view;
       if (convertView == null){
           view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,null);
           view.setElevation(0);
           view.setBackgroundColor(Color.parseColor("#ffffff"));

           view.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent product_derail_intent = new Intent(parent.getContext(), Product_Detail_Activity.class);
                   product_derail_intent.putExtra("PRODUCT_ID",horizontalscrollproductModelList.get(position).getProductID());
                   parent.getContext().startActivity(product_derail_intent);
               }
           });

           ImageView productImage = view.findViewById(R.id.h_s_product_image);
           TextView productTitle = view.findViewById(R.id.h_s_product_titale);
           TextView productDescriptions = view.findViewById(R.id.h_s_product_discriptions);
           TextView productPrice = view.findViewById(R.id.h_s_product_price);

           Glide.with(parent.getContext()).load(horizontalscrollproductModelList.get(position).getProduceImage()).apply(new RequestOptions().placeholder(R.drawable.blank_small)).into(productImage);
           productTitle.setText(horizontalscrollproductModelList.get(position).getProductTitle());
           productDescriptions.setText(horizontalscrollproductModelList.get(position).getProductDescription());
           productPrice.setText("Rs."+horizontalscrollproductModelList.get(position).getProductPrice()+"/-");

       }else {
           view = convertView;
       }
       return view;
    }
}
