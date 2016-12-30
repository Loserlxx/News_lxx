package com.example.admin.news_app.entity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.admin.news_app.utils.News;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by admin on 2016-12-22.
 * OrmLiteSqliteOpenHelper工具类
 * 收藏
 */

public class MyNewsOrmliteOpenHelper extends OrmLiteSqliteOpenHelper {

    private static final String DB_USER="db_user.db";
    private static final int DB_VERSION=1;
    private static MyNewsOrmliteOpenHelper instance;
    private static Dao<News,Long> dao;
    public static Dao<News,Long> getDao(Context context) throws SQLException {
        if (dao==null) {
           dao = getInstance(context).getDao(News.class);
        }
        return dao;
    }

    private MyNewsOrmliteOpenHelper(Context context) {
        super(context, DB_USER,null,DB_VERSION);
    }

    public static MyNewsOrmliteOpenHelper getInstance(Context context){
        if (instance==null) {
            instance=new MyNewsOrmliteOpenHelper(context);
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, News.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        if (newVersion>oldVersion) {
            try {
                TableUtils.dropTable(connectionSource,News.class,true);
                onCreate(database,connectionSource);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
