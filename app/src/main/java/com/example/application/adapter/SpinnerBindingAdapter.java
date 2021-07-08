package com.example.application.adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

public class SpinnerBindingAdapter {

    @BindingAdapter(value = {"spinnerBinder", "selectedValueAttrChanged"}, requireAll = false)
    public static void bindSpinnerData(Spinner spinner, String newSelectedValue, final InverseBindingListener newTextAttrChanged) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newTextAttrChanged.onChange();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (newSelectedValue != null) {
            int pos = ((ArrayAdapter<String>) spinner.getAdapter()).getPosition(newSelectedValue);
            spinner.setSelection(pos, true);
        }
    }
    @InverseBindingAdapter(attribute = "spinnerBinder", event = "selectedValueAttrChanged")
    public static String captureSelectedValue(Spinner spinner) {
        return (String) spinner.getSelectedItem();
    }
}
