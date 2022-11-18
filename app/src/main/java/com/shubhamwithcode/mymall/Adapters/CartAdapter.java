package com.shubhamwithcode.mymall.Adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.shubhamwithcode.mymall.DBqueries;
import com.shubhamwithcode.mymall.DeliveryActivity;
import com.shubhamwithcode.mymall.MainActivity;
import com.shubhamwithcode.mymall.Models.RewardModel;
import com.shubhamwithcode.mymall.Models.cartitemModel;
import com.shubhamwithcode.mymall.Product_Detail_Activity;
import com.shubhamwithcode.mymall.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class CartAdapter extends RecyclerView.Adapter {

    private List<cartitemModel> cartitemModelList;
    private int lastPosition = -1;
    private TextView cartTotalAmount;
    private boolean showDeleteBtn;

    public CartAdapter(List<cartitemModel> cartitemModelList, TextView cartTotalAmount, boolean showDeleteBtn) {
        this.cartitemModelList = cartitemModelList;
        this.cartTotalAmount = cartTotalAmount;
        this.showDeleteBtn = showDeleteBtn;
    }

    @Override
    public int getItemViewType(int position) {
        switch (cartitemModelList.get(position).getType()) {
            case 0:
                return cartitemModel.CART_ITEM;

            case 1:
                return cartitemModel.TOTAL_AMOUNT;
            default:
                return -1;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case cartitemModel.CART_ITEM:
                View cartItemview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout, parent, false);
                return new cartItemviewHolder(cartItemview);
            case cartitemModel.TOTAL_AMOUNT:
                View totalamountview = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_total_amount_layout, parent, false);
                return new totalAmountviewHolder(totalamountview);
            default:
                return null;

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        switch (cartitemModelList.get(position).getType()) {
            case cartitemModel.CART_ITEM:
                String productID = cartitemModelList.get(position).getProductID();
                String resource = cartitemModelList.get(position).getProductImage();
                String title = cartitemModelList.get(position).getProductTitle();
                Long freeCoupens = cartitemModelList.get(position).getFreeCoupens();
                String productPrice = cartitemModelList.get(position).getProductPrice();
                String cuttedPrice = cartitemModelList.get(position).getCuttedPrice();
                Long offersApplied = cartitemModelList.get(position).getOfferApplied();
                boolean inStock = cartitemModelList.get(position).isInStock();
                Long productQuantity = cartitemModelList.get(position).getProductQuantity();
                Long maxQuantity = cartitemModelList.get(position).getMaxQuantity();
                boolean qtyError = cartitemModelList.get(position).isQtyError();
                List<String> qtyIds = cartitemModelList.get(position).getQtyIDs();
                long stockQty = cartitemModelList.get(position).getStockQuantity();
                boolean COD = cartitemModelList.get(position).isCOD();

                ((cartItemviewHolder) holder).setItemDetails(productID, resource, title, freeCoupens, productPrice, cuttedPrice, offersApplied, position, inStock,String.valueOf(productQuantity),maxQuantity,qtyError,qtyIds,stockQty,COD);
                break;
            case cartitemModel.TOTAL_AMOUNT:
                int totalItems = 0;
                int totalItemPrice = 0;
                String deliveryPrice;
                int totalAmount;
                int savedAmount = 0;

                for (int x = 0; x < cartitemModelList.size(); x++) {
                    if (cartitemModelList.get(x).getType() == cartitemModel.CART_ITEM && cartitemModelList.get(x).isInStock()) {
                        int quantity = Integer.parseInt(String.valueOf(cartitemModelList.get(x).getProductQuantity()));
                        totalItems = totalItems + quantity;
                        if (TextUtils.isEmpty(cartitemModelList.get(x).getSelectedCoupenId())) {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartitemModelList.get(x).getProductPrice())*quantity;
                        }else {
                            totalItemPrice = totalItemPrice + Integer.parseInt(cartitemModelList.get(x).getDiscountedPrice())*quantity;
                        }
                        if (!TextUtils.isEmpty(cartitemModelList.get(x).getCuttedPrice())){
                            savedAmount = savedAmount + (Integer.parseInt(cartitemModelList.get(x).getCuttedPrice()) - Integer.parseInt(cartitemModelList.get(x).getProductPrice()))*quantity;
                            if (!TextUtils.isEmpty(cartitemModelList.get(x).getSelectedCoupenId())){
                                savedAmount = savedAmount + (Integer.parseInt(cartitemModelList.get(x).getProductPrice()) - Integer.parseInt(cartitemModelList.get(x).getDiscountedPrice()))*quantity;
                            }
                        }else {
                            if (!TextUtils.isEmpty(cartitemModelList.get(x).getSelectedCoupenId())){
                                savedAmount = savedAmount + (Integer.parseInt(cartitemModelList.get(x).getProductPrice()) - Integer.parseInt(cartitemModelList.get(x).getDiscountedPrice()))*quantity;
                            }
                        }
                    }
                }
                if (totalItemPrice > 500) {
                    deliveryPrice = "FREE";
                    totalAmount = totalItemPrice;
                } else {
                    deliveryPrice = "60";
                    totalAmount = totalItemPrice + 60;
                }
                cartitemModelList.get(position).setTotalItems(totalItems);
                cartitemModelList.get(position).setTotalItemPrice(totalItemPrice);
                cartitemModelList.get(position).setDeliveryPrice(deliveryPrice);
                cartitemModelList.get(position).setTotalAmount(totalAmount);
                cartitemModelList.get(position).setSavedAmount(savedAmount);

                ((totalAmountviewHolder) holder).setTotalAmount(totalItems, totalItemPrice, deliveryPrice, totalAmount, savedAmount);

                break;
            default:
                return;
        }


        if (lastPosition < position) {
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }

    }

    @Override
    public int getItemCount() {
        return cartitemModelList.size();
    }

    class cartItemviewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        ImageView freecoupenIcon;
        TextView productTitle;
        TextView freeCoupens;
        TextView productprice;
        TextView cuttedprice;
        TextView offersApplied;
        TextView coupensApplied;
        TextView productQuantity;
        LinearLayout deleteBtn;
        Button redeemBtn;
        LinearLayout coupenRedemptionLayout;
        TextView coupenRedemptionBody;
        ImageView codIndicator;

        ////////coupendialog
        private TextView coupenTitle;
        private TextView coupenExpiryData;
        private TextView coupenBody;
        private RecyclerView coupensRecyclerView;
        private LinearLayout selectedCoupen;
        private TextView orignalPrice;
        private TextView discountPrice;
        private LinearLayout applyORremoveBtnContainer;
        private TextView footerText;
        private Button removeCoupenBtn,applyCoupenBtn;
        private String productOriginalPrice;
        ////////coupendialog

        public cartItemviewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.Product_Image);
            freecoupenIcon = itemView.findViewById(R.id.Free_coupen_icon);
            productTitle = itemView.findViewById(R.id.Product_title);
            freeCoupens = itemView.findViewById(R.id.tv_Free_coupen);
            productprice = itemView.findViewById(R.id.Product_Price);
            cuttedprice = itemView.findViewById(R.id.Cutted_Price_1);
            offersApplied = itemView.findViewById(R.id.Offers_applied);
            coupensApplied = itemView.findViewById(R.id.coupens_applied);
            productQuantity = itemView.findViewById(R.id.product_quantity);
            deleteBtn = itemView.findViewById(R.id.Remove_item_btn);
            redeemBtn = itemView.findViewById(R.id.cupen_redeempation_btn);
            coupenRedemptionLayout = itemView.findViewById(R.id.cupen_redeempation_layout);
            coupenRedemptionBody = itemView.findViewById(R.id.tv_cupen_redeempation);
            codIndicator = itemView.findViewById(R.id.cod_indicator);
        }

        public void setItemDetails(String productID, String resource, String title, Long freeCoupensNo, String productPriceText, String cuttedPriceText, Long offerAppliedNo, int position, boolean inStock,String quantity,Long maxQuantity,boolean qtyError,List<String> qtyIds,long stockQty,boolean COD) {
            Glide.with(itemView.getContext()).load(resource).apply(new RequestOptions().placeholder(R.drawable.blank_small)).into(productImage);
            productTitle.setText(title);

            Dialog checkCoupenPriceDialog = new Dialog(itemView.getContext());
            checkCoupenPriceDialog.setContentView(R.layout.coupen_redeem_dialogue);
            checkCoupenPriceDialog.setCancelable(false);
            checkCoupenPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (COD){
                codIndicator.setVisibility(View.VISIBLE);
            }else {
                codIndicator.setVisibility(View.INVISIBLE);
            }

            if (inStock) {

                if (freeCoupensNo > 0) {
                    freecoupenIcon.setVisibility(View.VISIBLE);
                    freeCoupens.setVisibility(View.VISIBLE);
                    if (freeCoupensNo == 1) {
                        freeCoupens.setText("free " + freeCoupensNo + " Coupen");

                    } else {
                        freeCoupens.setText("free " + freeCoupensNo + " Coupens");
                    }
                } else {
                    freecoupenIcon.setVisibility(View.INVISIBLE);
                    freeCoupens.setVisibility(View.INVISIBLE);
                }

                productprice.setText("Rs." + productPriceText + "/-");
                productprice.setTextColor(Color.parseColor("#000000"));
                cuttedprice.setText("Rs." + cuttedPriceText + "/-");
                coupenRedemptionLayout.setVisibility(View.VISIBLE);

                  ////coupen dialog

                    ImageView toggleRecyclerView = checkCoupenPriceDialog.findViewById(R.id.toggle_recyclerview);
                    coupensRecyclerView = checkCoupenPriceDialog.findViewById(R.id.coupens_recyclerview);
                    selectedCoupen = checkCoupenPriceDialog.findViewById(R.id.selected_coupen);
                    coupenTitle = checkCoupenPriceDialog.findViewById(R.id.reward_coupen_title);
                    coupenExpiryData = checkCoupenPriceDialog.findViewById(R.id.reward_coupen_validity);
                    coupenBody = checkCoupenPriceDialog.findViewById(R.id.reward_coupen_body);
                    footerText = checkCoupenPriceDialog.findViewById(R.id.footer_text);
                    applyORremoveBtnContainer = checkCoupenPriceDialog.findViewById(R.id.applyORremoveBtnContainer);
                    removeCoupenBtn = checkCoupenPriceDialog.findViewById(R.id.remove_btn);
                    applyCoupenBtn = checkCoupenPriceDialog.findViewById(R.id.apply_btn);

                    footerText.setVisibility(View.GONE);
                    applyORremoveBtnContainer.setVisibility(View.VISIBLE);
                    orignalPrice = checkCoupenPriceDialog.findViewById(R.id.original_price);
                    discountPrice = checkCoupenPriceDialog.findViewById(R.id.discounted_price);


                    LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    coupensRecyclerView.setLayoutManager(layoutManager);


                    orignalPrice.setText(productprice.getText());
                    productOriginalPrice = productPriceText;
                    MyRewardAdapter myRewardAdapter = new MyRewardAdapter(position,DBqueries.rewardModelList, true,coupensRecyclerView,selectedCoupen,productOriginalPrice,coupenTitle,coupenExpiryData,coupenBody,discountPrice,cartitemModelList);
                    myRewardAdapter.notifyDataSetChanged();
                    coupensRecyclerView.setAdapter(myRewardAdapter);

                    applyCoupenBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!TextUtils.isEmpty(cartitemModelList.get(position).getSelectedCoupenId())) {
                                for (RewardModel rewardModel : DBqueries.rewardModelList) {
                                    if (rewardModel.getCoupenId().equals(cartitemModelList.get(position).getSelectedCoupenId())) {
                                        rewardModel.setAlreadyUsed(true);
                                        coupenRedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_bg_layout));
                                        coupenRedemptionBody.setText(rewardModel.getCoupenBody());
                                        redeemBtn.setText("Cupen");
                                    }
                                }
                                coupensApplied.setVisibility(View.VISIBLE);
                                cartitemModelList.get(position).setDiscountedPrice(discountPrice.getText().toString().substring(3,discountPrice.getText().length() -2));
                                productprice.setText(discountPrice.getText());
                                String offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(discountPrice.getText().toString().substring(3,discountPrice.getText().length() - 2)));
                                coupensApplied.setText("Coupen Applied -Rs." + offerDiscountedAmt+"/-");
                                notifyItemChanged(cartitemModelList.size() -1);
                                checkCoupenPriceDialog.dismiss();
                            }
                        }
                    });

                    removeCoupenBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (RewardModel rewardModel : DBqueries.rewardModelList){
                                if (rewardModel.getCoupenId().equals(cartitemModelList.get(position).getSelectedCoupenId())){
                                    rewardModel.setAlreadyUsed(false);
                                }
                            }
                            coupenTitle.setText("Coupen");
                            coupenExpiryData.setText("validity");
                            coupenBody.setText("Tap the icon on the top right corner to select your coupen.");
                            coupensApplied.setVisibility(View.INVISIBLE);
                            coupenRedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.coupenRed));
                            coupenRedemptionBody.setText("Apply your coupen here");
                            redeemBtn.setText("Redeem");
                            cartitemModelList.get(position).setSelectedCoupenId(null);
                            productprice.setText("Rs."+productPriceText+"/-");
                            notifyItemChanged(cartitemModelList.size() -1);
                            checkCoupenPriceDialog.dismiss();
                        }
                    });

                    toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDialogRecyclerView();
                        }
                    });

                if (!TextUtils.isEmpty(cartitemModelList.get(position).getSelectedCoupenId())) {
                    for (RewardModel rewardModel : DBqueries.rewardModelList) {
                        if (rewardModel.getCoupenId().equals(cartitemModelList.get(position).getSelectedCoupenId())) {
                            coupenRedemptionLayout.setBackground(itemView.getContext().getResources().getDrawable(R.drawable.reward_bg_layout));
                            coupenRedemptionBody.setText(rewardModel.getCoupenBody());
                            redeemBtn.setText("Cupen");

                            coupenBody.setText(rewardModel.getCoupenBody());
                            if (rewardModel.getType().equals("Discount")){
                                coupenTitle.setText(rewardModel.getType());
                            }else {
                                coupenTitle.setText("FLAT Rs."+rewardModel.getdiscORamt()+" OFF");
                            }
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
                            coupenExpiryData.setText("till "+simpleDateFormat.format(rewardModel.getDate()));
                        }
                    }
                    discountPrice.setText("Rs."+cartitemModelList.get(position).getDiscountedPrice()+"/-");
                    coupensApplied.setVisibility(View.VISIBLE);
                    productprice.setText("Rs."+cartitemModelList.get(position).getDiscountedPrice()+"/-");
                    String offerDiscountedAmt = String.valueOf(Long.valueOf(productPriceText) - Long.valueOf(cartitemModelList.get(position).getDiscountedPrice()));
                    coupensApplied.setText("Coupen Applied -Rs." + offerDiscountedAmt+"/-");
                }else {
                    coupensApplied.setVisibility(View.INVISIBLE);
                    coupenRedemptionLayout.setBackgroundColor(itemView.getContext().getResources().getColor(R.color.coupenRed));
                    coupenRedemptionBody.setText("Apply your coupen here");
                    redeemBtn.setText("Redeem");
                }

                ////coupen dialog


                productQuantity.setText("Qty: "+quantity);
                if (!showDeleteBtn) {
                    if (qtyError) {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.colour_primary));
                        //
                        // productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.colour_primary)));
                    } else {
                        productQuantity.setTextColor(itemView.getContext().getResources().getColor(R.color.black));
                      //  productQuantity.setBackgroundTintList(ColorStateList.valueOf(itemView.getContext().getResources().getColor(R.color.black)));
                    }
                }
                productQuantity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog quantityDialog = new Dialog(itemView.getContext());
                        quantityDialog.setContentView(R.layout.quantity_dialog);
                        quantityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        quantityDialog.setCancelable(false);
                        final EditText quantityNo = quantityDialog.findViewById(R.id.Quantity_No);
                        Button cancelbtn = quantityDialog.findViewById(R.id.Quantity_Cancel_btn);
                        Button okbtn = quantityDialog.findViewById(R.id.Quantity_OK_btn);
                        quantityNo.setHint("Max "+ String.valueOf(maxQuantity));

                        cancelbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quantityDialog.dismiss();
                            }
                        });
                        okbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!TextUtils.isEmpty(quantityNo.getText())){
                                if (Long.valueOf(quantityNo.getText().toString()) <= maxQuantity && Long.valueOf(quantityNo.getText().toString()) != 0 ) {
                                    if (itemView.getContext() instanceof MainActivity){
                                        cartitemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                    }else {
                                        if (DeliveryActivity.fromCart) {
                                            cartitemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                        } else {
                                            DeliveryActivity.cartitemModelList.get(position).setProductQuantity(Long.valueOf(quantityNo.getText().toString()));
                                        }
                                    }
                                    productQuantity.setText("Qty: " + quantityNo.getText());
                                    notifyItemChanged(cartitemModelList.size() -1);
                                    if (!showDeleteBtn) {
                                        DeliveryActivity.loadingDialog.show();
                                        DeliveryActivity.cartitemModelList.get(position).setQtyError(false);
                                        int initialQty = Integer.parseInt(quantity);
                                        int finalQTy = Integer.parseInt(quantityNo.getText().toString());
                                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                        if (finalQTy > initialQty) {
                                            for (int y = 0; y < finalQTy - initialQty; y++) {
                                                String quantityDocumentName = UUID.randomUUID().toString().substring(0, 20);

                                                Map<String, Object> timestamp = new HashMap<>();
                                                timestamp.put("time", FieldValue.serverTimestamp());
                                                int finalY = y;
                                                firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(quantityDocumentName).set(timestamp)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                qtyIds.add(quantityDocumentName);

                                                                if (finalY + 1 == finalQTy - initialQty) {
                                                                    firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").orderBy("time", Query.Direction.ASCENDING).limit(stockQty).get()
                                                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                    if (task.isSuccessful()) {
                                                                                        List<String> serverQuantity = new ArrayList<>();
                                                                                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                                                                                            serverQuantity.add(queryDocumentSnapshot.getId());
                                                                                        }
                                                                                        long AvailableQty = 0;
                                                                                        for (String qtyID : qtyIds) {
                                                                                            if (!serverQuantity.contains(qtyID)) {

                                                                                                    DeliveryActivity.cartitemModelList.get(position).setQtyError(true);
                                                                                                    DeliveryActivity.cartitemModelList.get(position).setMaxQuantity(AvailableQty);
                                                                                                    Toast.makeText(itemView.getContext(), "Sorry ! all products may not be available in requires quantity..", Toast.LENGTH_SHORT).show();

                                                                                            }else {
                                                                                                AvailableQty++;
                                                                                            }
                                                                                        }
                                                                                        DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                                    } else {
                                                                                        String error = task.getException().getMessage();
                                                                                        Toast.makeText(itemView.getContext(), error, Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                    DeliveryActivity.loadingDialog.dismiss();
                                                                                }
                                                                            });
                                                                }
                                                            }
                                                        });
                                            }
                                        }else if (initialQty > finalQTy){
                                            for (int x = 0; x < initialQty - finalQTy; x++) {
                                                String qtyid = qtyIds.get(qtyIds.size() - 1 - x);
                                                int finalX = x;
                                                firebaseFirestore.collection("PRODUCTS").document(productID).collection("QUANTITY").document(qtyid).delete()
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                qtyIds.remove(qtyid);
                                                                DeliveryActivity.cartAdapter.notifyDataSetChanged();
                                                                if (finalX +1 == initialQty - finalQTy){
                                                                    DeliveryActivity.loadingDialog.dismiss();
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    }
                                  }

                                }else {
                                    Toast.makeText(itemView.getContext(), "Max quantity"+maxQuantity.toString(), Toast.LENGTH_SHORT).show();

                                }
                                quantityDialog.dismiss();
                            }
                        });
                        quantityDialog.show();
                    }
                });

                if (offerAppliedNo > 0) {
                    offersApplied.setVisibility(View.VISIBLE);
                    String offerDiscountedAmt = String.valueOf(Long.valueOf(cuttedPriceText) - Long.valueOf(productPriceText));
                    offersApplied.setText("offer Applied - Rs."+offerDiscountedAmt+"/-");
                } else {
                    offersApplied.setVisibility(View.INVISIBLE);
                }
            } else {
                productprice.setText("Out of Stock");
                productprice.setTextColor(itemView.getContext().getResources().getColor(R.color.colour_primary));
                cuttedprice.setText("");
                coupenRedemptionLayout.setVisibility(View.GONE);
                freeCoupens.setVisibility(View.INVISIBLE);
                productQuantity.setVisibility(View.INVISIBLE);
                coupensApplied.setVisibility(View.GONE);
                offersApplied.setVisibility(View.GONE);
                freecoupenIcon.setVisibility(View.INVISIBLE);
            }

            if (showDeleteBtn) {
                deleteBtn.setVisibility(View.VISIBLE);
            } else {
                deleteBtn.setVisibility(View.GONE);
            }



            redeemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (RewardModel rewardModel : DBqueries.rewardModelList){
                        if (rewardModel.getCoupenId().equals(cartitemModelList.get(position).getSelectedCoupenId())){
                            rewardModel.setAlreadyUsed(false);
                        }
                    }

                    checkCoupenPriceDialog.show();
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!TextUtils.isEmpty(cartitemModelList.get(position).getSelectedCoupenId())) {
                        for (RewardModel rewardModel : DBqueries.rewardModelList) {
                            if (rewardModel.getCoupenId().equals(cartitemModelList.get(position).getSelectedCoupenId())) {
                                rewardModel.setAlreadyUsed(false);
                            }
                        }
                    }
                       if (!Product_Detail_Activity.running_cart_query) {
                        Product_Detail_Activity.running_cart_query = true;
                        DBqueries.removeFromCart(position, itemView.getContext(),cartTotalAmount);
                    }
                }
            });
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
    }

    class totalAmountviewHolder extends RecyclerView.ViewHolder {

        TextView totalItems;
        TextView totalItemPrice;
        TextView deliveryPrice;
        TextView totalAmount;
        TextView savedAmount;

        public totalAmountviewHolder(@NonNull View itemView) {
            super(itemView);
            totalItems = itemView.findViewById(R.id.Total_items);
            totalItemPrice = itemView.findViewById(R.id.Total_items_price);
            deliveryPrice = itemView.findViewById(R.id.delivery_Price);
            totalAmount = itemView.findViewById(R.id.Total_price);
            savedAmount = itemView.findViewById(R.id.saved_Amount);
        }

        public void setTotalAmount(int totalItemText, int totalItemPriceText, String deliveryPriceText, int totalAmountText, int savedAmountText) {
            totalItems.setText("Price(" + totalItemText + " items)");
            totalItemPrice.setText("Rs." + totalItemPriceText + "/-");
            if (deliveryPriceText.equals("FREE")) {
                deliveryPrice.setText(deliveryPriceText);
            } else {
                deliveryPrice.setText("Rs." + deliveryPriceText + "/-");
            }
            totalAmount.setText("Rs." + totalAmountText + "/-");
            savedAmount.setText("You saved Rs. " + savedAmountText + "/- on this order.");
            cartTotalAmount.setText("Rs." + totalAmountText + "/-");

            LinearLayout parent = (LinearLayout) cartTotalAmount.getParent().getParent();
            if (totalItemPriceText == 0) {
                if (DeliveryActivity.fromCart) {
                    cartitemModelList.remove(cartitemModelList.size() - 1);
                    DeliveryActivity.cartitemModelList.remove(DeliveryActivity.cartitemModelList.size() - 1);
                }
                if (showDeleteBtn){
                    cartitemModelList.remove(cartitemModelList.size() - 1);
                }
                parent.setVisibility(View.GONE);
            }else {
                parent.setVisibility(View.VISIBLE);
            }
        }
    }
}
