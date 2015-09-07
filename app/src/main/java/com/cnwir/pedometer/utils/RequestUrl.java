package com.cnwir.pedometer.utils;

/**
 * Created by heaven on 2015/7/29.
 */
public class RequestUrl {

    public static final String http = "http://app.cnwir.com/StepCounter/api";

//    public static final String http = "http://192.168.1.220:8081/StepCounter/api";

    public static String register(){

        return http + "/register.html";
    }

    public static String login(){

        return http + "/login.html";
    }

    public static String submitData(){

        return http + "/upload.html";
    }
    public static String get7DayData(){

        return http + "/recentlycounter.html";
    }

    public static String getRank(){

        return http + "/ranking.html";

    }
    public static String getVersionUpdate(){

        return http + "/checkupdate.html";
    }

}
