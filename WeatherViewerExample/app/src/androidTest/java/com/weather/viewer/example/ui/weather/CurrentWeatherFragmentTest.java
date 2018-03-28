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

import android.arch.lifecycle.MutableLiveData;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.weather.viewer.example.R;
import com.weather.viewer.example.binding.FragmentBindingAdapters;
import com.weather.viewer.example.testing.SingleFragmentActivity;
import com.weather.viewer.example.ui.common.NavigationController;
import com.weather.viewer.example.util.EspressoTestUtil;
import com.weather.viewer.example.util.TestUtil;
import com.weather.viewer.example.util.ViewModelUtil;
import com.weather.viewer.example.vo.Resource;
import com.weather.viewer.example.vo.WeatherData;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(AndroidJUnit4.class)
public class CurrentWeatherFragmentTest {
    @Rule
    public ActivityTestRule<SingleFragmentActivity> activityRule =
            new ActivityTestRule<>(SingleFragmentActivity.class, true, true);

    private CurrentWeatherViewModel viewModel;
    private NavigationController navigationController;
    private FragmentBindingAdapters fragmentBindingAdapters;
    private MutableLiveData<Resource<WeatherData>> data = new MutableLiveData<>();

    @Before
    public void init() throws Throwable {
        EspressoTestUtil.disableProgressBarAnimations(activityRule);
        CurrentWeatherFragment fragment = CurrentWeatherFragment.create("moscow");
        viewModel = mock(CurrentWeatherViewModel.class);
        when(viewModel.getWeather()).thenReturn(data);
        doNothing().when(viewModel).setCityName(anyString());
        navigationController = mock(NavigationController.class);
        fragmentBindingAdapters = mock(FragmentBindingAdapters.class);

        fragment.viewModelFactory = ViewModelUtil.createFor(viewModel);
        fragment.navigationController = navigationController;
        fragment.dataBindingComponent = () -> fragmentBindingAdapters;

        activityRule.getActivity().setFragment(fragment);
    }

    @Test
    public void loading() {
        data.postValue(Resource.loading(null));
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).check(matches(not(isDisplayed())));
    }

    @Test
    public void error() throws InterruptedException {
        doNothing().when(viewModel).retry();
        data.postValue(Resource.error("wtf", null));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.error_msg)).check(matches(withText("wtf")));
        onView(withId(R.id.retry)).check(matches(isDisplayed()));
        onView(withId(R.id.retry)).perform(click());
        verify(viewModel).retry();
    }

    @Test
    public void loadingWithCity() {
        WeatherData weatherData = TestUtil.createWeatherData("moscow");
        data.postValue(Resource.loading(weatherData));
        onView(withId(R.id.name)).check(matches(withText(weatherData.name)));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void loadedWeather() {
        WeatherData weatherData = TestUtil.createWeatherData("moscow");
        data.postValue(Resource.success(weatherData));
        onView(withId(R.id.name)).check(matches(withText(weatherData.name)));
        onView(withId(R.id.progress_bar)).check(matches(not(isDisplayed())));
    }

    @Test
    public void nullWeatherData() {
        data.postValue(null);
        onView(withId(R.id.name)).check(matches(not(isDisplayed())));
    }

    @Test
    public void nulledWeatherData() {
        WeatherData weatherData = TestUtil.createWeatherData("moscow");
        data.postValue(Resource.success(weatherData));
        onView(withId(R.id.name)).check(matches(withText(weatherData.name)));
        data.postValue(null);
        onView(withId(R.id.name)).check(matches(not(isDisplayed())));
    }
}