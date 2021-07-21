package com.example.application.adapter;

import android.app.Activity;
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

/**
 * Binds the convert by spinner to the data model with custom actions in the listener to
 * update the UI.
 */
public class SpinnerConvertByAdapter {

    /**
     * Binds the spinner using a listener. This custom binder is used so that a spinner
     * can be bound by the value rather than by the Android default which is by the position.
     * <p>
     * In addition to binding the spinner with the data model, this method updates the related
     * UI controls when the spinner is changed.
     *
     * @param spinner The spinner to bind and update.
     * @param newSelectedValue The value to change the spinner to.
     * @param newTextAttrChanged Used for the two way binding to work.
     */
    @BindingAdapter(value = {"spinnerConvertByBinder", "selectedValueAttrChanged"}, requireAll = false)
    public static void bindSpinnerData(Spinner spinner, String newSelectedValue, final InverseBindingListener newTextAttrChanged) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newTextAttrChanged.onChange();

                // Update the controls based on what the user selects in the spinner.
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
                    if (ingredient != null) {
                        CheckBox checkbox = ingredient.findViewById(R.id.checkBoxIsConvIngredient);
                        EditText editText = ingredient.findViewById(R.id.editIngredientQty);
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
                    }
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

    /**
     * Gets the selected value of the spinner.
     *
     * @param spinner The spinner to get the value for.
     * @return The selected value of the spinner.
     */
    @InverseBindingAdapter(attribute = "spinnerConvertByBinder", event = "selectedValueAttrChanged")
    public static String captureSelectedValue(Spinner spinner) {
        return (String) spinner.getSelectedItem();
    }
}
