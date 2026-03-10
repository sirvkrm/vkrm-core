/* Copyright (c) 2026 The Brave Authors. All rights reserved.
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at https://mozilla.org/MPL/2.0/. */

package org.chromium.chrome.browser.settings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.ColorInt;

import org.chromium.build.annotations.Nullable;

/** Simple HSV color wheel used by the VKRM custom theme color dialog. */
public class VkrmColorWheelView extends View {
    /** Listener for color changes caused by dragging on the wheel. */
    public interface OnColorChangeListener {
        void onColorChanged(@ColorInt int color);
    }

    private static final float DEFAULT_VALUE = 1f;

    private final Paint mBitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private final Paint mSelectorOuterPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mSelectorInnerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private @Nullable Bitmap mWheelBitmap;
    private @Nullable OnColorChangeListener mOnColorChangeListener;

    private float mRadius;
    private float mCenterX;
    private float mCenterY;
    private float mSelectorX;
    private float mSelectorY;
    private float mSelectorRadius;
    private @ColorInt int mSelectedColor = Color.parseColor("#0077B6");

    public VkrmColorWheelView(Context context) {
        this(context, null);
    }

    public VkrmColorWheelView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VkrmColorWheelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mSelectorOuterPaint.setStyle(Paint.Style.STROKE);
        mSelectorOuterPaint.setStrokeWidth(getResources().getDisplayMetrics().density * 3f);
        mSelectorOuterPaint.setColor(Color.WHITE);

        mSelectorInnerPaint.setStyle(Paint.Style.STROKE);
        mSelectorInnerPaint.setStrokeWidth(getResources().getDisplayMetrics().density * 1.5f);
        mSelectorInnerPaint.setColor(0xCC081B2D);
    }

    public void setOnColorChangeListener(@Nullable OnColorChangeListener listener) {
        mOnColorChangeListener = listener;
    }

    public void setColor(@ColorInt int color) {
        mSelectedColor = color;
        updateSelectorPosition(color);
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float minDimension = Math.min(w, h);
        mSelectorRadius = minDimension * 0.055f;
        mRadius = (minDimension / 2f) - (mSelectorRadius * 1.4f);
        mCenterX = w / 2f;
        mCenterY = h / 2f;

        int bitmapSize = Math.max(1, Math.round(mRadius * 2));
        mWheelBitmap = createColorWheelBitmap(bitmapSize);
        updateSelectorPosition(mSelectedColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mWheelBitmap != null) {
            float left = mCenterX - (mWheelBitmap.getWidth() / 2f);
            float top = mCenterY - (mWheelBitmap.getHeight() / 2f);
            canvas.drawBitmap(mWheelBitmap, left, top, mBitmapPaint);
        }

        canvas.drawCircle(mSelectorX, mSelectorY, mSelectorRadius, mSelectorOuterPaint);
        canvas.drawCircle(mSelectorX, mSelectorY, mSelectorRadius, mSelectorInnerPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                updateColorFromTouch(event.getX(), event.getY());
                return true;
            case MotionEvent.ACTION_UP:
                performClick();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    private void updateColorFromTouch(float touchX, float touchY) {
        float dx = touchX - mCenterX;
        float dy = touchY - mCenterY;
        float distance = (float) Math.hypot(dx, dy);
        float clampedDistance = Math.min(distance, mRadius);

        float hue = (float) Math.toDegrees(Math.atan2(dy, dx));
        if (hue < 0) hue += 360f;

        float saturation = mRadius == 0 ? 0 : clampedDistance / mRadius;
        mSelectedColor = Color.HSVToColor(new float[] {hue, saturation, DEFAULT_VALUE});

        if (distance > 0) {
            float scale = clampedDistance / distance;
            mSelectorX = mCenterX + (dx * scale);
            mSelectorY = mCenterY + (dy * scale);
        } else {
            mSelectorX = mCenterX;
            mSelectorY = mCenterY;
        }

        if (mOnColorChangeListener != null) {
            mOnColorChangeListener.onColorChanged(mSelectedColor);
        }

        invalidate();
    }

    private void updateSelectorPosition(@ColorInt int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        double angleRadians = Math.toRadians(hsv[0]);
        float distance = hsv[1] * mRadius;
        mSelectorX = mCenterX + (float) Math.cos(angleRadians) * distance;
        mSelectorY = mCenterY + (float) Math.sin(angleRadians) * distance;
    }

    private static Bitmap createColorWheelBitmap(int size) {
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        float radius = size / 2f;

        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                float dx = x - radius;
                float dy = y - radius;
                float distance = (float) Math.hypot(dx, dy);
                if (distance > radius) {
                    bitmap.setPixel(x, y, Color.TRANSPARENT);
                    continue;
                }

                float hue = (float) Math.toDegrees(Math.atan2(dy, dx));
                if (hue < 0) hue += 360f;
                float saturation = distance / radius;
                bitmap.setPixel(
                        x, y, Color.HSVToColor(new float[] {hue, saturation, DEFAULT_VALUE}));
            }
        }

        return bitmap;
    }
}
