package com.example.admin.news_app.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.example.admin.news_app.R;
import com.example.admin.news_app.utils.Constant;
import com.example.admin.news_app.utils.NoHttpUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity {

    @BindView(R.id.wv_activity_web)
    WebView wvActivityWeb;
    @BindView(R.id.tl_activity_web)
    Toolbar tlActivityWeb;
    @BindView(R.id.pb_activity_web)
    ProgressBar pbActivityWeb;
    @BindView(R.id.iv_activity_web)
    ImageView ivActivityWeb;
    @BindView(R.id.collapsing_tool_bar_activity_web)
    CollapsingToolbarLayout collapsingToolBarActivityWeb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        setSupportActionBar(tlActivityWeb);
        //初始化ToolBar
        initToolBar();
        //初始化可折叠toolbar
        String url = initCollApseToolbar();
        //初始化webiew
        initWebview(url);


    }

    /**
     * 初始化WebView
     * @param url
     */
    private void initWebview(String url) {
        //config setting设置类
        WebSettings settings = wvActivityWeb.getSettings();
        //settings.setSupportZoom(true);\
        //给权限
        settings.setJavaScriptEnabled(true);//支持js脚本
        settings.setDisplayZoomControls(true);
        wvActivityWeb.loadUrl(url);
        //设置图片的名字title
        wvActivityWeb.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                collapsingToolBarActivityWeb.setTitle(title);
            }
        });
        //设置加载之后
        wvActivityWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbActivityWeb.setVisibility(View.INVISIBLE);
            }
        });
    }

    /**
     * 初始化可折叠toolbar
     * @return
     */
    private String initCollApseToolbar() {
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String img_url=intent.getStringExtra("img_url");
        Glide.with(this).load(img_url).into(ivActivityWeb);

        //获取图片亮色设置为toolbar背景颜色
        Request<Bitmap> imageRequest = NoHttp.createImageRequest(img_url);
        NoHttpUtils.getInstance().add(Constant.WHAT_NEWS_REQUEST, imageRequest, new OnResponseListener<Bitmap>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<Bitmap> response) {
                int vibrantColor = Palette.from(response.get()).generate().getVibrantColor(getResources().getColor(R.color.colorPrimary));
                collapsingToolBarActivityWeb.setContentScrimColor(vibrantColor);
            }

            @Override
            public void onFailed(int what, Response<Bitmap> response) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
        return url;
    }

    /**
     * 初始化ToolBar
     */
    private void initToolBar() {
        ActionBar supportActionBar = getSupportActionBar();

        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
            supportActionBar.setDisplayShowHomeEnabled(true);
            supportActionBar.show();
        }
    }

   /* //点击返回
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }*/

    //调用系统返回指令
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
