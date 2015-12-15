package com.gejiahui.immersecircleprogressbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

public class MainActivity extends Activity {

    ImmerseCircleProgressBar ib;
    Button btn ;
    int i = 0;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            i++;
            ib.setProgress(i);
            if(i<100){
                handler.sendEmptyMessageDelayed(0,100);
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ib = (ImmerseCircleProgressBar)findViewById(R.id.ib);
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
