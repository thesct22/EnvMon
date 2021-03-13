package com.thesct22.envmon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    LineChart maintemp1;
    private DatabaseReference mdb;

    ArrayList<ILineDataSet> temp1ilds = new ArrayList<>();
    LineData temp1ld;
    private static final String TAG = "MainActivity";
    DrawerLayout dl;
    NavigationView nv;
    Toolbar tb;
    FloatingActionButton fab_one;
    envmon en;
    ArrayList<Integer> colours;
    SwitchCompat sw;

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


        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fab_one = findViewById(R.id.fabtemp);

            fab_one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent prof_intent = new Intent(getApplicationContext(),CheckboxActivity.class);

                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View,String>(fab_one,"activity_trans");


                    ActivityOptions options =ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, pairs);
                    startActivity(prof_intent,options.toBundle());
                }
            });
        }


        maintemp1 = findViewById(R.id.maintemp1);
        maintemp1.setTouchEnabled(true);

        // enable scaling and dragging
        maintemp1.setDragEnabled(true);
        maintemp1.setScaleEnabled(true);
        maintemp1.setScaleXEnabled(true);
        maintemp1.setScaleYEnabled(true);

        maintemp1.setPinchZoom(true);
        maintemp1.fitScreen();
        MarkerView mv = new YourMarkerView(this, R.layout.tvcontent);
        mv.setChartView(maintemp1); // For bounds control
        maintemp1.setMarker(mv);

        mdb = FirebaseDatabase.getInstance().getReference().child("temp");
        retrievedata();
        nv.setNavigationItemSelectedListener(this);

        MenuItem mitem=nv.getMenu().findItem(R.id.nav_item1);

        sw=(SwitchCompat) mitem.getActionView();
        int nightModeFlags =
                tb.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                sw.setChecked(true);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                sw.setChecked(false);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                sw.setChecked(false);
                break;
        }
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        MenuItem mitem=nv.getMenu().findItem(R.id.nav_item1);
        sw=(SwitchCompat) mitem.getActionView();
        int nightModeFlags =
                nv.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                sw.setChecked(true);
                break;

            case Configuration.UI_MODE_NIGHT_NO:
                sw.setChecked(false);
                break;

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                sw.setChecked(false);
                break;
        }
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
                else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
        super.onResume();
        retrievedata();
    }
    @Override
    public void onPause() {

        sw.setOnCheckedChangeListener(null);
        super.onPause();
    }
    @Override
    public void onStop() {

        sw.setOnCheckedChangeListener(null);
        super.onStop();
    }

    private void retrievedata(){
        // Read from the database
        mdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<ArrayList<Entry>>datavals= new ArrayList<ArrayList<Entry>>();
                ArrayList<String>labels=new ArrayList<>();

                if(dataSnapshot.hasChildren()){

                    for(DataSnapshot mydss:dataSnapshot.getChildren()){
                        ArrayList<Entry> midOne= new ArrayList<>();
                        labels.add(mydss.getKey());
                        for(DataSnapshot mydsscld:mydss.getChildren()) {
                            String tsstr = mydsscld.getKey();
                            long ts = Long.parseLong(tsstr);
                            Long templong = mydsscld.getValue(Long.class);
                            int tempint = templong.intValue();

                            midOne.add(new Entry((long) ts, tempint));
                        }
                        datavals.add(midOne);
                    }
                    en.setnames(labels);
                    showchart(datavals,labels);                }
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
    private void showchart(ArrayList<ArrayList<Entry>> showvals,ArrayList<String> labels){
        temp1ilds.clear();
        en=new envmon();
        colours=en.getcolor();
        boolean chkarr[]=en.getSomeVariable();
        for(int i=0;i<showvals.size();i++) {
            if(chkarr[i]) {
                LineDataSet temp1lds = new LineDataSet(showvals.get(i), labels.get(i));
                temp1lds.setColor(colours.get(i).intValue());
                temp1lds.setCircleColor(Color.RED);
                temp1lds.setDrawCircles(true);
                temp1lds.setDrawCircleHole(true);
                temp1lds.setLineWidth(2);
                temp1lds.setCircleRadius(4);
                temp1lds.setCircleHoleRadius(4);
                temp1lds.setValueTextSize(0);
                //temp1lds.setValueTextColor(Color.rgb(255, 192, 56));
                temp1ilds.add(temp1lds);
            }
        }

        temp1ld=new LineData(temp1ilds);
        maintemp1.clear();
        maintemp1.setData(temp1ld);
        maintemp1.invalidate();
        maintemp1.setDescription(null);
        maintemp1.setNoDataText("Data not Available");
        maintemp1.getLegend().setWordWrapEnabled(true);

        XAxis xaxis=maintemp1.getXAxis();
        //xaxis.setLabelRotationAngle(-30f);

        xaxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xaxis.setDrawLabels(true);
        xaxis.setAxisMinimum(temp1ld.getXMin() - 100f);
        xaxis.setAxisMaximum(temp1ld.getXMax() + 100f);
        //xaxis.setTypeface(mTfLight);
        xaxis.setTextSize(10f);
        xaxis.setTextColor(Color.WHITE);
        xaxis.setDrawAxisLine(false);
        xaxis.setDrawGridLines(true);
        xaxis.setTextColor(Color.RED);
        xaxis.setCenterAxisLabels(true);
        xaxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value) {
                long millis = TimeUnit.HOURS.toMillis((long) value);
                return mFormat.format(new Date(millis));
            }
        });

        YAxis leftAxis = maintemp1.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
//        leftAxis.setAxisMinimum(0f);
//        leftAxis.setAxisMaximum(170f);
//        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.RED);

        YAxis rightAxis = maintemp1.getAxisRight();
        rightAxis.setEnabled(false);

        maintemp1.getLegend().setTextColor(Color.RED);

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
            case R.id.graph_editor:
                startActivity(new Intent(MainActivity.this, EditGraphActivity.class));
//            case R.id.nav_item1:
//                setdarkmode(item);
//                break;
        }
        return true;
    }




//    public void setdarkmode(MenuItem item){
//        Switch sw= (Switch) item.getActionView();
//        int nightModeFlags =
//                tb.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
//        switch (nightModeFlags) {
//            case Configuration.UI_MODE_NIGHT_YES:
//                sw.setChecked(true);
//                break;
//
//            case Configuration.UI_MODE_NIGHT_NO:
//                sw.setChecked(false);
//                break;
//
//            case Configuration.UI_MODE_NIGHT_UNDEFINED:
//                sw.setChecked(false);
//                break;
//        }
//        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                // do something, the isChecked will be
//                // true if the switch is in the On position
//                if(isChecked){
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                }
//                else{
//                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                }
//            }
//        });
//    }
}
