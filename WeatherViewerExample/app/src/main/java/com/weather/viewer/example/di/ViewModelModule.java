package com.weather.viewer.example.di;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.weather.viewer.example.ui.cities.CitiesViewModel;
import com.weather.viewer.example.ui.weather.CurrentWeatherViewModel;
import com.weather.viewer.example.viewmodel.OpenWeatherMapViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(CurrentWeatherViewModel.class)
    abstract ViewModel bindCurrentWeatherViewModel(CurrentWeatherViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CitiesViewModel.class)
    abstract ViewModel bindCitiesViewModel(CitiesViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(OpenWeatherMapViewModelFactory factory);
}
