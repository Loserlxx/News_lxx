package com.example.admin.news_app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.admin.news_app.App;
import com.example.admin.news_app.R;
import com.example.admin.news_app.utils.Constant;
import com.wilddog.wilddogauth.core.Task;
import com.wilddog.wilddogauth.core.listener.OnCompleteListener;
import com.wilddog.wilddogauth.core.request.UserProfileChangeRequest;
import com.wilddog.wilddogauth.core.result.AuthResult;
import com.wilddog.wilddogauth.model.WilddogUser;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryActivity extends AppCompatActivity {

    @BindView(R.id.tiet_email_entry_activity)
    TextInputEditText tietEmailEntryActivity;
    @BindView(R.id.til_email_entry_activity)
    TextInputLayout tilEmailEntryActivity;
    @BindView(R.id.tiet_password_entry_activity)
    TextInputEditText tietPasswordEntryActivity;
    @BindView(R.id.til_password_entry_activity)
    TextInputLayout tilPasswordEntryActivity;
    private WilddogUser curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);
        //初始化编辑文本监听器
        initEditTextListener();
    }

    /**
     * 初始化编辑文本监听器
     */
    private void initEditTextListener() {
        tietEmailEntryActivity.addTextChangedListener(new TextWatcher() {
            //改变之前
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            //改变中
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            //改变之后
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() < 6) {
                    tilEmailEntryActivity.setError("邮箱不能少于6个字符！");
                } else {
                    tilEmailEntryActivity.setErrorEnabled(false);
                }
            }
        });
        tietPasswordEntryActivity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() < 6) {
                    tilPasswordEntryActivity.setError("密码不能少于6个字符！");
                } else {
                    tilPasswordEntryActivity.setErrorEnabled(false);
                }
            }
        });
    }

    /**
     * 点击登录
     * @param view
     */
    public void entry(View view) {
        String email = tietEmailEntryActivity.getText().toString().trim();
        String passWord = tietPasswordEntryActivity.getText().toString().trim();
        //传入文本，判断是否成功
        App.auth.signInWithEmailAndPassword(email, passWord)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(EntryActivity.class.getName(), "EntryActivity", task.getException());
                            Toast.makeText(EntryActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();

                        } else {
                            shwoDialog();
                        }
                    }
                });
    }
    /**
     * 点击注册
     * @param view
     */
    public void register(View view) {

        String email = tietEmailEntryActivity.getText().toString().trim();
        String passWord = tietPasswordEntryActivity.getText().toString().trim();
        if(email==null||passWord==null){
            Toast.makeText(this, "文本为空", Toast.LENGTH_SHORT).show();
        }else{
            App.auth.createUserWithEmailAndPassword(email, passWord)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //获取用户并且上传到到网络数据库。
                                curUser = task.getResult().getWilddogUser();
                                //上传数据库
                                App.user= curUser;
                                Toast.makeText(EntryActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                            } else {
                                //错误处理
                                Toast.makeText(EntryActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        }

    }
    /**
     * 成功执行照相，功能，选择
     */
    private void shwoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("choose your head portrait?")
                //对话框的按钮
                .setItems(new String[]{"camera", "gallery"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                FromCamera();
                                break;
                            case 1:
                                FromGallery();
                                break;
                        }
                    }
                }).show();
    }

    /**
     * 手机相册获取
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
     * 照相上传图片功能
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.GET_IMAGE_FROM_GAMERA && resultCode == RESULT_OK) {
            //返回值获取照相出来的图片
            Bundle extras = data.getExtras();
            //调用出系统自动保存路径的位置
            Bitmap bitmap = (Bitmap) extras.get("data");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            //传入修改的格式，
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            //进行转化图片的位数
            final String imgStr = Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
            if(App.user!=null){
                //不能直接上传BitMap因为无法上传128位的图片，只能64位
                App.ref.child(App.user.getUid()).setValue(imgStr);
                WilddogUser user =App.user;
                UserProfileChangeRequest profileUpdates = new  UserProfileChangeRequest.Builder()
                        .setPhotoUri(Uri.parse(curUser.getUid()))
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // 更新成功
                                    Intent intent = new Intent();
                                    intent.putExtra("data",imgStr);
                                    setResult(RESULT_OK,intent);
                                    finish();
                                }else{
                                    // 发生错误
                                }
                            }
                        });
            }else{
                Toast.makeText(this, "用户未登陆", Toast.LENGTH_SHORT).show();
            }


        }
    }


}
