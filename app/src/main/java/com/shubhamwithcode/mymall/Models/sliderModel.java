package com.shubhamwithcode.mymall.Models;

public class sliderModel {
    private String banner;
    private String background;

    public sliderModel(String banner, String background) {
        this.banner = banner;
        this.background = background;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }
}
