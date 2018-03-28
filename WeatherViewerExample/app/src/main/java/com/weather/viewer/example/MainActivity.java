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

package com.weather.viewer.example;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.weather.viewer.example.ui.common.NavigationController;

import javax.inject.Inject;

import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends AppCompatActivity implements HasSupportFragmentInjector {
    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;
    @Inject
    NavigationController navigationController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.main_activity );
        if (savedInstanceState == null) {
            navigationController.navigateToCurrentWeather( AppPreferences.defaultCities[0] );
        }
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        for (String defaultCity : AppPreferences.defaultCities) {
            MenuItem menuItem = menu.add( 0, menu.size() + 1, 0, defaultCity );
            menuItem.setShowAsActionFlags( MenuItem.SHOW_AS_ACTION_IF_ROOM );
            menuItem.setOnMenuItemClickListener( item -> {
                navigationController.navigateToCurrentWeather( item.getTitle().toString() );
                return false;
            } );
        }

        MenuItem add_item = menu.add( 0, menu.size() + 1, 1, R.string.add_city);
        add_item.setShowAsActionFlags( MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW );
        add_item.setOnMenuItemClickListener( item -> {
            navigationController.navigateToCurrentWeather( "" );
            return false;
        } );


        menu.addSubMenu( R.string.cities).getItem().setOnMenuItemClickListener( item -> {
            navigationController.navigateToCityList( );
            return false;
        } );
        return super.onCreateOptionsMenu( menu );
    }
}
