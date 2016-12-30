package com.example.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by admin on 2016-12-22.
 */
@DatabaseTable(tableName = "db_user")
public class User {
    @DatabaseField(columnName = "-id",generatedId = true)
    private long _id;
    @DatabaseField(columnName = "name")
    private String name;
    @DatabaseField(columnName = "age")
    private int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
