package com.espian.showcaseview.drawing;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.DynamicLayout;
import android.text.Html;
import android.text.Layout;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.DisplayMetrics;

import com.espian.showcaseview.ShowcaseView;
import com.espian.showcaseview.utils.ShowcaseAreaCalculator;

/**
 * Draws the text as required by the ShowcaseView
 */
public class TextDrawerImpl implements TextDrawer {
	
	private static final int PADDING = 24;
	private static final int ACTIONBAR_PADDING = 66;

    private final TextPaint mPaintTitle;
    private final TextPaint mPaintDetail;

    private CharSequence mTitle, mDetails;
    private float mDensityScale;
    private int largestArea;
    private ShowcaseAreaCalculator mCalculator;
    private float[] mBestTextPosition = new float[3];
    private DynamicLayout mDynamicTitleLayout;
    private DynamicLayout mDynamicDetailLayout;
    private TextAppearanceSpan mTitleSpan;
    private TextAppearanceSpan mDetailSpan;
    
    private int mForceTextPosition = -1;
	private int mDensityDpi;

    public TextDrawerImpl(float densityScale, int densityDpi,
    		ShowcaseAreaCalculator calculator) {
        mDensityScale = densityScale;
		mDensityDpi = densityDpi;
        mCalculator = calculator;

        mPaintTitle = new TextPaint();
        mPaintTitle.setAntiAlias(true);

        mPaintDetail = new TextPaint();
        mPaintDetail.setAntiAlias(true);
    }
    
    @Override
    public void draw(Canvas canvas, boolean hasPositionChanged, float offsetTop) {
        if (shouldDrawText()) {
            float[] textPosition = getBestTextPosition();
            float titleTopPosition;
            if (largestArea == POSITION_BOTTOM || mForceTextPosition == POSITION_BOTTOM) {
            	if (mDensityDpi == DisplayMetrics.DENSITY_LOW) {
            		titleTopPosition = offsetTop + 2 * mDensityScale;
            	} else {
            		titleTopPosition = offsetTop + 15 * mDensityScale;
            	}
            } else {
            	titleTopPosition = textPosition[POSITION_TOP];
            }
            
            if (!TextUtils.isEmpty(mTitle)) {
                canvas.save();
                if (hasPositionChanged) {
                    mDynamicTitleLayout = new DynamicLayout(mTitle, mPaintTitle,
                            (int) textPosition[POSITION_RIGHT], Layout.Alignment.ALIGN_NORMAL,
                            1.0f, 1.0f, true);
                }
				canvas.translate(textPosition[POSITION_LEFT], titleTopPosition);
                mDynamicTitleLayout.draw(canvas);
                canvas.restore();
            }

            if (!TextUtils.isEmpty(mDetails)) {
                canvas.save();
                if (hasPositionChanged) {
                    mDynamicDetailLayout = new DynamicLayout(mDetails, mPaintDetail,
                            (int) textPosition[POSITION_RIGHT],
                            Layout.Alignment.ALIGN_NORMAL,
                            1.2f, 1.0f, true);
                }
                canvas.translate(textPosition[POSITION_LEFT], titleTopPosition + 3 * mDensityScale + mDynamicTitleLayout.getHeight());
                mDynamicDetailLayout.draw(canvas);
                canvas.restore();
            }
        }
    }

