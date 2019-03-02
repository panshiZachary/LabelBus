package com.adyun.labelbus;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.adyun.labelbus.bus.PSBus;
import com.adyun.labelbus.bus.Subscribe;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PSBus.getDefault().register(this);
        PSBus.getDefault().post("1","2","3");
    }

    @Subscribe("1")
    public void test(String msg1,String msg2){
        Log.e(TAG, "test: "+msg1+" "+msg2 );

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        PSBus.getDefault().unregister(this);
    }
}
