package com.shubhamwithcode.mymall.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shubhamwithcode.mymall.Adapters.MyOrdersAdapter;
import com.shubhamwithcode.mymall.DBqueries;
import com.shubhamwithcode.mymall.R;


public class myOrdersFragment extends Fragment {

    private RecyclerView myOrderRecyclerview;
    public static MyOrdersAdapter myOrdersAdapter;
    private Dialog loadingDialog;



    public myOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        /////loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_bg));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        /////loading dialog



        myOrderRecyclerview = view.findViewById(R.id.my_orders_recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        myOrderRecyclerview.setLayoutManager(layoutManager);


        myOrdersAdapter = new MyOrdersAdapter(DBqueries.myOrderItemModelList,loadingDialog);
        myOrderRecyclerview.setAdapter(myOrdersAdapter);

        DBqueries.loadOrders(getContext(),myOrdersAdapter,loadingDialog);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        myOrdersAdapter.notifyDataSetChanged();
    }
}