    @Override
    public void setDetails(CharSequence details) {
        if (details != null) {
            SpannableString ssbDetail = new SpannableString(Html.fromHtml(details.toString()));
            ssbDetail.setSpan(mDetailSpan, 0, ssbDetail.length(), 0);
            mDetails = ssbDetail;
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        if (title != null) {
            SpannableString ssbTitle = new SpannableString(Html.fromHtml(title.toString()));
            ssbTitle.setSpan(mTitleSpan, 0, ssbTitle.length(), 0);
            mTitle = ssbTitle;
        }
    }

    /**
     * Calculates the best place to position text
     *
     * @param canvasW width of the screen
     * @param canvasH height of the screen
     * @return 
     */
    @Override
    public void calculateTextPosition(int canvasW, int canvasH, ShowcaseView showcaseView) {
    	Rect showcase = showcaseView.hasShowcaseView() ?
    			mCalculator.getShowcaseRect() :
    			new Rect();
    	
    	int[] areas = new int[4]; //left, top, right, bottom
    	areas[POSITION_LEFT] = showcase.left * canvasH;
    	areas[POSITION_TOP] = showcase.top * canvasW;
    	areas[POSITION_RIGHT] = (canvasW - showcase.right) * canvasH;
    	areas[POSITION_BOTTOM] = (canvasH - showcase.bottom) * canvasW;
    	
    	int largest = 0;
    	for(int i = 1; i < areas.length; i++) {
    		if(areas[i] > areas[largest])
    			largest = i;
    	}
    	
    	largestArea = largest;
    	if (mForceTextPosition > -1) {
    		largest = mForceTextPosition;
    	}
    	
    	// Position text in largest area
    	switch(largest) {
    	case POSITION_LEFT:
    		mBestTextPosition[0] = PADDING * mDensityScale;
    		mBestTextPosition[1] = PADDING * mDensityScale;
    		mBestTextPosition[2] = showcase.left - 2 * PADDING * mDensityScale;
    		break;
    	case POSITION_TOP:
    		mBestTextPosition[0] = PADDING * mDensityScale;
    		mBestTextPosition[1] = (PADDING + ACTIONBAR_PADDING) * mDensityScale;
    		mBestTextPosition[2] = canvasW - 2 * PADDING * mDensityScale;
    		break;
    	case POSITION_RIGHT:
    		mBestTextPosition[0] = showcase.right + PADDING * mDensityScale;
    		mBestTextPosition[1] = PADDING * mDensityScale;
    		mBestTextPosition[2] = (canvasW - showcase.right) - 2 * PADDING * mDensityScale;
    		break;
    	case POSITION_BOTTOM:
    		mBestTextPosition[0] = PADDING * mDensityScale;
    		mBestTextPosition[1] = showcase.bottom + PADDING * mDensityScale;
    		mBestTextPosition[2] = canvasW - 2 * PADDING * mDensityScale;
    		break;
    	}
    	if(showcaseView.getConfigOptions().centerText) {
	    	// Center text vertically or horizontally
	    	switch(largest) {
	    	case POSITION_LEFT:
	    	case POSITION_RIGHT:
	    		mBestTextPosition[1] += canvasH / 4;
	    		break;
	    	case POSITION_TOP:
	    	case POSITION_BOTTOM:
	    		mBestTextPosition[2] /= 2;
	    		mBestTextPosition[0] += canvasW / 4;
	    		break;
	    	} 
    	} else {
    		// As text is not centered add actionbar padding if the text is left or right
	    	switch(largest) {
	    		case POSITION_LEFT:
	    		case POSITION_RIGHT:
	    			mBestTextPosition[1] += ACTIONBAR_PADDING * mDensityScale;
	    			break;
	    	}
    	}
    	
    	// prepare for text writing
    	if (largest == POSITION_BOTTOM) {
    		float radiusOffset = mCalculator.getRadius() / 2;
    		float densityScaleOffset = 0;
    		if (radiusOffset < 30) {
    			radiusOffset = 15 * mDensityScale;
    			densityScaleOffset = 3 * mDensityScale;
    		}
			mBestTextPosition[POSITION_TOP] = (canvasH - showcase.bottom) + radiusOffset + densityScaleOffset;
    	}
    }

    @Override
    public void setTitleStyling(Context context, int styleId) {
        mTitleSpan = new TextAppearanceSpan(context, styleId);
    }

    @Override
    public void setDetailStyling(Context context, int styleId) {
        mDetailSpan = new TextAppearanceSpan(context, styleId);
    }

    public float[] getBestTextPosition() {
        return mBestTextPosition;
    }
    
    public boolean shouldDrawText() {
        return !TextUtils.isEmpty(mTitle) || !TextUtils.isEmpty(mDetails);
    }

	@Override
	public void setTypeface(Typeface typeface) {
		mPaintTitle.setTypeface(typeface);
		mPaintDetail.setTypeface(typeface);
	}
	
	@Override
	public void setForceTextPosition(Integer forceTextPosition) {
		this.mForceTextPosition = forceTextPosition;
	}
}
