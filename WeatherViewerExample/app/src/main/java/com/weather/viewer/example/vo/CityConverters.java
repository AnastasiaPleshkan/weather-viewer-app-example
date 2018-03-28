package com.weather.viewer.example.vo;

import android.arch.persistence.room.TypeConverter;

public class CityConverters {
    @TypeConverter
    public static City fromString(String value) {
        return new City(value);
    }

    @TypeConverter
    public static String fromType(City data) {
        return data.name;
    }
}