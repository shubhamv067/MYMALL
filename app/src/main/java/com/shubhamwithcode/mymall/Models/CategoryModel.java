package com.shubhamwithcode.mymall.Models;

public class CategoryModel {
    String CategoryLink;
    String CategoryName;

    public CategoryModel(String categoryLink, String categoryName) {
        CategoryLink = categoryLink;
        CategoryName = categoryName;
    }

    public String getCategoryLink() {
        return CategoryLink;
    }

    public void setCategoryLink(String categoryLink) {
        CategoryLink = categoryLink;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }
}
