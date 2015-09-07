package com.cnwir.pedometer.ui.circle;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.cnwir.pedometer.App;
import com.cnwir.pedometer.R;
import com.cnwir.pedometer.ui.BaseActivity;
import com.cnwir.pedometer.ui.BaseFragment;
import com.cnwir.pedometer.ui.history.HistoryRecordFragment;
import com.cnwir.pedometer.ui.home.HomeFragment;
import com.cnwir.pedometer.utils.ShareUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CircleActivty extends BaseActivity implements View.OnClickListener, OnMenuItemClickListener, OnMenuItemLongClickListener {

    private RadioButton rb_1, rb_2;

    private TextView topBarTitle;

    private ImageView topbar_left_icon, topbar_right_icon;

    private DialogFragment mMenuDialogFragment;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_activty);
        fragmentManager = getSupportFragmentManager();
        initView();
        initShareShow();
    }

    private void initView() {

        topBarTitle = (TextView) findViewById(R.id.top_bar_title);
        topBarTitle.setText("朋友圈");
        topbar_left_icon = (ImageView) findViewById(R.id.top_bar_letf_icon);
        topbar_left_icon.setImageResource(R.mipmap.return_icon);

        topbar_left_icon.setOnClickListener(this);
        topbar_right_icon = (ImageView) findViewById(R.id.top_bar_right_icon);
        topbar_right_icon.setImageResource(R.mipmap.share_icon);
        topbar_right_icon.setOnClickListener(this);
        rb_1 = (RadioButton) findViewById(R.id.circle_rb_1);
        rb_2 = (RadioButton) findViewById(R.id.circle_rb_2);
        rb_1.setOnCheckedChangeListener(listener);
        rb_2.setOnCheckedChangeListener(listener);
        rb_1.setChecked(true);
    }

    CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if (!isChecked) {
                return;
            }
            switch (buttonView.getId()) {

                case R.id.circle_rb_1:
                    replaceFragment(R.id.circle_content_frame, new TodayRankFragment());
                    break;
                case R.id.circle_rb_2:
                    replaceFragment(R.id.circle_content_frame, new HistoryRankFragment());
                default:
                    break;
            }

        }


    };

    protected void replaceFragment(int viewId, BaseFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.top_bar_letf_icon:
                finish();

                break;
            case R.id.top_bar_right_icon:
                mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                break;
            default:
                break;
        }

    }

    /**
     * 初始化分享页面
     */

    private void initShareShow() {

        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);


    }

    /**
     * 获取分享列表
     *
     * @return
     */

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.mipmap.icn_close);
        close.setBgColor(Color.TRANSPARENT);

        MenuObject send = new MenuObject();
        send.setResource(R.drawable.umeng_socialize_sina_on);
        send.setBgColor(Color.TRANSPARENT);

        MenuObject like = new MenuObject();
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.umeng_socialize_qzone_on);
        like.setBitmap(b);
        like.setBgColor(Color.TRANSPARENT);

        MenuObject addFr = new MenuObject();
        addFr.setResource(R.drawable.umeng_socialize_qq_on);
        addFr.setBgColor(Color.TRANSPARENT);

        MenuObject addFav = new MenuObject();
        addFav.setResource(R.drawable.umeng_socialize_wxcircle);
        addFav.setBgColor(Color.TRANSPARENT);

        MenuObject block = new MenuObject();
        block.setResource(R.drawable.umeng_socialize_wechat);
        block.setBgColor(Color.TRANSPARENT);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
        menuObjects.add(addFr);
        menuObjects.add(addFav);
        menuObjects.add(block);
        return menuObjects;
    }

    @Override
    public void onMenuItemClick(View view, int i) {
        String[] shareNotices = getResources().getStringArray(R.array.share_notice);

        Random random = new Random();

        int index = random.nextInt(shareNotices.length - 1);

        String shareNotice = shareNotices[index];

        String downLoadUrl = "http://app.cnwir.com/down/dn.html?t=1&u=jkyd";

        System.out.println("推荐码" + App.getInstance().getUser().getOrangekey());

        String title = "足迹  推荐码：" + App.getInstance().getUser().getOrangekey();
        switch (i) {
            case 1:
                ShareUtils.showShare(CircleActivty.this, SHARE_MEDIA.SINA, title, shareNotice,downLoadUrl, "");
                break;
            case 2:
                ShareUtils.showShare(CircleActivty.this, SHARE_MEDIA.QZONE, title, shareNotice, downLoadUrl, "");
                break;
            case 3:
                ShareUtils.showShare(CircleActivty.this, SHARE_MEDIA.QQ, title, shareNotice, downLoadUrl, "");
                break;
            case 4:
                ShareUtils.showShare(CircleActivty.this, SHARE_MEDIA.WEIXIN_CIRCLE, title, shareNotice,downLoadUrl, "");
                break;

            case 5:
                ShareUtils.showShare(CircleActivty.this, SHARE_MEDIA.WEIXIN, title, shareNotice, downLoadUrl, "");
                break;
            default:
                break;
        }


    }

    @Override
    public void onMenuItemLongClick(View view, int i) {

    }
}
