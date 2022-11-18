package com.shubhamwithcode.mymall.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shubhamwithcode.mymall.DBqueries;
import com.shubhamwithcode.mymall.Models.WishlistModel;
import com.shubhamwithcode.mymall.Product_Detail_Activity;
import com.shubhamwithcode.mymall.R;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.ViewHolder> {
    private boolean fromSearch;
    List<WishlistModel> wishlistModelList;
    private boolean wishlist;
    private int lastPosition = -1;

    public boolean isFromSearch() {
        return fromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
    }

    public WishlistAdapter(List<WishlistModel> wishlistModelList, boolean wishlist) {
        this.wishlistModelList = wishlistModelList;
        this.wishlist = wishlist;
    }

    public List<WishlistModel> getWishlistModelList() {
        return wishlistModelList;
    }

    public void setWishlistModelList(List<WishlistModel> wishlistModelList) {
        this.wishlistModelList = wishlistModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String productId = wishlistModelList.get(position).getProductId();
        String resource = wishlistModelList.get(position).getProductImage();
        String title = wishlistModelList.get(position).getProductTitle();
        long freeCoupens = wishlistModelList.get(position).getFreeCoupens();
        String rating = wishlistModelList.get(position).getRating();
        long TotalRating = wishlistModelList.get(position).getTotalRating();
        String productPrice = wishlistModelList.get(position).getProductPrice();
        String cuttedPrice = wishlistModelList.get(position).getCuttedPrice();
        boolean paymentMethod = wishlistModelList.get(position).isCOD();
        boolean inStock = wishlistModelList.get(position).isInStock();

        holder.setData(productId,resource,title,freeCoupens,rating,TotalRating,productPrice,cuttedPrice,paymentMethod,position,inStock);

        if (lastPosition < position){
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return wishlistModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView productTitle;
        private TextView freeCoupens;
        private ImageView coupenIcon;
        private TextView rating;
        private TextView totalRating;
        private View priceCut;
        private TextView productPrice;
        private TextView cuttedPrice;
        private TextView paymentMethod;
        private ImageView deletebtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.wishlist_product_image);
            productTitle = itemView.findViewById(R.id.wishlist_product_title);
            freeCoupens = itemView.findViewById(R.id.wishlist_product_tv_free_coupens);
            coupenIcon = itemView.findViewById(R.id.wishlist_product_coupens_icon);
            rating = itemView.findViewById(R.id.tv_product_rating_miniview);
            totalRating = itemView.findViewById(R.id.wishlist_product_total_rating);
            priceCut = itemView.findViewById(R.id.wishlist_product_price_cute);
            productPrice = itemView.findViewById(R.id.wishlist_product_price);
            cuttedPrice = itemView.findViewById(R.id.wishlist_product_cutted_price);
            paymentMethod = itemView.findViewById(R.id.wishlist_product_payment_method);
            deletebtn = itemView.findViewById(R.id.wishlist_product_delete_btn);
        }
        private void setData(String productId,String Resource, String title, long freeCoupensNo, String averageRate, long totalRatingsNo, String price, String cuttedPriceValue, Boolean COD,int index,boolean inStock){
            Glide.with(itemView.getContext()).load(Resource).apply(new RequestOptions().placeholder(R.drawable.blank_small)).into(productImage);
            productTitle.setText(title);
            if (freeCoupensNo != 0 && inStock){
                coupenIcon.setVisibility(View.VISIBLE);
                if (freeCoupensNo == 1){
                    freeCoupens.setText("free "+ freeCoupensNo +" coupen");
                }else {
                    freeCoupens.setText("free "+ freeCoupensNo +" coupens");
                }
            }else {
                coupenIcon.setVisibility(View.INVISIBLE);
                freeCoupens.setVisibility(View.INVISIBLE);
            }
            LinearLayout linearLayout = (LinearLayout) rating.getParent();
            if (inStock) {
                rating.setVisibility(View.VISIBLE);
                totalRating.setVisibility(View.VISIBLE);
                productPrice.setTextColor(Color.parseColor("#000000"));
                cuttedPrice.setVisibility(View.VISIBLE);
                linearLayout.setVisibility(View.VISIBLE);

                rating.setText(averageRate);
                totalRating.setText("(" + totalRatingsNo + ")ratings");
                productPrice.setText("Rs." + price + "/-");
                cuttedPrice.setText("Rs." + cuttedPriceValue + "/-");
                if (COD) {
                    paymentMethod.setVisibility(View.VISIBLE);
                } else {
                    paymentMethod.setVisibility(View.INVISIBLE);
                }
            }else {
                linearLayout.setVisibility(View.INVISIBLE);
                rating.setVisibility(View.INVISIBLE);
                totalRating.setVisibility(View.INVISIBLE);
                productPrice.setText("Out of Stock");
                productPrice.setTextColor(itemView.getContext().getResources().getColor(R.color.colour_primary));
                cuttedPrice.setVisibility(View.INVISIBLE);
                paymentMethod.setVisibility(View.INVISIBLE);

            }
            if (wishlist){
                deletebtn.setVisibility(View.VISIBLE);
            }else {
                deletebtn.setVisibility(View.GONE);
            }


            deletebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Product_Detail_Activity.running_wishlist_query) {
                        Product_Detail_Activity.running_wishlist_query = true;
                        DBqueries.removeFromWishlist(index, itemView.getContext());
                    }
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fromSearch){
                        Product_Detail_Activity.fromSearch = true;
                    }
                    Intent productDetailIntent = new Intent(itemView.getContext(), Product_Detail_Activity.class);
                    productDetailIntent.putExtra("PRODUCT_ID",productId);
                    itemView.getContext().startActivity(productDetailIntent);
                }
            });
        }
    }
}
