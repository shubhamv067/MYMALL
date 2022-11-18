package com.shubhamwithcode.mymall;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shubhamwithcode.mymall.Adapters.WishlistAdapter;
import com.shubhamwithcode.mymall.Adapters.gridproductlayoutAdapter;
import com.shubhamwithcode.mymall.Models.WishlistModel;
import com.shubhamwithcode.mymall.Models.horizontalscrollproductModel;

import java.util.List;

public class view_All_Activity extends AppCompatActivity {

    RecyclerView recyclerView;
    GridView gridView;
    public static List<WishlistModel> wishlistModelList;
    public static  List<horizontalscrollproductModel> horizontalscrollproductModelList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getIntent().getStringExtra("title"));

        recyclerView = findViewById(R.id.viewAll_RecyclerView);
        gridView = findViewById(R.id.viewAll_GridView);

        int layout_code = getIntent().getIntExtra("layout_code",-1);

        if (layout_code ==0){




        recyclerView.setVisibility(View.VISIBLE);
        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);




        WishlistAdapter wishlistAdapter = new WishlistAdapter(wishlistModelList,false);
        wishlistAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(wishlistAdapter);
        }

        else if (layout_code ==1) {
            gridView.setVisibility(View.VISIBLE);
            gridproductlayoutAdapter gridadapter = new gridproductlayoutAdapter(horizontalscrollproductModelList);
            gridView.setAdapter(gridadapter);

        }



    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}