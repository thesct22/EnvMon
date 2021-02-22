package com.thesct22.envmon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class Humidity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    LineChart mainhum1;
    private DatabaseReference mdb;
    ArrayList<Entry> temp1data;
    LineDataSet temp1lds= new LineDataSet(null, null);
    ArrayList<ILineDataSet> temp1ilds = new ArrayList<>();
    LineData temp1ld;
    private static final String TAG = "MainHumidity";
    DrawerLayout dl;
    NavigationView nv;
    Toolbar tb;
    FloatingActionButton fab_one;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity2);
        mainhum1 = findViewById(R.id.mainhum1);
        mainhum1.setTouchEnabled(true);
        mainhum1.setPinchZoom(true);
        mainhum1.fitScreen();

        dl = findViewById(R.id.drawer_hum);
        nv = findViewById(R.id.nav_hummain);
        tb = findViewById(R.id.toolbarhum);

        setSupportActionBar(tb);

        nv.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, tb, R.string.nd_open, R.string.nd_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();


        fab_one = findViewById(R.id.fabhum);

        fab_one.setOnClickListener(v -> {
            Intent prof_intent = new Intent(getApplicationContext(),CheckboxActivity.class);

            Pair[] pairs = new Pair[1];
            pairs[0] = new Pair<View,String>(fab_one,"activity_trans");


            ActivityOptions options =ActivityOptions.makeSceneTransitionAnimation(Humidity.this, pairs);
            startActivity(prof_intent,options.toBundle());
        });

        mainhum1 = findViewById(R.id.mainhum1);
        mainhum1.setTouchEnabled(true);


        // enable scaling and dragging
        mainhum1.setDragEnabled(true);
        mainhum1.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);


        mainhum1.setPinchZoom(true);
        mainhum1.fitScreen();
        MarkerView mv = new YourMarkerView(this, R.layout.tvcontent);
        mv.setChartView(mainhum1); // For bounds control
        mainhum1.setMarker(mv);


        mdb = FirebaseDatabase.getInstance().getReference().child("hum").child("hum1");

        retrievedata();
        nv.setNavigationItemSelectedListener(this);


    }

    private void retrievedata(){
        // Read from the database
        mdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Entry>datavals= new ArrayList<>();
                if(dataSnapshot.hasChildren()){

                    for(DataSnapshot mydss:dataSnapshot.getChildren()){

                        String tsstr = mydss.getKey();
                        long ts=Long.parseLong(tsstr);
                        Long templong=mydss.getValue(Long.class);
                        int tempint=templong.intValue();

                        datavals.add(new Entry((int)ts,tempint));
                    }
                    showchart(datavals);
                }
                else {
                    mainhum1.clear();
                    mainhum1.invalidate();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Context context = getApplicationContext();
                CharSequence text = "Database Loading error";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        });

    }
    private void showchart(ArrayList<Entry> showvals){

        temp1lds.setValues(showvals);
        temp1lds.setLabel("humidity1");
        temp1ilds.clear();
        temp1ilds.add(temp1lds);
        temp1ld=new LineData(temp1ilds);
        mainhum1.clear();
        mainhum1.setData(temp1ld);
        mainhum1.invalidate();

        mainhum1.setNoDataText("Data not Available");

        XAxis xaxis=mainhum1.getXAxis();
        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setDrawLabels(true);
        xaxis.setAxisMinimum(temp1ld.getXMin() - 100f);
        xaxis.setAxisMaximum(temp1ld.getXMax() + 100f);
        //xaxis.setTypeface(mTfLight);
        xaxis.setTextSize(10f);
        xaxis.setTextColor(Color.WHITE);
        xaxis.setDrawAxisLine(false);
        xaxis.setDrawGridLines(true);
        xaxis.setTextColor(Color.rgb(255, 192, 56));
        xaxis.setCenterAxisLabels(true);
        xaxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM HH:mm", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value) {
                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = mainhum1.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
//        leftAxis.setAxisMinimum(0f);
//        leftAxis.setAxisMaximum(170f);
//        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));

        YAxis rightAxis = mainhum1.getAxisRight();
        rightAxis.setEnabled(false);

        temp1lds.setCircleColor(Color.GREEN);
        temp1lds.setDrawCircles(true);
        temp1lds.setDrawCircleHole(true);
        temp1lds.setLineWidth(5);
        temp1lds.setCircleRadius(10);
        temp1lds.setCircleHoleRadius(10);
        temp1lds.setValueTextSize(10);
        temp1lds.setValueTextColor(Color.WHITE);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.tempmain:
                startActivity(new Intent(Humidity.this, MainActivity.class));
                break;
            case R.id.hummain:
                startActivity(new Intent(Humidity.this, Humidity.class));
                break;
        }
        return true;
    }

}