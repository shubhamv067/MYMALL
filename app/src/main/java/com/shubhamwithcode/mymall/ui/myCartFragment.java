package com.shubhamwithcode.mymall.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shubhamwithcode.mymall.Adapters.CartAdapter;
import com.shubhamwithcode.mymall.DBqueries;
import com.shubhamwithcode.mymall.DeliveryActivity;
import com.shubhamwithcode.mymall.Models.RewardModel;
import com.shubhamwithcode.mymall.Models.cartitemModel;
import com.shubhamwithcode.mymall.R;

import java.util.ArrayList;


public class myCartFragment extends Fragment {




    public myCartFragment() {
        // Required empty public constructor
    }

    RecyclerView cartItemRecyclerView;
    Button Continuebtn;

    private Dialog loadingDialog;
    public static CartAdapter cartAdapter;
    private TextView totalAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_cart, container, false);
        cartItemRecyclerView = view.findViewById(R.id.cart_item_RecyclerView);
        Continuebtn = view.findViewById(R.id.cart_continue_btn);
        totalAmount = view.findViewById(R.id.Total_cart_Amount);

        /////loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_bg));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        /////loading dialog

        LinearLayoutManager layoutManager  = new LinearLayoutManager(view.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cartItemRecyclerView.setLayoutManager(layoutManager);



        cartAdapter = new CartAdapter(DBqueries.cartitemModelList,totalAmount,true);
        cartItemRecyclerView.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();


        Continuebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeliveryActivity.cartitemModelList = new ArrayList<>();
                DeliveryActivity.fromCart = true;
                for (int x = 0; x < DBqueries.cartitemModelList.size();x++){
                    cartitemModel cartitemModel = DBqueries.cartitemModelList.get(x);
                     if (cartitemModel.isInStock()){
                        DeliveryActivity.cartitemModelList.add(cartitemModel);
                    }
                }
                DeliveryActivity.cartitemModelList.add(new cartitemModel(cartitemModel.TOTAL_AMOUNT));
              loadingDialog.show();
              if (DBqueries.addressesModelList.size() == 0) {
                  DBqueries.loadAddresses(getContext(), loadingDialog,true);
              }else {
                  loadingDialog.dismiss();
                  Intent DeliveryIntent = new Intent(getContext(), DeliveryActivity.class);
                  startActivity(DeliveryIntent);
              }
            }
        });
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        cartAdapter.notifyDataSetChanged();
        if (DBqueries.rewardModelList.size() == 0){
            loadingDialog.show();
            DBqueries.loadRewards(getContext(),loadingDialog,false);
        }
        if(DBqueries.cartitemModelList.size() == 0){
            DBqueries.cartList.clear();
            DBqueries.loadCartList(getContext(),loadingDialog,true,new TextView(getContext()),totalAmount);
        }else {
            if (DBqueries.cartitemModelList.get(DBqueries.cartitemModelList.size()-1).getType() == cartitemModel.TOTAL_AMOUNT){
                LinearLayout parent = (LinearLayout) totalAmount.getParent().getParent();
                parent.setVisibility(View.VISIBLE);
            }
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for (cartitemModel cartitemModel : DBqueries.cartitemModelList){
            if (!TextUtils.isEmpty(cartitemModel.getSelectedCoupenId())){
                for (RewardModel rewardModel : DBqueries.rewardModelList) {
                    if (rewardModel.getCoupenId().equals(cartitemModel.getSelectedCoupenId())) {
                        rewardModel.setAlreadyUsed(false);
                    }
                }
                cartitemModel.setSelectedCoupenId(null);
                if (myRewardsFragment.myRewardAdapter != null){
                    myRewardsFragment.myRewardAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}