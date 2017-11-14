package nh.glazelog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by vihaan on 1/9/15.
 *
 * Copied by Nick Hansen on 10/21/17
 * StackOverflow is the best
 */
public class VersionPager extends ViewPager {

    private View mCurrentView;

    public VersionPager(Context context) {
        super(context);
    }

    public VersionPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mCurrentView == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int height = 0;
        mCurrentView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int h = mCurrentView.getMeasuredHeight();
        if (h > height) height = h;
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void measureCurrentView(View currentView) {
        mCurrentView = currentView;
        requestLayout();
    }

    public int measureFragment(View view) {
        if (view == null)
            return 0;

        view.measure(0, 0);
        return view.getMeasuredHeight();
    }

    public VersionPagerAdapter getVersionPagerAdapter() {
        return (VersionPagerAdapter) getAdapter();
    }

    /*--------------------ENABLES ACTION ON SWIPING PAST THE FIRST OR LAST PAGES--------------------*/

                //----------NOT USED ANYMORE - DID NOT WORK PROPERLY----------
                    //----------KEPT BECAUSE IT'S GOOD CODE----------

    // SOURCE: https://stackoverflow.com/a/13347008/7311664

    /*
    public interface OnSwipeOutListener {
        public void onSwipeOutAtStart();
        public void onSwipeOutAtEnd();
    }
    OnSwipeOutListener onSwipeOutListener;
    public void setOnSwipeOutListener(OnSwipeOutListener listener) {
        onSwipeOutListener = listener;
    }
    public OnSwipeOutListener getOnSwipeOutListener() {
        return onSwipeOutListener;
    }

    float mStartDragX;
    private static final float swipeThreshold = 10;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float x = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartDragX = x;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mStartDragX-swipeThreshold < x && getCurrentItem() == 0) {
                    onSwipeOutListener.onSwipeOutAtStart();
                } else if (mStartDragX+swipeThreshold > x && getCurrentItem() == getAdapter().getCount() - 1) {
                    onSwipeOutListener.onSwipeOutAtEnd();
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
    */

}
