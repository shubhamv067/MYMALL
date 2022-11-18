package com.shubhamwithcode.mymall.ui;

import static com.shubhamwithcode.mymall.DBqueries.categoryModelList;
import static com.shubhamwithcode.mymall.DBqueries.lists;
import static com.shubhamwithcode.mymall.DBqueries.loadCategories;
import static com.shubhamwithcode.mymall.DBqueries.loadFragmentData;
import static com.shubhamwithcode.mymall.DBqueries.loadedCategoriesNames;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.shubhamwithcode.mymall.Adapters.CategoryAdapter;
import com.shubhamwithcode.mymall.Adapters.HomePageAdapter;
import com.shubhamwithcode.mymall.DBqueries;
import com.shubhamwithcode.mymall.Models.CategoryModel;
import com.shubhamwithcode.mymall.Models.HomePageModel;
import com.shubhamwithcode.mymall.Models.WishlistModel;
import com.shubhamwithcode.mymall.Models.horizontalscrollproductModel;
import com.shubhamwithcode.mymall.Models.sliderModel;
import com.shubhamwithcode.mymall.R;

import java.util.ArrayList;
import java.util.List;


public class MyMallFragment extends Fragment {


    public MyMallFragment() {
        // Required empty public constructor
    }

    RecyclerView CategoryRecyclerview;
    CategoryAdapter categoryadapter;

    HomePageAdapter homePageAdapter;
    RecyclerView mymall_RecyclerView;
    List<HomePageModel> homePageModelFakeList = new ArrayList<>();
    ImageView noInternetConnection;
    ConnectivityManager connectivityManager;
    public static SwipeRefreshLayout swipeRefreshLayout;
    NetworkInfo networkInfo;
    List<CategoryModel> categoryModelFakeList = new ArrayList<>();
    Button Retrybtn;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_mall, container, false);

        swipeRefreshLayout = view.findViewById(R.id.Refresh_Layout);
        noInternetConnection = view.findViewById(R.id.no_internet_connection);
        mymall_RecyclerView = view.findViewById(R.id.mymall_Recyclerview);
        CategoryRecyclerview = view.findViewById(R.id.CategoryRecyclerview);
        Retrybtn = view.findViewById(R.id.Retry_btn);

        swipeRefreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colour_primary),getContext().getResources().getColor(R.color.colour_primary),getContext().getResources().getColor(R.color.colour_primary));


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        CategoryRecyclerview.setLayoutManager(linearLayoutManager);


        LinearLayoutManager TestingLayoutManager = new LinearLayoutManager(getContext());
        TestingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mymall_RecyclerView.setLayoutManager(TestingLayoutManager);

        //////categories fake list

        categoryModelFakeList.add(new CategoryModel("null",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));
        categoryModelFakeList.add(new CategoryModel("",""));

        //////categories fake list

        //////home page fake list
        List<sliderModel> sliderModelFakeList = new ArrayList<>();

        sliderModelFakeList.add(new sliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new sliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new sliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new sliderModel("null","#dfdfdf"));
        sliderModelFakeList.add(new sliderModel("null","#dfdfdf"));


        List<horizontalscrollproductModel> horizontalscrollproductModelFakeList = new ArrayList<>();
        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));
        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));
        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));
        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));
        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));
        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));
        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#dfdfdf"));
        homePageModelFakeList.add(new HomePageModel(2,"#dfdfdf","",horizontalscrollproductModelFakeList,new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"#dfdfdf","",horizontalscrollproductModelFakeList));

        //////home page fake list



        categoryadapter = new CategoryAdapter(categoryModelFakeList);


        homePageAdapter = new HomePageAdapter(homePageModelFakeList);



        connectivityManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {
          // MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE);
            Retrybtn.setVisibility(View.GONE);
            CategoryRecyclerview.setVisibility(View.VISIBLE);
            mymall_RecyclerView.setVisibility(View.VISIBLE);


            if (categoryModelList.size() == 0){
                 loadCategories(CategoryRecyclerview,getContext());

            }else {
                categoryadapter.notifyDataSetChanged();
                categoryadapter = new CategoryAdapter(categoryModelList);
            }
            CategoryRecyclerview.setAdapter(categoryadapter);

            ////////////////////////////////////////////////////////////


           if (lists.size() == 0){
               loadedCategoriesNames.add("HOME");
               lists.add(new ArrayList<HomePageModel>());
               loadFragmentData(mymall_RecyclerView,getContext(),0,"Home");


            }else {
               homePageAdapter = new HomePageAdapter(lists.get(0));
               homePageAdapter.notifyDataSetChanged();
            }
            mymall_RecyclerView.setAdapter(homePageAdapter);

           ////////////////////////////////////////////////////////////
        }
        else {
          //  MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            CategoryRecyclerview.setVisibility(View.GONE);
            mymall_RecyclerView.setVisibility(View.GONE);
            Glide.with(this).load(R.drawable.nointernet).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            Retrybtn.setVisibility(View.VISIBLE);
        }
        //////refresh layout
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(true);
                reloadPage();
            }
        });
        //////refresh layout

        Retrybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage();
            }

        });

        return view;
    }
    private void reloadPage(){

        networkInfo = connectivityManager.getActiveNetworkInfo();
//        categoryModelList.clear();
//        lists.clear();
//        loadedCategoriesNames.clear();
        DBqueries.clearData();


        if (networkInfo != null && networkInfo.isConnected() == true) {
            //MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            noInternetConnection.setVisibility(View.GONE);
            Retrybtn.setVisibility(View.GONE);
            CategoryRecyclerview.setVisibility(View.VISIBLE);
            mymall_RecyclerView.setVisibility(View.VISIBLE);

            categoryadapter = new CategoryAdapter(categoryModelFakeList);
            homePageAdapter = new HomePageAdapter(homePageModelFakeList);
            CategoryRecyclerview.setAdapter(categoryadapter);
            mymall_RecyclerView.setAdapter(homePageAdapter);

            loadCategories(CategoryRecyclerview,getContext());

            loadedCategoriesNames.add("HOME");
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(mymall_RecyclerView,getContext(),0,"Home");


        }else {
           // MainActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            Toast.makeText(getContext(), "No Internet Connection!", Toast.LENGTH_SHORT).show();
            Glide.with(getContext()).load(R.drawable.nointernet).into(noInternetConnection);
            noInternetConnection.setVisibility(View.VISIBLE);
            Retrybtn.setVisibility(View.VISIBLE);
            CategoryRecyclerview.setVisibility(View.GONE);
            mymall_RecyclerView.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }

    }

}