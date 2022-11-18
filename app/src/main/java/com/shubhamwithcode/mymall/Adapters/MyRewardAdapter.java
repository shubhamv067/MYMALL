package com.shubhamwithcode.mymall.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shubhamwithcode.mymall.Models.RewardModel;
import com.shubhamwithcode.mymall.Models.cartitemModel;
import com.shubhamwithcode.mymall.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MyRewardAdapter extends RecyclerView.Adapter<MyRewardAdapter.ViewHolder> {

    List<RewardModel> rewardModelList;
    private boolean useMiniLayout = false;
    private RecyclerView coupensRecyclerView;
    private LinearLayout selectedCoupen;
    private String productoriginalPrice;
    private TextView selectedCoupenTitle;
    private TextView selectedCoupenExpiryDate;
    private TextView selectedCoupenBody;
    private TextView discountedPrice;
    private int cartItemPosition = -1;
    private List<cartitemModel> cartitemModelList;

    public MyRewardAdapter(List<RewardModel> rewardModelList,Boolean useMiniLayout) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
    }
    public MyRewardAdapter(List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerView, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody,TextView discountedPrice) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.selectedCoupen = selectedCoupen;
        this.productoriginalPrice = productOriginalPrice;
        this.selectedCoupenTitle = coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedCoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
    }
    public MyRewardAdapter(int cartItemPosition,List<RewardModel> rewardModelList, Boolean useMiniLayout, RecyclerView coupensRecyclerView, LinearLayout selectedCoupen, String productOriginalPrice, TextView coupenTitle, TextView coupenExpiryDate, TextView coupenBody,TextView discountedPrice,List<cartitemModel> cartitemModelList) {
        this.rewardModelList = rewardModelList;
        this.useMiniLayout = useMiniLayout;
        this.coupensRecyclerView = coupensRecyclerView;
        this.selectedCoupen = selectedCoupen;
        this.productoriginalPrice = productOriginalPrice;
        this.selectedCoupenTitle = coupenTitle;
        this.selectedCoupenExpiryDate = coupenExpiryDate;
        this.selectedCoupenBody = coupenBody;
        this.discountedPrice = discountedPrice;
        this.cartItemPosition = cartItemPosition;
        this.cartitemModelList = cartitemModelList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (useMiniLayout){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mini_my_reward_item_layout, parent, false);
            return new ViewHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_reward_item_layout, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String coupenId = rewardModelList.get(position).getCoupenId();
        String type = rewardModelList.get(position).getType();
        Date validity = rewardModelList.get(position).getDate();
        String body = rewardModelList.get(position).getCoupenBody();
        String lowerLimit = rewardModelList.get(position).getLowerLimit();
        String upperLimit = rewardModelList.get(position).getUpperLimit();
        String discORamt = rewardModelList.get(position).getdiscORamt();
        Boolean alreadyUsed = rewardModelList.get(position).isAlreadyUsed();

        holder.setData(coupenId,type,validity,body,upperLimit,lowerLimit,discORamt,alreadyUsed);

    }

    @Override
    public int getItemCount() {
        return rewardModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView coupenTitle;
        private TextView coupenExpiryDate;
        private TextView coupenBody;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            coupenTitle = itemView.findViewById(R.id.reward_coupen_title);
            coupenExpiryDate = itemView.findViewById(R.id.reward_coupen_validity);
            coupenBody = itemView.findViewById(R.id.reward_coupen_body);
        }
        private void setData(String coupenId,String type,Date validity,String body,String upperLimit,String lowerLimit,String discORamt,boolean alreadyUsed){

            if (type.equals("Discount")){
                coupenTitle.setText(type);
            }else {
                coupenTitle.setText("FLAT Rs."+discORamt+" OFF");
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
            if (alreadyUsed){
                coupenExpiryDate.setText("Already used");
                coupenExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.colour_primary));
                coupenBody.setTextColor(Color.parseColor("50ffffff"));
                coupenTitle.setTextColor(Color.parseColor("50ffffff"));
            }else {
                coupenBody.setTextColor(Color.parseColor("#ffffff"));
                coupenTitle.setTextColor(Color.parseColor("#ffffff"));
                coupenExpiryDate.setTextColor(itemView.getContext().getResources().getColor(R.color.coupenValidityColor));
                coupenExpiryDate.setText("till "+simpleDateFormat.format(validity));
            }
            coupenBody.setText(body);

             if (useMiniLayout){
                 itemView.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         if (!alreadyUsed) {
                             selectedCoupenTitle.setText(type);
                             selectedCoupenExpiryDate.setText(simpleDateFormat.format(validity));
                             selectedCoupenBody.setText(body);

                             if (Long.valueOf(productoriginalPrice) > Long.valueOf(lowerLimit) && Long.valueOf(productoriginalPrice) < Long.valueOf(upperLimit)) {
                                 if (type.equals("Discount")) {
                                     Long discountAmount = Long.valueOf(productoriginalPrice) * Long.valueOf(discORamt) / 100;
                                     discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productoriginalPrice) - discountAmount) + "/-");
                                 } else {
                                     discountedPrice.setText("Rs." + String.valueOf(Long.valueOf(productoriginalPrice) - Long.valueOf(discORamt)) + "/-");
                                 }
                                 if (cartItemPosition != -1) {
                                     cartitemModelList.get(cartItemPosition).setSelectedCoupenId(coupenId);
                                 }
                             } else {
                                 if (cartItemPosition != -1) {
                                     cartitemModelList.get(cartItemPosition).setSelectedCoupenId(null);
                                 }
                                 discountedPrice.setText("Invalid");
                                 Toast.makeText(itemView.getContext(), "Sorry ! Product does not  matches the coupen terms.", Toast.LENGTH_SHORT).show();
                             }

                             if (coupensRecyclerView.getVisibility() == View.GONE) {
                                 coupensRecyclerView.setVisibility(View.VISIBLE);
                                 selectedCoupen.setVisibility(View.GONE);
                             } else {
                                 coupensRecyclerView.setVisibility(View.GONE);
                                 selectedCoupen.setVisibility(View.VISIBLE);
                             }
                         }
                     }
                 });
             }
        }
    }
}
