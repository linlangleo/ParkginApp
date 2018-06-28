package com.example.leo.parkingapp.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.Toast;

/**
 * Created by X-FAN on 2016/7/5.
 */
public class AutoScrollTopBottomView extends RelativeLayout {
    private final int ANI_TIME = 800;
    private float mLastActionDownY;

    private float downX ;    //按下时 的X坐标
    private float downY ;    //按下时 的Y坐标
    private float upX ;    //离开时 的X坐标
    private float upY ;    //离开时 的Y坐标

    private float topY = 0;
    private float midY = -900;
    private float bottomY = -1460;

    private View mBottomView;
    private ViewGroup mTopView;
    private Scroller mScroller;
    private MotionEvent mLastMoveEvent;

    public AutoScrollTopBottomView(Context context) {
        super(context);
        mScroller = new Scroller(context);

    }

    public AutoScrollTopBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context);

    }

    public AutoScrollTopBottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() != 2) {
            throw new IllegalStateException("only and should contain two child view");
        }
        mBottomView = getChildAt(0);
        if (!(getChildAt(1) instanceof ViewGroup)) {
            throw new IllegalStateException("top view should be contained by a viewgroup");
        }
        mTopView = (ViewGroup) getChildAt(1);

        //将底部显示列放到中间
        //bottomY:底部
        //midY：中间
        //topY：顶部
        mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), 0, (int)midY, ANI_TIME);//放在中间
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        View view = null;

        if (mTopView.getChildCount() > 0) {
            view = mTopView.getChildAt(0);
        }

        if (view != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = ev.getX();//按下时 的X坐标
                    downY = ev.getY();//按下时 的Y坐标
                    L.e("=======按下时X："+downX);
                    L.e("=======按下时Y："+downY);
//                    if (!mScroller.computeScrollOffset()) {
//                        Toast.makeText(getContext(), mScroller.getCurrY()+"", Toast.LENGTH_SHORT).show();
//                        float distance_1 = ev.getY() - mLastActionDownY;
//                        if (distance_1 < 0 && !isViewAtTop(mTopView) ) {//pull
//                            if(mScroller.getCurrY() <= -1460){
//                                break;
//                            }
//                            mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), 0, -560, ANI_TIME);
//                        }
//                    }
                    mLastActionDownY = ev.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float distance = ev.getRawY() - mLastActionDownY;
                    mLastActionDownY = ev.getRawY();
                    mLastMoveEvent = ev;
                    if (!mScroller.computeScrollOffset()) {
                        if (distance > 0 && isViewAtTop(view)) {//pull down
                            if (Math.abs(mTopView.getScrollY() - distance) > mBottomView.getMeasuredHeight()) {//avoid out of bottom boundary
                                mTopView.scrollBy(0, -mTopView.getScrollY() - mBottomView.getMeasuredHeight());
                            } else {
                                mTopView.scrollBy(0, (int) -distance);
                            }
                            sendCancelEvent();
                            return true;
                        } else if (distance < 0 && !isViewAtTop(mTopView)) {//pull up
                            if ((distance - mTopView.getScrollY()) < 0) {//avoid out of top boundary
                                mTopView.scrollBy(0, -mTopView.getScrollY());
                            } else {
                                mTopView.scrollBy(0, (int) -distance);
                            }
                            sendCancelEvent();
                            return true;
                        }
                    } else {
                        sendCancelEvent();
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    upX = ev.getX();//离开时的X坐标
                    upY = ev.getY();//离开时的Y坐标
                    L.e("=======抬起时X："+upX);
                    L.e("=======抬起时Y："+upY);
                    float dx= upX-downX;
                    float dy = upY-downY;
                    String action = "";
                    //防止是按下也判断
                    if (Math.abs(dx)>8&&Math.abs(dy)>8) {
                        //通过距离差判断方向
                        int orientation = getOrientation(dx, dy);
                        switch (orientation) {
                            case 'r':
                                action = "右";
                                break;
                            case 'l':
                                action = "左";
                                break;
                            case 't':
                                if (mScroller.getCurrY() == (int)bottomY) {//pull down 从最底部挪到中间
                                    mScroller.startScroll(mTopView.getScrollX(), mTopView.getScrollY(), 0, ((int)midY)-mTopView.getScrollY(), ANI_TIME);
                                    mBottomView.bringToFront();
                                }
                                action = "上";
                                break;
                            case 'b':
                                if (mScroller.getCurrY() == (int)midY) {//pull down 从中间挪到最底部
                                    mScroller.startScroll(mScroller.getCurrX(), mScroller.getCurrY(), 0, -560, ANI_TIME);
                                }
                                action = "下";
                                break;
                        }
//                        Toast.makeText(getContext(), "向" + action + "滑动", Toast.LENGTH_SHORT).show();
                    }

                    if (isInUp()) {//prepare scroll to top
                        mScroller.startScroll(mTopView.getScrollX(), mTopView.getScrollY(), 0, -mTopView.getScrollY(), ANI_TIME);
                    } else if (isInDown()) {//prepare scroll to bottom
                        mScroller.startScroll(mTopView.getScrollX(), mTopView.getScrollY(), 0, -mTopView.getScrollY() - mBottomView.getMeasuredHeight(), ANI_TIME);
                    }
                    invalidate();
                    break;
                default:
                    break;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mTopView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();// 再次调用computeScroll。
        }
    }

    /**
     * 根据距离差判断 滑动方向
     * @param dx X轴的距离差
     * @param dy Y轴的距离差
     * @return 滑动的方向
     */
    private int getOrientation(float dx, float dy) {
        L.e("========X轴距离差："+dx);
        L.e("========Y轴距离差："+dy);
        if (Math.abs(dx)>Math.abs(dy)){
            //X轴移动
            return dx>0?'r':'l';
        }else{
            //Y轴移动
            return dy>0?'b':'t';
        }
    }


    /**
     * detect top view in top half of bottom view
     *
     * @return
     */
    private boolean isInUp() {//在上半部分内
        int y = -mTopView.getScrollY();
        if (y > 0 && y < mBottomView.getMeasuredHeight() / 2) {
//            Toast.makeText(getContext(), "上到了！", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    /**
     * detect top view in bottom half of bottom view
     *
     * @return
     */
    private boolean isInDown() {//在下半部分内
        int y = -mTopView.getScrollY();
        if (y >= mBottomView.getMeasuredHeight() / 2 && y < mBottomView.getMeasuredHeight()) {
//            Toast.makeText(getContext(), "下到了！", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private boolean isViewAtTop(View view) {
        if (view instanceof AbsListView) {//这里可以自己更改代码,判断listview等在什么情况下为拉到顶部,默认为第一个item可见的时候
            final AbsListView absListView = (AbsListView) view;
            return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() == 0 && absListView.getChildAt(0).getTop() >= absListView.getPaddingTop());
        } else {
            return view.getScrollY() == 0;
        }
    }

    /**
     * 滑动过程调用,解决滑动与其他事件冲突
     * solve conflict move event between other event
     */
    private void sendCancelEvent() {
        if (mLastMoveEvent == null) {
            return;
        }
        MotionEvent last = mLastMoveEvent;
        MotionEvent e = MotionEvent.obtain(last.getDownTime(), last.getEventTime() + ViewConfiguration.getLongPressTimeout(), MotionEvent.ACTION_CANCEL, last.getX(), last.getY(), last.getMetaState());
        super.dispatchTouchEvent(e);
    }

}
