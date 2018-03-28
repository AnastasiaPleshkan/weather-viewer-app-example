package com.weather.viewer.example.vo;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

public class MainConverters {
    @TypeConverter
    public static MainData fromString(String value) {
        return new Gson().fromJson(value, MainData.class);
    }

    @TypeConverter
    public static String fromType(MainData data) {
        return new Gson().toJson(data);
    }
}