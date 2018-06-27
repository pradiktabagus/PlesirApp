package com.example.dickycn.plesirapp.sekitar;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by rejak on 10/14/2017.
 */

class TouchUpRedirectLayout extends FrameLayout implements View.OnTouchListener {

    private int mTargetViewId;
    private View mTargetView;
    private boolean mTargetTouchActive;

    private GestureDetector mGestureDetector;

    public TouchUpRedirectLayout(Context context) {
        super(context);
        init(context);
    }

    public TouchUpRedirectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TouchUpRedirectLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mGestureDetector = new GestureDetector(context, mGestureListener);
    }

    public void setTargetViewId(int resId) {
        mTargetViewId = resId;
        updateTargetView();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //Find the target view, if set, once inflated
        updateTargetView();
    }

    //Set the target view to handle gestures
    private void updateTargetView() {
        if (mTargetViewId > 0) {
            mTargetView = findViewById(mTargetViewId);
            if (mTargetView != null) {
                mTargetView.setOnTouchListener(this);
            }
        }
    }

    private Rect mHitRect = new Rect();
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_UP:
                if (mTargetTouchActive) {
                    mTargetTouchActive = false;

                    //Validate the up
                    int index = indexOfChild(mTargetView) - 1;
                    if (index < 0) {
                        return false;
                    }

                    for (int i=index; i >= 0; i--) {
                        final View child = getChildAt(i);
                        child.getHitRect(mHitRect);
                        if (mHitRect.contains((int) event.getX(), (int) event.getY())) {
                            //Dispatch and mark handled
                            return child.dispatchTouchEvent(event);
                        }
                    }

                    //Steal this event
                    return true;
                }
                //Allow default processing
                return false;
            default:
                //Allow default processing
                return false;
        }
    }

    //Receive touch events from the target (scroll handling) view
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        mTargetTouchActive = true;
        return mGestureDetector.onTouchEvent(event);
    }

    //Handle gesture events in target view
    private GestureDetector.SimpleOnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("TAG", "onDown");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("TAG", "Scrolling...");
            return true;
        }
    };
}