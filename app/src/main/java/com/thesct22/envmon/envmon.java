package com.thesct22.envmon;

import android.app.Application;

public class envmon extends Application {

    private static boolean sensoractive[]=new boolean[10];

    public static boolean[] getSomeVariable() {
        return sensoractive;
    }

    public static void setSomeVariable(boolean someVariable[]) {
//        for (int i = 0; i < someVariable.length; i++)
//            sensoractive[i] = someVariable[i];
        sensoractive=someVariable;
    }
}
