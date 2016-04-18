package com.zql.android.cardmenulib;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by scott on 4/18/16.
 *
 */
public class CardMenu extends FrameLayout implements CardMenuItem.OnCardMenuItemClickListener{

    public static final String TAG = "CardMenu";

    public static final int CARD_LP_MATCH_PARENT = FrameLayout.LayoutParams.MATCH_PARENT;

    private static int CARD_ELEVATION = 30;

    private static int CARD_ELEVATION_NONE = 0;

    private int mH ;

    private int mW ;

    private List<CardMenuItem> menuItems ;

    private MenuState mCurrentMenuState = MenuState.Closed ;

    private int mMenuAnimationDuration = 400;

    private int mMenuSize = 220;

    private float mChildElevation;

    private Context mContext ;

    private String[] materialColors = {"#00BCD4","#F44336","#FF4081","#9C27B0","#673AB7","#3F51B5","#2196F3","#03A9F4","#00BCD4","#009688","#4CAF50","#8BC34A","#CDDC39","#FFEB3B","#FFC107","#FF9800","#FF5722","#795548","#9E9E9E","#607D8B"};

    private OnCardMenuSelecetedListener mListener ;

    public interface OnCardMenuSelecetedListener{
        void cardMenuSelected(CardMenuItem item);
    }
    public CardMenu(final Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        menuItems = new ArrayList<>(4);
        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                mH = getMeasuredHeight();
                mW = getMeasuredWidth();
                Log.d(TAG, "mH = " + mH + "    mW = " + mW);
                return false;
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        if(childCount > 1){
            throw new RuntimeException("CardMenu just support one child view");
        }
        View child = getChildAt(0);
        child.setElevation(CARD_ELEVATION_NONE - 1);


    }

    public void initMenus(int[] nameIds,int[] iconIds,int[] menuColors){
        if(nameIds.length != iconIds.length){
            throw  new RuntimeException("nameIds.length must equals iconIds.length");
        }
        if(nameIds.length > materialColors.length){
            throw  new RuntimeException("just support menu length : " + materialColors.length);
        }
        int[] colors ;
        if(menuColors != null){
            if(nameIds.length != menuColors.length){
                throw  new RuntimeException("nameIds.length must equals menuColors.length");
            }
            colors = menuColors;
        }else{
            colors = new int[nameIds.length];
        }

        Random r = new Random();
        for(int j = 0;j<colors.length;j++){

            int c;
            do {
                c = Color.parseColor(materialColors[r.nextInt(materialColors.length)]);
            }while (isContains(colors,c));

            colors[j] = c ;
        }
        for(int i = 0;i<nameIds.length;i++){
            CardMenuItem menu = new CardMenuItem(mContext,i,nameIds[i],iconIds[i]);
            menu.setCardBackgroundColor(colors[i]);
            if(i == 0){
                menu.setCardElevation(CARD_ELEVATION);
            }else{
                menu.setCardElevation(CARD_ELEVATION_NONE);
            }

            CardMenuItem.LayoutParams clp = new CardMenuItem.LayoutParams(CARD_LP_MATCH_PARENT,mMenuSize);
            clp.setMargins(0,0,0,0);
            menu.setLayoutParams(clp);
            menu.setOnMenuClickListener(this);
            this.addView(menu);
            menuItems.add(menu);
        }
    }

    private boolean isContains(int[] colors,int color){
        for(int i = 0;i < colors.length ;i++){
            if(colors[i] == color){
                return true;
            }
        }
        return false ;
    }
    private void initMenusElevation(){
        for(int i = 0 ; i < menuItems.size();i++){
            menuItems.get(i).setCardElevation(CARD_ELEVATION - i * 2);
        }
    }
    private void changeMenuPosition(int value){
        for(CardMenuItem item : menuItems){
            int index= item.getIndex();
            int marginTop = value*index;
            CardMenuItem.LayoutParams clp = (LayoutParams) item.getLayoutParams();
            clp.setMargins(0, marginTop, 0, 0);
            item.setLayoutParams(clp);
        }
    }

    private void changeMenuSize(int value){
        for(CardMenuItem item : menuItems){
            CardMenuItem.LayoutParams clp = (LayoutParams) item.getLayoutParams();
            if(value < mMenuSize){
                clp.height = mMenuSize;
            }else{
                clp.height = value;
            }
            item.setLayoutParams(clp);
            item.onMenuAction(value * 1.0/(mH/menuItems.size()));
        }
    }

    @Override
    public void onCardMenuClick(CardMenuItem cardMenuItem) {
        touchMenu(cardMenuItem);
    }

    private void touchMenu(final CardMenuItem cardMenuItem){
        if(mCurrentMenuState == MenuState.Closing || mCurrentMenuState == MenuState.Opening){
            return ;
        }
        ValueAnimator animator;
        int start = 0, stop = 0;

        if(mCurrentMenuState == MenuState.Closed){
            start = 0;
            stop = mH/menuItems.size();
        }
        if(mCurrentMenuState == MenuState.Opened){
            start = mH/menuItems.size();
            stop = 0;
        }
        animator = ValueAnimator.ofInt(start,stop);
        animator.setDuration(mMenuAnimationDuration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                changeMenuPosition(value);
                changeMenuSize(value);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (mCurrentMenuState == MenuState.Closed) {
                    mCurrentMenuState = MenuState.Opening;
                    //打开菜单时重新对菜单进行深度初始化
                    initMenusElevation();
                }
                if (mCurrentMenuState == MenuState.Opened) {
                    mCurrentMenuState = MenuState.Closing;
                    //关闭菜单是，将被选择的菜单的高度设置为最大，使其出现在最上方
                    for(CardMenuItem item : menuItems){
                        if(item.getIndex() != cardMenuItem.getIndex()){
                            item.setCardElevation(CARD_ELEVATION_NONE);
                        }
                    }
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCurrentMenuState == MenuState.Opening) {
                    mCurrentMenuState = MenuState.Opened;
                }
                if (mCurrentMenuState == MenuState.Closing) {
                    mCurrentMenuState = MenuState.Closed;
                    if(mListener != null){
                        mListener.cardMenuSelected(cardMenuItem);
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    public void setOnCardMenuSelectedListener(OnCardMenuSelecetedListener listener){
        mListener = listener;
    }

    enum MenuState{
        Opening,Closing,Opened,Closed;
    }
}
