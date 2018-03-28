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

import android.support.test.runner.AndroidJUnit4;

import com.weather.viewer.example.util.TestUtil;
import com.weather.viewer.example.vo.WeatherData;

import org.junit.Test;
import org.junit.runner.RunWith;

import static com.weather.viewer.example.util.LiveDataTestUtil.getValue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(AndroidJUnit4.class)
public class WeatherDaoTest extends DbTest {

    @Test
    public void insertAndLoad() throws InterruptedException {
        final WeatherData weatherData = TestUtil.createWeatherData("moscow");
        db.weatherDao().insert(weatherData);

        final WeatherData loaded = getValue(db.weatherDao().findByCity(weatherData.name));
        assertThat(loaded.name, is("moscow"));

        final WeatherData replacement = TestUtil.createWeatherData("sankt-peterburg");
        db.weatherDao().insert(replacement);

        final WeatherData loadedReplacement = getValue(db.weatherDao().findByCity("sankt-peterburg"));
        assertThat(loadedReplacement.name, is("sankt-peterburg"));
    }
}
