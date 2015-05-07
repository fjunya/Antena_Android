package com.example.android.slidingtabscolors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by fjunya on 2015/04/18.
 */
public class CustomAdapter extends ArrayAdapter<CustomData> {

    private LayoutInflater layoutInflater_;

    public CustomAdapter(Context context, int textViewResourceId, List<CustomData> objects) {
        super(context, textViewResourceId, objects);
        layoutInflater_ = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 特定の行(position)のデータを得る
        CustomData item = (CustomData)getItem(position);

        // convertViewは使い回しされている可能性があるのでnullの時だけ新しく作る
        if (null == convertView) {
            convertView = layoutInflater_.inflate(R.layout.customdata_list, null);
        }

        // CustomDataのデータをViewの各Widgetにセットする
        ImageView imageView;
        imageView = (ImageView)convertView.findViewById(R.id.image);
        if(item.getImageData() != null){
            imageView.setImageBitmap(item.getImageData());
        }else {
            if(item.getSite().equals("SPA")){
                imageView.setImageResource(R.drawable.spa_logo);
            }else if (item.getSite().equals("R25")){
                imageView.setImageResource(R.drawable.r25_logo);
            }else if (item.getSite().equals("文春")){
                imageView.setImageResource(R.drawable.bunsyun_logo);
            }
            else {
                imageView.setImageResource(R.drawable.mikumiku);
            }
        }

        TextView textView;
        textView = (TextView)convertView.findViewById(R.id.text);
        textView.setText(item.getTextData());

        TextView textView1;
        textView1 = (TextView)convertView.findViewById(R.id.text1);
        textView1.setText(item.getSite());

        return convertView;
    }


}
