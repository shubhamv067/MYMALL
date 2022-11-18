package com.shubhamwithcode.mymall.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shubhamwithcode.mymall.Models.HomePageModel;
import com.shubhamwithcode.mymall.Models.WishlistModel;
import com.shubhamwithcode.mymall.Models.horizontalscrollproductModel;
import com.shubhamwithcode.mymall.Models.sliderModel;
import com.shubhamwithcode.mymall.Product_Detail_Activity;
import com.shubhamwithcode.mymall.R;
import com.shubhamwithcode.mymall.view_All_Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePageAdapter extends RecyclerView.Adapter {

    List<HomePageModel> homePageModelList;
    private RecyclerView.RecycledViewPool recycledViewPool;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private int lastPosition = -1;

    public HomePageAdapter(List<HomePageModel> homePageModelList) {
        this.homePageModelList = homePageModelList;
        recycledViewPool = new RecyclerView.RecycledViewPool();
    }

    @Override
    public int getItemViewType(int position) {
        switch (homePageModelList.get(position).getType()) {
            case 0:
                return HomePageModel.BANNER_SLIDER;
            case 1:
                return HomePageModel.STRIP_AD_BANNER;
            case 2:
                return HomePageModel.HORIZONTAL_PRODUCT_VIEW;
            case 3:
                return HomePageModel.GRID_PRODUCT_VIEW;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HomePageModel.BANNER_SLIDER:
                View SliderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sliding_ad_banner, parent, false);
                return new bannersliderViewHolder(SliderView);

            case HomePageModel.STRIP_AD_BANNER:
                View StripView = LayoutInflater.from(parent.getContext()).inflate(R.layout.strip_ad_layout, parent, false);
                return new StripAdViewHolder(StripView);
            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                View HorizontalProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_layout, parent, false);
                return new HorizontalProductViewHolder(HorizontalProductView);
            case HomePageModel.GRID_PRODUCT_VIEW:
                View GridProductView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_product_layout, parent, false);
                return new GridProductViewHolder(GridProductView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        switch (homePageModelList.get(position).getType()) {
            case HomePageModel.BANNER_SLIDER:
                List<sliderModel> sliderModelList = homePageModelList.get(position).getSliderModelList();
                ((bannersliderViewHolder) holder).setBannerSliderViewPager(sliderModelList);
                break;
            case HomePageModel.STRIP_AD_BANNER:
                String resource = homePageModelList.get(position).getResource();
                String color = homePageModelList.get(position).getBackgroundColor();
                ((StripAdViewHolder) holder).setStripAd(resource, color);
                break;
            case HomePageModel.HORIZONTAL_PRODUCT_VIEW:
                String layoutColor = homePageModelList.get(position).getBackgroundColor();
                String HorizontalLayouttitle = homePageModelList.get(position).getTitle();
                List<WishlistModel> viewAllProductList = homePageModelList.get(position).getViewAllProductList();
                List<horizontalscrollproductModel> horizontalscrollproductModelList = homePageModelList.get(position).getHorizontalscrollproductModelList();
                ((HorizontalProductViewHolder) holder).setHorizontalProductLayout(horizontalscrollproductModelList, HorizontalLayouttitle,layoutColor,viewAllProductList);
                break;
            case HomePageModel.GRID_PRODUCT_VIEW:
                String gridlayoutColor = homePageModelList.get(position).getBackgroundColor();
                String GridLayouttitle = homePageModelList.get(position).getTitle();
                List<horizontalscrollproductModel> GridscrollproductModelList = homePageModelList.get(position).getHorizontalscrollproductModelList();
                ((GridProductViewHolder) holder).setGridProductLayout(GridscrollproductModelList,GridLayouttitle,gridlayoutColor);
                break;
            default:
                return;
        }

        if (lastPosition < position){
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return homePageModelList.size();
    }

    public class bannersliderViewHolder extends RecyclerView.ViewHolder {

        ViewPager bannerSliderViewPager;
        int currentPager;
        Timer timer;
        final private long DELAY_TIME = 3000;
        final private long PERIOD_TIME = 3000;
        List<sliderModel> arrangedList;

        public bannersliderViewHolder(@NonNull View itemView) {
            super(itemView);
            bannerSliderViewPager = itemView.findViewById(R.id.benner_slider_view_pager);
        }

        private void setBannerSliderViewPager(List<sliderModel> sliderModelList) {

            currentPager = 2;
            if (timer != null){
                timer.cancel();
            }

            arrangedList = new ArrayList<>();
            for (int x=0;x< sliderModelList.size();x++){
                arrangedList.add(x,sliderModelList.get(x));
            }

            arrangedList.add(0,sliderModelList.get(sliderModelList.size() - 2));
            arrangedList.add(1,sliderModelList.get(sliderModelList.size() - 1));
            arrangedList.add(sliderModelList.get(0));
            arrangedList.add(sliderModelList.get(1));

            sliderAdapter SliderAdapter = new sliderAdapter(arrangedList);
            bannerSliderViewPager.setAdapter(SliderAdapter);
            bannerSliderViewPager.setClipToPadding(false);
            bannerSliderViewPager.setPageMargin(20);

            bannerSliderViewPager.setCurrentItem(currentPager);

            ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPager = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    if (state == ViewPager.SCROLL_STATE_IDLE) {
                        pagerLooper(arrangedList);
                    }

                }
            };
            bannerSliderViewPager.addOnPageChangeListener(onPageChangeListener);

            startbannerSlideShow(arrangedList);
            bannerSliderViewPager.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    pagerLooper(arrangedList);
                    stopbannerSlideShow(arrangedList);
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        startbannerSlideShow(arrangedList);
                    }
                    return false;
                }
            });

        }

        public void pagerLooper(List<sliderModel> sliderModelList) {
            if (currentPager == sliderModelList.size() - 2) {
                currentPager = 2;
                bannerSliderViewPager.setCurrentItem(currentPager, false);

            }
            if (currentPager == 1) {
                currentPager = sliderModelList.size() - 3;
                bannerSliderViewPager.setCurrentItem(currentPager, false);

            }


        }

        public void startbannerSlideShow(List<sliderModel> sliderModelList) {
            Handler handler = new Handler();
            Runnable update = new Runnable() {
                @Override
                public void run() {
                    if (currentPager >= sliderModelList.size()) {
                        currentPager = 1;
                    }
                    bannerSliderViewPager.setCurrentItem(currentPager++, true);
                }
            };
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(update);
                }
            }, DELAY_TIME, PERIOD_TIME);
        }

        public void stopbannerSlideShow(List<sliderModel> sliderModelList) {
            timer.cancel();
        }
    }

    public class StripAdViewHolder extends RecyclerView.ViewHolder {

        ImageView stripadImage;
        ConstraintLayout striadContainer;

        public StripAdViewHolder(@NonNull View itemView) {
            super(itemView);

            stripadImage = itemView.findViewById(R.id.strip_ad_image);
            striadContainer = itemView.findViewById(R.id.strip_ad_Container);

        }

        private void setStripAd(String resource, String color) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.blank_big)).into(stripadImage);
            striadContainer.setBackgroundColor(Color.parseColor(color));
        }
    }

    public class HorizontalProductViewHolder extends RecyclerView.ViewHolder {

        ConstraintLayout container;
        TextView horizontalProductLayoutTitle;
        TextView horizontalProductLayoutButton;
        RecyclerView horizontalProductLayoutRecyclerview;

        public HorizontalProductViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container_horizontal);
            horizontalProductLayoutTitle = itemView.findViewById(R.id.horizontal_scroll_layout_textview);
            horizontalProductLayoutButton = itemView.findViewById(R.id.horizontal_scroll_layout_view_all_button);
            horizontalProductLayoutRecyclerview = itemView.findViewById(R.id.horizontal_scroll_layout_recyclerview);
            horizontalProductLayoutRecyclerview.setRecycledViewPool(recycledViewPool);
        }

        private void setHorizontalProductLayout(List<horizontalscrollproductModel> horizontalscrollproductModelList, String title, String layoutColor, List<WishlistModel> viewAllProductList) {
       // container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor()));
         horizontalProductLayoutTitle.setText(title);
         for (horizontalscrollproductModel model:horizontalscrollproductModelList){
             if (!model.getProductID().isEmpty() && model.getProductTitle().isEmpty()){
                 firebaseFirestore.collection("PRODUCTS")
                         .document(model.getProductID())
                         .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                     @Override
                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                         if (task.isSuccessful()){
                             model.setProductTitle(task.getResult().getString("product_title"));
                             model.setProduceImage(task.getResult().getString("product_image_1"));
                             model.setProductPrice(task.getResult().getString("product_price"));

                             WishlistModel wishlistModel = viewAllProductList
                                     .get(horizontalscrollproductModelList.indexOf(model));
                             wishlistModel.setTotalRating(task.getResult().getLong("total_ratings"));
                             wishlistModel.setRating(task.getResult().getString("average_rating"));
                             wishlistModel.setProductTitle(task.getResult().getString("product_title"));
                             wishlistModel.setProductPrice(task.getResult().getString("product_price"));
                             wishlistModel.setProductImage(task.getResult().getString("product_image_1"));
                             wishlistModel.setFreeCoupens(task.getResult().getLong("free_coupens"));
                             wishlistModel.setCuttedPrice(task.getResult().getString("cutted_price"));
                             wishlistModel.setCOD(task.getResult().getBoolean("COD"));
                             wishlistModel.setInStock(task.getResult().getLong("stock_quantity") > 0);

                             if (horizontalscrollproductModelList.indexOf(model) == horizontalscrollproductModelList.size()-1){
                                 if (horizontalProductLayoutRecyclerview.getAdapter() != null){
                                     horizontalProductLayoutRecyclerview.getAdapter().notifyDataSetChanged();
                                 }
                             }

                         }else {
                         //do nothing
                         }
                     }
                 });
             }

         }

            if (horizontalscrollproductModelList.size() > 8) {
                horizontalProductLayoutButton.setVisibility(View.VISIBLE);
                horizontalProductLayoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view_All_Activity.wishlistModelList = viewAllProductList;
                        Intent intentVewAllbtn = new Intent(itemView.getContext(), view_All_Activity.class);
                        intentVewAllbtn.putExtra("layout_code",0);
                        intentVewAllbtn.putExtra("title",title);
                        itemView.getContext().startActivity(intentVewAllbtn);
                    }
                });

                if (title.equals("")){
                    horizontalProductLayoutButton.setVisibility(View.GONE);
                }
            } else {
                horizontalProductLayoutButton.setVisibility(View.INVISIBLE);
            }

            horizontalscrollproductAdapter horizontalscrollproductAdapter = new horizontalscrollproductAdapter(horizontalscrollproductModelList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            horizontalProductLayoutRecyclerview.setLayoutManager(layoutManager);
            horizontalProductLayoutRecyclerview.setAdapter(horizontalscrollproductAdapter);
            horizontalscrollproductAdapter.notifyDataSetChanged();

           container.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(layoutColor)));

        }
    }

    public class GridProductViewHolder extends RecyclerView.ViewHolder {


        ConstraintLayout gridlayoutContainer;
        TextView gridproductTitle;
        Button gridproductviewallBtn;
        GridLayout gridproductlayout;


        public GridProductViewHolder(@NonNull View itemView) {
            super(itemView);
            gridlayoutContainer = itemView.findViewById(R.id.container_grid_layout);
            gridproductTitle = itemView.findViewById(R.id.grid_product_layout_title);
            gridproductviewallBtn = itemView.findViewById(R.id.grid_product_layout_view_all_btn);
            gridproductlayout = itemView.findViewById(R.id.grid_layout);
        }
        private void setGridProductLayout(List<horizontalscrollproductModel>horizontalscrollproductModelList,String title,String gridcolor){
            gridlayoutContainer.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(gridcolor)));
            gridproductTitle.setText(title);

            for (horizontalscrollproductModel model:horizontalscrollproductModelList){
                if (!model.getProductID().isEmpty() && model.getProductTitle().isEmpty()){
                    firebaseFirestore.collection("PRODUCTS")
                            .document(model.getProductID())
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                model.setProductTitle(task.getResult().getString("product_title"));
                                model.setProduceImage(task.getResult().getString("product_image_1"));
                                model.setProductPrice(task.getResult().getString("product_price"));


                                if (horizontalscrollproductModelList.indexOf(model) == horizontalscrollproductModelList.size()-1){
                                    setGridData(title,horizontalscrollproductModelList);

                                    if (!title.equals("")) {
                                        gridproductviewallBtn.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                view_All_Activity.horizontalscrollproductModelList = horizontalscrollproductModelList;
                                                Intent intentVewAllbtn = new Intent(itemView.getContext(), view_All_Activity.class);
                                                intentVewAllbtn.putExtra("layout_code", 1);
                                                intentVewAllbtn.putExtra("title", title);
                                                itemView.getContext().startActivity(intentVewAllbtn);


                                            }
                                        });
                                    }
                                }

                            }else {
                                //do nothing
                            }
                        }
                    });
                }

            }
            setGridData(title,horizontalscrollproductModelList);
        }
        private void setGridData(String title,List<horizontalscrollproductModel> horizontalscrollproductModelList){
            for (int x=0;x<4;x++){
                ImageView productImage = gridproductlayout.getChildAt(x).findViewById(R.id.h_s_product_image);
                TextView productTitle = gridproductlayout.getChildAt(x).findViewById(R.id.h_s_product_titale);
                TextView productDiscription = gridproductlayout.getChildAt(x).findViewById(R.id.h_s_product_discriptions);
                TextView productPrice = gridproductlayout.getChildAt(x).findViewById(R.id.h_s_product_price);

                Glide.with(itemView.getContext()).load(horizontalscrollproductModelList.get(x).getProduceImage()).apply(new RequestOptions().placeholder(R.drawable.blank_small)).into(productImage);
                productTitle.setText(horizontalscrollproductModelList.get(x).getProductTitle());
                productDiscription.setText(horizontalscrollproductModelList.get(x).getProductDescription());
                productPrice.setText("Rs."+horizontalscrollproductModelList.get(x).getProductPrice()+"/-");
                gridproductlayout.getChildAt(x).setBackgroundColor(Color.parseColor("#ffffff"));

                if (!title.equals("")) {
                    int finalX = x;
                    gridproductlayout.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent productDetailsIntent = new Intent(itemView.getContext(), Product_Detail_Activity.class);
                            productDetailsIntent.putExtra("PRODUCT_ID",horizontalscrollproductModelList.get(finalX).getProductID());
                            itemView.getContext().startActivity(productDetailsIntent);
                        }
                    });
                }
                if (title.equals("")){
                    gridproductviewallBtn.setVisibility(View.GONE);
                }


            }
        }
    }
}
