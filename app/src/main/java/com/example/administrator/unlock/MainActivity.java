package com.example.administrator.unlock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intent = new Intent();
    }

    public void click(View v) {
        intent.setClass(this, UnLock.class);
        startActivity(intent);
    }


    public void click2(View v) {
        intent.setClass(this, SettingPwd.class);
        startActivity(intent);
    }
}
