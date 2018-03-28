package com.weather.viewer.example.vo;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListDataConverters {
    @TypeConverter
    public static ArrayList<ListData> fromString(String value) {
        Type listType = new TypeToken<ArrayList<ListData>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<ListData> list) {
        return new Gson().toJson(list);
    }
}