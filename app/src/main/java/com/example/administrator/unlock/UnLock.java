package com.example.administrator.unlock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.administrator.unlock.customview.CustomMainAppLock;

/**
 * Created by Administrator on 2016/12/6.
 */

public class UnLock extends Activity {
    private CustomMainAppLock lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlock);
        lock = (CustomMainAppLock) findViewById(R.id.lock);
        lock.setAppLockUnlockListence("123456", new CustomMainAppLock.AppLockUnlockListence() {
            @Override
            public void unlockFaile(int count) {
                Toast.makeText(UnLock.this, "密码错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unlockSuccess() {
                Toast.makeText(UnLock.this, "解锁成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
