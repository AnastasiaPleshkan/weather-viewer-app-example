/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.weather.viewer.example.vo;

import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListData {
    @SerializedName("weather")
    @TypeConverters(WeatherConverters.class)
    public ArrayList<Weather> weather;
    @SerializedName("wind")
    @TypeConverters(WindConverters.class)
    public Wind wind;
    @SerializedName("main")
    @TypeConverters(MainConverters.class)
    public MainData main;
    @SerializedName("clouds")
    @TypeConverters(CloudsConverters.class)
    public Clouds clouds;
    @SerializedName("dt_txt")
    public String dt_txt;

    public Weather getDetail() {
        return weather.get(0);
    }
}
