package com.example.application.activity;

import android.view.View;
import android.widget.AdapterView;

/**
 * Used for spinners when an item is selected.
 */
public interface SpinnerItemSelected {
    void fromSystemSelected(AdapterView<?> parent, View view, int position, long id);
    void toSystemSelected(AdapterView<?> parent, View view, int position, long id);
}
