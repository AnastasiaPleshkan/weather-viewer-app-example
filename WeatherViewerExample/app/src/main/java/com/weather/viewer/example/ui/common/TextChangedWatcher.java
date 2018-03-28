package com.weather.viewer.example.ui.common;

import android.text.Editable;
import android.text.TextWatcher;

public abstract class TextChangedWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
