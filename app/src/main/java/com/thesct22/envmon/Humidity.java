package com.thesct22.envmon;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Humidity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    LineChart mainhum1;
    private DatabaseReference mdb;
    ArrayList<ILineDataSet> temp1ilds = new ArrayList<>();
    LineData temp1ld;
    private static final String TAG = "MainHumidity";
    DrawerLayout dl;
    NavigationView nv;
    Toolbar tb;
    FloatingActionButton fab_one,fab_settings,fab_graph;
    envmon en;
    ArrayList<Integer> colours;
    SwitchCompat sw;
    FirebaseFirestore fstore;
    Map<String,Object> userInfo;
    boolean isRotate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity2);
        mainhum1 = findViewById(R.id.mainhum1);
        mainhum1.setTouchEnabled(true);
        mainhum1.setPinchZoom(true);
        mainhum1.fitScreen();
        mainhum1.setScaleMinima(10f, 1f);

        dl = findViewById(R.id.drawer_hum);
        nv = findViewById(R.id.nav_hummain);
        tb = findViewById(R.id.toolbarhum);

        setSupportActionBar(tb);

        nv.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, tb, R.string.nd_open, R.string.nd_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        fstore= FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        DocumentReference df=fstore.collection("Users").document(user.getUid());
        df.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                assert document != null;
                if (document.exists()) {
                    userInfo =document.getData();
                    assert userInfo != null;
                    if((Boolean)userInfo.get("isAdmin")){
                        Menu nav_Menu = nv.getMenu();
                        nav_Menu.findItem(R.id.adminpanel).setVisible(true);
                    }
                    else{
                        Menu nav_Menu = nv.getMenu();
                        nav_Menu.findItem(R.id.adminpanel).setVisible(false);
                    }

                }
            }
        });

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            fab_settings = findViewById(R.id.graphSettingshum);
            fab_one = findViewById(R.id.fabhum);
            fab_graph = findViewById(R.id.graphParametershum);
            ViewAnimation.init(fab_graph);
            ViewAnimation.init(fab_one);

            fab_settings.setOnClickListener(v -> {
                isRotate = ViewAnimation.rotateFab(v, !isRotate);
                if (isRotate) {
                    ViewAnimation.showIn(fab_graph);
                    ViewAnimation.showIn(fab_one);
                } else {
                    ViewAnimation.showOut(fab_graph);
                    ViewAnimation.showOut(fab_one);
                }
            });
            fab_one.setOnClickListener(v -> {
                Intent prof_intent = new Intent(getApplicationContext(),CheckboxActivity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(fab_one,"activity_trans");


                ActivityOptions options =ActivityOptions.makeSceneTransitionAnimation(Humidity.this, pairs);
                startActivity(prof_intent,options.toBundle());
            });

            fab_graph.setOnClickListener(v -> {
                Intent prof_intent = new Intent(getApplicationContext(),EditGraphActivity.class);

                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View,String>(fab_one,"activity_trans");


                ActivityOptions options =ActivityOptions.makeSceneTransitionAnimation(Humidity.this, pairs);
                startActivity(prof_intent,options.toBundle());
            });
        }

        mainhum1 = findViewById(R.id.mainhum1);
        mainhum1.setTouchEnabled(true);


        // enable scaling and dragging
        mainhum1.setDragEnabled(true);
        mainhum1.setScaleEnabled(true);
        mainhum1.setScaleXEnabled(true);
        mainhum1.setScaleYEnabled(true);


        mainhum1.setPinchZoom(true);
        mainhum1.fitScreen();
        MarkerView mv = new YourMarkerView(this, R.layout.tvcontent);
        mv.setChartView(mainhum1); // For bounds control
        mainhum1.setMarker(mv);


        mdb = FirebaseDatabase.getInstance().getReference().child("hum");

        retrievedata();
        nv.setNavigationItemSelectedListener(this);

        MenuItem mitem=nv.getMenu().findItem(R.id.nav_item1);

        sw=(SwitchCompat) mitem.getActionView();
        int nightModeFlags =
                nv.getContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_YES:
                sw.setChecked(true);
                break;

            case Configuration.UI_MODE_NIGHT_NO:

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                sw.setChecked(false);
                break;
        }
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // do something, the isChecked will be
            // true if the switch is in the On position
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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

            case Configuration.UI_MODE_NIGHT_UNDEFINED:
                sw.setChecked(false);
                break;
        }
        sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // do something, the isChecked will be
            // true if the switch is in the On position
            if(isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
            else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
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
        boolean[] radioselect= envmon.getradio();
        // Read from the database
        mdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<ArrayList<Entry>>datavals= new ArrayList<>();
                ArrayList<String>labels=new ArrayList<>();
                if(dataSnapshot.hasChildren()){

                    for(DataSnapshot mydss:dataSnapshot.getChildren()){
                        ArrayList<Entry> midOne= new ArrayList<>();
                        labels.add(mydss.getKey());
                        for(DataSnapshot mydsscld:mydss.getChildren()) {
                            String tsstr = mydsscld.getKey();
                            assert tsstr != null;
                            long ts = Long.parseLong(tsstr);
                            Long templong = mydsscld.getValue(Long.class);
                            assert templong != null;
                            int tempint = templong.intValue();

                            if((radioselect[1]||radioselect[3])&&(envmon.getFromDataMillis()<=ts&& envmon.getToDataMillis()>=ts)) {
                                midOne.add(new Entry(ts, tempint));
//                                Log.i(TAG, Long.toString(ts));
                            }
                            else if((!radioselect[1])&&(!radioselect[3])) {
                                midOne.add(new Entry(ts, tempint));
//                                Log.i(TAG, Long.toString(ts));

                            }
                        }
                        if(radioselect[2]) {
                            midOne = new ArrayList<>(midOne.subList(Math.max(midOne.size() - (int) envmon.getFromDataMillis(), 0), midOne.size()));
//                            Toast.makeText(getApplicationContext(), Long.toString(en.getFromDataMillis()), Toast.LENGTH_SHORT).show();
                        }
                        datavals.add(midOne);
                    }
                    envmon.setnames(labels);
                    showchart(datavals,labels);
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
    private void showchart(ArrayList<ArrayList<Entry>> showvals,ArrayList<String> labels){
        temp1ilds.clear();
        en=new envmon();
        colours= envmon.getcolor();
        boolean[] chkarr = envmon.getSomeVariable();
        for(int i=0;i<showvals.size();i++) {
            if(chkarr[i]) {
                LineDataSet temp1lds = new LineDataSet(showvals.get(i), labels.get(i));
                temp1lds.setColor(colours.get(i));
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
        mainhum1.clear();
        mainhum1.setData(temp1ld);
        mainhum1.invalidate();
        mainhum1.setNoDataText("Data not Available");
        mainhum1.getLegend().setWordWrapEnabled(true);

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

        YAxis leftAxis = mainhum1.getAxisLeft();
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        //leftAxis.setTypeface(mTfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);
//        leftAxis.setAxisMinimum(0f);
//        leftAxis.setAxisMaximum(170f);
//        leftAxis.setYOffset(-9f);
        leftAxis.setTextColor(Color.RED);

        YAxis rightAxis = mainhum1.getAxisRight();
        rightAxis.setEnabled(false);

        mainhum1.getLegend().setTextColor(Color.RED);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.signup:
                startActivity(new Intent(Humidity.this, Register.class));
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Humidity.this, Login.class));
                break;
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