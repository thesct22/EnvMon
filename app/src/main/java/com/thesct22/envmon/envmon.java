package com.thesct22.envmon;

import android.app.Application;

public class envmon extends Application {

    private boolean sensoractive[]=new boolean[10];

    public boolean[] getSomeVariable() {
        return sensoractive;
    }

    public void setSomeVariable(boolean someVariable[]) {
        for (int i = 0; i < someVariable.length; i++)
            sensoractive[i] = someVariable[i];
    }
}
