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

package com.weather.viewer.example.api;

import com.weather.viewer.example.vo.ForecastWeatherData;
import com.weather.viewer.example.vo.WeatherData;

import android.arch.lifecycle.LiveData;


import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * REST API access points
 */
public interface OpenWeatherMapService {


    @GET("weather")
    LiveData<ApiResponse<WeatherData>> getCurrentWeather(
            @Query("q") String city_name,
            @Query("appid") String appid,
            @Query("units") String units);

    @GET("forecast")
    LiveData<ApiResponse<ForecastWeatherData>> getForecastWeather(
            @Query("q") String city_name,
            @Query("appid") String appid,
            @Query("units") String units);
}
