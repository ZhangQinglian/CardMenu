package com.zql.android;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zql.android.cardmenulib.CardMenu;
import com.zql.android.cardmenulib.CardMenuItem;

public class MainActivity extends AppCompatActivity implements CardMenu.OnCardMenuSelecetedListener{

    private FrameLayout mMainContent;
    private TextView mContentText;
    int[] names = new int[]{R.string.m_bike,R.string.m_boat,R.string.m_car,R.string.m_airport};

    int[] icons = new int[]{R.mipmap.ic_directions_bike_white_36dp,R.mipmap.ic_directions_boat_white_36dp,R.mipmap.ic_directions_car_white_36dp,R.mipmap.ic_local_airport_white_36dp};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContentText = (TextView) findViewById(R.id.content_text);
        mMainContent = (FrameLayout) findViewById(R.id.main_content);

        CardMenu menu = (CardMenu) findViewById(R.id.cardMenu);

        menu.initMenus(names, icons, null);
        menu.setOnCardMenuSelectedListener(this);

    }

    @Override
    public void cardMenuSelected(CardMenuItem item) {
        switch (item.getIndex()){
            case 0 :
            case 1 :
            case 2 :
            case 3 :
                mContentText.setText(names[item.getIndex()]);
        }
    }
}
