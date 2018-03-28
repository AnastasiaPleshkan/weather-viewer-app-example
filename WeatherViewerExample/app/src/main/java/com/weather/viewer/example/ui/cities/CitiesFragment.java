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

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.weather.viewer.example.R;
import com.weather.viewer.example.binding.FragmentDataBindingComponent;
import com.weather.viewer.example.databinding.CitiesFragmentBinding;
import com.weather.viewer.example.di.Injectable;
import com.weather.viewer.example.ui.common.CityListAdapter;
import com.weather.viewer.example.ui.common.NavigationController;
import com.weather.viewer.example.util.AutoClearedValue;

import javax.inject.Inject;

public class CitiesFragment extends Fragment implements Injectable {
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent( this );
    private CitiesViewModel citiesViewModel;
    @VisibleForTesting
    AutoClearedValue<CitiesFragmentBinding> binding;
    private AutoClearedValue<CityListAdapter> adapter;

    public static CitiesFragment create() {
        return new CitiesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        CitiesFragmentBinding dataBinding = DataBindingUtil.inflate( inflater, R.layout.cities_fragment,
                container, false, dataBindingComponent );
        binding = new AutoClearedValue<>( this, dataBinding );
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );
        citiesViewModel = ViewModelProviders.of( this, viewModelFactory ).get( CitiesViewModel.class );

        citiesViewModel.getCities().observe( this, listResource -> {

            if (listResource == null || listResource.data == null) {
                adapter.get().replace( null );
            } else {
                adapter.get().replace( listResource.data );
            }

            // this is only necessary because espresso cannot read data binding callbacks.
            binding.get().executePendingBindings();
        } );
        CityListAdapter rvAdapter = new CityListAdapter( dataBindingComponent,
                item -> {
                    navigationController.navigateToCurrentWeather( item.name );
                } );
        binding.get().citiesList.setAdapter( rvAdapter );
        this.adapter = new AutoClearedValue<>( this, rvAdapter );
//        initRepoList();
    }

/*
    private void initRepoList() {
        citiesViewModel.getForecastWeather().observe( this, items -> {
            // no null checks for adapter.get() since LiveData guarantees that we'll not receive
            // the event if fragment is now show.
        } );
    }
*/
}
