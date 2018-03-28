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

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.weather.viewer.example.api.ApiResponse;
import com.weather.viewer.example.api.OpenWeatherMapService;
import com.weather.viewer.example.db.WeatherDao;
import com.weather.viewer.example.util.ApiUtil;
import com.weather.viewer.example.util.InstantAppExecutors;
import com.weather.viewer.example.util.TestUtil;
import com.weather.viewer.example.vo.Resource;
import com.weather.viewer.example.vo.WeatherData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class CurrentWeatherRepositoryTest {
    private WeatherDao weatherDao;
    private OpenWeatherMapService openWeatherMapService;
    private CurrentWeatherRepository repo;

    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        weatherDao = mock(WeatherDao.class);
        openWeatherMapService = mock(OpenWeatherMapService.class);
        repo = new CurrentWeatherRepository(new InstantAppExecutors(), weatherDao, openWeatherMapService);
    }

    @Test
    public void loadCurrentWeather() {
        repo.load("moscow");
        verify(weatherDao).findByCity("moscow");
    }

    @Test
    public void goToNetwork() {
        MutableLiveData<WeatherData> dbData = new MutableLiveData<>();
        when(weatherDao.findByCity("moscow")).thenReturn(dbData);
        WeatherData weatherData = TestUtil.createWeatherData("moscow");
        LiveData<ApiResponse<WeatherData>> call = ApiUtil.successCall(weatherData);
        when(openWeatherMapService.getCurrentWeather("moscow",
                "7b4b4790eb352c47b9fa8cae5683fd07",
                "metric")).thenReturn(call);
        Observer<Resource<WeatherData>> observer = mock(Observer.class);

        repo.load("moscow").observeForever(observer);
        verify(openWeatherMapService, never()).getCurrentWeather("moscow",
                "7b4b4790eb352c47b9fa8cae5683fd07",
                "metric");
        MutableLiveData<WeatherData> updatedDbData = new MutableLiveData<>();
        when(weatherDao.findByCity("moscow")).thenReturn(updatedDbData);
        dbData.setValue(null);
        verify(openWeatherMapService).getCurrentWeather("moscow",
                "7b4b4790eb352c47b9fa8cae5683fd07",
                "metric");
    }

    @Test
    public void dontGoToNetwork() {
        MutableLiveData<WeatherData> dbData = new MutableLiveData<>();
        WeatherData weatherData = TestUtil.createWeatherData("moscow");
        dbData.setValue(weatherData);
        when(weatherDao.findByCity("moscow")).thenReturn(dbData);
        Observer<Resource<WeatherData>> observer = mock(Observer.class);
        repo.load("moscow").observeForever(observer);
        verify(openWeatherMapService, never()).getCurrentWeather("moscow",
                "7b4b4790eb352c47b9fa8cae5683fd07",
                "metric");
        verify(observer).onChanged(Resource.success(weatherData));
    }
}