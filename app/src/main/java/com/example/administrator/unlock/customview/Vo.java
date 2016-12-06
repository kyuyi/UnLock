package com.example.administrator.unlock.customview;

/**
 * Created by KyuYi on 2016/11/18.
 * E-Mail:kyu_yi@sina.com
 */

public class Vo {
    float x;
    float y;
    int pw;
    int status = 0;//大圆还是小圆


    public Vo(float x, float y, int pw, int status) {
        this.x = x;
        this.y = y;
        this.pw = pw;
        this.status = status;
    }
}
