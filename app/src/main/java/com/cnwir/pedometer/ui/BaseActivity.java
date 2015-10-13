package com.cnwir.pedometer.ui;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cnwir.pedometer.data.RequestManager;


/**
 * Created by heaven on 2015/5/15.
 */
public class BaseActivity extends AppCompatActivity {


    protected void executeRequest(Request<?> request) {
        RequestManager.addRequestQueue(request, this);
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        };
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAllRequest(this);
    }
}
