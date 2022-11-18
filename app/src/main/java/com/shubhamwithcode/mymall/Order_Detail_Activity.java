package com.shubhamwithcode.mymall;

import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.shubhamwithcode.mymall.Models.MyOrderItemModel;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class Order_Detail_Activity extends AppCompatActivity {
    private int position;
    private TextView title,price,quantity;
    private ImageView productImage,orderedIndicator,packedIndicator,shippedIndicator,deliveredIndicator;
    private ProgressBar O_P_progress,P_S_progress,S_D_progress;
    private TextView orderedTitle,packedTitle,shippedTitle,deliveredTitle;
    private TextView orderDate,packedDate,shippedDate,deliveredDate;
    private TextView orderedBody,packedBody,shippedBody,deliveredBody;
    private LinearLayout RateNow_Container;
    private int rating;
    private TextView fullName,address,pincode;
    private TextView totalItems,totalItemsPrice,deliveryPrice,totalAmount,savedAmount;
    private Dialog loadingDialog,cancelDialog;
    private SimpleDateFormat simpleDateFormat;
    private Button cancelOrderBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Detail");

        /////loading dialog
        loadingDialog = new Dialog(Order_Detail_Activity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_bg));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        /////loading dialog

        /////cancel dialog
        cancelDialog = new Dialog(Order_Detail_Activity.this);
        cancelDialog.setContentView(R.layout.order_cancel_dialog);
        cancelDialog.setCancelable(true);
        cancelDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_bg));
     //   cancelDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        /////cancel dialog



        position = getIntent().getIntExtra("Position", -1);
        MyOrderItemModel model = DBqueries.myOrderItemModelList.get(position);

        title = findViewById(R.id.Order_Detail_Product_Title);
        price = findViewById(R.id.Order_Detail_Product_Price);
        quantity = findViewById(R.id.Order_Detail_Product_quantity);

        productImage = findViewById(R.id.Order_Detail_Product_Image);
        cancelOrderBtn = findViewById(R.id.cancel_btn);

        orderedIndicator = findViewById(R.id.Ordered_indicator);
        packedIndicator = findViewById(R.id.Packed_Indicator);
        shippedIndicator = findViewById(R.id.Shipping_Indicator);
        deliveredIndicator = findViewById(R.id.Delivered_Indicator);

        O_P_progress = findViewById(R.id.Ordered_Packed_Progress);
        P_S_progress= findViewById(R.id.Packed_Shipping_Progress);
        S_D_progress = findViewById(R.id.Shipping_Delivered_Progress);

        orderedTitle = findViewById(R.id.Ordered_Title);
        packedTitle = findViewById(R.id.Packed_title);
        shippedTitle = findViewById(R.id.Shipped_title);
        deliveredTitle = findViewById(R.id.Delivered_title);

        orderDate = findViewById(R.id.Ordered_Date);
        packedDate = findViewById(R.id.packed_date);
        shippedDate = findViewById(R.id.Shipped_date);
        deliveredDate = findViewById(R.id.Delivered_date);

        orderedBody = findViewById(R.id.Ordered_Bodey);
        packedBody = findViewById(R.id.Packed_Body);
        shippedBody = findViewById(R.id.Shipped_Body);
        deliveredBody = findViewById(R.id.Delivered_Body);

        RateNow_Container = findViewById(R.id.Rate_now_container);
        fullName = findViewById(R.id.Full_Name);
        address = findViewById(R.id.Full_Address);
        pincode = findViewById(R.id.pinCode);

        totalItems = findViewById(R.id.Total_items);
        totalItemsPrice = findViewById(R.id.Total_items_price);
        deliveryPrice = findViewById(R.id.delivery_Price);
        totalAmount = findViewById(R.id.Total_price);
        savedAmount = findViewById(R.id.saved_Amount);


        title.setText(model.getProductTitle());
        if (!model.getDiscountedPrice().equals("")){
            price.setText("Rs."+model.getDiscountedPrice()+"/-");
        }else {
            price.setText("Rs."+model.getProductPrice()+"/-");
        }
        quantity.setText("Qty :"+String.valueOf(model.getProductQuantity()));
        Glide.with(this).load(model.getProductImage()).into(productImage);

        simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm aa");

        switch (model.getOrderStatus()){

            case "Ordered":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderdDate())));

                P_S_progress.setVisibility(View.GONE);
                S_D_progress.setVisibility(View.GONE);
                O_P_progress.setVisibility(View.GONE);

                packedIndicator.setVisibility(View.GONE);
                packedBody.setVisibility(View.GONE);
                packedDate.setVisibility(View.GONE);
                packedTitle.setVisibility(View.GONE);

                shippedBody.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);
                shippedIndicator.setVisibility(View.GONE);
                shippedTitle.setVisibility(View.GONE);

                deliveredBody.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredIndicator.setVisibility(View.GONE);
                break;
            case "Packed":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderdDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                O_P_progress.setProgress(100);

                P_S_progress.setVisibility(View.GONE);
                S_D_progress.setVisibility(View.GONE);


                shippedBody.setVisibility(View.GONE);
                shippedDate.setVisibility(View.GONE);
                shippedIndicator.setVisibility(View.GONE);
                shippedTitle.setVisibility(View.GONE);

                deliveredBody.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredIndicator.setVisibility(View.GONE);

                break;
            case "Shipped":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderdDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));


                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);

                S_D_progress.setVisibility(View.GONE);

                deliveredBody.setVisibility(View.GONE);
                deliveredTitle.setVisibility(View.GONE);
                deliveredDate.setVisibility(View.GONE);
                deliveredIndicator.setVisibility(View.GONE);

                break;

            case "Out for Delivery":
                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderdDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getDeliveredDate())));


                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);
                S_D_progress.setProgress(100);

                deliveredTitle.setText("Out for Delivery");
                deliveredBody.setText("Your order is out for delivery. ");

                break;
            case "Delivered":

                orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderdDate())));

                packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getDeliveredDate())));


                O_P_progress.setProgress(100);
                P_S_progress.setProgress(100);
                S_D_progress.setProgress(100);
                break;
            case "Cancelled":
                if (model.getPackedDate().after(model.getOrderdDate())){
                    if (model.getShippedDate().after(model.getPackedDate())){

                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderdDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getShippedDate())));

                        deliveredIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colour_primary)));
                        deliveredDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));
                        deliveredTitle.setText("Cancelled");
                        deliveredBody.setText("Your order has been cancelled");

                        O_P_progress.setProgress(100);
                        P_S_progress.setProgress(100);
                        S_D_progress.setProgress(100);
                    }else {
                        orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderdDate())));

                        packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                        packedDate.setText(String.valueOf(simpleDateFormat.format(model.getPackedDate())));

                        shippedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colour_primary)));
                        shippedDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));
                        shippedTitle.setText("Cancelled");
                        shippedBody.setText("Your order has been cancelled");


                        O_P_progress.setProgress(100);
                        P_S_progress.setProgress(100);

                        S_D_progress.setVisibility(View.GONE);

                        deliveredBody.setVisibility(View.GONE);
                        deliveredTitle.setVisibility(View.GONE);
                        deliveredDate.setVisibility(View.GONE);
                        deliveredIndicator.setVisibility(View.GONE);

                    }
                }else {
                    orderedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.successGreen)));
                    orderDate.setText(String.valueOf(simpleDateFormat.format(model.getOrderdDate())));

                    packedIndicator.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.colour_primary)));
                    packedDate.setText(String.valueOf(simpleDateFormat.format(model.getCancelledDate())));
                    packedTitle.setText("Cancelled");
                    packedBody.setText("Your order has been cancelled");

                    O_P_progress.setProgress(100);

                    P_S_progress.setVisibility(View.GONE);
                    S_D_progress.setVisibility(View.GONE);


                    shippedBody.setVisibility(View.GONE);
                    shippedDate.setVisibility(View.GONE);
                    shippedIndicator.setVisibility(View.GONE);
                    shippedTitle.setVisibility(View.GONE);

                    deliveredBody.setVisibility(View.GONE);
                    deliveredTitle.setVisibility(View.GONE);
                    deliveredDate.setVisibility(View.GONE);
                    deliveredIndicator.setVisibility(View.GONE);
                }
                break;
        }

        //////rating layout
        rating = model.getRating();
        setRating(rating);
        for (int x = 0;x<RateNow_Container.getChildCount();x++){
            final int starPosition = x;
            RateNow_Container.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadingDialog.show();
                    setRating(starPosition);
                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS").document(model.getProductId());
                    FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Object>() {
                        @Nullable
                        @Override
                        public Object apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                            DocumentSnapshot documentSnapshot = transaction.get(documentReference);

                            if (rating != 0){
                                Long increase = documentSnapshot.getLong(starPosition+1+"_star") + 1;
                                Long decrease = documentSnapshot.getLong(rating+1+"_star") - 1;
                                transaction.update(documentReference,starPosition+1+"_star",increase);
                                transaction.update(documentReference,rating+1+"_star",decrease);
                            }else {
                                Long increase = documentSnapshot.getLong(starPosition+1+"_star") +1;
                                transaction.update(documentReference,starPosition+1+"_star",increase);
                            }
                            return null;
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Object>() {
                        @Override
                        public void onSuccess(Object o) {
                            Map<String, Object> myrating = new HashMap<>();
                            if (DBqueries.myRatedIds.contains(model.getProductId())) {
                                myrating.put("rating_" + DBqueries.myRatedIds.indexOf(model.getProductId()), (long) starPosition + 1);
                            } else {
                                myrating.put("list_size", DBqueries.myRatedIds.size() + 1);
                                myrating.put("product_ID_" + DBqueries.myRatedIds.size(), model.getProductId());
                                myrating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);
                            }

                            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                                    .update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        DBqueries.myOrderItemModelList.get(position).setRating(starPosition);
                                        if (DBqueries.myRatedIds.contains(model.getProductId())){
                                            DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(model.getProductId()),Long.parseLong(String.valueOf(starPosition+1)));
                                        }else {
                                            DBqueries.myRatedIds.add(model.getProductId());
                                            DBqueries.myRating.add(Long.parseLong(String.valueOf(starPosition+1)));
                                        }
                                    }else {
                                        String error = task.getException().getMessage();
                                        Toast.makeText(Order_Detail_Activity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    loadingDialog.dismiss();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loadingDialog.dismiss();
                        }
                    });
                }
            });
        }

        //////rating layout

        if (model.isCancellationRequested()){
            cancelOrderBtn.setVisibility(View.VISIBLE);
            cancelOrderBtn.setEnabled(false);
            cancelOrderBtn.setText("Cancellation process.");
            cancelOrderBtn.setTextColor(getResources().getColor(R.color.colour_primary));
            cancelOrderBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));

        }else {
            if (model.getOrderStatus().equals("Ordered") || model.getOrderStatus().equals("Packed")){
                cancelOrderBtn.setVisibility(View.VISIBLE);
                cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelDialog.findViewById(R.id.no_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                cancelDialog.dismiss();
                            }
                        });
                        cancelDialog.findViewById(R.id.yes_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               cancelDialog.dismiss();
                               loadingDialog.show();
                               Map<String,Object> map = new HashMap<>();
                               map.put("Order Id",model.getOrderID());
                               map.put("product Id",model.getProductId());
                               map.put("Oder Cancelled",false);
                               FirebaseFirestore.getInstance().collection("CANCELLED ORDERS").document().set(map)
                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if (task.isSuccessful()){
                                                 FirebaseFirestore.getInstance().collection("ORDERS").document(model.getOrderID()).collection("OrderItems").document(model.getProductId()).update("Cancellation requested",true)
                                                         .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                             @Override
                                                             public void onComplete(@NonNull Task<Void> task) {
                                                                 if (task.isSuccessful()){
                                                                     model.setCancellationRequested(true);
                                                                     cancelOrderBtn.setEnabled(false);
                                                                     cancelOrderBtn.setText("Cancellation process.");
                                                                     cancelOrderBtn.setTextColor(getResources().getColor(R.color.colour_primary));
                                                                     cancelOrderBtn.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ffffff")));
                                                                 }else {
                                                                     String error = task.getException().getMessage();
                                                                     Toast.makeText(Order_Detail_Activity.this, error, Toast.LENGTH_SHORT).show();
                                                                 }
                                                                 loadingDialog.dismiss();
                                                             }
                                                         });
                                               }else {
                                                   loadingDialog.dismiss();
                                                   String error = task.getException().getMessage();
                                                   Toast.makeText(Order_Detail_Activity.this, error, Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       });
                            }
                        });
                        cancelDialog.show();
                    }
                });
            }
        }

        fullName.setText(model.getFullName());
        address.setText(model.getAddress());
        pincode.setText(model.getPincode());



        Long totalItemsPriceValue;
        if (model.getDiscountedPrice().equals("")){
            totalItemsPriceValue = model.getProductQuantity()*Long.valueOf(model.getProductPrice());
            totalItemsPrice.setText("Rs."+totalItemsPriceValue+"/-");
        }else {
            totalItemsPriceValue = model.getProductQuantity()*Long.valueOf(model.getDiscountedPrice());
            totalItemsPrice.setText("Rs."+totalItemsPriceValue+"/-");
        }
        if (model.getDeliveryPrice().equals("FREE")){
            deliveryPrice.setText(model.getDeliveryPrice());
            totalAmount.setText(totalItemsPrice.getText());
        }else {
            deliveryPrice.setText("Rs."+model.getDeliveryPrice() +"/-");
            totalAmount.setText("Rs."+(totalItemsPriceValue+Long.valueOf(model.getDeliveryPrice()))+"/-");
        }
        if (!model.getCuttedPrice().equals("")){
            if (!model.getDiscountedPrice().equals("")){
                savedAmount.setText("You saved Rs"+model.getProductQuantity()*(Long.valueOf(model.getCuttedPrice()) - Long.valueOf(model.getDiscountedPrice()))+"on this order");
            }else {
                savedAmount.setText("You saved Rs"+model.getProductQuantity()*(Long.valueOf(model.getCuttedPrice()) - Long.valueOf(model.getProductPrice()))+"on this order");
            }
        }else {
            if (!model.getDiscountedPrice().equals("")){
                savedAmount.setText("You saved Rs"+model.getProductQuantity()*(Long.valueOf(model.getProductPrice()) - Long.valueOf(model.getDiscountedPrice()))+"on this order");
            }else {
                savedAmount.setText("You saved Rs.0/- on this order");
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            return true;
        } return super.onOptionsItemSelected(item);

    }

    private void setRating(int starPosition) {
        for (int x = 0;x < RateNow_Container.getChildCount();x++){
            ImageView starBtn =(ImageView) RateNow_Container.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition){
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#E4CD00")));
            }
        }
    }
}