package com.example.admin.news_app.frament;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.news_app.Adapter.MyCollectAdapter;
import com.example.admin.news_app.Adapter.MyDatasAdapter;
import com.example.admin.news_app.R;
import com.example.admin.news_app.activity.WebActivity;
import com.example.admin.news_app.entity.MyNewsOrmliteOpenHelper;
import com.example.admin.news_app.utils.News;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by admin on 2016-12-14.
 */

public class GifFrament extends Fragment {

    private SwipeRefreshLayout sRL;
    private ListView listview;
    private MyCollectAdapter myCollectAdapter;
    private List<News> newses;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        sRL = (SwipeRefreshLayout) inflater.inflate(R.layout.lv_my_news_fragment_activity_main, null);
        listview = (ListView) sRL.findViewById(R.id.lv_news_fragment_activity_main);
        sRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myCollectAdapter.notifyDataSetChanged();
                                sRL.setRefreshing(false);

                            }
                        });
                    }
                }).start();
            }
        });
        return sRL;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        try {
            newses = MyNewsOrmliteOpenHelper.getDao(getContext()).queryForAll();

            myCollectAdapter = new MyCollectAdapter(newses, getActivity());
            listview.setAdapter(myCollectAdapter);
            //点击
            onItemClickListener();
            //长按
            onItemLongClickListener();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 点击listView
     */
    private void onItemClickListener() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                String url = newses.get(i).getUrl();
                intent.putExtra("url", url);
                String img_url = newses.get(i).getImg();
                intent.putExtra("img_url", img_url);
                startActivity(intent);
            }
        });
    }

    /**
     * 长按
     */
    private void onItemLongClickListener() {
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int postion, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("是否删除？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    MyNewsOrmliteOpenHelper.getDao(getContext()).delete(newses.get(postion));
                                    myCollectAdapter.news.remove(postion);
                                    myCollectAdapter.notifyDataSetChanged();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getContext(), "取消了删除", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();



                return true;
            }
        });
    }

    /**
     * 刷新收藏
     * @param hidden 判断是否隐藏
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden){
            try {
                newses = MyNewsOrmliteOpenHelper.getDao(getContext()).queryForAll();
                myCollectAdapter.news=newses;
                myCollectAdapter.notifyDataSetChanged();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
