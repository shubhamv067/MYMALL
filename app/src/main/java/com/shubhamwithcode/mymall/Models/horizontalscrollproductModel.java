package com.shubhamwithcode.mymall.Models;

public class horizontalscrollproductModel {
    String productID;
    String produceImage;
    String productTitle;
    String productDescription;
    String productPrice;

    public horizontalscrollproductModel(String productID, String produceImage, String productTitle, String productDescription, String productPrice) {
        this.productID = productID;
        this.produceImage = produceImage;
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProduceImage() {
        return produceImage;
    }

    public void setProduceImage(String produceImage) {
        this.produceImage = produceImage;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
