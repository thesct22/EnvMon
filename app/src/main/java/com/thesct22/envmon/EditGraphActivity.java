package com.thesct22.envmon;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class EditGraphActivity extends AppCompatActivity {

    envmon en;
    EditText fromDate,fromTime,toDate,toTime,settime,setvals;
    private int fromYear, fromMonth, fromDay, fromHour, fromMinute;
    private int toYear, toMonth, toDay, toHour, toMinute;
    RadioButton all, lastDates, lastValues, fromto;
    Spinner spinner;
    Toolbar tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_graph);

        en=new envmon();
        en.setradio(new boolean[]{true, false, false, false});

        tb = findViewById(R.id.toolbarcheckeditgraph);
        tb.setNavigationOnClickListener(v -> onBackPressed());

        spinner=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.graph_select_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        fromDate=findViewById(R.id.editTextDate);
        fromTime=findViewById(R.id.editTextTime);
        toDate=findViewById(R.id.editTextDate2);
        toTime=findViewById(R.id.editTextTime2);
        settime=findViewById(R.id.editTextNumber2);
        setvals=findViewById(R.id.editTextNumber3);

        all=findViewById(R.id.radioButton2);
        lastDates=findViewById(R.id.radioButton);
        lastValues=findViewById(R.id.radioButton3);
        fromto=findViewById(R.id.radioButton5);

        fromDate.setFocusable(false);
        fromTime.setFocusable(false);
        toDate.setFocusable(false);
        toTime.setFocusable(false);

        fromDate.setEnabled(false);
        fromTime.setEnabled(false);
        toDate.setEnabled(false);
        toTime.setEnabled(false);
        settime.setEnabled(false);
        setvals.setEnabled(false);
        spinner.setEnabled(false);


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
                settime.setEnabled(false);
                setvals.setEnabled(false);
                spinner.setEnabled(false);

                en.setradio(new boolean[]{true, false, false, false});
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
                settime.setEnabled(false);
                setvals.setEnabled(true);
                spinner.setEnabled(false);

                en.setradio(new boolean[]{false, false, true, false});
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
                settime.setEnabled(true);
                setvals.setEnabled(false);
                spinner.setEnabled(true);

                en.setradio(new boolean[]{false, true, false, false});
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
                settime.setEnabled(false);
                setvals.setEnabled(false);
                spinner.setEnabled(false);

                en.setradio(new boolean[]{false, false, false, true});
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        en=new envmon();

        spinner=findViewById(R.id.spinner);
        fromDate=findViewById(R.id.editTextDate);
        fromTime=findViewById(R.id.editTextTime);
        toDate=findViewById(R.id.editTextDate2);
        toTime=findViewById(R.id.editTextTime2);
        settime=findViewById(R.id.editTextNumber2);
        setvals=findViewById(R.id.editTextNumber3);

        boolean[] radios=en.getradio();

        if(radios[1]){
            long nowmillis=System.currentTimeMillis();
            int multiplier=Integer.parseInt(settime.getText().toString());
            String param=spinner.getSelectedItem().toString();
            switch(param){
                case "Minute(s)":   {en.setDataMillis(nowmillis-60000L*multiplier,nowmillis);
                                    break;}
                case "Hour(s)":     {en.setDataMillis(nowmillis-3600000L*multiplier,nowmillis);
                                    break;}
                case "Day(s)":      {en.setDataMillis(nowmillis-86400000L*multiplier,nowmillis);
                                    break;}
                case "Week(s)":     {en.setDataMillis(nowmillis-604800000L*multiplier,nowmillis);
                                    break;}
                case "Month(s)":    {en.setDataMillis(nowmillis-2629746000L*multiplier,nowmillis);
                                    break;}
                case "Year(s)":     {en.setDataMillis(nowmillis-31556952000L*multiplier,nowmillis);
                                    break;}
            }
        }
        else if(radios[2]){
            en.setDataMillis(Integer.parseInt(setvals.getText().toString()),0);
            Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT).show();
        }
        else if(radios[3]){

            String from= TextUtils.isEmpty(fromDate.getText().toString())?"01-01-1970":fromDate.getText().toString();
            from+=" ";
            from+=TextUtils.isEmpty(fromTime.getText().toString())?"00:00":fromTime.getText().toString();
            String to=TextUtils.isEmpty(toDate.getText().toString())?"01-01-1970":toDate.getText().toString();
            to+=" ";
            to+=TextUtils.isEmpty(toTime.getText().toString())?"00:00":toTime.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm", Locale.ENGLISH);

            Date fromdate = new Date();
            try {
                fromdate = sdf.parse(from);
            } catch (ParseException e) {
                e.printStackTrace();
                //Toast.makeText(getApplicationContext(),"Hello Javatpoint11",Toast.LENGTH_SHORT).show();
            }
            Date todate = new Date();
            try {
                todate = sdf.parse(to);
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
            }
            long frommillis = fromdate.getTime();
            long tomillis = todate.getTime();

            en.setDataMillis(frommillis, tomillis);
            //Toast.makeText(getApplicationContext(),Long.toString(tomillis),Toast.LENGTH_LONG).show();

        }

        else{
            en.setDataMillis(0,0);
        }
    }
}