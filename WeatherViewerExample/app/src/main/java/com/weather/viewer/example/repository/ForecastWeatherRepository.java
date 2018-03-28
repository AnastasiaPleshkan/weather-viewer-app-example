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

package com.weather.viewer.example.repository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.weather.viewer.example.AppExecutors;
import com.weather.viewer.example.AppPreferences;
import com.weather.viewer.example.api.ApiResponse;
import com.weather.viewer.example.api.OpenWeatherMapService;
import com.weather.viewer.example.db.ForecastWeatherDao;
import com.weather.viewer.example.vo.ForecastWeatherData;
import com.weather.viewer.example.vo.Resource;
import com.weather.viewer.example.vo.WeatherData;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles {@link WeatherData} objects.
 */
@Singleton
public class ForecastWeatherRepository {
    private final ForecastWeatherDao weatherDao;
    private final OpenWeatherMapService openWeatherMapService;
    private final AppExecutors appExecutors;

    @Inject
    ForecastWeatherRepository(AppExecutors appExecutors, ForecastWeatherDao weatherDao, OpenWeatherMapService openWeatherMapService) {
        this.weatherDao = weatherDao;
        this.openWeatherMapService = openWeatherMapService;
        this.appExecutors = appExecutors;
    }

    private LiveData<Resource<ForecastWeatherData>> load(String city_name, boolean reload) {
        return new NetworkBoundResource<ForecastWeatherData, ForecastWeatherData>(appExecutors, reload) {
            @Override
            protected void saveCallResult(@NonNull ForecastWeatherData item) {
                weatherDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable ForecastWeatherData data) {
                return data == null;
            }

            @NonNull
            @Override
            protected LiveData<ForecastWeatherData> loadFromDb() {
                return weatherDao.findByCity(city_name);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<ForecastWeatherData>> createCall() {
                return openWeatherMapService.getForecastWeather(city_name,
                        AppPreferences.key, AppPreferences.units_default);
            }
        }.asLiveData();
    }

    public LiveData<Resource<ForecastWeatherData>> load(String city_name) {
        return load( city_name, true );
    }
}
