package com.shubhamwithcode.mymall.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.shubhamwithcode.mymall.Fragments.ProductDescriptionsFragment;
import com.shubhamwithcode.mymall.Fragments.ProductSpecificationsFragment;
import com.shubhamwithcode.mymall.Models.productspecificationsModel;

import java.util.List;

public class productdetailAdapter  extends FragmentPagerAdapter {

    int totalTabs;
    String productDescription;
    String productOtherDetails;
    private List<productspecificationsModel> productspecificationsModelList;

    public productdetailAdapter(@NonNull FragmentManager fm,int totalTabs, String productDescription, String productOtherDetails, List<productspecificationsModel> productspecificationsModelList) {
        super(fm);
        this.productDescription = productDescription;
        this.productOtherDetails = productOtherDetails;
        this.productspecificationsModelList = productspecificationsModelList;
        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ProductDescriptionsFragment productDescriptionsFragment1 = new ProductDescriptionsFragment();
                productDescriptionsFragment1.body = productDescription;
                return productDescriptionsFragment1;

            case 1:
                ProductSpecificationsFragment productSpecificationsFragment = new ProductSpecificationsFragment();
                productSpecificationsFragment.productspecificationsModelList = productspecificationsModelList;
                return productSpecificationsFragment;


            case 2:
                ProductDescriptionsFragment productDescriptionsFragment2 = new ProductDescriptionsFragment();
                productDescriptionsFragment2.body = productOtherDetails;
                return productDescriptionsFragment2;

                default:

                return null;
        }
    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
