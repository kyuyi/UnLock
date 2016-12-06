package com.example.administrator.unlock.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.administrator.unlock.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 九宫格图案锁
 * Created by KyuYi on 2016/11/17.
 * E-Mail:kyu_yi@sina.com
 */

public class CustomMainAppLock extends View {
    Paint mPaint;
    int baseX = 0, baseY = 0, x = 0, y = 0;
    List<Vo> listVo;
    List<Vo> pressVo;
    List<LineVo> mLineVo;
    private int lessPoint = 3; //最少连接多少个密码点
    StringBuffer pw;
    boolean isFrist = false;
    @SuppressWarnings("deprecation")
    private int non_circle_color = getResources().getColor(R.color.tras_dark);  //默认圆圈的颜色
    private float non_circle_size = 7f; //默认圆的大小
    private float non_circle_width = 2f; //默认圆边缘的宽度
    private float touch_big_circle_size = 20f;
    private float touch_center_circle_size = 18f;
    @SuppressWarnings("deprecation")
    private int touch_small_circle_color = getResources().getColor(R.color.light_blue);
    @SuppressWarnings("deprecation")
    private int touch_center_circle_color = getResources().getColor(R.color.tras_blue);

    @SuppressWarnings("deprecation")
    private int error_small_circle_color = getResources().getColor(R.color.light_red);
    @SuppressWarnings("deprecation")
    private int error_center_circle_color = getResources().getColor(R.color.tras_red);
    private int max_try_num = 5;

    private int user_modle;

    public CustomMainAppLock(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        listVo = new ArrayList<>();
        pressVo = new ArrayList<>();
        mLineVo = new ArrayList<>();
        pw = new StringBuffer();
        //初始化默认数据
        non_circle_size = DisplayUtils.dp2px(context, non_circle_size);
        touch_big_circle_size = DisplayUtils.dp2px(context, touch_big_circle_size);
        non_circle_width = DisplayUtils.dp2px(context, non_circle_width);
        touch_center_circle_size = DisplayUtils.dp2px(context, touch_center_circle_size);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomMainAppLock);
        non_circle_color = ta.getColor(R.styleable.CustomMainAppLock_non_circle_color, non_circle_color);
        non_circle_width = ta.getDimension(R.styleable.CustomMainAppLock_non_circle_width, non_circle_width);
        non_circle_size = ta.getDimension(R.styleable.CustomMainAppLock_non_circle_size, non_circle_size);


        touch_small_circle_color = ta.getColor(R.styleable.CustomMainAppLock_touch_small_circle_color, touch_small_circle_color);
        touch_big_circle_size = ta.getDimension(R.styleable.CustomMainAppLock_touch_big_circle_size, touch_big_circle_size);
        touch_center_circle_color = ta.getColor(R.styleable.CustomMainAppLock_touch_center_circle_color, touch_center_circle_color);
        touch_center_circle_size = ta.getDimension(R.styleable.CustomMainAppLock_touch_center_circle_size, touch_center_circle_size);

        error_small_circle_color = ta.getColor(R.styleable.CustomMainAppLock_error_small_circle_color, error_small_circle_color);
        error_center_circle_color = ta.getColor(R.styleable.CustomMainAppLock_error_center_circle_color, error_center_circle_color);
        lessPoint = ta.getInteger(R.styleable.CustomMainAppLock_less_point, lessPoint);
        user_modle = ta.getInteger(R.styleable.CustomMainAppLock_user_modle, 1);
        max_try_num = ta.getInteger(R.styleable.CustomMainAppLock_max_try_num, max_try_num);

