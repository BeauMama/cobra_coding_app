package com.example.application;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public interface SpinnerItemSelected {
    public void fromMeasurementSelected(AdapterView<?> parent, View view, int position, long id);
    public void toMeasurementSelected(AdapterView<?> parent, View view, int position, long id);
}
