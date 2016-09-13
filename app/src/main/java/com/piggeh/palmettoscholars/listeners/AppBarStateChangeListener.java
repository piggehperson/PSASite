package com.piggeh.palmettoscholars.listeners;

import android.support.design.widget.AppBarLayout;

/**
 * Created by peter on 9/8/2016.
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {

    public static final int STATE_EXPANDED = 2;
    public static final int STATE_IDLE = 1;
    public static final int STATE_COLLAPSED = 0;

    private int mCurrentState = STATE_IDLE;

    @Override
    public final void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if (i == 0) {
            if (mCurrentState != STATE_EXPANDED) {
                onStateChanged(appBarLayout, STATE_EXPANDED);
            }
            mCurrentState = STATE_EXPANDED;
        } else if (Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if (mCurrentState != STATE_COLLAPSED) {
                onStateChanged(appBarLayout, STATE_COLLAPSED);
            }
            mCurrentState = STATE_COLLAPSED;
        } else {
            if (mCurrentState != STATE_IDLE) {
                onStateChanged(appBarLayout, STATE_IDLE);
            }
            mCurrentState = STATE_IDLE;
        }
    }

    public abstract void onStateChanged(AppBarLayout appBarLayout, int state);
}