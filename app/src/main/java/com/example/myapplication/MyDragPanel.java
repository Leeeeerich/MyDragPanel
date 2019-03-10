package com.example.myapplication;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class MyDragPanel extends FrameLayout {

    private static DecelerateInterpolator sDecelerator = new DecelerateInterpolator();
    private float mDefaultPositionDragPanel;
    private float mTouchY;
    private int mMaxHeightDragPanel;
    private boolean isOpened = false;
    private boolean isTouching = false;
    private boolean isAnimating = false;

    private LinearLayout mDragPanel;

    private OnClickInDragListener mListener;
    public void setOnClickDragListener (OnClickInDragListener onClickInDragListener) {
        this.mListener = onClickInDragListener;
    }

    public MyDragPanel(Context context) {
        super(context);
    }

    public MyDragPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyDragPanel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        init();
    }

    private void init() {
        mDragPanel = findViewById(R.id.dragPanel);
        mDefaultPositionDragPanel = mDragPanel.getY();
        mMaxHeightDragPanel = mDragPanel.getHeight() - 100;

        findViewById(R.id.one).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrCloseDragPanelByButton();
            }
        });

        findViewById(R.id.two).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickByButton(2);
            }
        });

        findViewById(R.id.four).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickByButton(4);
            }
        });
        findViewById(R.id.five).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickByButton(5);
            }
        });

        findViewById(R.id.touch).setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isTouching = true;
                    isAnimating = true;
                    mTouchY = event.getY();
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    float translation = event.getY();
                    translation = boundTranslation(translation);

                    mDragPanel.setTranslationY(translation);
                    ViewCompat.postInvalidateOnAnimation(mDragPanel);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    isTouching = false;
                    if (mDragPanel.getY() == mDefaultPositionDragPanel) {
                        isOpened = false;
                    } else {
                        isOpened = true;
                    }
                    isAnimating = false;
                }
                return false;
            }
        });
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (isAnimating) {
            ViewCompat.postInvalidateOnAnimation(mDragPanel);
        }
    }

    public float boundTranslation(float translation) {
        if (isOpened) {
            if (translation > 0) {
                translation = 0;
            }
            if (Math.abs(translation) >= mDragPanel.getHeight() - mMaxHeightDragPanel) {
                translation = -mDragPanel.getHeight() + mMaxHeightDragPanel;
            }
        } else {
            if (translation < 0) {
                translation = 0;
            }
            if (translation >= mDragPanel.getHeight() - mMaxHeightDragPanel) {
                translation = mDragPanel.getHeight() - mMaxHeightDragPanel;
            }
        }
        return translation;
    }

    private void openOrCloseDragPanelByButton() {
        int height = mDragPanel.getHeight() >= mMaxHeightDragPanel ? mMaxHeightDragPanel : mDragPanel.getHeight();
        if (isOpened) {
            ObjectAnimator.ofFloat(mDragPanel, View.TRANSLATION_Y,
                    mDragPanel.getTranslationY(), mDragPanel.getTranslationY() + height).start();
            isOpened = false;
        } else {
            ObjectAnimator.ofFloat(mDragPanel, View.TRANSLATION_Y,
                    mDragPanel.getTranslationY(), mDragPanel.getTranslationY() - height).start();
            isOpened = true;
        }
    }

    public interface OnClickInDragListener {
        void onClickByButton(int i);
    }
}
