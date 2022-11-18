package com.shubhamwithcode.mymall;

import static com.shubhamwithcode.mymall.MainActivity.showCart;
import static com.shubhamwithcode.mymall.Register_Activity.setSignUpFragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shubhamwithcode.mymall.Adapters.MyRewardAdapter;
import com.shubhamwithcode.mymall.Adapters.ProductimagesAdapter;
import com.shubhamwithcode.mymall.Adapters.productdetailAdapter;
import com.shubhamwithcode.mymall.Fragments.signin_Fragment;
import com.shubhamwithcode.mymall.Fragments.signup_Fragment;
import com.shubhamwithcode.mymall.Models.WishlistModel;
import com.shubhamwithcode.mymall.Models.cartitemModel;
import com.shubhamwithcode.mymall.Models.productspecificationsModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product_Detail_Activity extends AppCompatActivity {


    private TextView productTitle;
    private TextView averageRatingMiniView;
    private TextView totalRatingMiniView;
    private TextView productPrice;
    private String productOriginalPrice;
    private TextView cuttedPrice;
    private ImageView codIndicator;
    private TextView tvCodIndicator;

    private TextView rewardTitle;
    private TextView rewardBody;


    Toolbar mytoolbar;
    ViewPager productImagesViewPager;
    TabLayout viewpagerIndicate;
    public static FloatingActionButton addToWishListButton;
    Button coupenRedeembtn;
    LinearLayout coupenRedeemptionsLayout;
    public static boolean ALREADY_ADDED_TO_WISHLIST = false;
    public static boolean ALREADY_ADDED_TO_CART = false;

    public static boolean running_wishlist_query = false;
    public static boolean running_rating_query = false;
    public static boolean running_cart_query = false;
    public static Activity productDetailsActivity;

    public static boolean fromSearch = false;

    ///////product Description
    private List<productspecificationsModel> productspecificationsModelList = new ArrayList<>();
    TabLayout productDetailTabLayout;
    ViewPager productDetailViewpager;
    ConstraintLayout productDetailsOnlyContainer;
    ConstraintLayout productDetailsTabsContainer;
    TextView productOnlyDescriptionBody;
    private String productDescriptions;
    private static String productOtherDetails;


    ///////product Description


    //////rating layout
    public static int initialRating;
    public static LinearLayout RateNow_Container;
    TextView totalRatings;
    LinearLayout ratingsNoContainer;
    TextView totalRatingsFigure;
    LinearLayout ratingsProgressBarContainer;
    TextView averageRating;
    //////rating layout


    private FirebaseFirestore firebaseFirestore;

    Button BuyNow_btn;
    LinearLayout addToCartBtn;
    public static MenuItem cartItem;

    ////////coupendialog
    private TextView coupenTitle;
    private TextView coupenExpiryData;
    private TextView coupenBody;
    private RecyclerView coupensRecyclerView;
    private LinearLayout selectedCoupen;
    private TextView orignalPrice;
    private TextView discountPrice;

    ////////coupendialog

    private Dialog signInDialog;
    public Dialog loadingDialog;
    FirebaseUser currentUser;
    public static String productID;
    private TextView badgeCount;
    private boolean inStock = false;


    private DocumentSnapshot documentSnapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        addToWishListButton = findViewById(R.id.add_to_wishlist_btn);
        productImagesViewPager = findViewById(R.id.product_images_viewPager);
        viewpagerIndicate = findViewById(R.id.ViewPager_indicator);
        productDetailTabLayout = findViewById(R.id.product_details_tablayout);
        productDetailViewpager = findViewById(R.id.product_details_viewpager);
        BuyNow_btn = findViewById(R.id.buy_now_btn);
        addToCartBtn = findViewById(R.id.add_to_cart_btn);
        coupenRedeembtn = findViewById(R.id.cupen_redeempation_btn);

        productTitle = findViewById(R.id.product_title);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_miniview);
        totalRatingMiniView = findViewById(R.id.total_ratings_miniview);
        productPrice = findViewById(R.id.product_price);
        cuttedPrice = findViewById(R.id.cutted_price);
        codIndicator = findViewById(R.id.code_indicator_imageview);
        tvCodIndicator = findViewById(R.id.tv_cod_indicator);

        rewardTitle = findViewById(R.id.Reward_title);
        rewardBody = findViewById(R.id.Reward_body);


        productDetailsTabsContainer = findViewById(R.id.product_detail_tabs_container);
        productDetailsOnlyContainer = findViewById(R.id.product_detail_container);
        productOnlyDescriptionBody = findViewById(R.id.product_detail_body);

        totalRatings = findViewById(R.id.Total_rating);
        ratingsNoContainer = findViewById(R.id.Rating_Numbers_Contailner);
        totalRatingsFigure = findViewById(R.id.Total_Rating_figur);
        ratingsProgressBarContainer = findViewById(R.id.Rating_prograssbar_Container);
        averageRating = findViewById(R.id.avrage_rating);
        coupenRedeemptionsLayout = findViewById(R.id.cupen_redeempation_layout);

        initialRating = -1;

        /////loading dialog
        loadingDialog = new Dialog(Product_Detail_Activity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_bg));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        /////loading dialog

        ////coupen dialog
        Dialog checkCoupenPriceDialog = new Dialog(Product_Detail_Activity.this);
        checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialogue);
        checkCoupenPriceDialog.setCancelable(true);
        checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggleRecyclerView = checkCoupenPriceDialog.findViewById(R.id.toggle_recyclerview);
        coupensRecyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
        selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);
        coupenTitle = checkCoupenPriceDialog.findViewById(R.id.reward_coupen_title);
        coupenExpiryData = checkCoupenPriceDialog.findViewById(R.id.reward_coupen_validity);
        coupenBody = checkCoupenPriceDialog.findViewById(R.id.reward_coupen_body);


        orignalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
        discountPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);


        LinearLayoutManager layoutManager = new LinearLayoutManager(Product_Detail_Activity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        coupensRecyclerView.setLayoutManager(layoutManager);

        toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }
        });
        ////coupen dialog


        mytoolbar = (Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        firebaseFirestore = FirebaseFirestore.getInstance();
        List<String> productImages = new ArrayList<>();
        productID = getIntent().getStringExtra("PRODUCT_ID");

        firebaseFirestore.collection("PRODUCTS").document(productID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    documentSnapshot = task.getResult();

                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {

                                        for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                                            productImages.add(documentSnapshot.get("product_image_" + x).toString());
                                        }
                                        ProductimagesAdapter productimagesAdapter = new ProductimagesAdapter(productImages);
                                        productImagesViewPager.setAdapter(productimagesAdapter);

                                        productTitle.setText(documentSnapshot.get("product_title").toString());
                                        averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                                        totalRatingMiniView.setText("(" + (long) documentSnapshot.get("total_ratings") + ")ratings");
                                        productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");

                                        //for coupen dialog
                                        orignalPrice.setText(productPrice.getText());
                                        productOriginalPrice = documentSnapshot.get("product_price").toString();
                                        MyRewardAdapter myRewardAdapter = new MyRewardAdapter(DBqueries.rewardModelList, true, coupensRecyclerView, selectedCoupen, productOriginalPrice, coupenTitle, coupenExpiryData, coupenBody, discountPrice);
                                        myRewardAdapter.notifyDataSetChanged();
                                        coupensRecyclerView.setAdapter(myRewardAdapter);
                                        //for coupen dialog

                                        cuttedPrice.setText("Rs." + documentSnapshot.get("cutted_price").toString() + "/-");

                                        if ((boolean) documentSnapshot.get("COD")) {
                                            codIndicator.setVisibility(View.VISIBLE);
                                            tvCodIndicator.setVisibility(View.VISIBLE);
                                        } else {
                                            codIndicator.setVisibility(View.INVISIBLE);
                                            tvCodIndicator.setVisibility(View.INVISIBLE);
                                        }
                                        rewardTitle.setText((long) documentSnapshot.get("free_coupens") + " " + documentSnapshot.get("free_coupen_title").toString());
                                        rewardBody.setText(documentSnapshot.get("free_coupen_body").toString());

                                        if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                            productDetailsTabsContainer.setVisibility(View.VISIBLE);
                                            productDetailsOnlyContainer.setVisibility(View.GONE);
                                            productDescriptions = documentSnapshot.get("product_description").toString();

                                            productOtherDetails = documentSnapshot.get("product_other_details").toString();

                                            for (long x = 1; x < (long) documentSnapshot.get("total_spec_titles") + 1; x++) {
                                                productspecificationsModelList.add(new productspecificationsModel(0, documentSnapshot.get("spec_title_" + x).toString()));
                                                for (long y = 1; y < (long) documentSnapshot.get("spec_title_" + x + "_total_fields") + 1; y++) {
                                                    productspecificationsModelList.add(new productspecificationsModel(1, documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(), documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()));

                                                }
                                            }
                                        } else {
                                            productDetailsTabsContainer.setVisibility(View.GONE);
                                            productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                                            productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());

                                        }
                                        totalRatings.setText((long) documentSnapshot.get("total_ratings") + " ratings");
                                        for (int x = 0; x < 5; x++) {
                                            TextView rating = (TextView) ratingsNoContainer.getChildAt(x);
                                            rating.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));

                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                            int maxProgress = Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                            progressBar.setMax(maxProgress);
                                            progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));
                                        }
                                        totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
                                        averageRating.setText(documentSnapshot.get("average_rating").toString());
                                        productDetailViewpager.setAdapter(new productdetailAdapter(getSupportFragmentManager(), productDetailTabLayout.getTabCount(), productDescriptions, productOtherDetails, productspecificationsModelList));

                                        if (currentUser != null) {
                                            if (DBqueries.myRating.size() == 0) {
                                                DBqueries.loadRatingsList(Product_Detail_Activity.this);
                                            }
                                            if (DBqueries.cartList.size() == 0) {
                                                DBqueries.loadCartList(Product_Detail_Activity.this, loadingDialog, false, badgeCount, new TextView(Product_Detail_Activity.this));
                                            }
                                            if (DBqueries.wishList.size() == 0) {
                                                DBqueries.loadWishList(Product_Detail_Activity.this, loadingDialog, false);
                                            }
                                            if (DBqueries.rewardModelList.size() == 0) {
                                                DBqueries.loadRewards(Product_Detail_Activity.this, loadingDialog, false);
                                            }
                                            if (DBqueries.cartList.size() != 0 && DBqueries.wishList.size() != 0 && DBqueries.rewardModelList.size() != 0) {
                                                loadingDialog.dismiss();
                                            }
                                        } else {
                                            loadingDialog.dismiss();
                                        }

                                        if (DBqueries.myRatedIds.contains(productID)) {
                                            int index = DBqueries.myRatedIds.indexOf(productID);
                                            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
                                            setRating(initialRating);
                                        }

                                        if (DBqueries.cartList.contains(productID)) {
                                            ALREADY_ADDED_TO_CART = true;
                                        } else {
                                            ALREADY_ADDED_TO_CART = false;
                                        }

                                        if (DBqueries.wishList.contains(productID)) {
                                            ALREADY_ADDED_TO_WISHLIST = true;
                                            addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colour_primary));

                                        } else {
                                            addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                            ALREADY_ADDED_TO_WISHLIST = false;
                                        }

                                        if (task.getResult().getDocuments().size() < (long) documentSnapshot.get("stock_quantity")) {
                                            inStock = true;
                                            BuyNow_btn.setVisibility(View.VISIBLE);
                                            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    if (currentUser == null) {
                                                        signInDialog.show();
                                                    } else {
                                                        if (!running_cart_query) {
                                                            running_cart_query = true;
                                                            if (ALREADY_ADDED_TO_CART) {
                                                                running_cart_query = false;
                                                                Toast.makeText(Product_Detail_Activity.this, "Already added to Cart", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Map<String, Object> addProduct = new HashMap<>();
                                                                addProduct.put("product_ID_" + String.valueOf(DBqueries.cartList.size()), productID);
                                                                addProduct.put("list_size", (long) (DBqueries.wishList.size() + 1));


                                                                firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                                                        .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {

                                                                            if (DBqueries.cartitemModelList.size() != 0) {
                                                                                DBqueries.cartitemModelList.add(0, new cartitemModel(documentSnapshot.getBoolean("COD"), cartitemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                                                                                        , documentSnapshot.get("product_title").toString()
                                                                                        , (long) documentSnapshot.get("free_coupens")
                                                                                        , documentSnapshot.get("product_price").toString()
                                                                                        , documentSnapshot.get("cutted_price").toString()
                                                                                        , (long) 1
                                                                                        , (long) documentSnapshot.get("offers_applied")
                                                                                        , (long) 0
                                                                                        , inStock
                                                                                        , (long) documentSnapshot.get("max-quantity")
                                                                                        , (long) documentSnapshot.get("stock_quantity")));
                                                                            }

                                                                            ALREADY_ADDED_TO_CART = true;
                                                                            DBqueries.cartList.add(productID);
                                                                            Toast.makeText(Product_Detail_Activity.this, "Added to cart Successfully!", Toast.LENGTH_SHORT).show();
                                                                            invalidateOptionsMenu();
                                                                            running_cart_query = false;
                                                                        } else {
                                                                            running_cart_query = false;
                                                                            String error = task.getException().getMessage();
                                                                            Toast.makeText(Product_Detail_Activity.this, error, Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });

                                                            }
                                                        }
                                                    }
                                                }
                                            });

                                        } else {
                                            inStock = false;
                                            BuyNow_btn.setVisibility(View.GONE);
                                            TextView outOfStock = (TextView) addToCartBtn.getChildAt(0);
                                            outOfStock.setText("Out of Stock");
                                            outOfStock.setTextColor(getResources().getColor(R.color.colour_primary));
                                            outOfStock.setCompoundDrawables(null, null, null, null);
                                        }
                                    } else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(Product_Detail_Activity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    loadingDialog.dismiss();
                    String error = task.getException().getMessage();
                    Toast.makeText(Product_Detail_Activity.this, error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewpagerIndicate.setupWithViewPager(productImagesViewPager, true);

        addToWishListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    if (!running_wishlist_query) {
                        running_wishlist_query = true;
                        if (ALREADY_ADDED_TO_WISHLIST) {
                            int index = DBqueries.wishList.indexOf(productID);
                            DBqueries.removeFromWishlist(index, Product_Detail_Activity.this);
                            addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));

                        } else {
                            addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colour_primary));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + String.valueOf(DBqueries.wishList.size()), productID);
                            addProduct.put("list_size", (long) (DBqueries.wishList.size() + 1));

                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        if (DBqueries.wishlistModelList.size() != 0) {
                                            DBqueries.wishlistModelList.add(new WishlistModel(productID, documentSnapshot.get("product_image_1").toString()
                                                    , documentSnapshot.get("product_title").toString()
                                                    , (long) documentSnapshot.get("free_coupens")
                                                    , documentSnapshot.get("average_rating").toString()
                                                    , (long) documentSnapshot.get("total_ratings")
                                                    , documentSnapshot.get("product_price").toString()
                                                    , documentSnapshot.get("cutted_price").toString()
                                                    , (boolean) documentSnapshot.get("COD")
                                                    , inStock));

                                        }
                                        ALREADY_ADDED_TO_WISHLIST = true;
                                        addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colour_primary));
                                        DBqueries.wishList.add(productID);
                                        Toast.makeText(Product_Detail_Activity.this, "Added to wishList Successfully!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(Product_Detail_Activity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishlist_query = false;
                                }
                            });

                        }
                    }
                }
            }
        });

        productDetailViewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailTabLayout));
        productDetailTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                productDetailViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //////rating layout
        RateNow_Container = findViewById(R.id.Rate_now_container);

        for (int x = 0; x < RateNow_Container.getChildCount(); x++) {
            final int starPosition = x;
            RateNow_Container.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        if (starPosition != initialRating) {
                            setRating(starPosition);
                            Map<String, Object> updateRating = new HashMap<>();
                            if (DBqueries.myRatedIds.contains(productID)) {

                                TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);

                                updateRating.put(initialRating + 1 + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                updateRating.put(initialRating + 1 + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                updateRating.put("average_rating", calculateAverageRating((long) starPosition - initialRating, true));
                            } else {
                                if (!running_rating_query) {
                                    running_rating_query = false;
                                    updateRating.put(starPosition + 1 + "_star", (long) documentSnapshot.get(starPosition + 1 + "_star") + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition + 1, false));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);
                                }
                            }
                            firebaseFirestore.collection("PRODUCTS").document(productID)
                                    .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Map<String, Object> myrating = new HashMap<>();
                                        if (DBqueries.myRatedIds.contains(productID)) {
                                            myrating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);
                                        } else {
                                            myrating.put("list_size", DBqueries.myRatedIds.size() + 1);
                                            myrating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                            myrating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);
                                        }

                                        firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                .update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    if (DBqueries.myRatedIds.contains(productID)) {
                                                        DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);

                                                        TextView oldRating = (TextView) ratingsNoContainer.getChildAt(5 - initialRating - 1);
                                                        TextView finalRating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                        oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                        finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));


                                                    } else {

                                                        DBqueries.myRatedIds.add(productID);
                                                        DBqueries.myRating.add((long) starPosition + 1);

                                                        TextView rating = (TextView) ratingsNoContainer.getChildAt(5 - starPosition - 1);
                                                        rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                        totalRatingMiniView.setText("(" + ((long) documentSnapshot.get("total_ratings") + 1) + ")ratings");
                                                        totalRatings.setText((long) documentSnapshot.get("total_ratings") + 1 + " ratings");
                                                        totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings") + 1));

                                                        Toast.makeText(Product_Detail_Activity.this, "Thank you! for rating.", Toast.LENGTH_SHORT).show();
                                                    }
                                                    for (int x = 0; x < 5; x++) {
                                                        TextView ratingfigures = (TextView) ratingsNoContainer.getChildAt(x);
                                                        ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);

                                                        int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                        progressBar.setMax(maxProgress);
                                                        progressBar.setProgress(Integer.parseInt(ratingfigures.getText().toString()));
                                                    }
                                                    initialRating = starPosition;
                                                    averageRating.setText(calculateAverageRating(0, true));
                                                    averageRatingMiniView.setText(calculateAverageRating(0, true));

                                                    if (DBqueries.wishList.contains(productID) && DBqueries.wishlistModelList.size() != 0) {
                                                        int index = DBqueries.wishList.indexOf(productID);
                                                        DBqueries.wishlistModelList.get(index).setRating(averageRating.getText().toString());
                                                        DBqueries.wishlistModelList.get(index).setTotalRating(Long.parseLong(totalRatingsFigure.getText().toString()));
                                                    }


                                                } else {
                                                    setRating(initialRating);
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(Product_Detail_Activity.this, error, Toast.LENGTH_SHORT).show();
                                                }
                                                running_wishlist_query = false;
                                            }
                                        });

                                    } else {
                                        running_wishlist_query = false;
                                        setRating(initialRating);
                                        String error = task.getException().getMessage();
                                        Toast.makeText(Product_Detail_Activity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                }
            });
        }

        //////rating layout

        BuyNow_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    DeliveryActivity.fromCart = false;
                    loadingDialog.show();
                    productDetailsActivity = Product_Detail_Activity.this;
                    DeliveryActivity.cartitemModelList = new ArrayList<>();
                    DeliveryActivity.cartitemModelList.add(new cartitemModel(documentSnapshot.getBoolean("COD"), cartitemModel.CART_ITEM, productID, documentSnapshot.get("product_image_1").toString()
                            , documentSnapshot.get("product_title").toString()
                            , (long) documentSnapshot.get("free_coupens")
                            , documentSnapshot.get("product_price").toString()
                            , documentSnapshot.get("cutted_price").toString()
                            , (long) 1
                            , (long) documentSnapshot.get("offers_applied")
                            , (long) 0
                            , inStock
                            , (long) documentSnapshot.get("max-quantity")
                            , (long) documentSnapshot.get("stock_quantity")));

                    DeliveryActivity.cartitemModelList.add(new cartitemModel(cartitemModel.TOTAL_AMOUNT));

                    if (DBqueries.addressesModelList.size() == 0) {
                        DBqueries.loadAddresses(Product_Detail_Activity.this, loadingDialog, true);
                    } else {
                        loadingDialog.dismiss();
                        Intent DeliveryIntent = new Intent(Product_Detail_Activity.this, DeliveryActivity.class);
                        startActivity(DeliveryIntent);
                    }
                }
            }
        });


        coupenRedeembtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCoupenPriceDialog.show();
            }
        });
        ////coupen dialog

        ////SignIn dialog
        signInDialog = new Dialog(Product_Detail_Activity.this);
        signInDialog.setContentView(R.layout.sign_in_dialogue);
        signInDialog.setCancelable(true);

        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        Button dialogSignInBtn = signInDialog.findViewById(R.id.Quantity_Cancel_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.Quantity_OK_btn);
        final Intent registerIntent = new Intent(Product_Detail_Activity.this, Register_Activity.class);

        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin_Fragment.disableCloseBtn = true;
                signup_Fragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin_Fragment.disableCloseBtn = true;
                signup_Fragment.disableCloseBtn = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });

        ////SignIn dialog


    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            coupenRedeemptionsLayout.setVisibility(View.GONE);
        } else {
            coupenRedeemptionsLayout.setVisibility(View.VISIBLE);
        }

        if (currentUser != null) {
            if (DBqueries.myRating.size() == 0) {
                DBqueries.loadRatingsList(Product_Detail_Activity.this);
            }
            if (DBqueries.wishList.size() == 0) {
                DBqueries.loadWishList(Product_Detail_Activity.this, loadingDialog, false);
            }
            if (DBqueries.rewardModelList.size() == 0) {
                DBqueries.loadRewards(Product_Detail_Activity.this, loadingDialog, false);
            }
            if (DBqueries.cartList.size() != 0 && DBqueries.wishList.size() != 0 && DBqueries.rewardModelList.size() != 0) {
                loadingDialog.dismiss();
            }
        } else {
            loadingDialog.dismiss();
        }

        if (DBqueries.myRatedIds.contains(productID)) {
            int index = DBqueries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRating.get(index))) - 1;
            setRating(initialRating);
        }

        if (DBqueries.cartList.contains(productID)) {
            ALREADY_ADDED_TO_CART = true;
        } else {
            ALREADY_ADDED_TO_CART = false;
        }

        if (DBqueries.wishList.contains(productID)) {
            ALREADY_ADDED_TO_WISHLIST = true;
            addToWishListButton.setSupportImageTintList(getResources().getColorStateList(R.color.colour_primary));

        } else {
            addToWishListButton.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
            ALREADY_ADDED_TO_WISHLIST = false;
        }
        invalidateOptionsMenu();

    }

    private void showDialogRecyclerView() {
        if (coupensRecyclerView.getVisibility() == View.GONE) {
            coupensRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupen.setVisibility(View.GONE);
        } else {
            coupensRecyclerView.setVisibility(View.GONE);
            selectedCoupen.setVisibility(View.VISIBLE);
        }
    }

    public static void setRating(int starPosition) {
        for (int x = 0; x < RateNow_Container.getChildCount(); x++) {
            ImageView starBtn = (ImageView) RateNow_Container.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#E4CD00")));
            }
        }
    }

    private String calculateAverageRating(long currentUserRating, boolean update) {
        Double totalStars = Double.valueOf(0);
        for (int x = 1; x < 6; x++) {
            TextView ratingNo = (TextView) ratingsNoContainer.getChildAt(5 - x);
            totalStars = totalStars + (Long.parseLong(ratingNo.getText().toString()) * x);
        }

        totalStars = totalStars + currentUserRating;
        if (update) {
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0, 3);
        } else {
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString() + 1)).substring(0, 3);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);

        for (int i = 0; i < menu.size(); i++) {
            Drawable drawable = menu.getItem(i).getIcon();
            if (drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }

            cartItem = menu.findItem(R.id.main_my_cart);
            cartItem.setActionView(R.layout.badge_layout);
            ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
            badgeIcon.setImageResource(R.drawable.cart);
            badgeIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
            badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

            if (currentUser != null) {
                if (DBqueries.cartList.size() == 0) {
                    DBqueries.loadCartList(Product_Detail_Activity.this, loadingDialog, false, badgeCount, new TextView(Product_Detail_Activity.this));
                } else {
                    badgeCount.setVisibility(View.VISIBLE);
                    if (DBqueries.cartList.size() < 99) {
                        badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                    } else {
                        badgeCount.setText("99");
                    }
                }
            }

            cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {
                        Intent cartIntent = new Intent(Product_Detail_Activity.this, MainActivity.class);
                        showCart = true;
                        startActivity(cartIntent);
                    }
                }
            });
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fromSearch = false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        productDetailsActivity = null;
        if (id == android.R.id.home) {
            finish();

            return true;
        } else if (id == R.id.main_search) {
            if (fromSearch){
             finish();
            }else {
                Intent searchIntent = new Intent(this, SearchActivity.class);
                startActivity(searchIntent);
            }
            return true;
        } else if (id == R.id.main_my_cart) {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                Intent cartIntent = new Intent(Product_Detail_Activity.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        productDetailsActivity = null;
        super.onBackPressed();
    }
}