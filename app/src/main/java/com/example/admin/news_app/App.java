package com.example.admin.news_app;

import android.app.Application;
import android.widget.Toast;

import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.model.WilddogUser;
import com.wilddog.wilddogcore.WilddogApp;
import com.wilddog.wilddogcore.WilddogOptions;
import com.yolanda.nohttp.NoHttp;

import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import cn.smssdk.SMSSDK;

/**
 * Created by admin on 2016-12-13.
 */

public class App extends Application {


    public static WilddogAuth auth;
    public static SyncReference ref;
    public static WilddogUser user;
    @Override
    public void onCreate() {
        super.onCreate();
        NoHttp.initialize(this);
        x.Ext.init(this);
        x.Ext.setDebug(true);
        WilddogOptions options = new WilddogOptions.Builder().setSyncUrl("https://2016121901.wilddogio.com").build();
        WilddogApp.initializeApp(this, options);
        auth = WilddogAuth.getInstance();
        ref = WilddogSync.getInstance().getReference();
        //
        WilddogUser currentUser = auth.getCurrentUser();
        if(currentUser != null){
            user=currentUser;
        }else{
            Toast.makeText(this, "用户未登录", Toast.LENGTH_SHORT).show();
        }
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        ShareSDK.initSDK(this);

        SMSSDK.initSDK(this, "1a4792cd47720", "ca6e1160d83aa4e9e6e5dfcd02be49bd");
    }
}
