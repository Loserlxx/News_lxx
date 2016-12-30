package com.example.admin.news_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.admin.news_app.R;
import com.example.admin.news_app.utils.MyChat;

import java.util.List;

/**
 * Created by admin on 2016-12-23.
 */

public class MyChatAdapter extends BaseAdapter {

    private List<MyChat> chats;
    private Context context;

    public MyChatAdapter(List<MyChat> chats, Context context) {
        this.chats = chats;
        this.context = context;
    }

    @Override
    public int getCount() {
        return chats.size();
    }

    @Override
    public Object getItem(int i) {
        return chats.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        MyViewHouder viewHouder = null;
        switch (getItemViewType(i)) {
            case 0:
                if (view == null) {
                    viewHouder=new MyViewHouder();
                    view = LayoutInflater.from(context).inflate(R.layout.chat_fragment_from, viewGroup, false);
                    viewHouder.tv= (TextView) view.findViewById(R.id.tv_from_chat_fragment);
                    view.setTag(viewHouder);
                }else {
                    viewHouder= (MyViewHouder) view.getTag();
                }
                viewHouder.tv.setText(chats.get(i).getChat());

                break;
            case 1:
                if (view == null) {
                    viewHouder=new MyViewHouder();
                    view = LayoutInflater.from(context).inflate(R.layout.chat_fragment_to, viewGroup, false);
                    viewHouder.tv= (TextView) view.findViewById(R.id.tv_to_chat_fragment);
                    //viewHouder.iv= (ImageView) view.findViewById(R.id.iv_to_chat_fragment);
                    view.setTag(viewHouder);
                }else {
                    viewHouder= (MyViewHouder) view.getTag();
                }
                //Glide.with(context).load(chats.get(i).getImg()).into(viewHouder.iv);
                viewHouder.tv.setText(chats.get(i).getChat());

                break;
        }
        return view;
    }

    class MyViewHouder {
       // ImageView iv;
        TextView tv;
    }

    /**
     * 判断是发送
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        int type = 0;
        switch (chats.get(position).getType()) {
            case 0:
                return type = 0;
            case 1:
                return type = 1;
        }

        return type;
    }

    /**
     * 有几种方法
     *
     * @return
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }
}
