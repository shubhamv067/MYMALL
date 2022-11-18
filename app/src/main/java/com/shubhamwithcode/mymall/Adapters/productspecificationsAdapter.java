package com.shubhamwithcode.mymall.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shubhamwithcode.mymall.Models.productspecificationsModel;
import com.shubhamwithcode.mymall.R;

import java.util.List;

public class productspecificationsAdapter extends RecyclerView.Adapter<productspecificationsAdapter.viewHolder> {

    List<productspecificationsModel> productspecificationsModelList;

    public productspecificationsAdapter(List<productspecificationsModel> productspecificationsModelList) {
        this.productspecificationsModelList = productspecificationsModelList;
    }

    @Override
    public int getItemViewType(int position) {
        switch (productspecificationsModelList.get(position).getType()) {
            case 0:
                return productspecificationsModel.SPECIFICATIONS_TITLE;

            case 1:
                return productspecificationsModel.SPECIFICATIONS_BODY;

            default:
                return -1;

        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case productspecificationsModel.SPECIFICATIONS_TITLE:
                TextView title = new TextView(parent.getContext());
                title.setTypeface(null, Typeface.BOLD);
                title.setTextColor(Color.parseColor("#000000"));
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(setDp(16, parent.getContext()), setDp(16, parent.getContext()), setDp(16, parent.getContext()), setDp(8, parent.getContext()));
                title.setLayoutParams(layoutParams);
                return new viewHolder(title);


            case productspecificationsModel.SPECIFICATIONS_BODY:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_specifications_iteam_layout, parent, false);
                return new viewHolder(view);

            default:
                return null;


        }


    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        switch (productspecificationsModelList.get(position).getType()) {
            case productspecificationsModel.SPECIFICATIONS_TITLE:
                holder.setTitle(productspecificationsModelList.get(position).getTitle());
                break;
            case productspecificationsModel.SPECIFICATIONS_BODY:
                String featureTitle = productspecificationsModelList.get(position).getFeatureName();
                String featureName = productspecificationsModelList.get(position).getFeatureValue();
                holder.setFeatures(featureTitle, featureName);
                break;
            default:
                return;

        }


    }

    @Override
    public int getItemCount() {
        return productspecificationsModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView featureName;
        TextView featureValue;
        TextView title;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

        }

        private void setTitle(String titleText) {
            title = (TextView) itemView;
            title.setText(titleText);
        }

        private void setFeatures(String featureTitle, String featureDetail) {
            featureName = itemView.findViewById(R.id.Featur_name);
            featureValue = itemView.findViewById(R.id.Featur_value);

            featureName.setText(featureTitle);
            featureValue.setText(featureDetail);
        }
    }

    private int setDp(int dp, Context context) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());

    }
}
