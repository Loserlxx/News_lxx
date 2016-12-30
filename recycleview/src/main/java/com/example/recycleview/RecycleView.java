package com.example.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class RecycleView extends RecyclerView.Adapter<RecycleView.MyViewHolder> {
    private List<String> datas;
    private Context context;

    public RecycleView(List<String> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(context).inflate(R.layout.activity_recycle_view, parent, false);
        return new MyViewHolder(inflate);
    }
    //初始化数据
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tv.setText(datas.get(position));
    }




    @Override
    public int getItemCount() {
        return datas.size();
    }
    class  MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView iv;
        private TextView tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            iv= (ImageView) itemView.findViewById(R.id.iv);
            tv= (TextView) itemView.findViewById(R.id.tv);
        }
    }
}
