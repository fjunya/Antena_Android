package com.example.android.slidingtabscolors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by fjunya on 2015/04/20.
 */
public class AddAdapterThread extends Thread {
    /**
     * 受け取ったニュースデータをAdapterに追加し、画面描写する
     */

    HashMap<String, String> hashMap;
    CustomAdapter adapter;
    Handler handler;
    boolean INSERT_TOP;
    SwipeRefreshLayout swipeRefreshLayout;

    public AddAdapterThread(HashMap<String, String> hashMap, CustomAdapter adapter,
                            Handler handler){
        this.hashMap = hashMap;
        this.adapter = adapter;
        this.handler = handler;
        this.INSERT_TOP = false;
    }

    public AddAdapterThread(HashMap<String, String> hashMap, CustomAdapter adapter,
                            Handler handler, boolean INSERT_TOP,
                            SwipeRefreshLayout swipeRefreshLayout){
        this.hashMap = hashMap;
        this.adapter = adapter;
        this.handler = handler;
        this.INSERT_TOP = INSERT_TOP;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void run(){
        CustomData customData = new CustomData();
        customData.setTextData(this.hashMap.get("title"));
        customData.setImagaData(getBitMap(this.hashMap.get("thumb_url")));
        customData.setUrl(this.hashMap.get("url"));
        customData.setSite(this.hashMap.get("site"));
        final CustomData data = customData;
        if(this.INSERT_TOP){
            boolean check_exist = true;
            for(int i=0; i < 30; i++ ){
                String exist_check_url = this.adapter.getItem(i).getUrl();
                if(exist_check_url.equals(customData.getUrl())){
                    check_exist = false;
                }
            }
            if(check_exist){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        adapter.insert(data,0);
                    }
                });
            }
        }else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    adapter.add(data);
                }
            });
        }
    }

    public static Bitmap getBitMap(String urlString){
        Bitmap bitmap;
        try {
            URL url = new URL(urlString);
            InputStream inputStream = url.openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
