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

package com.weather.viewer.example.ui.cities;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.weather.viewer.example.repository.CitiesRepository;
import com.weather.viewer.example.vo.Resource;
import com.weather.viewer.example.vo.WeatherData;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CitiesViewModel extends ViewModel {
    private LiveData<Resource<List<WeatherData>>> cities;

    @SuppressWarnings("unchecked")
    @Inject
    public CitiesViewModel(CitiesRepository citiesRepository) {
        cities = citiesRepository.load();
    }

    public LiveData<Resource<List<WeatherData>>> getCities() {
        return cities;
    }
}
