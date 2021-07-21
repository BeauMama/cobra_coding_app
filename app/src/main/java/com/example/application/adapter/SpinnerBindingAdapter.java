package com.example.application.adapter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

/**
 * Binds spinners to the data model that do not have listeners.
 */
public class SpinnerBindingAdapter {

    /**
     * Binds the spinner using a listener. This custom binder is used so that a spinner
     * can be bound by the value rather than by the Android default which is by the position.
     *
     * @param spinner The spinner to bind and update.
     * @param newSelectedValue The value to change the spinner to.
     * @param newTextAttrChanged Used for the two way binding to work.
     */
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

    /**
     * Gets the selected value of the spinner.
     *
     * @param spinner The spinner to get the value for.
     * @return The selected value of the spinner.
     */
    @InverseBindingAdapter(attribute = "spinnerBinder", event = "selectedValueAttrChanged")
    public static String captureSelectedValue(Spinner spinner) {
        return (String) spinner.getSelectedItem();
    }
}
