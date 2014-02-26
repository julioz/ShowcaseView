package com.espian.showcaseview.drawing;

import com.espian.showcaseview.ShowcaseView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;

/**
 * Created by curraa01 on 13/10/2013.
 */
public interface TextDrawer {
	
	public static final int POSITION_LEFT = 0;
	public static final int POSITION_TOP = 1;
	public static final int POSITION_RIGHT = 2;
	public static final int POSITION_BOTTOM = 3;

    void draw(Canvas canvas, boolean hasPositionChanged, float offsetTop);

    void setDetails(CharSequence details);

    void setTitle(CharSequence title);

    void calculateTextPosition(int canvasW, int canvasH, ShowcaseView showcaseView);

    void setTitleStyling(Context context, int styleId);

    void setDetailStyling(Context context, int styleId);
    
	void setTypeface(Typeface typeface);

	void setForceTextPosition(Integer forceTextPosition);
}
