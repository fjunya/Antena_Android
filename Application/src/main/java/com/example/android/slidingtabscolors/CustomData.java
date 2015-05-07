package com.example.android.slidingtabscolors;

import android.graphics.Bitmap;

/**
 * Created by fjunya on 2015/04/18.
 */
public class CustomData {
    private Bitmap imageData_;
    private String textData_;
    private String url_;
    private String site_;

    public void setImagaData(Bitmap image) {
        imageData_ = image;
    }

    public Bitmap getImageData() {
        return imageData_;
    }

    public void setTextData(String text) {
        textData_ = text;
    }

    public String getTextData() {
        return textData_;
    }

    public void setUrl(String url){
        url_ = url;
    }

    public String getUrl(){
        return url_;
    }

    public void setSite(String site){
        site_ = site;
    }

    public String getSite(){
        return site_;
    }
}
