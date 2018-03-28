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

package com.weather.viewer.example.ui.weather;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.VisibleForTesting;

import com.weather.viewer.example.repository.CurrentWeatherRepository;
import com.weather.viewer.example.repository.ForecastWeatherRepository;
import com.weather.viewer.example.util.AbsentLiveData;
import com.weather.viewer.example.util.Objects;
import com.weather.viewer.example.vo.ForecastWeatherData;
import com.weather.viewer.example.vo.Resource;
import com.weather.viewer.example.vo.WeatherData;

import javax.inject.Inject;

public class CurrentWeatherViewModel extends ViewModel {
    @VisibleForTesting
    final MutableLiveData<String> city_name = new MutableLiveData<>();
    private final LiveData<Resource<WeatherData>> weather;
    private final LiveData<Resource<ForecastWeatherData>> forecastWeather;

    @SuppressWarnings("unchecked")
    @Inject
    public CurrentWeatherViewModel(CurrentWeatherRepository weatherRepository, ForecastWeatherRepository forecastWeatherRepository) {
        weather = Transformations.switchMap( city_name, city_name -> {
            if (city_name == null) {
                return AbsentLiveData.create();
            } else {
                return weatherRepository.load( city_name );
            }
        } );
        forecastWeather = Transformations.switchMap( city_name, city_name -> {
            if (city_name == null) {
                return AbsentLiveData.create();
            } else {
                return forecastWeatherRepository.load( city_name );
            }
        } );
    }

    public void setCityName(String city_name) {
        if (Objects.equals( this.city_name.getValue(), city_name )) {
            return;
        }
        this.city_name.setValue( city_name );
    }

    public LiveData<Resource<WeatherData>> getWeather() {
        return weather;
    }

    @VisibleForTesting
    public void retry() {
        if (this.city_name.getValue() != null) {
            this.city_name.setValue(this.city_name.getValue());
        }
    }

    public String getCity() {
        return city_name.getValue();
    }

    public LiveData<Resource<ForecastWeatherData>> getForecastWeather() {
        return forecastWeather;
    }
}
