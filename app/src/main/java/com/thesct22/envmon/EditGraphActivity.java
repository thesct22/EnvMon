package com.thesct22.envmon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.Calendar;

public class EditGraphActivity extends AppCompatActivity {

    EditText fromDate,fromTime,toDate,toTime;
    private int fromYear, fromMonth, fromDay, fromHour, fromMinute;
    private int toYear, toMonth, toDay, toHour, toMinute;
    RadioButton all, lastDates, lastValues, fromto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_graph);

        Spinner spinner=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.graph_select_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        fromDate=findViewById(R.id.editTextDate);
        fromTime=findViewById(R.id.editTextTime);
        toDate=findViewById(R.id.editTextDate2);
        toTime=findViewById(R.id.editTextTime2);

        all=findViewById(R.id.radioButton2);
        lastDates=findViewById(R.id.radioButton);
        lastValues=findViewById(R.id.radioButton3);
        fromto=findViewById(R.id.radioButton5);

        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                fromYear = c.get(Calendar.YEAR);
                fromMonth = c.get(Calendar.MONTH);
                fromDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EditGraphActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                fromDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, fromYear, fromMonth, fromDay);
                datePickerDialog.show();
            }
        });

        fromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                fromHour = c.get(Calendar.HOUR_OF_DAY);
                fromMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditGraphActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                fromTime.setText(hourOfDay + ":" + minute);
                            }
                        }, fromHour, fromMinute, false);
                timePickerDialog.show();
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                toYear = c.get(Calendar.YEAR);
                toMonth = c.get(Calendar.MONTH);
                toDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(EditGraphActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                toDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, toYear, toMonth, toDay);
                datePickerDialog.show();
            }
        });

        toTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                toHour = c.get(Calendar.HOUR_OF_DAY);
                toMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(EditGraphActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                toTime.setText(hourOfDay + ":" + minute);
                            }
                        }, toHour, toMinute, false);
                timePickerDialog.show();
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setChecked(true);
                lastDates.setChecked(false);
                lastValues.setChecked(false);
                fromto.setChecked(false);

                fromDate.setFocusable(false);
                fromTime.setFocusable(false);
                toDate.setFocusable(false);
                toTime.setFocusable(false);

                fromDate.setEnabled(false);
                fromTime.setEnabled(false);
                toDate.setEnabled(false);
                toTime.setEnabled(false);
            }
        });

        lastValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setChecked(false);
                lastDates.setChecked(false);
                lastValues.setChecked(true);
                fromto.setChecked(false);

                fromDate.setFocusable(false);
                fromTime.setFocusable(false);
                toDate.setFocusable(false);
                toTime.setFocusable(false);

                fromDate.setEnabled(false);
                fromTime.setEnabled(false);
                toDate.setEnabled(false);
                toTime.setEnabled(false);
            }
        });

        lastDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setChecked(false);
                lastDates.setChecked(true);
                lastValues.setChecked(false);
                fromto.setChecked(false);

                fromDate.setFocusable(false);
                fromTime.setFocusable(false);
                toDate.setFocusable(false);
                toTime.setFocusable(false);

                fromDate.setEnabled(false);
                fromTime.setEnabled(false);
                toDate.setEnabled(false);
                toTime.setEnabled(false);
            }
        });

        fromto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                all.setChecked(false);
                lastDates.setChecked(false);
                lastValues.setChecked(false);
                fromto.setChecked(true);

                fromDate.setFocusable(true);
                fromTime.setFocusable(true);
                toDate.setFocusable(true);
                toTime.setFocusable(true);

                fromDate.setEnabled(true);
                fromTime.setEnabled(true);
                toDate.setEnabled(true);
                toTime.setEnabled(true);
            }
        });
    }
}