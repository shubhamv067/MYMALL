package com.shubhamwithcode.mymall;

import static com.shubhamwithcode.mymall.DBqueries.lists;
import static com.shubhamwithcode.mymall.DBqueries.loadFragmentData;
import static com.shubhamwithcode.mymall.DBqueries.loadedCategoriesNames;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shubhamwithcode.mymall.Adapters.HomePageAdapter;
import com.shubhamwithcode.mymall.Models.HomePageModel;
import com.shubhamwithcode.mymall.Models.WishlistModel;
import com.shubhamwithcode.mymall.Models.horizontalscrollproductModel;
import com.shubhamwithcode.mymall.Models.sliderModel;

import java.util.ArrayList;
import java.util.List;


public class category extends AppCompatActivity {

    Toolbar mytoolbar;
    RecyclerView categoryRecyclerView;
    HomePageAdapter homePageAdapter;
    List<HomePageModel> homePageModelFakeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        mytoolbar =(Toolbar) findViewById(R.id.mytoolbar);
        setSupportActionBar(mytoolbar);

        String title = getIntent().getStringExtra("categoryName");
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //////home page fake list
        List<sliderModel> sliderModelFakeList = new ArrayList<>();

        sliderModelFakeList.add(new sliderModel("null","#ffffff"));
        sliderModelFakeList.add(new sliderModel("null","#ffffff"));
        sliderModelFakeList.add(new sliderModel("null","#ffffff"));
        sliderModelFakeList.add(new sliderModel("null","#ffffff"));
        sliderModelFakeList.add(new sliderModel("null","#ffffff"));
        sliderModelFakeList.add(new sliderModel("null","#ffffff"));

        List<horizontalscrollproductModel> horizontalscrollproductModelFakeList = new ArrayList<>();

        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));
        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));
        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));
        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));
        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));
        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));
        horizontalscrollproductModelFakeList.add(new horizontalscrollproductModel("","","","",""));

        homePageModelFakeList.add(new HomePageModel(0,sliderModelFakeList));
        homePageModelFakeList.add(new HomePageModel(1,"","#ffffff"));
        homePageModelFakeList.add(new HomePageModel(2,"#ffffff","",horizontalscrollproductModelFakeList,new ArrayList<WishlistModel>()));
        homePageModelFakeList.add(new HomePageModel(3,"#ffffff","",horizontalscrollproductModelFakeList));

        //////home page fake list

        categoryRecyclerView = findViewById(R.id.categoryrecyclerView);



        ////////////////////////////////////////////////////////////

        LinearLayoutManager TestingLayoutManager = new LinearLayoutManager(category.this);
        TestingLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        categoryRecyclerView.setLayoutManager(TestingLayoutManager);
        homePageAdapter = new HomePageAdapter(homePageModelFakeList);


        int listPosition = 0;
        for (int x = 0;x < loadedCategoriesNames.size();x++){
            if (loadedCategoriesNames.get(x).equals(title.toUpperCase())){
                listPosition = x;
            }
        }
        if (listPosition == 0){
            loadedCategoriesNames.add(title.toUpperCase());
            lists.add(new ArrayList<HomePageModel>());
            loadFragmentData(categoryRecyclerView,this,loadedCategoriesNames.size() - 1,title);
        }else {
            homePageAdapter = new HomePageAdapter(lists.get(listPosition));
        }
        categoryRecyclerView.setAdapter(homePageAdapter);
        homePageAdapter.notifyDataSetChanged();
        ////////////////////////////////////////////////////////////
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shearch_icon, menu);

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.main_search){
            Intent searchIntent = new Intent(this,SearchActivity.class);
            startActivity(searchIntent);
            return true;
        }else if (id == android.R.id.home){
            finish();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}