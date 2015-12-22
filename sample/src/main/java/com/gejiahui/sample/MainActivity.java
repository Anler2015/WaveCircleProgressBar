package com.gejiahui.sample;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.gejiahui.immersecircleprogressbar.R;
import com.gejiahui.wavecircleprogressbar.WaveCircleProgressBar;

public class MainActivity extends Activity {

    WaveCircleProgressBar ib;
    WaveCircleProgressBar ib1;
    WaveCircleProgressBar ib2;
    WaveCircleProgressBar ib3;
    WaveCircleProgressBar ib4;
    WaveCircleProgressBar ib5;
    WaveCircleProgressBar ib6;
    WaveCircleProgressBar ib7;


    Button btn ;
    int i = 0;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            i++;
            ib.setProgress(i);
            ib1.setProgress(i);
            ib2.setProgress(i);
            ib3.setProgress(i);
            ib4.setProgress(i);
            ib5.setProgress(i);
            ib6.setProgress(i);
            ib7.setProgress(i);


            if(i<100){
                handler.sendEmptyMessageDelayed(0,200);
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        ib = (WaveCircleProgressBar)findViewById(R.id.ib);
        ib1 = (WaveCircleProgressBar)findViewById(R.id.ib1);
        ib2 = (WaveCircleProgressBar)findViewById(R.id.ib2);
        ib3 = (WaveCircleProgressBar)findViewById(R.id.ib3);
        ib4 = (WaveCircleProgressBar)findViewById(R.id.ib4);
        ib5 = (WaveCircleProgressBar)findViewById(R.id.ib5);
        ib6 = (WaveCircleProgressBar)findViewById(R.id.ib6);
        ib7 = (WaveCircleProgressBar)findViewById(R.id.ib7);

        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 0;
                handler.sendEmptyMessage(0);
            }
        });
    }


}
