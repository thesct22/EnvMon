package com.thesct22.envmon;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YourMarkerView extends MarkerView {

    private TextView tvContent;

    public YourMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        // find your layout components
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
// content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        DateFormat simple = new SimpleDateFormat("dd/MMM/yyyy  HH:mm:ss");

        Date result = new Date((long) e.getX());

        tvContent.setText("  " + e.getY() + ",  " + simple.format(result)+ "  ");

        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -(getHeight()/2));
        }

        return mOffset;
    }}