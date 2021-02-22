package com.thesct22.envmon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

public class CheckboxActivity extends AppCompatActivity {

    CheckBox chk1,chk2,chk3,chk4,chk5,chk6,chk7,chk8,chk9,chk10;
    boolean sensorArray[];
    Button btn;
    List <CheckBox> chklist;
    envmon em;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkbox);
        em=new envmon();
        sensorArray=em.getSomeVariable();
        chklist= new ArrayList<CheckBox>();
        chklist.add((CheckBox)findViewById(R.id.sensorcb1));
        chklist.add((CheckBox)findViewById(R.id.sensorcb2));
        chklist.add((CheckBox)findViewById(R.id.sensorcb3));
        chklist.add((CheckBox)findViewById(R.id.sensorcb4));
        chklist.add((CheckBox)findViewById(R.id.sensorcb5));
        chklist.add((CheckBox)findViewById(R.id.sensorcb6));
        chklist.add((CheckBox)findViewById(R.id.sensorcb7));
        chklist.add((CheckBox)findViewById(R.id.sensorcb8));
        chklist.add((CheckBox)findViewById(R.id.sensorcb9));
        chklist.add((CheckBox)findViewById(R.id.sensorcb10));
        boolean allchk=true;
        for (int i=0;i<10;i++){
            allchk=allchk&&sensorArray[i];
            if(sensorArray[i]){
                chklist.get(i).setChecked(true);
            }
            else{
                chklist.get(i).setChecked(false);
            }
        }
        btn =(Button)findViewById(R.id.button1);
        if(allchk)
            btn.setText("Unselect All");
        else
            btn.setText("Select All");

    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.sensorcb1:
                if (checked) sensorArray[0]=true;
                else sensorArray[0]=false;
                break;
            case R.id.sensorcb2:
                if (checked) sensorArray[1]=true;
                else sensorArray[1]=false;
                break;
            case R.id.sensorcb3:
                if (checked) sensorArray[2]=true;
                else sensorArray[2]=false;
                break;
            case R.id.sensorcb4:
                if (checked) sensorArray[3]=true;
                else sensorArray[3]=false;
                break;
            case R.id.sensorcb5:
                if (checked) sensorArray[4]=true;
                else sensorArray[4]=false;
                break;
            case R.id.sensorcb6:
                if (checked) sensorArray[5]=true;
                else sensorArray[5]=false;
                break;
            case R.id.sensorcb7:
                if (checked) sensorArray[6]=true;
                else sensorArray[6]=false;
                break;
            case R.id.sensorcb8:
                if (checked) sensorArray[7]=true;
                else sensorArray[7]=false;
                break;
            case R.id.sensorcb9:
                if (checked) sensorArray[8]=true;
                else sensorArray[8]=false;
                break;
            case R.id.sensorcb10:
                if (checked) sensorArray[9]=true;
                else sensorArray[9]=false;
                break;
        }
        boolean allchk=true;
        for (int i=0;i<10;i++) {
            allchk = allchk && sensorArray[i];
        }
        if(allchk)
            btn.setText("Unselect All");
        else
            btn.setText("Select All");
        em.setSomeVariable(sensorArray);
    }

    public void onButtonClicked(View view) {
        boolean allchk=true;
        for (int i=0;i<10;i++) {
            allchk = allchk && sensorArray[i];
        }
        if(allchk){
            for (int i=0;i<10;i++) {
                sensorArray[i]=false;
                chklist.get(i).setChecked(false);
            }
        }
        else{
            for (int i=1;i<=10;i++) {
                sensorArray[i] = true;
                chklist.get(i).setChecked(true);
            }
        }
        allchk=true;
        for (int i=0;i<10;i++) {
            allchk = allchk && sensorArray[i];
        }
        if(allchk)
            btn.setText("Unselect All");
        else
            btn.setText("Select All");
        em.setSomeVariable(sensorArray);
    }
}