package com.example.android.get_api;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.example.android.slidingtabscolors.AddAdapterThread;
import com.example.android.slidingtabscolors.CustomAdapter;
import com.example.android.slidingtabscolors.CustomData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fjunya on 2015/04/30.
 */
public class GetNewsDataThread extends Thread {

    int start_loc;
    String search = null;
    static String URL_STRING = "http://158.199.193.86/antena/putNews";
    Context context;
    CustomAdapter customAdapter;
    final Handler handler = new Handler();
    boolean INSERT_TOP;
    SwipeRefreshLayout swipeRefreshLayout;

    public GetNewsDataThread(Context context, CustomAdapter customAdapter,
                             int start_loc, String search){
        this.context = context;
        this.customAdapter = customAdapter;
        this.start_loc = start_loc;
        this.search = search;
        this.INSERT_TOP = false;
    }

    public GetNewsDataThread(Context context, CustomAdapter customAdapter,
                             int start_loc){
        this.customAdapter = customAdapter;
        this.context = context;
        this.start_loc = start_loc;
        this.INSERT_TOP = false;
    }

    public GetNewsDataThread(Context context, CustomAdapter customAdapter,
                             String search){
        this.context = context;
        this.customAdapter = customAdapter;
        this.search = search;
        this.INSERT_TOP = false;
    }

    public GetNewsDataThread(Context context, CustomAdapter customAdapter,
                             String search, boolean INSERT_TOP,
                             SwipeRefreshLayout swipeRefreshLayout){
        this.context = context;
        this.customAdapter = customAdapter;
        this.search = search;
        this.INSERT_TOP = INSERT_TOP;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public GetNewsDataThread(Context context, CustomAdapter customAdapter,
                             boolean INSERT_TOP,
                             SwipeRefreshLayout swipeRefreshLayout){
        this.context = context;
        this.customAdapter = customAdapter;
        this.INSERT_TOP = INSERT_TOP;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    public void run(){
        ArrayList<HashMap<String, String>> arrayList = getNewsData(start_loc,30,search);
        List<CustomData> list = new ArrayList<CustomData>();
        if(this.INSERT_TOP){
            for(HashMap<String, String> map: arrayList){
                AddAdapterThread addAdapterThread = new AddAdapterThread(map, customAdapter,
                                                                    handler, this.INSERT_TOP,
                                                                    swipeRefreshLayout);
                addAdapterThread.start();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }else {
            for(HashMap<String, String> map: arrayList){
                AddAdapterThread addAdapterThread = new AddAdapterThread(map, customAdapter,
                        handler);
                addAdapterThread.start();
            }
        }
    }

    private static ArrayList<HashMap<String, String>> getNewsData(int start,
                                                                  int limit,
                                                                  String search){
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

        String get_url = "";
        if(search == null){
            get_url = "?start=" + String.valueOf(start) +
                    "&limit=" + String.valueOf(limit);
        }else{
            try {
                get_url = "?start=" + String.valueOf(start) +
                        "&limit=" + String.valueOf(limit) +
                        "&search=" + URLEncoder.encode(search, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        try {
            URL url = new URL(URL_STRING + get_url);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream inputStream = connection.getInputStream();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readValue(inputStream, JsonNode.class);
                int i = 0;
                while (rootNode.has(i)) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("title", rootNode.get(i).get("title").asText());
                    map.put("thumb_url", rootNode.get(i).get("thumb_url").asText());
                    map.put("url", rootNode.get(i).get("url").asText());
                    map.put("site", rootNode.get(i).get("site").asText());
                    arrayList.add(map);
                    i++;
                }
            }
            return arrayList;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
