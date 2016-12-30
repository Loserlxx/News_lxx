package com.example.admin.news_app.activity;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.admin.news_app.App;
import com.example.admin.news_app.R;
import com.example.admin.news_app.frament.ChatFrament;
import com.example.admin.news_app.frament.GifFrament;
import com.example.admin.news_app.frament.ImageFrament;
import com.example.admin.news_app.frament.NewsFrament;
import com.example.admin.news_app.utils.Constant;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.SyncError;
import com.wilddog.client.ValueEventListener;
import com.wilddog.wilddogauth.model.WilddogUser;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;
import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.toolbar_main_activity)
    Toolbar toolbar;
    @BindView(R.id.fl_main_activity)
    FrameLayout flMainActivity;
    @BindView(R.id.nv_main_activity)
    NavigationView nvMainActivity;
    @BindView(R.id.drawer_layout_activity_main)
    DrawerLayout drawerLayout;
    private CircleImageView cIv;
    private FragmentManager FragmentManager;
    private List<Fragment> fragments;
    private Fragment curfragments;
    private String phone;
    private String country;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        cIv = (CircleImageView) nvMainActivity.getHeaderView(0).findViewById(R.id.iv_na_main_activity);
        //初始化toolbar
        initToolBar();
        //获取fragmentManager管理者
        initFragment();
        //默认选择新闻
        //初始化导航试图
        initNavgetionview();
        //初始化野狗
        initWilldog();
    }

    /**
     * 初始化野狗
     */
    private void initWilldog() {
        WilddogUser user = App.user;

        cIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (App.user != null) {
                    shwoDialog();
                } else {
                    Intent intent = new Intent(MainActivity.this, EntryActivity.class);
                    //携带3过去让页面返回3
                    startActivityForResult(intent, Constant.GET_IMAGE_FROM_SERVICE);
                }
            }
        });
        //第一次进入app获取头像
        FirstApp();


    }

    /**
     * 第一次进入获取app头像
     */
    private void FirstApp() {
        //第一次进入默认获取头像
        if (App.user != null) {
            //野狗的value改变监听事件
            ValueEventListener postListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String imgStr = (String) dataSnapshot.getValue();
                    //把String 转化成bitmap 反编译
                    byte[] decode = Base64.decode(imgStr, Base64.DEFAULT);
                    Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
                    cIv.setImageBitmap(bitmap);
                }

                @Override
                public void onCancelled(SyncError syncError) {
                    //获取数据失败，打印错误信息
                }
            };
            //给当前数据库设置数据改变监听器
            App.ref.child(App.user.getUid()).addValueEventListener(postListener);
        }
    }

    /**
     * 初始化导航试图
     */
    private void initNavgetionview() {
        nvMainActivity.setCheckedItem(R.id.item_news_nav_main);
        curfragments = fragments.get(0);
        FragmentManager.beginTransaction().replace(R.id.fl_main_activity, curfragments).commit();

        //切换
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        toggle.syncState();
        nvMainActivity.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int index = 0;
                switch (item.getItemId()) {
                    case R.id.item_news_nav_main:
                        toolbar.setTitle("新闻");
                        index = 0;
                        break;
                    case R.id.item_image_nav_main:
                        toolbar.setTitle("图片");
                        index = 1;
                        break;
                    case R.id.item_collection_nav_main:
                        toolbar.setTitle("收藏");
                        index = 2;
                        break;
                    case R.id.item_chat_main_activity:
                        toolbar.setTitle("聊天");
                        index = 3;
                        break;

                }
                //初始化Fragment
                replaceFragment(index);


                return true;
            }
        });
    }

    /**
     * 初始化Fragment
     *
     * @param index
     */
    private void replaceFragment(int index) {
        //打开管理事务
        FragmentTransaction transaction = FragmentManager.beginTransaction();
        //切换fragment
        Fragment nextfragments = fragments.get(index);
        if (nextfragments != curfragments) {
            //isAdded是否已经添加 ，如果添加过了，就隐藏原先的，展示新的界面
            if (!nextfragments.isAdded()) {
                //判断是否有原先的界面，如果有就隐藏原来的界面，在添加一个新的界面
                if (curfragments != null) {
                    transaction.hide(curfragments);
                }
                transaction.add(R.id.fl_main_activity, nextfragments);
            } else {
                if (curfragments != null) {
                    transaction.hide(curfragments);
                }
                transaction.show(nextfragments);
            }
            curfragments = nextfragments;
        }
        transaction.commit();
        //关闭侧拉菜单
        drawerLayout.closeDrawers();
    }

    /**
     * 初始化管理者
     */
    private void initFragment() {
        FragmentManager = getSupportFragmentManager();
        fragments = new ArrayList<>();
        fragments.add(new NewsFrament());
        fragments.add(new ImageFrament());
        fragments.add(new GifFrament());
        fragments.add(new ChatFrament());
    }

    /**
     * 初始化toolBar
     */
    private void initToolBar() {
        toolbar.setTitle("新闻");
        toolbar.setLogo(R.mipmap.ic_launcher);
        toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);
    }

    /**
     * 成功执行照相，功能，选择
     */
    private void shwoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择切换图片？")
                //对话框的按钮
                .setItems(new String[]{"相机", "相册"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                ZhuCe();

                                //打开通信录好友列表页面*/

                                //FromCamera();
                                break;
                            case 1:
                                FromGallery();
                                break;
                        }
                    }
                }).show();
    }

    /**
     * 注册
     */
    private void ZhuCe() {
        //打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    country = (String) phoneMap.get("country");
                    phone = (String) phoneMap.get("phone");

                    // 提交用户信息（此方法可以不调用）
                    registerUser(country, phone);
                }
            }
        });
        registerPage.show(MainActivity.this);
    }

    private void registerUser(String country, String phone) {
        Toast.makeText(this, country+phone, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取相册
     */
    private void FromGallery() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");//image图片类型，*所以的后缀名：.jpg.png.gif...
        startActivityForResult(intent, Constant.GET_IMAGE_EROM_GALLERY);
    }

    /**
     * 照相获取返回值
     */
    private void FromCamera() {
        //相机的照相及返回值隐私意图
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //拿到数据
        startActivityForResult(intent, Constant.GET_IMAGE_FROM_GAMERA);

    }

    /**
     * 返回值，来设置头像
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.GET_IMAGE_FROM_SERVICE && resultCode == RESULT_OK) {
            String imgStr = data.getStringExtra("data");
            //把String 转化成bitmap 反编译
            byte[] decode = Base64.decode(imgStr, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decode, 0, decode.length);
            cIv.setImageBitmap(bitmap);
        } else if (requestCode == Constant.GET_IMAGE_FROM_GAMERA && resultCode == RESULT_OK) {
            //返回值获取照相出来的图片
            Bundle extras = data.getExtras();
            //调用出系统自动保存路径的位置
            Bitmap bitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //传入修改的格式，
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            //进行转化图片的位数
            String imgStr = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
            App.ref.child(App.user.getUid()).setValue(imgStr);
        } else if (requestCode == Constant.GET_IMAGE_EROM_GALLERY && resultCode == RESULT_OK) {
            Uri imgUrl = data.getData();
            ContentResolver contentResolver = getContentResolver();
            try {
                InputStream inputStream = contentResolver.openInputStream(imgUrl);
                Rect rect = new Rect(0, 0, 96, 96);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 20;
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream, rect, options);
                /*float byteCount = bitmap.getRowBytes()*bitmap.getHeight();
                if (byteCount/1024>3700){
                    Toast.makeText(this, "上传文件不能大于1Mb", Toast.LENGTH_SHORT).show();
                }else {*/
                   /* Cursor cursor = contentResolver.query(imgUrl,null,null,null,null);
                    cursor.moveToFirst();
                    String Path = cursor.getString(1);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    //采样值，缩放比例
                    options.inSampleSize=50;//真实的图片 imageView ：拿到真实图片的宽高和控件的宽高
                    Bitmap bitmap1 = BitmapFactory.decodeFile(Path, options);*/
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                String imgStr = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
                App.ref.child(App.user.getUid()).setValue(imgStr);
                cIv.setImageBitmap(bitmap);
                //}
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }


    }


}
