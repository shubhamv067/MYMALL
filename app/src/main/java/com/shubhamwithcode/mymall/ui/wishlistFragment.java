package com.shubhamwithcode.mymall.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shubhamwithcode.mymall.Adapters.WishlistAdapter;
import com.shubhamwithcode.mymall.DBqueries;
import com.shubhamwithcode.mymall.R;


public class wishlistFragment extends Fragment {


    public wishlistFragment() {
        // Required empty public constructor
    }

    RecyclerView wishList_Item_RecyclerView;
    private Dialog loadingDialog;
    public static WishlistAdapter wishlistAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wishlist, container, false);
        wishList_Item_RecyclerView = view.findViewById(R.id.wishList_Item_RecyclerView);
        /////loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_bg));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        /////loading dialog


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        wishList_Item_RecyclerView.setLayoutManager(layoutManager);

        if(DBqueries.wishlistModelList.size() == 0){
            DBqueries.wishList.clear();
            DBqueries.loadWishList(getContext(),loadingDialog,true);
        }else {
            loadingDialog.dismiss();
        }

         wishlistAdapter = new WishlistAdapter(DBqueries.wishlistModelList,true);
        wishlistAdapter.notifyDataSetChanged();
        wishList_Item_RecyclerView.setAdapter(wishlistAdapter);

      return view;
    }
}