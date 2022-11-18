package com.shubhamwithcode.mymall.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shubhamwithcode.mymall.Adapters.MyRewardAdapter;
import com.shubhamwithcode.mymall.DBqueries;
import com.shubhamwithcode.mymall.R;


public class myRewardsFragment extends Fragment {



    public myRewardsFragment() {
        // Required empty public constructor
    }

    RecyclerView my_Reward_RecyclerView;
    private Dialog loadingDialog;
    public static MyRewardAdapter myRewardAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_rewards, container, false);

        /////loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_bg));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        /////loading dialog

        my_Reward_RecyclerView = view.findViewById(R.id.my_Reward_RecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        my_Reward_RecyclerView.setLayoutManager(layoutManager);
        if (DBqueries.rewardModelList.size() == 0){
            DBqueries.loadRewards(getContext(),loadingDialog,true);
        }else {
            loadingDialog.dismiss();
        }

        myRewardAdapter = new MyRewardAdapter(DBqueries.rewardModelList,false);
        myRewardAdapter.notifyDataSetChanged();
        my_Reward_RecyclerView.setAdapter(myRewardAdapter);
        return  view;
    }
}