package com.thesct22.envmon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    LineChart maintemp1;
    private DatabaseReference mdb;
    ArrayList<Entry> temp1data;
    LineDataSet temp1lds= new LineDataSet(null, null);
    ArrayList<ILineDataSet> temp1ilds = new ArrayList<>();
    LineData temp1ld;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        maintemp1 = findViewById(R.id.maintemp1);
        maintemp1.setTouchEnabled(true);
        maintemp1.setPinchZoom(true);
//        LineDataSet temp1lds= new LineDataSet(temp1data, "temp1");
//        ArrayList<ILineDataSet> temp1ilds = new ArrayList<>();
//        temp1ilds.add(temp1lds);

        mdb = FirebaseDatabase.getInstance().getReference().child("temp").child("temp1");

        retrievedata();
//        LineData lineData = new LineData(temp1ilds);
//        maintemp1.setData(lineData);
//        maintemp1.invalidate();
        //if you want set background color use below method
        //lineChart.setBackgroundColor(Color.RED);

        // set text if data are are not available
        //maintemp1.setNoDataText("Data not Available");

        //you can modify your line chart graph according to your requirement there are lots of method available in this library

        //now customize line chart

//        temp1lds.setColor(Color.BLUE);
//        temp1lds.setCircleColor(Color.GREEN);
//        temp1lds.setDrawCircles(true);
//        temp1lds.setDrawCircleHole(true);
//        temp1lds.setLineWidth(5);
//        temp1lds.setCircleRadius(10);
//        temp1lds.setCircleHoleRadius(10);
//        temp1lds.setValueTextSize(10);
//        temp1lds.setValueTextColor(Color.BLACK);

    }

    private void retrievedata(){
        // Read from the database
        mdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Entry>datavals= new ArrayList<>();
                if(dataSnapshot.hasChildren()){

                    for(DataSnapshot mydss:dataSnapshot.getChildren()){
                        //tempclass tempclass1=mydss.getValue(tempclass.class);
                        //Context context = getApplicationContext();
                        String tsstr = mydss.getKey();
                        long ts=Long.parseLong(tsstr);
                        Long templong=mydss.getValue(Long.class);
                        int tempint=templong.intValue();
                        //int duration = Toast.LENGTH_LONG;

                        //Toast toast = Toast.makeText(context, meh.toString(), duration);
                        //toast.show();
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
                CharSequence text = "hello";
                int duration = Toast.LENGTH_LONG;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }
    private void showchart(ArrayList<Entry> showvals){

        for(Entry ee: showvals){
            double qwe=ee.getX();
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_LONG;

            Toast toast = Toast.makeText(context, String.valueOf(qwe), duration);
            toast.show();

        }

        temp1lds.setValues(showvals);
        temp1lds.setLabel("temp1");
        temp1ilds.clear();
        temp1ilds.add(temp1lds);
        temp1ld=new LineData(temp1ilds);
        maintemp1.clear();
        maintemp1.setData(temp1ld);
        maintemp1.invalidate();


        //maintemp1.setNoDataText("Data not Available");

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



}