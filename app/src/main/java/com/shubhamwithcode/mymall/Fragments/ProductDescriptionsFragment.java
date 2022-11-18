package com.shubhamwithcode.mymall.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.shubhamwithcode.mymall.R;


public class ProductDescriptionsFragment extends Fragment {


    public ProductDescriptionsFragment() {
        // Required empty public constructor
    }
    private TextView descriptionBody;
    public String body;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_descriptions, container, false);

        descriptionBody = view.findViewById(R.id.tv_product_Descriptions);
        descriptionBody.setText(body);

    return view;
    }
}