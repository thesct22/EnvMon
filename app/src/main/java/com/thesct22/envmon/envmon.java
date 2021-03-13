package com.thesct22.envmon;

import android.app.Application;
import android.graphics.Color;

import java.util.ArrayList;

public class envmon extends Application {

    private static boolean sensoractive[]=new boolean[10];
    private static ArrayList<Integer> lineColors=new ArrayList<>();
    private static ArrayList<String>sensornames=new ArrayList<>();
    envmon(){
        super();
        lineColors.add(0xFF009933);
        lineColors.add(0xFF003366);
        lineColors.add(0xFF663300);
        lineColors.add(0xFF99ff33);
        lineColors.add(0xFF3399ff);
        lineColors.add(0xFFffff00);
        lineColors.add(0xFF00cc66);
        lineColors.add(0xFF9900ff);
        lineColors.add(0xFFff6699);
        lineColors.add(0xFF00ccff);

    }
    public static ArrayList<Integer> getcolor(){return lineColors;}
    public static boolean[] getSomeVariable() {
        return sensoractive;
    }

    public static void setSomeVariable(boolean someVariable[]) {
//        for (int i = 0; i < someVariable.length; i++)
//            sensoractive[i] = someVariable[i];
        sensoractive=someVariable;
    }
    public static ArrayList<String> getnames(){return sensornames;}
    public static void setnames(ArrayList<String> names){
        sensornames=(ArrayList<String>)names.clone();
    }
}
