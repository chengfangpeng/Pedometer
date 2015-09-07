package com.cnwir.pedometer.ui.loadding;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cnwir.pedometer.MainActivity;
import com.cnwir.pedometer.R;
import com.cnwir.pedometer.ui.BaseActivity;


/**
 * Created by heanvn on 2015/6/26.
 */
public class GuideAcitity extends BaseActivity {


    private static final int NUM_PAGES = 5;

    private ViewPager guidePager;

    private PagerAdapter guidePagerAdpater;

    private Button skip, done;

    private ImageView next;

    private LinearLayout guideCircle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        setContentView(R.layout.activity_guide);
        guidePager = (ViewPager) findViewById(R.id.guide_pager);
        guidePagerAdpater = new GuidePagerAdapter(getSupportFragmentManager());
        guidePager.setAdapter(guidePagerAdpater);
        skip = (Button) findViewById(R.id.guide_skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGuide();
            }
        });
        next = (ImageView) findViewById(R.id.guide_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guidePager.setCurrentItem(guidePager.getCurrentItem() + 1, true);
            }
        });

        done = (Button) findViewById(R.id.guide_done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endGuide();
            }
        });

        guidePager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {


            }

            @Override
            public void onPageSelected(int position) {

                setIndexCircle(position);
                Log.d("TAG", position + "");

                if (position == NUM_PAGES - 2) {

                    next.setVisibility(View.GONE);
                    skip.setVisibility(View.GONE);
                    done.setVisibility(View.GONE);

                }else if (position < NUM_PAGES - 2){

                    next.setVisibility(View.GONE);
                    skip.setVisibility(View.GONE);
                    done.setVisibility(View.GONE);
                }else if(position == NUM_PAGES - 1){
                    endGuide();

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        buildCircles();
    }

    private void buildCircles() {
        guideCircle = (LinearLayout) findViewById(R.id.guide_circle);
        float scale = getResources().getDisplayMetrics().density;

        int padding = (int) ((int) scale * 5 + 0.5f);

        for(int i = 0; i < NUM_PAGES - 1; i++){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.mipmap.ic_swipe_indicator_white_18dp);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setAdjustViewBounds(true);
            imageView.setPadding(padding, 0, padding, 0);
            guideCircle.addView(imageView);



        }
        setIndexCircle(0);

    }

    /**
     *
     * 描述：设置小圆点
     * */
    private void setIndexCircle(int position) {

        if(position < NUM_PAGES){
            for (int i = 0; i < NUM_PAGES - 1; i++){
                ImageView imageView = (ImageView) guideCircle.getChildAt(i);
                if(i == position){

                    imageView.setColorFilter(getResources().getColor(R.color.text_selected));
                }else{

                    imageView.setColorFilter(getResources().getColor(android.R.color.transparent));
                }
            }

        }
    }


    /**
     * 描述：结束引导页
     */
    private void endGuide() {
        Intent intent = new Intent();
        intent.setClass(GuideAcitity.this, MainActivity.class);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_left_in,
                R.anim.slide_right_out);

        finish();


    }

    public class GuidePagerAdapter extends FragmentStatePagerAdapter {


        public GuidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            GuideFragment fg = null;

            switch (position) {
                case 0:
                    fg = GuideFragment.newInstance(R.layout.guide_fragment_1);
                    break;
                case 1:
                    fg = GuideFragment.newInstance(R.layout.guide_fragment_2);
                    break;
                case 2:

                    fg = GuideFragment.newInstance(R.layout.guide_fragment_3);
                    break;
                case 3:
                    fg = GuideFragment.newInstance(R.layout.guide_fragment_4);
                    break;
                case 4:
                    fg = GuideFragment.newInstance(R.layout.guide_fragment_5);
                    break;


            }
            return fg;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onBackPressed() {

        if(guidePager.getCurrentItem() == 0){

            super.onBackPressed();
        }else{

            guidePager.setCurrentItem(guidePager.getCurrentItem() - 1);
        }

    }
}
