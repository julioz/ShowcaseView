package com.espian.showcaseview.targets;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;
import android.view.ViewParent;

public class ViewTarget implements Target {

    private final View mView;

    public ViewTarget(View view) {
        mView = view;
    }

    public ViewTarget(int viewId, Activity activity) {
        mView = activity.findViewById(viewId);
    }
    
    public ViewParent getParent() {
    	return mView.getParent();
    }
    
    public boolean isParent(ViewTarget child) {
    	return child.getParent() == mView;
	}

    @Override
    public Point getPoint() {
        int[] location = new int[2];
        mView.getLocationInWindow(location);
        int x = location[0] + mView.getWidth() / 2;
        int y = location[1] + mView.getHeight() / 2;
        return new Point(x, y);
    }
}
