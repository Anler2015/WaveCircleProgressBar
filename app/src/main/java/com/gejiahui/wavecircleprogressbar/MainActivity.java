package com.gejiahui.wavecircleprogressbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.gejiahui.immersecircleprogressbar.R;

public class MainActivity extends Activity {

    WaveCircleProgressBar ib;
    Button btn ;
    int i = 0;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            i++;
            ib.setProgress(i);
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
        ib.setProgress(0);
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
