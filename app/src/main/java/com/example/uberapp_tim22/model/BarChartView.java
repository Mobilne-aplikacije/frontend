package com.example.uberapp_tim22.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BarChartView extends View {

    private static final int BAR_COLOR = Color.BLUE;
    private static final int TEXT_COLOR = Color.BLACK;
    private static final float TEXT_SIZE = 24f;

    private List<String> labels;
    private List<Integer> values;

    public BarChartView(Context context) {
        super(context);
        init();
    }

    public BarChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        labels = new ArrayList<>();
        values = new ArrayList<>();
    }

    public void setData(List<String> labels, List<Integer> values) {
        this.labels = labels;
        this.values = values;
        invalidate();  // Refresh the view
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (labels.isEmpty() || values.isEmpty()) {
            return;
        }

        int width = getWidth();
        int height = getHeight();
        int barCount = labels.size();
        int maxCount = Collections.max(values);
        int barWidth = width / barCount;

        Paint barPaint = new Paint();
        barPaint.setColor(BAR_COLOR);

        Paint textPaint = new Paint();
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(TEXT_SIZE);
        textPaint.setTextAlign(Paint.Align.CENTER);

        for (int i = 0; i < barCount; i++) {
            String label = labels.get(i);
            int value = values.get(i);
            float barHeight = (float) value / maxCount * height;

            float left = i * barWidth;
            float right = left + barWidth;
            float bottom = height;
            float top = height - barHeight;

            canvas.drawRect(left, top, right, bottom, barPaint);
            canvas.drawText(label, left + barWidth / 2, height - TEXT_SIZE, textPaint);
        }
    }
}
