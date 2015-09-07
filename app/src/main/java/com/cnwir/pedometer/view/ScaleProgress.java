package com.cnwir.pedometer.view;

import android.content.Context;
import android.content.SyncStatusObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.cnwir.pedometer.utils.DeviceHelper;

/**
 * Created by heaven on 2015/7/15.
 */
public class ScaleProgress extends View {

    private static final double PI = Math.PI;


    private int defalult_scale_color = Color.rgb(0, 0, 0);

    private int default_background_color = Color.argb(200, 255, 255, 255);

    private int finish_scale_color = Color.rgb(0, 182, 254);


    private int default_circle_content_text_size;

    private int default_circle_content_text_color = Color.rgb(0, 0, 0);

    private String step_name = "步数";


    private int step_num;


    private static final String STEP = "步";

    /**
     * 目标步数
     * 默认为10000
     */
    private int target_num = 50;


    private Paint paint;

    private Paint textPaint;

    /**
     * 表盘的半径
     */
    private float radius;


    public ScaleProgress(Context context) {
        super(context);
    }

    public ScaleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        default_circle_content_text_size = (int) DeviceHelper.sp2px(context, 18);
        init();
    }

    public ScaleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化画笔
     */
    private void init() {

        textPaint = new TextPaint();
        textPaint.setColor(default_circle_content_text_color);
        textPaint.setTextSize(default_circle_content_text_size);
        textPaint.setAntiAlias(true);


    }
    /*
    *设置步数
    *
    * */

    public void setStepNum(int step_num) {

        if (step_num >= 0 && step_num <= target_num) {

            this.step_num = step_num;

            invalidate();
        }


    }

    /**
     * 设置目标步数
     */

    public void setTargetNum(int target_num) {
        if (target_num >= 0) {

            this.target_num = target_num;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {

        paint = new Paint();
        paint.setColor(default_background_color);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth() / 2f, getHeight() / 2f, getWidth() / 2f, paint);

        canvas.save();

        paint.setColor(finish_scale_color);
        paint.setStyle(Paint.Style.STROKE);//设置为空心

        paint.setStrokeWidth(2);
        float angle = (float) ((5 * PI) / 4);
        float outX, outY, inX, inY;
        radius = getWidth() / 2f;

        float outRadius = radius - 40;
        float inRadius = radius - 70;

        float percent = (float) step_num / target_num;
        for (int i = 0; i < 27; i++) {

            outX = (float) (radius + (outRadius * Math.cos(angle)));
            outY = (float) (radius - (outRadius * Math.sin(angle)));
            inX = (float) (radius + (inRadius * Math.cos(angle)));
            inY = (float) (radius - (inRadius * Math.sin(angle)));
            if (i == (int) (percent * 27)) {
                float tempAngle;
                if (angle == (float) ((5 * PI) / 4)) {
                    tempAngle = angle;
                } else {
                    tempAngle = (float) (angle + (PI / 18));
                }

                //绘制指示用的三角形
                float point_1_x = (float) (radius + (radius * Math.cos(tempAngle - (PI / 72))));
                float point_1_y = (float) (radius - (radius * Math.sin(tempAngle - (PI / 72))));
                float point_2_x = (float) (radius + (radius * Math.cos(tempAngle + (PI / 72))));
                float point_2_y = (float) (radius - (radius * Math.sin(tempAngle + (PI / 72))));
                float tempoutX = (float) (radius + (outRadius * Math.cos(tempAngle)));
                float tempoutY = (float) (radius - (outRadius * Math.sin(tempAngle)));
                Path path = new Path();
                path.moveTo(tempoutX, tempoutY);
                path.lineTo(point_1_x, point_1_y);
                path.lineTo(point_2_x, point_2_y);
                path.close();
                paint.setStyle(Paint.Style.FILL);
                canvas.drawPath(path, paint);
                paint.setColor(defalult_scale_color);
                paint.setStyle(Paint.Style.STROKE);
            }
            canvas.drawLine(outX, outY, inX, inY, paint);
            angle -= (PI / 18);
        }

        if (!TextUtils.isEmpty(step_name)) {

            float textHeight = textPaint.descent() + textPaint.ascent();
            canvas.drawText(step_name, (getWidth() - textPaint.measureText(step_name)) / 2.0f, (getWidth() + (3 * textHeight)) / 2.0f, textPaint);
            textPaint.setColor(Color.RED);
            String step_num_text = step_num + STEP;
            canvas.drawText(step_num_text, (getWidth() - textPaint.measureText(step_num_text)) / 2.0f, (getWidth() - textHeight) / 2.0f, textPaint);
            textPaint.setColor(default_circle_content_text_color);
            String step_target_num_text = "目标：" + target_num;
            canvas.drawText(step_target_num_text, (getWidth() - textPaint.measureText(step_target_num_text)) / 2.0f, (getWidth() - 6 * textHeight) / 2.0f, textPaint);

        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }
}
