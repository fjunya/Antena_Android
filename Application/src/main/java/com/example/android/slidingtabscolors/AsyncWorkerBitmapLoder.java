package com.example.android.slidingtabscolors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.AsyncTaskLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fjunya on 2015/04/19.
 */
public class AsyncWorkerBitmapLoder extends AsyncTaskLoader<Bitmap> {
    private Bitmap bitmap;
    private String urlString;

    public AsyncWorkerBitmapLoder(Context context, String url){
        super(context);
        this.urlString = url;
    }

    @Override
    public Bitmap loadInBackground(){
        try {
            URL url = new URL(urlString);
            InputStream inputStream = url.openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
