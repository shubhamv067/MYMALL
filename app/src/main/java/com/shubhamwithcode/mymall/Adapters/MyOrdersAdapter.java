package com.shubhamwithcode.mymall.Adapters;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

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
import com.shubhamwithcode.mymall.DBqueries;
import com.shubhamwithcode.mymall.Models.MyOrderItemModel;
import com.shubhamwithcode.mymall.Order_Detail_Activity;
import com.shubhamwithcode.mymall.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {
    List<MyOrderItemModel> myOrderItemModelList;
    private Dialog loadingDialog;

    public MyOrdersAdapter(List<MyOrderItemModel> myOrderItemModelList,Dialog loadingDialog) {
        this.myOrderItemModelList = myOrderItemModelList;
        this.loadingDialog = loadingDialog;
    }

    @NonNull
    @Override
    public MyOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_item_layout,parent,false);
      return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyOrdersAdapter.ViewHolder holder, int position) {
        String productId = myOrderItemModelList.get(position).getProductId();
        String resources = myOrderItemModelList.get(position).getProductImage();
        int rating = myOrderItemModelList.get(position).getRating();
        String title = myOrderItemModelList.get(position).getProductTitle();
        String orderStatus = myOrderItemModelList.get(position).getOrderStatus();
        Date date;
        switch (orderStatus){

            case "Ordered":
                date = myOrderItemModelList.get(position).getOrderdDate();
                break;
            case "Packed":
                date = myOrderItemModelList.get(position).getPackedDate();
                break;
            case "Shipped":
                date = myOrderItemModelList.get(position).getShippedDate();
                break;
            case "Delivered":
                date = myOrderItemModelList.get(position).getDeliveredDate();
                break;
            case "Cancelled":
                date = myOrderItemModelList.get(position).getCancelledDate();
                break;
            default:
                date = myOrderItemModelList.get(position).getCancelledDate();
                break;
        }
        holder.setData(resources,title,orderStatus,date,rating,productId,position);

    }

    @Override
    public int getItemCount() {
        return myOrderItemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private ImageView orderIndicator;
        private TextView productTitle;
        private TextView orderDeliveredStatus;
        private LinearLayout RateNow_Container;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productTitle = itemView.findViewById(R.id.order_product_title);
            orderIndicator = itemView.findViewById(R.id.Order_indicator);
            orderDeliveredStatus = itemView.findViewById(R.id.order_deliverd_date);
            RateNow_Container = itemView.findViewById(R.id.Rate_now_container);

        }
        private void setData(String resource,String title,String orderStatus,Date date, int rating, String productID,int position){
            Glide.with(itemView.getContext()).load(resource).into(productImage);
            productTitle.setText(title);

            if (orderStatus.equals("Cancelled")){

                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colour_primary)));

            }else {

                orderIndicator.setImageTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.successGreen)));

            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE, dd MMM yyyy hh:mm aa");
            orderDeliveredStatus.setText(orderStatus+ String.valueOf(simpleDateFormat.format(date)));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent OrderDetailIntent = new Intent(itemView.getContext(), Order_Detail_Activity.class);
                    OrderDetailIntent.putExtra("Position",position);
                    itemView.getContext().startActivity(OrderDetailIntent);
                }
            });
            //////rating layout
             setRating(rating);
            for (int x = 0;x<RateNow_Container.getChildCount();x++){
                final int starPosition = x;
                RateNow_Container.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadingDialog.show();
                        setRating(starPosition);
                        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("PRODUCTS").document(productID);
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
                                if (DBqueries.myRatedIds.contains(productID)) {
                                    myrating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);
                                } else {
                                    myrating.put("list_size", DBqueries.myRatedIds.size() + 1);
                                    myrating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                    myrating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);
                                }

                                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid()).collection("USER_DATA").document("MY_RATINGS")
                                        .update(myrating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    DBqueries.myOrderItemModelList.get(position).setRating(starPosition);
                                                    if (DBqueries.myRatedIds.contains(productID)){
                                                        DBqueries.myRating.set(DBqueries.myRatedIds.indexOf(productID),Long.parseLong(String.valueOf(starPosition+1)));
                                                    }else {
                                                        DBqueries.myRatedIds.add(productID);
                                                        DBqueries.myRating.add(Long.parseLong(String.valueOf(starPosition+1)));
                                                    }
                                                }else {
                                                    String error = task.getException().getMessage();
                                                    Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
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
}
