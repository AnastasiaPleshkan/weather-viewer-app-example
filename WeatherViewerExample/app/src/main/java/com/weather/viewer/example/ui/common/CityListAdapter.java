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

package com.weather.viewer.example.ui.common;

import com.weather.viewer.example.R;
import com.weather.viewer.example.databinding.CityItemBinding;
import com.weather.viewer.example.util.Objects;
import com.weather.viewer.example.vo.WeatherData;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * A RecyclerView adapter for {@link String} class.
 */
public class CityListAdapter extends DataBoundListAdapter<WeatherData, CityItemBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final CityClickCallback cityClickCallback;

    public CityListAdapter(DataBindingComponent dataBindingComponent,
                              CityClickCallback cityClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.cityClickCallback = cityClickCallback;
    }

    @Override
    protected CityItemBinding createBinding(ViewGroup parent) {
        CityItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.city_item,
                        parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            WeatherData data = binding.getItem();
            if (data != null && cityClickCallback != null) {
                cityClickCallback.onClick(data);
            }
        });
        return binding;
    }

    @Override
    protected void bind(CityItemBinding binding, WeatherData item) {
        binding.setItem(item);
    }

    @Override
    protected boolean areItemsTheSame(WeatherData oldItem, WeatherData newItem) {
        return Objects.equals(oldItem.name, newItem.name);
    }

    @Override
    protected boolean areContentsTheSame(WeatherData oldItem, WeatherData newItem) {
        return Objects.equals(oldItem.name, newItem.name);
    }

    public interface CityClickCallback {
        void onClick(WeatherData data);
    }
}
