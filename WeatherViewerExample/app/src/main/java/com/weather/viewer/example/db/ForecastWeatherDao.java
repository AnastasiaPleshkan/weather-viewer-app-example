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

package com.weather.viewer.example.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.weather.viewer.example.vo.ForecastWeatherData;
import com.weather.viewer.example.vo.WeatherData;

/**
 * Interface for database access for {@link ForecastWeatherData} related operations.
 */
@Dao
public interface ForecastWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ForecastWeatherData weather);

    @Query("SELECT * FROM forecastweatherdata WHERE LOWER(city) = LOWER(:city_name)")
    LiveData<ForecastWeatherData> findByCity(String city_name);

    @Query("DELETE FROM forecastweatherdata")
    void deleteAll();
}
