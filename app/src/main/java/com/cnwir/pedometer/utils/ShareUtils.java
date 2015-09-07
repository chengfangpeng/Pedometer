package com.cnwir.pedometer.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.cnwir.pedometer.R;
import com.cnwir.pedometer.view.CustomShareBoard;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.bean.StatusCode;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.RenrenShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.TencentWbShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.json.JSONObject;

import javax.xml.transform.ErrorListener;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;

/**
 * Created by heaven on 2015/8/1.
 */
public class ShareUtils {

    /**
     * Create intent with subject and body
     *
     * @param subject
     * @param body
     * @return intent
     */
    public static Intent create(final CharSequence subject,
                                final CharSequence body) {
        Intent intent = new Intent(ACTION_SEND);
        intent.setType("text/plain");
        if (!TextUtils.isEmpty(subject))
            intent.putExtra(EXTRA_SUBJECT, subject);
        intent.putExtra(EXTRA_TEXT, body);
        return intent;
    }

    /**
     * Get body from intent
     *
     * @param intent
     * @return body
     */
    public static String getBody(final Intent intent) {
        return intent != null ? intent.getStringExtra(EXTRA_TEXT) : null;
    }

    /**
     * Get subject from intent
     *
     * @param intent
     * @return subject
     */
    public static String getSubject(final Intent intent) {
        return intent != null ? intent.getStringExtra(EXTRA_SUBJECT) : null;
    }



    private static UMSocialService mController = null;
    private SHARE_MEDIA mPlatform = SHARE_MEDIA.SINA;

    public static void showShare(Context context,SHARE_MEDIA platform , String title, String contents, String targetUrl, String imageUrl) {
        mController = UMServiceFactory.getUMSocialService("com.umeng.share");
        configPlatforms(context);
        // 设置分享的内容
        setShareContent(context, title, contents, targetUrl, imageUrl);
        // mController.openShare((Activity) context, false);

        performShare(context,platform);

    }

    /**
     * 功能描述：设置分享的内容
     *
     * */

    private static void setShareContent(Context context, String title, String contents, String targetUrl,
                                        String imageUrl) {

        UMImage localImage = new UMImage(context, R.mipmap.ic_launcher);
        UMImage urlImage = new UMImage(context, imageUrl);
        urlImage.setTargetUrl(targetUrl);

        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setShareContent(contents + targetUrl);
        weixinContent.setTitle(title);
        weixinContent.setTargetUrl(targetUrl);
        weixinContent.setShareMedia(urlImage);
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setShareContent(contents);
        circleMedia.setTitle(title);
        circleMedia.setShareMedia(urlImage);
        circleMedia.setTargetUrl(targetUrl);
        mController.setShareMedia(circleMedia);

        // 设置renren分享内容
        RenrenShareContent renrenShareContent = new RenrenShareContent();
        renrenShareContent.setShareContent(contents);
        renrenShareContent.setAppWebSite(targetUrl);
        mController.setShareMedia(renrenShareContent);

        // 设置QQ空间分享内容
        QZoneShareContent qzone = new QZoneShareContent();
        qzone.setShareContent(contents);
        qzone.setTargetUrl(targetUrl);
        qzone.setTitle(title);
        qzone.setShareMedia(urlImage);
        mController.setShareMedia(qzone);

        QQShareContent qqShareContent = new QQShareContent();
        qqShareContent.setShareContent(contents);
        qqShareContent.setTitle(title);
        qqShareContent.setTargetUrl(targetUrl);
        qqShareContent.setShareMedia(urlImage);
        mController.setShareMedia(qqShareContent);

//        TencentWbShareContent tencent = new TencentWbShareContent();
//        tencent.setShareContent(contents);
//        // 设置tencent分享内容
//        tencent.setAppWebSite(targetUrl);
//        tencent.setTargetUrl(targetUrl);
//        tencent.setShareMedia(urlImage);
//        tencent.setTitle(title);
//        mController.setShareMedia(tencent);

//        // 设置邮件分享内容， 如果需要分享图片则只支持本地图片
//        MailShareContent mail = new MailShareContent(localImage);
//        mail.setShareContent(contents + targetUrl);
//        // 设置tencent分享内容
//        mail.setTitle(title);
//        mController.setShareMedia(mail);
//
//        // 设置短信分享内容
//        SmsShareContent sms = new SmsShareContent();
//        sms.setShareContent(contents + targetUrl);
//        sms.setShareImage(urlImage);
//        mController.setShareMedia(sms);

        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent(contents + targetUrl);
        sinaContent.setAppWebSite(targetUrl);
        sinaContent.setShareMedia(urlImage);
        sinaContent.setTitle(title);
        mController.setShareMedia(sinaContent);

    }

    private static void configPlatforms(Context context) {

        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler());
//        // 添加腾讯微博SSO授权
//        mController.getConfig().setSsoHandler(new TencentWBSsoHandler());
        // 添加人人网SSO授权
//        RenrenSsoHandler renrenSsoHandler = new RenrenSsoHandler(context, "201874", "28401c0964f04a72a14c812d6132fcef",
//                "3bf66e42db1e4fa9829b955cc300b737");
//        mController.getConfig().setSsoHandler(renrenSsoHandler);

        // 添加QQ、QZone平台
        addQQQZonePlatform(context);

        // 添加微信、微信朋友圈平台
        addWXPlatform(context);

    }

    private static void addWXPlatform(Context context) {

        // 注意：在微信授权的时候，必须传递appSecret
        // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, Constant.APP_ID, Constant.APP_SECRET);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, Constant.APP_ID, Constant.APP_SECRET);
        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();

    }

    private static void addQQQZonePlatform(Context context) {

        String appId = "1104763873";
        String appKey = "tAS5NFKswynoB71U";
        // 添加QQ支持, 并且设置QQ分享内容的target url
        UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler((Activity) context, appId, appKey);
        qqSsoHandler.addToSocialSDK();

        // 添加QZone平台
        QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler((Activity) context, appId, appKey);
        qZoneSsoHandler.addToSocialSDK();

    }

    private static void performShare(final Context context, SHARE_MEDIA platform) {
        mController.postShare(context, platform, new SocializeListeners.SnsPostListener() {

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
                Toast.makeText(context, showText, Toast.LENGTH_SHORT).show();

            }
        });
    }


}
