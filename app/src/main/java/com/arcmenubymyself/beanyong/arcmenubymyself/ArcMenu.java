package com.arcmenubymyself.beanyong.arcmenubymyself;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

/**
 * Created by BeanYong on 2015/7/29.
 */
public class ArcMenu extends ViewGroup {

    /**
     * 动画的持续时长
     */
    private final static int ANIMATION_DURATION = 300;

    /**
     * 子菜单到根菜单的半径
     */
    private int mRadius = 0;

    /**
     * 屏幕的宽度
     */
    private int mScreenWidth = 0;

    /**
     * 屏幕的高度
     */
    private int mScreenHeight = 0;

    /**
     * 子菜单数目
     */
    private int mSubMenuCount = 0;

    /**
     * 菜单的状态，open为true，close为false，默认为false
     */
    private boolean isOpen = false;

    private OnArcMenuItemClick mOnArcMenuItemClick = null;

    /**
     * 用于点击子菜单的回调接口
     */
    public interface OnArcMenuItemClick {
        public void onArcMenuItemClick(View child);
    }

    public void setmOnArcMenuItemClick(OnArcMenuItemClick onArcMenuItemClick) {
        this.mOnArcMenuItemClick = onArcMenuItemClick;
    }

    public ArcMenu(Context context) {
        this(context, null);
    }

    public ArcMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ArcMenu, defStyleAttr, 0);
        mRadius = (int) a.getDimension(R.styleable.ArcMenu_radius,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics()));
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mSubMenuCount = getChildCount() - 1;//子菜单个数
        for (int i = 0; i <= mSubMenuCount; i++) {
            measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutRootMenu();
        layoutChild();
    }

    /**
     * 布局根菜单
     */
    private void layoutRootMenu() {
        mScreenWidth = getMeasuredWidth();//屏幕宽度
        mScreenHeight = getMeasuredHeight();//屏幕高度

        View root = getChildAt(0);
        int rootWidth = root.getMeasuredWidth();
        int rootHeight = root.getMeasuredHeight();

        int layoutL = mScreenWidth - rootWidth;
        int layoutT = mScreenHeight - rootHeight;
        root.layout(layoutL, layoutT, mScreenWidth, mScreenHeight);

        root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                rootMenuAnim(v);
                childMenuAnim();
                changeMenuStatus();
            }
        });
    }

    /**
     * 切换菜单状态
     */
    private void changeMenuStatus() {
        if (isOpen) {
            isOpen = false;
        } else {
            isOpen = true;
        }
    }

    /**
     * 子菜单的动画
     */
    private void childMenuAnim() {
        for (int i = 0; i < mSubMenuCount; i++) {
            View child = getChildAt(i + 1);
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (mSubMenuCount - 1) * i));
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (mSubMenuCount - 1) * i));

            TranslateAnimation transAnim = null;
            RotateAnimation rotateAnim = null;

            if (isOpen) {
                transAnim = new TranslateAnimation(0, cl, 0, ct);
                rotateAnim = new RotateAnimation(0, 720,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            } else {
                transAnim = new TranslateAnimation(cl, 0, ct, 0);
                rotateAnim = new RotateAnimation(720, 0,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            }

            AnimationSet animSet = new AnimationSet(true);
            animSet.addAnimation(rotateAnim);
            animSet.addAnimation(transAnim);
            animSet.setDuration(ANIMATION_DURATION);
            animSet.setStartOffset(i * 20);
            if (!isOpen) {//由于changeMenuStatus()方法还没有调用，所以判断相反
                animSet.setFillAfter(true);
            }
            child.startAnimation(animSet);
        }
    }

    /**
     * 根布局动画
     *
     * @param root
     */
    private void rootMenuAnim(View root) {
        RotateAnimation rotateAnim = null;
        if (isOpen) {
            rotateAnim = new RotateAnimation(135, 0,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            rotateAnim = new RotateAnimation(0, 135,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }

        rotateAnim.setDuration(ANIMATION_DURATION);
        rotateAnim.setFillAfter(true);
        root.startAnimation(rotateAnim);
    }

    /**
     * 布局子菜单
     */
    private void layoutChild() {
        for (int i = 0; i < mSubMenuCount; i++) {
            int cl = (int) (mRadius * Math.sin(Math.PI / 2 / (mSubMenuCount - 1) * i));//子菜单距离根菜单的水平距离
            int ct = (int) (mRadius * Math.cos(Math.PI / 2 / (mSubMenuCount - 1) * i));//子菜单距离根菜单的竖直距离
            View child = getChildAt(i + 1);
            int childWidth = child.getMeasuredWidth();//子菜单宽度
            int childHeight = child.getMeasuredHeight();//子菜单高度

            int layoutL = mScreenWidth - cl - childWidth;//child实际布局的左边界
            int layoutT = mScreenHeight - ct - childHeight;//child实际布局的上边界

            child.layout(layoutL, layoutT, layoutL + childWidth, layoutT + childHeight);
            child.setVisibility(View.GONE);

            final int pos = i + 1;//被点击的child的position

            child.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnArcMenuItemClick != null) {
                        mOnArcMenuItemClick.onArcMenuItemClick(v);
                    }

                    childClickedAnim(pos);
                    rootMenuAnim(getChildAt(0));
                    changeMenuStatus();
                }
            });
        }
    }

    private void childClickedAnim(int pos) {
        for (int i = 1; i <= mSubMenuCount; i++) {
            zoomAlphaAnim(getChildAt(i), i == pos);
        }
    }

    private void zoomAlphaAnim(View child, boolean isZoomBig) {
        ScaleAnimation scaleAnim = null;
        if (isZoomBig) {
            scaleAnim = new ScaleAnimation(1.0f, 4.0f, 1.0f, 4.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            scaleAnim = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        }
        AlphaAnimation alphaAnim = new AlphaAnimation(1.0f, 0.0f);

        AnimationSet animSet = new AnimationSet(true);
        animSet.addAnimation(alphaAnim);
        animSet.addAnimation(scaleAnim);
        animSet.setDuration(ANIMATION_DURATION);
        animSet.setFillAfter(true);
        child.startAnimation(animSet);

        child.setVisibility(View.GONE);
    }
}
