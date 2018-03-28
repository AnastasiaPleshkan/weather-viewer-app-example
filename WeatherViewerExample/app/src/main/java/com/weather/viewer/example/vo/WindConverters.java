package com.weather.viewer.example.vo;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

public class WindConverters {
    @TypeConverter
    public static Wind fromString(String value) {
        return new Gson().fromJson(value, Wind.class);
    }

    @TypeConverter
    public static String fromType(Wind wind) {
        return new Gson().toJson(wind);
    }
}