package com.example.admin.news_app.frament;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.admin.news_app.Adapter.MyChatAdapter;
import com.example.admin.news_app.Adapter.MyDatasAdapter;
import com.example.admin.news_app.R;
import com.example.admin.news_app.entity.NewsAPP;
import com.example.admin.news_app.entity.Robot;
import com.example.admin.news_app.utils.Constant;
import com.example.admin.news_app.utils.MyChat;
import com.example.admin.news_app.utils.NoHttpUtils;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016-12-14.
 */

public class ChatFrament extends Fragment{
    private List<MyChat> chats=new ArrayList<>();
    private ListView listView;
    private MyChatAdapter myChatAdapter;
    private EditText et;
    private Button btn;
    private String textStr;
    private String userText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.chat_fragment_listview, null);
        listView = (ListView) inflate.findViewById(R.id.lv_chat_fragment);
        et = (EditText) inflate.findViewById(R.id.et_chat_fragment);
        btn = (Button) inflate.findViewById(R.id.btn_chat_fragment);


        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        /**
         * 点击发送发送消息
         * @param view
         */
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userText = et.getText().toString();
                chats.add(new MyChat(1, userText));
                et.setText("");
                myChatAdapter = new MyChatAdapter(chats, getContext());
                listView.setAdapter(myChatAdapter);
                //设置默认显示最后一段
                myChatAdapter.notifyDataSetChanged();
                listView.setSelection(chats.size());
                SendInfo();
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }




    private void SendInfo() {
        Request<String> TopNewsStringRequest = NoHttp.createStringRequest("http://op.juhe.cn/robot/index?info="+userText + "&key=edcc787fb20361114e4aa540cf6925a7");

        NoHttpUtils.getInstance().add(Constant.WHAT_NEWS_REQUEST, TopNewsStringRequest, new OnResponseListener<String>() {

            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                String result = response.get();
                Robot robot = JSON.parseObject(result, Robot.class);
                Robot.ResultBean result1 = robot.getResult();
                if (result1!=null){
                    textStr = robot.getResult().getText();
                    chats.add(new MyChat(0, textStr));
                }else{
                    chats.add(new MyChat(0,"你说的我不知道？"));
                }

                myChatAdapter = new MyChatAdapter(chats, getContext());
                listView.setAdapter(myChatAdapter);
                //设置默认显示最后一段
                myChatAdapter.notifyDataSetChanged();
                listView.setSelection(chats.size());
            }

            @Override
            public void onFailed(int what, Response<String> response) {
                Toast.makeText(getContext(), "请链接网络", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }


}
