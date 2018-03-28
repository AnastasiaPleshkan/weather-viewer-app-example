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

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.Bindable;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.weather.viewer.example.R;
import com.weather.viewer.example.binding.FragmentDataBindingComponent;
import com.weather.viewer.example.databinding.CurrentWeatherFragmentBinding;
import com.weather.viewer.example.di.Injectable;
import com.weather.viewer.example.ui.common.NavigationController;
import com.weather.viewer.example.ui.common.TextChangedWatcher;
import com.weather.viewer.example.ui.common.WeatherListAdapter;
import com.weather.viewer.example.util.AutoClearedValue;

import javax.inject.Inject;

public class CurrentWeatherFragment extends Fragment implements Injectable {
    private static final String CITY_KEY = "city";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent( this );
    private CurrentWeatherViewModel currentWeatherViewModel;
    @VisibleForTesting
    AutoClearedValue<CurrentWeatherFragmentBinding> binding;
    private AutoClearedValue<WeatherListAdapter> adapter;

    public static CurrentWeatherFragment create(String city) {
        CurrentWeatherFragment currentWeatherFragment = new CurrentWeatherFragment();
        Bundle bundle = new Bundle();
        if (city != null)
            bundle.putString( CITY_KEY, city );
        currentWeatherFragment.setArguments( bundle );
        return currentWeatherFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        CurrentWeatherFragmentBinding dataBinding = DataBindingUtil.inflate( inflater, R.layout.current_weather_fragment,
                container, false, dataBindingComponent );
        dataBinding.setRetryCallback( () -> {
//            currentWeatherViewModel.setCityName( binding.get().cityEdit.getText().toString() );
            currentWeatherViewModel.retry();
        } );
        binding = new AutoClearedValue<>( this, dataBinding );
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        currentWeatherViewModel = ViewModelProviders.of( this, viewModelFactory ).get( CurrentWeatherViewModel.class );

        String city = getArguments().getString( CITY_KEY );
        if (!TextUtils.isEmpty( city )) {
            currentWeatherViewModel.setCityName( city );
        } else {
            binding.get().setCanEdit( true );
            binding.get().cityEdit
                    .addTextChangedListener( new TextChangedWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            currentWeatherViewModel.setCityName( s.toString() );
                        }
                    } );
        }
        currentWeatherViewModel.getWeather().observe( this, weatherDataResource -> {
            binding.get().setWeatherData( weatherDataResource == null ? null : weatherDataResource.data );
            binding.get().setWeatherResource( weatherDataResource );
            // this is only necessary because espresso cannot read data binding callbacks.
            binding.get().executePendingBindings();
        } );
        WeatherListAdapter rvAdapter = new WeatherListAdapter( dataBindingComponent,
                item -> {
                } );
        binding.get().repoList.setAdapter( rvAdapter );
        this.adapter = new AutoClearedValue<>( this, rvAdapter );
        currentWeatherViewModel.getForecastWeather().observe( this, items -> {
            // no null checks for adapter.get() since LiveData guarantees that we'll not receive
            // the event if fragment is now show.
            if (items == null || items.data == null) {
                adapter.get().replace( null );
            } else {
                adapter.get().replace( items.data.list );
            }
        } );
    }
}
