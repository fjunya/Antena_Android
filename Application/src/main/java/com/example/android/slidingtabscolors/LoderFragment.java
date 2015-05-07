package com.example.android.slidingtabscolors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.android.get_api.GetNewsDataThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by fjunya on 2015/04/19.
 */
//public class LoderFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<HashMap<String, String>>> {

public class LoderFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String KEY_TITLE = "title";
    private static final String KEY_INDICATOR_COLOR = "indicator_color";
    private static final String KEY_DIVIDER_COLOR = "divider_color";
    List<CustomData> list = new ArrayList<CustomData>();
    final Handler handler = new Handler();
    CustomAdapter customAdapater = null;
    ArrayList<HashMap<String, String>> arrayList = null;
    //ListView最下部にクルクルを表示させる用
    View list_footer = null;
    //ページ最下部で重複して取得しない用
    int requested_start_loc = 0;
    //画面を下げて更新に使用
    private SwipeRefreshLayout mSwipeRefreshLayout;


    public static LoderFragment newInstance(CharSequence title, int indicatorColor,
                                                  int dividerColor) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(KEY_TITLE, title);
        bundle.putInt(KEY_INDICATOR_COLOR, indicatorColor);
        bundle.putInt(KEY_DIVIDER_COLOR, dividerColor);

        LoderFragment fragment = new LoderFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // loaderの初期化
//        getLoaderManager().initLoader(0, null, this);
        customAdapater = new CustomAdapter(this.getActivity(), 0, list);

        View view = inflater.inflate(R.layout.pager_item2, container, false);

        // 画面を下げて更新用のSwipeRefreshLayoutを設定
        mSwipeRefreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        return view;
//        return inflater.inflate(R.layout.pager_item2, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView)view.findViewById(R.id.listView);
        listView.setAdapter(customAdapater);
        //ListView最下部にクルクル（更新）の追加
        if(list_footer == null){
            LayoutInflater layoutInflater_ = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View list_footer = layoutInflater_.inflate(R.layout.listview_footer, null);
            listView.addFooterView(list_footer);
        }

        if(customAdapater.getCount() == 0){
            GetNewsDataThread getNewsDataThread = new GetNewsDataThread(getActivity(), customAdapater, 0);
            getNewsDataThread.start();
        }

        //ニュースをクリックしたらニュースのWebページをWebViewで表示
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(customAdapater != null){
//                    HashMap<String, String> hashMap = arrayList.get(position);
                    CustomData customData = customAdapater.getItem(position);
                    Intent intent = new Intent(getActivity(),WebViewActivity.class);
                    intent.putExtra("url",customData.getUrl());
                    startActivity(intent);
                }


            }
        });

        //最下部までスクロールしたらデータを取得する
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int total_count = view.getCount();
                if((total_count <= view.getFirstVisiblePosition() + view.getChildCount()) &&
                        (total_count != requested_start_loc)){
                    GetNewsDataThread getNewsDataThread = new GetNewsDataThread(getActivity(), customAdapater,
                                                                                total_count -1);
                    getNewsDataThread.start();
                    requested_start_loc = total_count;
                }

                Log.d("SCROLLSTATE", String.valueOf(scrollState));
                Log.d("getFirstVisiblePosition",String.valueOf(view.getFirstVisiblePosition()));
                Log.d("getChildCount", String.valueOf(view.getChildCount()));
                Log.d("getBottom", String.valueOf(view.getCount()));
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    @Override
    public void onRefresh() {
        GetNewsDataThread getNewsDataThread = new GetNewsDataThread(getActivity(), customAdapater,
                                                                    true, mSwipeRefreshLayout);
        getNewsDataThread.start();
    }

}
