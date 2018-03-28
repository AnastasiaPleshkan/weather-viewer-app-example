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

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

@Entity(primaryKeys = {"city"}, indices = {@Index("city")})
public class ForecastWeatherData {
    @SerializedName("city")
    @TypeConverters(CityConverters.class)
    @NonNull
    public City city;
    @SerializedName("list")
    @TypeConverters(ListDataConverters.class)
    public ArrayList<ListData> list;

    public ForecastWeatherData() {
    }
}
