package com.thesct22.envmon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

public class CheckboxActivity extends AppCompatActivity {

    CheckBox chk1,chk2,chk3,chk4,chk5,chk6,chk7,chk8,chk9,chk10;
    boolean sensorArray[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox);
        envmon em=new envmon();
        sensorArray=em.getSomeVariable();
        chk1=(CheckBox)findViewById(R.id.sensorcb1);
        chk2=(CheckBox)findViewById(R.id.sensorcb2);
        chk3=(CheckBox)findViewById(R.id.sensorcb3);
        chk4=(CheckBox)findViewById(R.id.sensorcb4);
        chk5=(CheckBox)findViewById(R.id.sensorcb5);
        chk6=(CheckBox)findViewById(R.id.sensorcb6);
        chk7=(CheckBox)findViewById(R.id.sensorcb7);
        chk8=(CheckBox)findViewById(R.id.sensorcb8);
        chk9=(CheckBox)findViewById(R.id.sensorcb9);
        chk10=(CheckBox)findViewById(R.id.sensorcb10);

    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.sensorcb1:
                if (checked) sensorArray[1]=true;
                else sensorArray[1]=false;
            case R.id.sensorcb2:
                if (checked) sensorArray[2]=true;
                else sensorArray[2]=false;
            case R.id.sensorcb3:
                if (checked) sensorArray[3]=true;
                else sensorArray[3]=false;
            case R.id.sensorcb4:
                if (checked) sensorArray[4]=true;
                else sensorArray[4]=false;
            case R.id.sensorcb5:
                if (checked) sensorArray[5]=true;
                else sensorArray[5]=false;
            case R.id.sensorcb6:
                if (checked) sensorArray[6]=true;
                else sensorArray[6]=false;
            case R.id.sensorcb7:
                if (checked) sensorArray[7]=true;
                else sensorArray[7]=false;
            case R.id.sensorcb8:
                if (checked) sensorArray[8]=true;
                else sensorArray[8]=false;
            case R.id.sensorcb9:
                if (checked) sensorArray[9]=true;
                else sensorArray[9]=false;
            case R.id.sensorcb10:
                if (checked) sensorArray[10]=true;
                else sensorArray[10]=false;
        }
    }
}