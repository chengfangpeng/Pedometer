/**
 * 
 */

package com.cnwir.pedometer.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.cnwir.pedometer.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;

/**
 * 
 */
public class CustomShareBoard extends PopupWindow implements OnClickListener {

	private UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private Activity mActivity;

	public CustomShareBoard(Activity activity) {
		super(activity);
		this.mActivity = activity;
		initView(activity);
	}

	@SuppressWarnings("deprecation")
	private void initView(Activity context) {
		View rootView = LayoutInflater.from(context).inflate(
				R.layout.custom_board, null);

		rootView.findViewById(R.id.wechat).setOnClickListener(this);
		rootView.findViewById(R.id.wechat_circle).setOnClickListener(this);
		rootView.findViewById(R.id.qq).setOnClickListener(this);
		rootView.findViewById(R.id.qzone).setOnClickListener(this);
		rootView.findViewById(R.id.sina_weibo).setOnClickListener(this);
		int h = context.getWindowManager().getDefaultDisplay().getHeight();
		int w = context.getWindowManager().getDefaultDisplay().getWidth();
		setContentView(rootView);
		setWidth(4 * w / 5);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		this.setAnimationStyle(R.style.share_popwindow_anim);

		setBackgroundDrawable(new BitmapDrawable());
		setTouchable(true);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.wechat:
			performShare(SHARE_MEDIA.WEIXIN);
			break;
		case R.id.wechat_circle:
			performShare(SHARE_MEDIA.WEIXIN_CIRCLE);
			break;
		case R.id.qq:
			performShare(SHARE_MEDIA.QQ);
			break;
		case R.id.qzone:
			performShare(SHARE_MEDIA.QZONE);
			break;

		case R.id.sina_weibo:
			performShare(SHARE_MEDIA.SINA);
			break;
//		case R.id.tencent_weibo:
//			performShare(SHARE_MEDIA.TENCENT);
//			break;
//		case R.id.reren:
//			performShare(SHARE_MEDIA.RENREN);
//			break;
//		case R.id.gmail:
//
//			performShare(SHARE_MEDIA.EMAIL);
//			break;
//		case R.id.sms:
//			performShare(SHARE_MEDIA.SMS);
//			break;
		default:
			break;
		}
	}

	private void performShare(SHARE_MEDIA platform) {
		mController.postShare(mActivity, platform, new SnsPostListener() {

			@Override
			public void onStart() {

			}

			@Override
			public void onComplete(SHARE_MEDIA platform, int eCode,
					SocializeEntity entity) {
				String showText = platform.toString();
				if (eCode == StatusCode.ST_CODE_SUCCESSED) {
					showText += "平台分享成功";
				} else {
					showText += "平台分享失败";
				}
				Toast.makeText(mActivity, showText, Toast.LENGTH_SHORT).show();
				dismiss();
			}
		});
	}
	
	

}
