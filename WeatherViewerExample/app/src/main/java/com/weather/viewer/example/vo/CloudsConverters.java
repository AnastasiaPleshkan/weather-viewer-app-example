package com.weather.viewer.example.vo;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;

public class CloudsConverters {
    @TypeConverter
    public static Clouds fromString(String value) {
        return new Gson().fromJson(value, Clouds.class);
    }

    @TypeConverter
    public static String fromType(Clouds data) {
        return new Gson().toJson(data);
    }
}