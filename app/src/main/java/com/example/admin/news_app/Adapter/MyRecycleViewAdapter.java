package com.example.admin.news_app.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.Target;
import com.example.admin.news_app.R;
import com.example.admin.news_app.entity.NewsAPP;

import java.util.List;

/**
 * Created by admin on 2016-12-21.
 */

public class MyRecycleViewAdapter extends RecyclerView.Adapter<MyRecycleViewAdapter.MyViewHorld>{

    private List<NewsAPP.ResultBean.DataBean> datas;
    private Context context;
    private OnRecyclerviewItemClickListener listener;

    public MyRecycleViewAdapter(List<NewsAPP.ResultBean.DataBean> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    public List<NewsAPP.ResultBean.DataBean> getDatas() {
        return datas;
    }

    public void setDatas(List<NewsAPP.ResultBean.DataBean> datas) {
        this.datas = datas;
    }

    @Override
    public MyViewHorld onCreateViewHolder(ViewGroup parent, int viewType) {

        View inflate = LayoutInflater.from(context).inflate(R.layout.recycleview_image_frament_inf, parent, false);

        return new MyViewHorld(inflate);
    }

    @Override
    public void onBindViewHolder(MyViewHorld holder, int position) {
         Glide.with(context).load(datas.get(position).getThumbnail_pic_s()).into(holder.imageView);

        holder.textView.setText(datas.get(position).getAuthor_name());

    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    class MyViewHorld extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        public MyViewHorld(final View itemView) {
            super(itemView);

            imageView= (ImageView) itemView.findViewById(R.id.iv_icon_image_frament);

            textView= (TextView) itemView.findViewById(R.id.tv_title_image_frament);
            //数值传递到外部
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(itemView,getLayoutPosition());
                }
            });

        }
    }
    public void setOnRecycleviewItemClickListener(OnRecyclerviewItemClickListener listener){
        this.listener=listener;
    }
    public interface OnRecyclerviewItemClickListener{
        void onItemClick(View view,int position);
    }
}
