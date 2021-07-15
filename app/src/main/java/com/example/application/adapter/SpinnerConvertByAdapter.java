package com.example.application.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.recyclerview.widget.RecyclerView;

import com.example.application.R;

public class SpinnerConvertByAdapter {
    @BindingAdapter(value = {"spinnerConvertByBinder", "selectedValueAttrChanged"}, requireAll = false)
    public static void bindSpinnerData(Spinner spinner, String newSelectedValue, final InverseBindingListener newTextAttrChanged) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newTextAttrChanged.onChange();

                String convertBy = parent.getItemAtPosition(position).toString();
                Activity activity = (Activity) view.getContext();
                RecyclerView recyclerView = activity.findViewById(R.id.ingredientList);
                EditText editTextConvAmount =  activity.findViewById(R.id.convAmount);

                int visibility;
                if (convertBy.toLowerCase().equals("one ingredient")) {
                    visibility = View.VISIBLE;
                    editTextConvAmount.setVisibility(View.INVISIBLE);
                } else {
                    visibility = View.INVISIBLE;
                    editTextConvAmount.setVisibility(View.VISIBLE);
                }
                
                for(int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {
                    View ingredient = recyclerView.getLayoutManager().findViewByPosition(i);
                    //try {
                        CheckBox checkbox = ingredient.findViewById(R.id.checkBoxIsConvIngredient);
                        EditText editText = ingredient.findViewById(R.id.editOneIngredient);
                        TextView textView = ingredient.findViewById(R.id.calcConvQuantity);

                        checkbox.setVisibility(visibility);

                        if (visibility == View.VISIBLE) {
                            if (checkbox.isChecked()) {
                                editText.setVisibility(View.VISIBLE);
                                textView.setVisibility(View.INVISIBLE);
                            } else {
                                editText.setVisibility(View.INVISIBLE);
                                textView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            editText.setVisibility(View.INVISIBLE);
                            textView.setVisibility(View.VISIBLE);
                        }
                    //}
                    //catch (Exception e) {            }
                }
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
    @InverseBindingAdapter(attribute = "spinnerConvertByBinder", event = "selectedValueAttrChanged")
    public static String captureSelectedValue(Spinner spinner) {
        return (String) spinner.getSelectedItem();
    }
}
