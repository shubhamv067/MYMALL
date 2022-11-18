package com.shubhamwithcode.mymall.Models;

public class productspecificationsModel {
    public static final int SPECIFICATIONS_TITLE = 0;
    public static final int SPECIFICATIONS_BODY = 1;

    private int Type;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    ///////specifications_Title
   private String Title;

    public productspecificationsModel(int type, String title) {
        Type = type;
        Title = title;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
    ///////specifications_Title

    ///////specifications_body
   private String FeatureName;
   private String FeatureValue;

    public productspecificationsModel(int type, String featureName, String featureValue) {
        Type = type;
        FeatureName = featureName;
        FeatureValue = featureValue;
    }

    public String getFeatureName() {
        return FeatureName;
    }

    public void setFeatureName(String featureName) {
        FeatureName = featureName;
    }

    public String getFeatureValue() {
        return FeatureValue;
    }

    public void setFeatureValue(String featureValue) {
        FeatureValue = featureValue;
    }

    ///////specifications_body



}
