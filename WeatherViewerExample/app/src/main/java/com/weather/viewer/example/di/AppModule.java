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

package com.weather.viewer.example.di;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.weather.viewer.example.BuildConfig;
import com.weather.viewer.example.api.OpenWeatherMapService;
import com.weather.viewer.example.db.ForecastWeatherDao;
import com.weather.viewer.example.db.WeatherDb;
import com.weather.viewer.example.db.WeatherDao;
import com.weather.viewer.example.util.LiveDataCallAdapterFactory;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
class AppModule {
    private OkHttpClient mOkHttpClient;

    @Singleton
    @Provides
    OpenWeatherMapService provideOpenWeatherMapService() {

        // specify a okHttpClient to modify the default timeout
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.MINUTES);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        HttpLoggingInterceptor.Level logLevel = BuildConfig.DEBUG
                ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE;
        interceptor.setLevel(logLevel);
        builder.addInterceptor(interceptor);
        mOkHttpClient = builder.build();

        return new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                .client(mOkHttpClient)
                .build()
                .create(OpenWeatherMapService.class);
    }

    @Singleton
    @Provides
    WeatherDb provideDb(Application app) {
        return Room.databaseBuilder(app, WeatherDb.class, "weather.db").build();
    }

    @Singleton
    @Provides
    WeatherDao provideWeatherDao(WeatherDb db) {
        return db.weatherDao();
    }

    @Singleton
    @Provides
    ForecastWeatherDao provideForecastWeatherDao(WeatherDb db) {
        return db.forecastWeatherDao();
    }

    public String getBaseUrl() {
        return "http://api.openweathermap.org/data/2.5/";
    }
}
