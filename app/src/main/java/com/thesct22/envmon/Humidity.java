package com.thesct22.envmon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_humidity2);
        mainhum1 = findViewById(R.id.mainhum1);
        mainhum1.setTouchEnabled(true);
        mainhum1.setPinchZoom(true);

        dl = findViewById(R.id.drawer_hum);
        nv = findViewById(R.id.nav_hummain);
        tb = findViewById(R.id.toolbarhum);

        setSupportActionBar(tb);

        nv.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl, tb, R.string.nd_open, R.string.nd_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();


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
        temp1lds.setLabel("temp1");
        temp1ilds.clear();
        temp1ilds.add(temp1lds);
        temp1ld=new LineData(temp1ilds);
        mainhum1.clear();
        mainhum1.setData(temp1ld);
        mainhum1.invalidate();

        mainhum1.setNoDataText("Data not Available");

        //you can modify your line chart graph according to your requirement there are lots of method available in this library
        //now customize line chart

        temp1lds.setColor(Color.BLUE);
        temp1lds.setCircleColor(Color.GREEN);
        temp1lds.setDrawCircles(true);
        temp1lds.setDrawCircleHole(true);
        temp1lds.setLineWidth(5);
        temp1lds.setCircleRadius(10);
        temp1lds.setCircleHoleRadius(10);
        temp1lds.setValueTextSize(10);
        temp1lds.setValueTextColor(Color.WHITE);


    }
    public void sendMessage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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