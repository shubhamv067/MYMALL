package com.shubhamwithcode.mymall.Adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shubhamwithcode.mymall.Models.CategoryModel;
import com.shubhamwithcode.mymall.R;
import com.shubhamwithcode.mymall.category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {

    List<CategoryModel> categoryModelList;
    private int lastPosition = -1;

    public CategoryAdapter(List<CategoryModel> categoryModelList) {
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public CategoryAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_sample,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewHolder holder, @SuppressLint("RecyclerView") int position) {
    String Icon = categoryModelList.get(position).getCategoryLink();
    String Name = categoryModelList.get(position).getCategoryName();
    holder.setCategory(Name,position);
    holder.setCategoryIcon(Icon);

        if (lastPosition < position){
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in);
            holder.itemView.setAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView CategoryIcon;
        TextView CategoryName;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            CategoryIcon = itemView.findViewById(R.id.CategoryIcon);
            CategoryName = itemView.findViewById(R.id.CategoryName);
        }

        private void setCategoryIcon(String iconUrl) {
            if (!iconUrl.equals("null")) {
                Glide.with(itemView.getContext()).load(iconUrl).apply(new RequestOptions().placeholder(R.drawable.blank_small)).into(CategoryIcon);
            }else {
                CategoryIcon.setImageResource(R.drawable.home);
            }
        }
        private void setCategory( final String name,int position){
            CategoryName.setText(name);
            if (!name.equals("")) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position != 0) {
                            Intent intent = new Intent(itemView.getContext(), category.class);
                            intent.putExtra("categoryName", name);
                            itemView.getContext().startActivity(intent);
                        }
                    }
                });
            }

        }
    }
}
