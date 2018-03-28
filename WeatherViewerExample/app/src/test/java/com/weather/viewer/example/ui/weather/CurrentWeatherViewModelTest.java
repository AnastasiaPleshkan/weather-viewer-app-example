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

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;

import com.weather.viewer.example.repository.CurrentWeatherRepository;
import com.weather.viewer.example.repository.ForecastWeatherRepository;
import com.weather.viewer.example.util.TestUtil;
import com.weather.viewer.example.vo.Resource;
import com.weather.viewer.example.vo.WeatherData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
@RunWith(JUnit4.class)
public class CurrentWeatherViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private CurrentWeatherViewModel currentWeatherViewModel;
    private CurrentWeatherRepository repository;
    private ForecastWeatherRepository forecastWeatherRepository;

    @Before
    public void setup() {
        repository = mock(CurrentWeatherRepository.class);
        currentWeatherViewModel = new CurrentWeatherViewModel(repository, forecastWeatherRepository);
    }

    @Test
    public void testNull() {
        assertThat(currentWeatherViewModel.getWeather(), notNullValue());
        verify(repository, never()).load(anyString());
        currentWeatherViewModel.setCityName("moscow");
        verify(repository, never()).load(anyString());
    }

    @Test
    public void sendResultToUI() {
        MutableLiveData<Resource<WeatherData>> foo = new MutableLiveData<>();
        MutableLiveData<Resource<WeatherData>> bar = new MutableLiveData<>();
        when(repository.load("moscow")).thenReturn(foo);
        when(repository.load("sankt-peterburg")).thenReturn(bar);
        Observer<Resource<WeatherData>> observer = mock(Observer.class);
        currentWeatherViewModel.getWeather().observeForever(observer);
        currentWeatherViewModel.setCityName("moscow");
        verify(observer, never()).onChanged(any(Resource.class));
        WeatherData fooData = TestUtil.createWeatherData("moscow");
        Resource<WeatherData> fooValue = Resource.success(fooData);

        foo.setValue(fooValue);
        verify(observer).onChanged(fooValue);
        reset(observer);
        WeatherData barData = TestUtil.createWeatherData("sankt-peterburg");
        Resource<WeatherData> barValue = Resource.success(barData);
        bar.setValue(barValue);
        currentWeatherViewModel.setCityName("sankt-peterburg");
        verify(observer).onChanged(barValue);
    }

    @Test
    public void retry() {
        currentWeatherViewModel.setCityName("moscow");
        verifyNoMoreInteractions(repository);
        currentWeatherViewModel.retry();
        verifyNoMoreInteractions(repository);
        Observer observer = mock(Observer.class);
        currentWeatherViewModel.getWeather().observeForever(observer);

        verify(repository).load("moscow");
        reset(repository);

        currentWeatherViewModel.retry();
        verify(repository).load("moscow");
        reset(repository);
        currentWeatherViewModel.getWeather().removeObserver(observer);

        currentWeatherViewModel.retry();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void nullWeatherData() {
        Observer<Resource<WeatherData>> observer = mock(Observer.class);
        currentWeatherViewModel.setCityName("moscow");
        currentWeatherViewModel.setCityName(null);
        currentWeatherViewModel.getWeather().observeForever(observer);
        verify(observer).onChanged(null);
    }

    @Test
    public void dontRefreshOnSameData() {
        Observer<String> observer = mock(Observer.class);
        currentWeatherViewModel.city_name.observeForever(observer);
        verifyNoMoreInteractions(observer);
        currentWeatherViewModel.setCityName("moscow");
        verify(observer).onChanged("moscow");
        reset(observer);
        currentWeatherViewModel.setCityName("moscow");
        verifyNoMoreInteractions(observer);
        currentWeatherViewModel.setCityName("sankt-peterburg");
        verify(observer).onChanged("sankt-peterburg");
    }

    @Test
    public void noRetryWithoutWeatherData() {
        currentWeatherViewModel.retry();
        verifyNoMoreInteractions(repository);
    }
}