package com.shubhamwithcode.mymall.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shubhamwithcode.mymall.Adapters.productspecificationsAdapter;
import com.shubhamwithcode.mymall.Models.productspecificationsModel;
import com.shubhamwithcode.mymall.R;

import java.util.List;


public class ProductSpecificationsFragment extends Fragment {


    RecyclerView productspecificationsrecyclerview;
    public List<productspecificationsModel> productspecificationsModelList;




    public ProductSpecificationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_specifications, container, false);

        productspecificationsrecyclerview = view.findViewById(R.id.product_specifications_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        productspecificationsrecyclerview.setLayoutManager(linearLayoutManager);

//        productspecificationsModelList.add(new productspecificationsModel(0,"general"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(0,"progress"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(0,"general"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(0,"progress"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));
//        productspecificationsModelList.add(new productspecificationsModel(1,"RAM","5gb"));





        productspecificationsAdapter productspecificationsAdapter= new productspecificationsAdapter(productspecificationsModelList);
        productspecificationsAdapter.notifyDataSetChanged();
        productspecificationsrecyclerview.setAdapter(productspecificationsAdapter);

        return view;
    }
}