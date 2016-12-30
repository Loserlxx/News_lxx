package com.example.admin.news_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.news_app.R;
import com.example.admin.news_app.utils.News;

import java.util.List;

/**
 * Created by admin on 2016-12-22.
 */

public class MyCollectAdapter extends BaseAdapter {
    public List<News> news;
    private Context context;

    public MyCollectAdapter(List<News> news, Context context) {
        this.news = news;
        this.context = context;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    @Override
    public Object getItem(int i) {
        return news.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View ContextView, ViewGroup viewGroup) {
        View view=null;
        MyViewHolder holder=null;
        if (ContextView==null){
            holder = new MyViewHolder();
            view = View.inflate(context, R.layout.listview_my_datas_fragment, null);
            holder.mIv_icon = (ImageView) view.findViewById(R.id.iv_icon_my_datas_fragment_activity_main);
            holder.mTv_date = (TextView) view.findViewById(R.id.tv_date_my_datas_fragment_activity_main);
            holder.mTv_title = (TextView) view.findViewById(R.id.tv_title_my_datas_fragment_activity_main);
            view.setTag(holder);
        }else {
            view = ContextView;
            holder = (MyViewHolder) view.getTag();
        }

        Glide.with(context).load(news.get(i).getImg()).into(holder.mIv_icon);
        holder.mTv_title.setText(news.get(i).getTitle());
        holder.mTv_date.setText(news.get(i).getData());

        return view;
    }
    class MyViewHolder{
        ImageView mIv_icon;
        TextView mTv_title;
        TextView mTv_date;
    }
}
