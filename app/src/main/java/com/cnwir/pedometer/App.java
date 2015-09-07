package com.cnwir.pedometer;

import android.app.Application;
import android.content.Context;

import com.cnwir.pedometer.db.PedometerDB;
import com.cnwir.pedometer.domain.User;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by heaven on 2015/5/15.
 */
public class App extends Application {

    private static Context sContext;

    private static App instance;

    private static User user;

    private PedometerDB pedometerDB;

    public static int CURRENT_STEPS;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        instance = this;
        pedometerDB = PedometerDB.getInstance(sContext);
        initImageLoad(sContext);
    }

    public static App getInstance() {

        return instance;
    }

    public static Context getContext() {

        return sContext;
    }

    /**
     * 初始化Android-Universal-Image-Loader 配置
     * 文档地址 https://github.com/nostra13/Android-Universal-Image-Loader
     */
    private static void initImageLoad(Context context) {

        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public User getUser() {

        if(user == null){

            user = pedometerDB.loadFirstUser();
        }


        return user;


    }

    public void setUser(User user) {

        if (user != null) {
            this.user = user;
            if (pedometerDB.loadUser(user.getId()) != null) {
                pedometerDB.updateUser(user);
            } else {

                pedometerDB.saveUser(user);
            }
        }


    }
}
