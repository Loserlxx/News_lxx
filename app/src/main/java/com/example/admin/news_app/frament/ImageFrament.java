package com.example.admin.news_app.frament;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.admin.news_app.Adapter.MyRecycleViewAdapter;
import com.example.admin.news_app.R;
import com.example.admin.news_app.activity.WebActivity;
import com.example.admin.news_app.entity.NewsAPP;
import com.example.admin.news_app.utils.Constant;
import com.example.admin.news_app.utils.NoHttpUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.yolanda.nohttp.rest.StringRequest;

/**
 * Created by admin on 2016-12-14.
 */

public class ImageFrament extends Fragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.recycleview_image_frament, container, false);

        final RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recycle_view_image_frament);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));

        Request<String> stringRequest = NoHttp.createStringRequest(Constant.BASE_URL + Constant.YULE_NEWS_QUERY_STRING);

        NoHttpUtils.getInstance().add(0, stringRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {

                String jsonStr = response.get();

                NewsAPP newsAPP = JSON.parseObject(jsonStr, NewsAPP.class);

                final MyRecycleViewAdapter adapter = new MyRecycleViewAdapter(newsAPP.getResult().getData(), getContext());
                //自定义点击事件跳转页面到Web页面
                adapter.setOnRecycleviewItemClickListener(new MyRecycleViewAdapter.OnRecyclerviewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        String url = adapter.getDatas().get(position).getUrl();
                        String img_url = adapter.getDatas().get(position).getThumbnail_pic_s();
                        Intent intent = new Intent(getContext(), WebActivity.class);
                        intent.putExtra("url",url);
                        intent.putExtra("img_url",img_url);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailed(int what, Response<String> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });


        return inflate;
    }
}
