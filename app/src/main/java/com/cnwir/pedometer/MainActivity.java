package com.cnwir.pedometer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.pedometer.ui.BaseActivity;
import com.cnwir.pedometer.ui.BaseFragment;
import com.cnwir.pedometer.ui.history.HistoryRecordFragment;
import com.cnwir.pedometer.ui.home.HomeFragment;
import com.cnwir.pedometer.ui.loadding.GuideAcitity;
import com.cnwir.pedometer.ui.login.LoginActivity;
import com.cnwir.pedometer.ui.register.RegisterActivity_;
import com.cnwir.pedometer.ui.update.UpdateChecker;
import com.cnwir.pedometer.utils.DoubleClickExitHelper;
import com.cnwir.pedometer.utils.RequestUrl;
import com.cnwir.pedometer.utils.ShareUtils;
import com.cnwir.pedometer.utils.SharedPrefUtils;
import com.cnwir.pedometer.utils.VersionManager;
import com.cnwir.pedometer.view.ScaleProgress;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends BaseActivity implements View.OnClickListener, OnMenuItemClickListener, OnMenuItemLongClickListener {

    private ScaleProgress scaleProgress;

    private RadioButton rb_1, rb_2;
    private int step;
    private TextView topBarTitle;
    private ImageView topbar_left_icon, topbar_right_icon;

    private DoubleClickExitHelper exitApp;

    private DialogFragment mMenuDialogFragment;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppManager.getAppManager().addActivity(this);

        exitApp = new DoubleClickExitHelper(this);


        if (App.getInstance().getUser() == null) {

//            goRegister();
            login();

        } else {

//            submit();
            fragmentManager = getSupportFragmentManager();
            initView();
            initShareShow();
            checkUpdate();
        }


    }

    /**
     * 检查版本更新
     */

    private void checkUpdate() {
        JsonObjectRequest request = new JsonObjectRequest(RequestUrl.getVersionUpdate(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println("版本更新" + jsonObject.toString());
                try {
                    if (jsonObject == null) {
                        return;
                    }
                    int error = (int) jsonObject.get("error");
                    if (error != 0) {

                        return;
                    }
                    JSONObject json = (JSONObject) jsonObject.get("data");


                    UpdateChecker checker = new UpdateChecker(MainActivity.this);
                    checker.checkForUpdates(json.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        executeRequest(request);

    }


    /**
     * 描述：跳转到注册页面
     */
    private void goRegister() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity_.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        finish();


    }

    private void login() {

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        finish();
    }


    private void initView() {
        topBarTitle = (TextView) findViewById(R.id.top_bar_title);
        topBarTitle.setText(getResources().getString(R.string.top_bar_title));
        topbar_left_icon = (ImageView) findViewById(R.id.top_bar_letf_icon);
        topbar_left_icon.setImageResource(R.mipmap.return_icon);

        topbar_left_icon.setOnClickListener(this);
        topbar_right_icon = (ImageView) findViewById(R.id.top_bar_right_icon);
        topbar_right_icon.setImageResource(R.mipmap.share_icon);
        topbar_right_icon.setOnClickListener(this);
        rb_1 = (RadioButton) findViewById(R.id.home_rb_1);
        rb_2 = (RadioButton) findViewById(R.id.home_rb_2);
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

                case R.id.home_rb_1:
                    replaceFragment(R.id.content_frame, new HomeFragment());
                    break;
                case R.id.home_rb_2:
                    replaceFragment(R.id.content_frame, new HistoryRecordFragment());
                    break;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return exitApp.doubleClickExit(keyCode, event);
        }

        return super.onKeyDown(keyCode, event);
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
                ShareUtils.showShare(MainActivity.this, SHARE_MEDIA.SINA, title, shareNotice, downLoadUrl, "");
                break;
            case 2:
                ShareUtils.showShare(MainActivity.this, SHARE_MEDIA.QZONE, title, shareNotice, downLoadUrl, "");
                break;
            case 3:
                ShareUtils.showShare(MainActivity.this, SHARE_MEDIA.QQ, title, shareNotice, downLoadUrl, "");
                break;
            case 4:
                ShareUtils.showShare(MainActivity.this, SHARE_MEDIA.WEIXIN_CIRCLE, title, shareNotice, downLoadUrl, "");
                break;

            case 5:
                ShareUtils.showShare(MainActivity.this, SHARE_MEDIA.WEIXIN, title, shareNotice, downLoadUrl, "");
                break;
            default:
                break;
        }


    }

    @Override
    public void onMenuItemLongClick(View view, int i) {

    }

}
