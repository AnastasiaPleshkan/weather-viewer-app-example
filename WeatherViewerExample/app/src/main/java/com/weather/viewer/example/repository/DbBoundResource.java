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

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import com.weather.viewer.example.util.Objects;
import com.weather.viewer.example.vo.Resource;

/**
 * A generic class that can provide a resource backed by the sqlite database.
 *
 * @param <ResultType>
 */
public abstract class DbBoundResource<ResultType> {

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    @MainThread
    DbBoundResource() {
        result.setValue( Resource.loading( null ) );
        LiveData<ResultType> dbSource = loadFromDb();
        result.addSource( dbSource, data -> {
            result.removeSource( dbSource );
            result.addSource( dbSource, newData -> setValue( Resource.success( newData ) ) );
        } );
    }

    @MainThread
    private void setValue(Resource<ResultType> newValue) {
        if (!Objects.equals( result.getValue(), newValue )) {
            result.setValue( newValue );
        }
    }

    public LiveData<Resource<ResultType>> asLiveData() {
        return result;
    }

    @NonNull
    @MainThread
    protected abstract LiveData<ResultType> loadFromDb();
}
