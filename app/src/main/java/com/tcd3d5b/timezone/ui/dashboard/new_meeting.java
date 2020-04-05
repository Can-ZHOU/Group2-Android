package com.tcd3d5b.timezone.ui.dashboard;

import android.view.View;


import androidx.fragment.app.DialogFragment;

import com.tcd3d5b.timezone.DatePickerFragment;
import com.tcd3d5b.timezone.TimePickerFragment;

public class new_meeting extends TimePickerFragment {


    public void showTimePickerDialog(View view) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View view) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }
}
