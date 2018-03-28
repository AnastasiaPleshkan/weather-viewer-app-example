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
import com.weather.viewer.example.db.WeatherDao;
import com.weather.viewer.example.vo.Resource;
import com.weather.viewer.example.vo.WeatherData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Repository that handles {@link String} objects.
 */
@Singleton
public class CitiesRepository {
    private final WeatherDao weatherDao;

    @Inject
    CitiesRepository(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    public LiveData<Resource<List<WeatherData>>> load() {
        return new DbBoundResource<List<WeatherData>>() {
            @NonNull
            @Override
            protected LiveData<List<WeatherData>> loadFromDb() {
                return weatherDao.load();
            }
        }.asLiveData();
    }
}
