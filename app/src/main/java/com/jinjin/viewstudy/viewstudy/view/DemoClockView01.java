package com.jinjin.viewstudy.viewstudy.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.jinjin.viewstudy.viewstudy.R;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * @since 1.0
 * <p>自定义View钟表表盘
 * SnowJun  2017/2/12
 */
public class DemoClockView01 extends View {

    /**
     * 边框的尺寸和颜色
     */
    private int mBorderWidth;
    private int mBorderColor;

    /**
     * 数字的尺寸和颜色
     */
    private int mNumSize;
    private int mNumColor;

    /**
     * 周围点的颜色
     */
    private int mPointColor;

    /**
     * 时分秒三个针的颜色
     */
    private int mHourColor;
    private int mMinuteColor;
    private int mSecondColor;

    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 矩形范围
     */
    private Rect mBound;

    /**
     * 区域的宽度和高度
     */
    private int mWidth;
    private int mHeight;

    private boolean isShow;

    private Calendar mCalendar;//日历类   用来计算时间相应的角度

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            invalidate();
        }
    };

    public DemoClockView01(Context context) {
        this(context, null);
    }

    public DemoClockView01(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DemoClockView01(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DemoClockView01, defStyleAttr, 0);
        for (int i = 0; i < array.getIndexCount(); i++) {//用getIndexCount   减少循环次数，提高性能   用.length也不能执行所有的case情况
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.DemoClockView01_borderwidth://边框宽度
                    mBorderWidth = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.DemoClockView01_bordercolor://边框颜色
                    mBorderColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.DemoClockView01_numcolor://数字颜色
                    mNumColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.DemoClockView01_numsize://数字字号
                    mNumSize = array.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.DemoClockView01_pointcolor://周围小点颜色
                    mPointColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.DemoClockView01_hourcolor://时针颜色
                    mHourColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.DemoClockView01_minutecolor://分针颜色
                    mMinuteColor = array.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.DemoClockView01_secondcolor://秒针颜色
                    mSecondColor = array.getColor(attr, Color.BLACK);
                    break;
            }
        }
        array.recycle();
        isShow = true;
        mCalendar = Calendar.getInstance();
        mPaint = new Paint();
        mBound = new Rect();
        Timer timer = new Timer("绘制线程");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("test", "绘制一次");
                if (isShow) {
                    mHandler.sendEmptyMessage(1);
                }
            }
        }, 0, 1000);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            int desire = getPaddingLeft() + getPaddingRight() + (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            mWidth = Math.min(desire, widthSize);
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            int desire = getPaddingTop() + getPaddingBottom() + (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());
            mHeight = Math.min(desire, heightSize);
        }
        mWidth = Math.min(mWidth, mHeight);//取最小值  防止绘制内容出错   以最小的边来为基准进行相关的绘制
        setMeasuredDimension(mWidth, mWidth);
    }


    @Override
    protected void onDraw(final Canvas canvas) {
        if (mBorderWidth == 0) {
            mBorderWidth = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics());
        }
        /*
        整体绘制区域的规定
         */
        mBound.left = getPaddingLeft();
        mBound.right = mWidth - getPaddingRight();
        mBound.top = getPaddingTop();
        mBound.bottom = mHeight - getPaddingBottom();
        /**
         * 圆心的xy和圆环的宽度
         */
        final int cx, cy, width;
        cx = getPaddingLeft() + (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 2;
        cy = getPaddingTop() + (getMeasuredHeight() - getPaddingTop() - getPaddingBottom()) / 2;
        width = Math.min(getWidth() / 2, getHeight() / 2);//半径

        mPaint.setAntiAlias(true);//去除边缘锯齿，优化绘制效果
        mPaint.setColor(mBorderColor);
        if (mBorderColor == 0) {
            mPaint.setColor(Color.BLACK);
        }
        canvas.drawCircle(cx, cy, width, mPaint);//外圆  红色

        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(cx, cy, width - mBorderWidth, mPaint);//内圆 白色


        mPaint.setColor(Color.RED);
        canvas.drawCircle(cx, cy, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()), mPaint);//圆心，红色

        mPaint.setColor(mPointColor);
        if (mPointColor == 0) {
            mPaint.setColor(Color.BLACK);
        }
        canvas.save();//保存当前的状态
        for (int i = 0; i < 60; i++) {//总共60个点  所以绘制60次  //绘制一圈的小黑点
            if (i % 5 == 0) {
                canvas.drawRect(cx - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()),
                        getPaddingTop() + mBorderWidth + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()),
                        cx + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()),
                        getPaddingTop() + mBorderWidth + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics()), mPaint);
            } else {
                canvas.drawRect(cx - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),
                        getPaddingTop() + mBorderWidth + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()),
                        cx + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),
                        getPaddingTop() + mBorderWidth + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()), mPaint);
            }
            canvas.rotate(6, cx, cy);//360度  绘制60次   每次旋转6度
        }
        canvas.restore();//将canvas转回来

        mPaint.setColor(mNumColor);
        if (mNumColor == 0) {
            mPaint.setColor(Color.BLACK);
        }
        mPaint.setTextSize(mNumSize);
        if (mNumSize == 0) {
            mPaint.setTextSize((int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        }
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        String[] strs = new String[]{"12", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};//绘制数字1-12  (数字角度不对  可以进行相关的处理)
        Rect rect = new Rect();
        canvas.save();
        for (int i = 0; i < 12; i++) {//绘制12次  每次旋转30度
            mPaint.getTextBounds(strs[i], 0, strs[i].length(), rect);
            canvas.drawText(strs[i], cx - rect.width() / 2,
                    getPaddingTop() + mBorderWidth + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 18, getResources().getDisplayMetrics()) + rect.height(), mPaint);
            canvas.rotate(30, cx, cy);
        }
        canvas.restore();


        //关于当前时间的计算，默认为当前时间  当然是可以设置的

        int hour = mCalendar.get(Calendar.HOUR);//HOUR    进制为12小时   HOUR_OF_DAY  为24小时
        int minute = mCalendar.get(Calendar.MINUTE);//分钟
        int second = mCalendar.get(Calendar.SECOND) + 1;//秒数
        if (second == 60) {
            minute += 1;
            second = 0;
        }
        if (minute == 60){
            hour += 1;
            minute = 0;
        }
        if (hour == 12){
            hour = 0;
        }
        mCalendar.set(Calendar.SECOND, second);
        mCalendar.set(Calendar.MINUTE, minute);
        mCalendar.set(Calendar.HOUR, hour);
        float hourDegree = 360 * hour / 12 + 360 / 12 * minute / 60;//时针转动的角度   小时对应角度  加上  分钟对应角度   秒针忽略
        float minuteDegree = 360 * minute / 60 + 360 / 60 * second / 60;//分针转动的角度   分针对应角度  加上  秒数对应角度
        float secondDegree = 360 * second / 60;// 秒数对应角度

        mPaint.setColor(mHourColor);
        if (mHourColor == 0) {
            mPaint.setColor(Color.BLACK);
        }
        canvas.save();//绘制时针
        canvas.rotate(hourDegree, cx, cy);
        canvas.drawRect(cx - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()),
                getPaddingTop() + mBorderWidth + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()) + rect.width(),
                cx + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()),
                cy, mPaint);
        canvas.restore();

        mPaint.setColor(mMinuteColor);
        if (mMinuteColor == 0) {
            mPaint.setColor(Color.BLACK);
        }
        canvas.save();//保存后面的状态
        canvas.rotate(minuteDegree, cx, cy);
        canvas.drawRect(cx - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()),
                getPaddingTop() + mBorderWidth + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics()),
                cx + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()),
                cy, mPaint);
        canvas.restore();//撤销保存的状态

        mPaint.setColor(mSecondColor);
        if (mSecondColor == 0) {
            mPaint.setColor(Color.BLACK);
        }
        canvas.save();
        mPaint.setColor(Color.RED);
        canvas.rotate(secondDegree, cx, cy);
        canvas.drawRect(cx - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),
                getPaddingTop() + mBorderWidth + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, getResources().getDisplayMetrics()),
                cx + TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics()),
                cy, mPaint);
        canvas.restore();

        mPaint.setColor(Color.RED);
        canvas.drawCircle(cx, cy, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 6, getResources().getDisplayMetrics()), mPaint);//圆心，红色

    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isShow = false;
    }

    /**
     * 设置时钟的时间
     *
     * @param calendar
     */
    public void setTime(Calendar calendar) {
        mCalendar = calendar;
    }


}
