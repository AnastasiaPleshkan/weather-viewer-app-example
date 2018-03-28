package com.weather.viewer.example.vo;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WeatherConverters {
    @TypeConverter
    public static ArrayList<Weather> fromString(String value) {
        Type listType = new TypeToken<ArrayList<Weather>>() {
        }.getType();
        return new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<Weather> list) {
        return new Gson().toJson(list);
    }
}