package com.example.admin.news_app.frament;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.admin.news_app.Adapter.MyDatasAdapter;
import com.example.admin.news_app.R;
import com.example.admin.news_app.activity.WebActivity;
import com.example.admin.news_app.entity.MyNewsOrmliteOpenHelper;
import com.example.admin.news_app.entity.NewsAPP;
import com.example.admin.news_app.utils.CaCheUtils;
import com.example.admin.news_app.utils.Constant;
import com.example.admin.news_app.utils.GlideImageLoader;
import com.example.admin.news_app.utils.News;
import com.example.admin.news_app.utils.NoHttpUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;
import com.youth.banner.Banner;
import com.youth.banner.Transformer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.onekeyshare.OnekeyShare;


/**
 * Created by admin on 2016-12-14.
 */

public class MyNewsFrament extends Fragment {


    private SwipeRefreshLayout sRL;
    private ListView listview;
    private List<NewsAPP.ResultBean.DataBean> datas;
    private String url;
    private MyDatasAdapter myDatasAdapter;

    public MyNewsFrament(String url) {
        this.url = url;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sRL = (SwipeRefreshLayout) inflater.inflate(R.layout.lv_my_news_fragment_activity_main, null);
        listview = (ListView) sRL.findViewById(R.id.lv_news_fragment_activity_main);
        //registerForContextMenu(listview);
        sRL.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SystemClock.sleep(2000);
                        myDatasAdapter.datas.remove(0);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                myDatasAdapter.notifyDataSetChanged();
                                sRL.setRefreshing(false);

                            }
                        });
                    }
                }).start();
            }
        });
        //设置banner
        Banner();

        return sRL;
    }

    /**
     * 设置banner
     */
    private void Banner() {
        Banner banner = new Banner(getContext());
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, 300);
        banner.setLayoutParams(params);
        //设置轮播时间默认2000
        banner.setDelayTime(4000);
        //设置动画
        banner.setBannerAnimation(Transformer.Accordion);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        List<String> images = new ArrayList<>();
        images.add("http://p1.bqimg.com/567571/f03710539523f9be.png");
        images.add("http://p1.bqimg.com/567571/965c0951911cc05a.png");
        images.add("http://p1.bqimg.com/567571/e2eb80a14dcee535.png");
        images.add("http://p1.bqimg.com/567571/d702e7b94aaf6d61.png");
        banner.setImages(images);
        listview.addHeaderView(banner);
        banner.start();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        Request<String> TopNewsStringRequest = NoHttp.createStringRequest(Constant.BASE_URL + url);

        NoHttpUtils.getInstance().add(Constant.WHAT_NEWS_REQUEST, TopNewsStringRequest, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                NewsAPP newsAPP = JSON.parseObject(result, NewsAPP.class);
                datas = newsAPP.getResult().getData();
                myDatasAdapter = new MyDatasAdapter(getContext(), datas);
                listview.setAdapter(myDatasAdapter);
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Toast.makeText(getContext(), "请链接网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish(int what) {

            }
        });
        //点击一下
        OnItemClickListener();
        //长按点击收藏
        onItemLongClickListener();

        super.onViewCreated(view, savedInstanceState);
    }

    /**
     * 第二次点击收藏
     */
    private void onItemLongClickListener() {
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int postion, long l) {
                NewsAPP.ResultBean.DataBean dataBean = myDatasAdapter.datas.get(postion - 1);
                //二次添加收藏
                long newId = SenoudAdd(postion);
                final News news = new News(newId, dataBean.getThumbnail_pic_s(), dataBean.getTitle(), dataBean.getDate(), dataBean.getUrl());
                //创建弹出框
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                //对话框的按钮
                builder.setItems(new String[]{"分享", "收藏"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                OnekeyShare oks = new OnekeyShare();
                                //关闭sso授权
                                oks.disableSSOWhenAuthorize();
                                // title标题，印象笔记、邮箱、信息、微信、人人网、QQ和QQ空间使用
                                oks.setTitle(datas.get(postion-1).getTitle());
                                // titleUrl是标题的网络链接，仅在Linked-in,QQ和QQ空间使用
                                oks.setTitleUrl(datas.get(postion-1).getUrl());
                                // text是分享文本，所有平台都需要这个字段
                                oks.setText("鑫鑫分享");
                                //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
                                oks.setImageUrl(datas.get(postion-1).getThumbnail_pic_s());
                                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
                                //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                                // url仅在微信（包括好友和朋友圈）中使用
                                oks.setUrl(datas.get(postion-1).getUrl());
                                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                                oks.setComment("我是测试评论文本");
                                // site是分享此内容的网站名称，仅在QQ空间使用
                                oks.setSite("ShareSDK");
                                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                                oks.setSiteUrl(datas.get(postion-1).getUrl());

                                // 启动分享GUI
                                oks.show(getContext());
                                break;
                            case 1:
                                try {
                                    List<News> newses = MyNewsOrmliteOpenHelper.getDao(getContext()).queryForAll();

                                    MyNewsOrmliteOpenHelper.getDao(getContext()).createIfNotExists(news);
                                    Toast.makeText(getContext(), "收藏成功", Toast.LENGTH_SHORT).show();


                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                    }
                }).show();


                return true;
            }
        });
    }

    /**
     * 点击跳转
     */
    private void OnItemClickListener() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getActivity(), WebActivity.class);
                String url = datas.get(i - 1).getUrl();
                //点击之后变色
                BianSe(url);
                intent.putExtra("url", url);
                String img_url = datas.get(i - 1).getThumbnail_pic_s();
                intent.putExtra("img_url", img_url);
                startActivity(intent);
            }
        });
    }

    /**
     * 二次添加收藏
     *
     * @param i
     * @return
     */
    private long SenoudAdd(int i) {
        String url = datas.get(i).getUrl();
        int indexStart = url.indexOf("mobile/");
        int indexEnd = url.indexOf(".html");
        String substring = url.substring(indexStart + 7, indexEnd);
        return Long.parseLong(substring);
    }

    /*@Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.news_fragment_activity_collect,menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.collcet_menu_item:
                Toast.makeText(getContext(), "俺收藏了", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onContextItemSelected(item);
    }*/

    /**
     * 点击之后变颜色
     *
     * @param url
     */
    private void BianSe(String url) {
        //点击变灰
        String readed = CaCheUtils.getStringData(getContext(), "readed");
        if (!readed.contains(url)) {
            readed = readed + url + ",";

        } else {

            Toast.makeText(getContext(), "1", Toast.LENGTH_SHORT).show();
        }
        CaCheUtils.putStringData(getContext(), "readed", readed);
        myDatasAdapter.notifyDataSetChanged();
    }


}
