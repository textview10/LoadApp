package com.instahela.deni.mkopo.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ConvertUtils;

/**
 * @author xu.wang
 * @date 2019/3/12 21:44
 * @desc
 */
public class NotifyTextView extends AppCompatTextView {
    private boolean drawNotify = false; //是否绘制提醒的红点
    private int RADIUS ;
    private Paint mPaint;
    private int redLight = -1;

    public NotifyTextView(Context context) {
        this(context, null);
    }

    public NotifyTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotifyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        redLight = Color.RED;
        mPaint.setColor(redLight);
        RADIUS = ConvertUtils.dp2px(4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!drawNotify) {
            return;
        }
        if (getWidth() == 0 || getHeight() == 0 ||getTextWidth() == 0) {
            return;
        }
        float circleX = 0;
        if (getWidth() - getTextWidth() > 15){
            circleX = getWidth() - RADIUS - 15;
        } else {
            circleX = getWidth() - RADIUS;
        }
        canvas.drawCircle(circleX, RADIUS, RADIUS, mPaint);
    }


    public void setDrawNotify(boolean drawNotify) {
        this.drawNotify = drawNotify;
        if (redLight == -1) {
            return;
        }
        postInvalidate();
    }

    public boolean isDrawNotify() {
        return drawNotify;
    }

    private int getTextWidth() {
        int textWidth = 0;
        String str = getText().toString();
        if (getTextSize() == Float.NaN || TextUtils.isEmpty(str)) {
            return 0;
        }
        Paint paint = new Paint();
        int len = str.length();
        float[] widths = new float[len];
        paint.getTextWidths(str, widths);
        for (int j = 0; j < len; j++) {
            textWidth += (int) Math.ceil(widths[j]);
        }
        return textWidth;
    }
}
