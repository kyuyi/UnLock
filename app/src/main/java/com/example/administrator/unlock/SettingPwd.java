package com.example.administrator.unlock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.unlock.customview.CustomMainAppLock;

/**
 * Created by Administrator on 2016/12/6.
 */

public class SettingPwd extends Activity {
    private CustomMainAppLock lock;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        lock = (CustomMainAppLock) findViewById(R.id.lock);
        tv = (TextView) findViewById(R.id.tv);
        lock.setAppLockSettingListence(new CustomMainAppLock.AppLockSettingListence() {
            @Override
            public void passwordIsNotSame(String firstPass, String secondPass) {
                tv.setText("两次密码不一致");
            }

            @Override
            public void passSetAgain() {
                tv.setText("请再次绘制密码");
            }

            @Override
            public void passSettingSuccess(String pass) {
                tv.setText("密码设置成功！密码是：" + pass);
            }

            @Override
            public void passPointLess(int lessPoint) {
                Toast.makeText(SettingPwd.this, "请至少连接" + lessPoint + "个密码点", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
