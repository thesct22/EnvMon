package com.thesct22.envmon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;



import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
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



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    LineChart maintemp1;
    private DatabaseReference mdb;
    ArrayList<Entry> temp1data;
    LineDataSet temp1lds= new LineDataSet(null, null);
    ArrayList<ILineDataSet> temp1ilds = new ArrayList<>();
    LineData temp1ld;
    private static final String TAG = "MainActivity";
    DrawerLayout dl;
    NavigationView nv;
    Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dl = findViewById(R.id.drawer_temp);
        nv = findViewById(R.id.nav_tempmain);
        tb = findViewById(R.id.toolbartemp);

        setSupportActionBar(tb);

        nv.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, tb, R.string.nd_open, R.string.nd_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();

        maintemp1 = findViewById(R.id.maintemp1);
        maintemp1.setTouchEnabled(true);


        // enable scaling and dragging
        maintemp1.setDragEnabled(true);
        maintemp1.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);


        maintemp1.setPinchZoom(true);
        maintemp1.fitScreen();
        MarkerView mv = new YourMarkerView(this, R.layout.tvcontent);
        mv.setChartView(maintemp1); // For bounds control
        maintemp1.setMarker(mv);

        mdb = FirebaseDatabase.getInstance().getReference().child("temp").child("temp1");
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
                    maintemp1.clear();
                    maintemp1.invalidate();
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
        temp1lds.setLabel("temp1");
        temp1ilds.clear();
        temp1ilds.add(temp1lds);
        temp1ld=new LineData(temp1ilds);
        maintemp1.clear();
        maintemp1.setData(temp1ld);
        maintemp1.invalidate();
        maintemp1.setDescription(null);
        maintemp1.setNoDataText("Data not Available");


        XAxis xaxis=maintemp1.getXAxis();
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

        YAxis leftAxis = maintemp1.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
//        leftAxis.setAxisMinimum(0f);
//        leftAxis.setAxisMaximum(170f);
//        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.rgb(255, 192, 56));

        YAxis rightAxis = maintemp1.getAxisRight();
        rightAxis.setEnabled(false);


        temp1lds.setCircleColor(Color.GREEN);

        temp1lds.setDrawCircles(true);
        temp1lds.setDrawCircleHole(true);
        temp1lds.setLineWidth(5);
        temp1lds.setCircleRadius(10);
        temp1lds.setCircleHoleRadius(10);
        temp1lds.setValueTextSize(10);
        temp1lds.setValueTextColor(Color.BLACK);


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.tempmain:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                break;
            case R.id.hummain:
                startActivity(new Intent(MainActivity.this, Humidity.class));
                break;
        }
        return true;
    }
}
