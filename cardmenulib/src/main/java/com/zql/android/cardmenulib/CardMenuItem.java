package com.zql.android.cardmenulib;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by scott on 4/18/16.
 *
 */
public class CardMenuItem extends CardView {

    private int mIndex = -1;

    private OnCardMenuItemClickListener mMenuClickListener;

    private TextView mText;

    private ImageView mImageView;

    private Context mContext;

    private LinearLayout mContainer;

    private int mImageW ;

    private int mImageH ;

    protected interface OnCardMenuItemClickListener{
        void onCardMenuClick(CardMenuItem cardMenuItem);
    }

    public CardMenuItem(Context context,int index,int textId,int drawableResId) {
        super(context);
        mIndex = index;
        setRadius(0);
        mContext = context;
        mContainer = new LinearLayout(context);
        CardMenuItem.LayoutParams clp = new CardMenuItem.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        clp.gravity = Gravity.CENTER;
        mContainer.setOrientation(LinearLayout.VERTICAL);
        mContainer.setLayoutParams(clp);
        addView(mContainer);
        initIcon(drawableResId);
        initText(textId);
    }

    private void initIcon(int drawableResId){
        mImageView = new ImageView(mContext);
        mImageView.setAdjustViewBounds(true);
        mImageView.setImageDrawable(mContext.getDrawable(drawableResId));
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        rlp.gravity = Gravity.CENTER;
        mImageView.setLayoutParams(rlp);

        mImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                mImageW = mImageView.getMeasuredWidth();
                mImageH = mImageView.getMeasuredHeight();
                onMenuAction(0);
                return false;
            }
        });
        mContainer.addView(mImageView);
    }

    private void initText(int textId){
        mText = new TextView(mContext);
        mText.setText(textId);
        mText.setTextSize(25);
        mText.setTextColor(Color.WHITE);
        mText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        LinearLayout.LayoutParams rlp = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        rlp.gravity = Gravity.CENTER;
        mText.setLayoutParams(rlp);
        mContainer.addView(mText);
    }

    public void setOnMenuClickListener(OnCardMenuItemClickListener listener){
        mMenuClickListener = listener;
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMenuClickListener != null) {
                    mMenuClickListener.onCardMenuClick(CardMenuItem.this);
                }
            }
        });
    }

    public void onMenuAction(double percent){
        LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) mImageView.getLayoutParams();
        llp.width = (int)(mImageW * percent);
        llp.height =(int)(mImageH * percent);
        mImageView.setLayoutParams(llp);
    }

    public void setIndex(int index) {
        this.mIndex = index;
    }

    public int getIndex() {
        return mIndex;
    }
}
