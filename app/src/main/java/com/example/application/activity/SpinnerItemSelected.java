package com.example.application.activity;

import android.view.View;
import android.widget.AdapterView;

public interface SpinnerItemSelected {
    public void fromMeasurementSelected(AdapterView<?> parent, View view, int position, long id);
    public void toMeasurementSelected(AdapterView<?> parent, View view, int position, long id);
}
