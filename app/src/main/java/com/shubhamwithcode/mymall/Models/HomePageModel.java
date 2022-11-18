package com.shubhamwithcode.mymall.Models;

import java.util.List;

public class HomePageModel {

    public static final int BANNER_SLIDER =0;
    public static final int STRIP_AD_BANNER =1;
    public static final int HORIZONTAL_PRODUCT_VIEW =2;
    public static final int GRID_PRODUCT_VIEW =3;

    private int type ;
    private String backgroundColor;

    ////////banner slider

    List<sliderModel> sliderModelList;

    public HomePageModel(int type, List<sliderModel> sliderModelList) {
        this.type = type;
        this.sliderModelList = sliderModelList;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public List<sliderModel> getSliderModelList() {
        return sliderModelList;
    }
    public void setSliderModelList(List<sliderModel> sliderModelList) {
        this.sliderModelList = sliderModelList;
    }

    ////////banner slider

    //////Strip ad
    private String resource;


    public HomePageModel(int type, String resource, String backgroundColor) {
        this.type = type;
        this.resource = resource;
        this.backgroundColor = backgroundColor;
    }

    public String getResource() {
        return resource;
    }
    public void setResource(String resource) {
        this.resource = resource;
    }
    public String getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    //////Strip ad



    private String Title;
    private List<horizontalscrollproductModel> horizontalscrollproductModelList;

    //////horizontal product(scroll) layout
    private  List<WishlistModel> viewAllProductList;

    public HomePageModel(int type,String backgroundColor, String title, List<horizontalscrollproductModel> horizontalscrollproductModelList,List<WishlistModel> viewAllProductList) {
        this.type = type;
        this.backgroundColor = backgroundColor;
        Title = title;
        this.horizontalscrollproductModelList = horizontalscrollproductModelList;
        this.viewAllProductList = viewAllProductList;
    }

    public List<WishlistModel> getViewAllProductList() {
        return viewAllProductList;
    }

    public void setViewAllProductList(List<WishlistModel> viewAllProductList) {
        this.viewAllProductList = viewAllProductList;
    }
    //////horizontal product(scroll) layout

    //////Gride product(scroll) layout

    public HomePageModel(int type, String backgroundColor, String title, List<horizontalscrollproductModel> horizontalscrollproductModelList) {
        this.type = type;
        this.backgroundColor = backgroundColor;
        Title = title;
        this.horizontalscrollproductModelList = horizontalscrollproductModelList;
    }
    //////Gride product(scroll) layout

    public String getTitle() {
        return Title;
    }
    public void setTitle(String title) {
        Title = title;
    }
    public List<horizontalscrollproductModel> getHorizontalscrollproductModelList() {
        return horizontalscrollproductModelList;
    }
    public void setHorizontalscrollproductModelList(List<horizontalscrollproductModel> horizontalscrollproductModelList) {
        this.horizontalscrollproductModelList = horizontalscrollproductModelList;
    }

}
