# UnLock
### 简介：
UnLock 一款仿vivo手机的九宫格解锁程序，可以根据需求设置解锁或者设置密码模式。
### 效果：
![](https://github.com/kyuyi/UnLock/blob/master/material/d54ed071-9ff5-4396-9e5f-19037c6ce05a.gif)
### 属性说明：
```java
    <!--九宫格图案锁的属性-->
    <declare-styleable name="CustomMainAppLock">
        <!--没有触摸的属性(默认的属性，手指没有触摸到的格子)-->
        <attr name="non_circle_color" format="color" />  <!--圆圈的颜色-->
        <attr name="non_circle_size" format="dimension" /> <!--圆圈的大小-->
        <attr name="non_circle_width" format="dimension" /> <!--圆圈的边框宽度-->
        <!--手指头触摸的格子和连接闲的属性-->
        <attr name="touch_small_circle_color" format="color" /><!--已经连接的格子的小圆的颜色-->
        <attr name="touch_center_circle_color" format="color" /><!--已经连接的格子的中间元的颜色-->
        <attr name="touch_big_circle_size" format="dimension" /><!--大圆的大小-->
        <attr name="touch_center_circle_size" format="dimension" /> <!--中间圆的大小-->
        <!--密码错误的颜色-->
        <attr name="error_small_circle_color" format="color" /> <!--密码错误时候小圆和大圆的颜色-->
        <attr name="error_center_circle_color" format="color" /><!--密码错误的时候中间圆的颜色-->
        <attr name="user_modle">//使用的模式
            //设置密码模式
            <enum name="MODLE_SETTING_PASSWORD" value="0" />
            //解锁模式
            <enum name="MODLE_UNLOCK" value="1" />
        </attr>
        <attr name="less_point" format="integer" /> <!--密码的最少连接个数-->
        <attr name="max_try_num" format="integer" /> <!--密码输入错误时最大尝试次数-->
    </declare-styleable>
```

### 使用说明：
```
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:gravity="center"
    android:orientation="vertical">

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginBottom="20dp"
        android:text="Tips:   连接123456个点完成解锁"
        android:textColor="@color/light_red" />

    <com.example.administrator.unlock.customview.CustomMainAppLock
        android:id="@+id/lock"
        android:layout_width="match_parent"
        android:layout_height="350dp"
        android:layout_centerInParent="true"
        app:error_center_circle_color="@color/error_tras"
        app:error_small_circle_color="@color/error_light"
        app:less_point="3"
        app:non_circle_color="@color/default_color"
        app:non_circle_size="5dp"
        app:non_circle_width="2dp"
        app:touch_big_circle_size="21dp"
        app:touch_center_circle_color="@color/touch_tras"
        app:touch_center_circle_size="18dp"
        app:touch_small_circle_color="@color/touch_light" />
</LinearLayout>
```
