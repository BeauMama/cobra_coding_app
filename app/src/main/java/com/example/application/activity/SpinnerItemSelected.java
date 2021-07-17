package com.example.application.activity;

import android.view.View;
import android.widget.AdapterView;

public interface SpinnerItemSelected {
    public void fromSystemSelected(AdapterView<?> parent, View view, int position, long id);
    public void toSystemSelected(AdapterView<?> parent, View view, int position, long id);
    public void convertBySelected(AdapterView<?> parent, View view, int position, long id);
}
