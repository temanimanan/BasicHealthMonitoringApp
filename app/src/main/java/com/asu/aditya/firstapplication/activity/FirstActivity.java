package com.asu.aditya.firstapplication.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.asu.aditya.firstapplication.R;
import com.asu.aditya.firstapplication.views.GraphView;

public class FirstActivity extends Activity implements View.OnClickListener {
    /**
     * Variable Array for GraphView
     * verlabel : Background Height Values
     * horlabel : Background Width Values
     * values : Max Values of Foreground Active Graph
     */
    private float[] values = new float[60];
    private String[] verticalLabels = new String[]{"600", "500", "400", "300", "200", "100", "80", "60", "40", "20", "0",};
    private String[] horizontalLabels = new String[]{"0", "10", "20", "30", "40", "50", "60"};
    private GraphView graphView;
    private LinearLayout graph;
    private boolean runnable = false;
    private Button btnStartGraph, btnStopGraph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        graph = (LinearLayout) findViewById(R.id.graph);
        btnStartGraph = (Button) findViewById(R.id.start_graph);
        btnStopGraph = (Button) findViewById(R.id.stop_graph);

        graphView = new GraphView(FirstActivity.this, values, "TEST GRAPH", horizontalLabels, verticalLabels, GraphView.LINE);
        btnStartGraph.setOnClickListener(this);
        btnStopGraph.setOnClickListener(this);
        btnStartGraph.setEnabled(true);
        btnStopGraph.setEnabled(false);
        graph.addView(graphView);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        runnable = false;
    }

    public void setGraph(int data) {
        for (int i = 0; i < values.length - 1; i++) {
            values[i] = values[i + 1];
        }

        values[values.length - 1] = (float) data;
        graphView.invalidate();
//        graph.removeView(graphView);
//        graph.addView(graphView);
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case 0x01:
                    int testValue = (int) (Math.random() * 600) + 1;
                    setGraph(testValue);
                    break;
            }
        }
    };

    public class RunGraph extends Thread {
        @Override
        public void run() {
            while (runnable) {
                handler.sendEmptyMessage(0x01);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_graph:
                runnable = true;
                new RunGraph().start();
//                startDraw.start();
                btnStartGraph.setEnabled(false);
                btnStopGraph.setEnabled(true);
                break;
            case R.id.stop_graph:
                runnable = false;
                btnStartGraph.setEnabled(true);
                btnStopGraph.setEnabled(false);
        }
    }
}