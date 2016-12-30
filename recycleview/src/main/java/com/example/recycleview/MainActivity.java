package com.example.recycleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView viewById;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewById = (RecyclerView) findViewById(R.id.recycleView);
        List<String > datas=new ArrayList<>();
        for (int i = 0; i <100 ; i++) {
            datas.add("小羊羊"+i+"号");
        }
        //行向滑动还是纵向滑动，false是从小到大 true 从大到小
        //viewById.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        //几行拨动
        viewById.setLayoutManager(new GridLayoutManager(this,3,LinearLayoutManager.HORIZONTAL,false));
        RecycleView recycleView = new RecycleView(datas,this);
        viewById.setAdapter(recycleView);
    }
}