        ta.recycle();
        initDefaultCricleData();
    }

    int allCount = 0;
    boolean isError = false;
     Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            isError = false;
            reSetting();
            invalidate();
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        /**
         * 将画布区域分成6X6的格子，
         * j表示横，i表示列
         */

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                // i%2 == 1 并且 j%2 == 1的坐标将会画上圆圈，第一次将会画上默认的圆圈
                if (i % 2 == 1 && j % 2 == 1) {
                    allCount++;
                    x = j * baseX;
                    y = i * baseY;
                    listVo.get(allCount - 1).pw = allCount;
                    listVo.get(allCount - 1).x = x;
                    listVo.get(allCount - 1).y = y;
                    if (mIsTry) {
                        if (listVo.get(allCount - 1).status == 0) {
                            //画默认的圆
                            drawDefaultCricle(x, y, canvas);
                        } else {
                            //画已经按下去状态的圆
                            if (!isError) {
                                drawPressCricle(canvas, x, y, touch_small_circle_color, touch_center_circle_color);
                            } else {
                                drawPressCricle(canvas, x, y, error_small_circle_color, error_center_circle_color);

                            }
                        }
                    } else {
                        drawPressCricle(canvas, x, y, error_small_circle_color, error_center_circle_color);
                    }
                }

            }
        }
        allCount = 0;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //开始计算每个需要画默认点的坐标
        baseX = getMeasuredWidth() / 6;
        baseY = getMeasuredHeight() / 6;

    }

    boolean isNeedWithHand = true;

    public void drawLine(Canvas canvas) {
        mPaint.setStrokeWidth(non_circle_width);
        mPaint.setStyle(Paint.Style.STROKE);
        if (!isError) {
            mPaint.setColor(touch_small_circle_color);
        } else {
            mPaint.setColor(error_small_circle_color);
        }

        for (int i = 1; i < mLineVo.size(); i++) {

            canvas.drawLine(mLineVo.get(i - 1).X, mLineVo.get(i - 1).Y, mLineVo.get(i).X, mLineVo.get(i).Y, mPaint);
        }
        if (mLineVo.size() > 0 && mLineVo.size() < 9 && isNeedWithHand) {

            canvas.drawLine(mLineVo.get(mLineVo.size() - 1).X, mLineVo.get(mLineVo.size() - 1).Y, touchX, touchY, mPaint);
        }
    }

    public void initDefaultCricleData() {
        for (int i = 0; i < 9; i++) {
            Vo mVo = new Vo(0, 0, i + 1, 0);
            listVo.add(mVo);
        }
    }

    public void drawDefaultCricle(int x, int y, Canvas canvas) {
        mPaint.setStrokeWidth(non_circle_width);
        mPaint.setColor(non_circle_color);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x, y, non_circle_size - non_circle_width / 2, mPaint);
    }

    float touchX, touchY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mIsTry) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isNeedWithHand = true;
                    touchX = event.getX();
                    touchY = event.getY();
                    isACell(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    touchX = event.getX();
                    touchY = event.getY();
                    isACell(touchX, touchY);
                    break;
                case MotionEvent.ACTION_UP:
                    getModle();
                    break;
            }
            return true;
        } else {
            mUnlockListence.unlockFaile(max_try_num);
            return false;
        }
    }


    //判断用户的使用模式
    public void getModle() {


        if (user_modle == 0) {
            for (int i = 0; i < pressVo.size(); i++) {
                pw.append(pressVo.get(i).pw);
            }
            if (pressVo.size() >= lessPoint) {
                if (isFrist) {
                    isFrist = false;
                    String frist = pw.toString().substring(0, pw.toString().length() / 2);
                    String second = pw.toString().substring(pw.toString().length() / 2, pw.toString().length());
                    if (frist.equals(second)) {
                        mSettingListence.passSettingSuccess(second);
                        reSetting();
                    } else {
                        mSettingListence.passwordIsNotSame(frist, second);
                        pw.setLength(0);
                        isError = true;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mHandler.sendEmptyMessage(0);
                            }
                        }, 1000);


                    }
                } else {
                    //设置密码模式:清空所有的数据，重新绘制默认圆
                    isFrist = true;
                    reSetting();
                    mSettingListence.passSetAgain();
                }
            } else {
                reSetting();
                mSettingListence.passPointLess(lessPoint);
            }
        } else {
            //解锁模式
            for (int i = 0; i < pressVo.size(); i++) {
                pw.append(pressVo.get(i).pw);
            }
            if (pw.toString().equals(mPass)) {
                mUnlockListence.unlockSuccess();
                max_try_num = 5;
                reSetting();
            } else {
                if (pressVo.size() != 0) {
                    max_try_num--;
                    mUnlockListence.unlockFaile(max_try_num);
                    pressVo.clear();
                    pw.setLength(0);

                    isError = true;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(0);
                        }
                    }, 800);
                }


            }

        }

        isNeedWithHand = false;
        invalidate();
    }

    public void reSetting() {
        listVo.clear();
        pressVo.clear();
        mLineVo.clear();
        initDefaultCricleData();
    }


    public void isACell(float x, float y) {
        //开始计算触摸点的位置是否在某个格子的指定范围之中
        for (int i = 0; i < 9; i++) {
            if (listVo.get(i).x + touch_big_circle_size > x && listVo.get(i).x - touch_big_circle_size < x) {
                if (listVo.get(i).y + touch_big_circle_size > y && listVo.get(i).y - touch_big_circle_size < y) {
                    listVo.get(i).status = 2;

                    if (!pressVo.contains(listVo.get(i))) {
                        pressVo.add(listVo.get(i));
                        LineVo mVo = new LineVo(listVo.get(i).x, listVo.get(i).y);
                        mLineVo.add(mVo);
                    }

                }
            }
        }
        invalidate();
    }

    public void drawPressCricle(Canvas canvas, float x, float y, int lightColor, int trasColor) {
        drawPressBigCricle(canvas, x, y, lightColor);
        drawPressCenterCricle(canvas, x, y, trasColor);
        drawPressSmallCricle(canvas, x, y, lightColor);
    }

    public void drawPressSmallCricle(Canvas canvas, float x, float y, int lightColor) {
        mPaint.setColor(lightColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, non_circle_size, mPaint);
    }

    public void drawPressCenterCricle(Canvas canvas, float x, float y, int trasColor) {
        mPaint.setStrokeWidth((touch_center_circle_size - non_circle_size));
        mPaint.setColor(trasColor);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(x, y, non_circle_size + (touch_center_circle_size - non_circle_size) / 2, mPaint);
    }

    public void drawPressBigCricle(Canvas can, float x, float y, int lightColor) {
        mPaint.setStrokeWidth(non_circle_width);
        mPaint.setColor(lightColor);
        mPaint.setStyle(Paint.Style.STROKE);
        can.drawCircle(x, y, touch_big_circle_size, mPaint);
    }

    public interface AppLockSettingListence {
        //设置密码模式的时候两次密码不一致
        void passwordIsNotSame(String firstPass, String secondPass);

        //再次设置密码的回调（第一次密码设置成功后回调）
        void passSetAgain();

        //密码设置成功
        void passSettingSuccess(String pass);

        //密码的连接数小于定义的连接数的时候回调
        void passPointLess(int lessPoint);
    }

    public interface AppLockUnlockListence {
        //解锁成功的时候回调
        void unlockSuccess();

        /**
         * 解锁失败的时候回调
         *
         * @param count ，剩余的尝试次数
         */
        void unlockFaile(int count);
    }

    private AppLockSettingListence mSettingListence;

    /**
     * 设置 设置密码的监听
     *
     * @param settingListence 监听事件
     */
    public void setAppLockSettingListence(AppLockSettingListence settingListence) {
        this.mSettingListence = settingListence;
    }

    private AppLockUnlockListence mUnlockListence;
    private String mPass;

    /**
     * 设置解锁模式的监听
     *
     * @param pass           正确的密码
     * @param unlockListence 监听事件
     */
    public void setAppLockUnlockListence(String pass, AppLockUnlockListence unlockListence) {
        this.mPass = pass;
        this.mUnlockListence = unlockListence;
    }

    private boolean mIsTry = true;

    public void setIsTry(boolean isTry) {
        this.mIsTry = isTry;
        if (!mIsTry) {
            invalidate();
        }
    }

}
