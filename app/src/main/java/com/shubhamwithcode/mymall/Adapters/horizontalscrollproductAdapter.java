package com.shubhamwithcode.mymall.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shubhamwithcode.mymall.Models.horizontalscrollproductModel;
import com.shubhamwithcode.mymall.Product_Detail_Activity;
import com.shubhamwithcode.mymall.R;

import java.util.List;

public class horizontalscrollproductAdapter extends RecyclerView.Adapter<horizontalscrollproductAdapter.viewHolder> {

    List<horizontalscrollproductModel> horizontalscrollproductModelList;

    public horizontalscrollproductAdapter(List<horizontalscrollproductModel> horizontalscrollproductModelList) {
        this.horizontalscrollproductModelList = horizontalscrollproductModelList;
    }

    @NonNull
    @Override
    public horizontalscrollproductAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull horizontalscrollproductAdapter.viewHolder holder, int position) {
        String resource = horizontalscrollproductModelList.get(position).getProduceImage();
        String Title = horizontalscrollproductModelList.get(position).getProductTitle();
        String Discriptions = horizontalscrollproductModelList.get(position).getProductDescription();
        String Price = horizontalscrollproductModelList.get(position).getProductPrice();
        String productId = horizontalscrollproductModelList.get(position).getProductID();

      holder.setData(productId,resource,Title,Discriptions,Price);

    }

    @Override
    public int getItemCount() {
        if (horizontalscrollproductModelList.size() > 4) {
           return 4;
        } else {
            return horizontalscrollproductModelList.size();
        }
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productTitle;
        TextView productDiscriptions;
        TextView productPrice;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.h_s_product_image);
            productTitle = itemView.findViewById(R.id.h_s_product_titale);
            productDiscriptions = itemView.findViewById(R.id.h_s_product_discriptions);
            productPrice = itemView.findViewById(R.id.h_s_product_price);


        }
        private void setData(String productId,String resource,String Title,String Discriptions,String Price){
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.blank_small)).into(productImage);
            productTitle.setText(Title);
            productDiscriptions.setText(Discriptions);
            productPrice.setText("Rs."+Price+"/-");

            if (!Title.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent productDetailActivityIntent = new Intent(itemView.getContext(), Product_Detail_Activity.class);
                        productDetailActivityIntent.putExtra("PRODUCT_ID",productId);
                        itemView.getContext().startActivity(productDetailActivityIntent);
                    }
                });
            }
        }
    }
}
