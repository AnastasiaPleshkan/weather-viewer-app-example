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
import com.weather.viewer.example.databinding.WeatherItemBinding;
import com.weather.viewer.example.util.Objects;
import com.weather.viewer.example.vo.ListData;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * A RecyclerView adapter for {@link com.weather.viewer.example.vo.ListData} class.
 */
public class WeatherListAdapter extends DataBoundListAdapter<ListData, WeatherItemBinding> {
    private final DataBindingComponent dataBindingComponent;
    private final RepoClickCallback repoClickCallback;

    public WeatherListAdapter(DataBindingComponent dataBindingComponent,
                              RepoClickCallback repoClickCallback) {
        this.dataBindingComponent = dataBindingComponent;
        this.repoClickCallback = repoClickCallback;
    }

    @Override
    protected WeatherItemBinding createBinding(ViewGroup parent) {
        WeatherItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.weather_item,
                        parent, false, dataBindingComponent);
        binding.getRoot().setOnClickListener(v -> {
            ListData data = binding.getItemData();
            if (data != null && repoClickCallback != null) {
                repoClickCallback.onClick(data);
            }
        });
        return binding;
    }

    @Override
    protected void bind(WeatherItemBinding binding, ListData item) {
        binding.setItemData(item);
    }

    @Override
    protected boolean areItemsTheSame(ListData oldItem, ListData newItem) {
        return Objects.equals(oldItem.dt_txt, newItem.dt_txt);
    }

    @Override
    protected boolean areContentsTheSame(ListData oldItem, ListData newItem) {
        return Objects.equals(oldItem.dt_txt, newItem.dt_txt);
    }

    public interface RepoClickCallback {
        void onClick(ListData repo);
    }
}
