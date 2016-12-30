package com.example.admin.news_app.utils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by admin on 2016-12-22.
 */
@DatabaseTable(tableName = "new_database.db")
public class News {
    @DatabaseField(columnName = "_id",id = true)
    private long _id;
    @DatabaseField(columnName = "img")
    private String img;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "data")
    private String data;
    @DatabaseField(columnName = "url")
    private String url;


    public News(long _id,String img, String title, String data, String url) {
        this._id=_id;
        this.img = img;
        this.title = title;
        this.data = data;
        this.url = url;
    }

    public News() {
    }

    @Override
    public String toString() {
        return "News{" +
                "img='" + img + '\'' +
                ", title='" + title + '\'' +
                ", data='" + data + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
