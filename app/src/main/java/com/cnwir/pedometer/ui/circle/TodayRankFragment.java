package com.cnwir.pedometer.ui.circle;

import android.app.Activity;
import android.app.DownloadManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.pedometer.App;
import com.cnwir.pedometer.R;
import com.cnwir.pedometer.adapter.CircleListViewAdaper;
import com.cnwir.pedometer.domain.Friend;
import com.cnwir.pedometer.service.NormalPostRequest;
import com.cnwir.pedometer.ui.BaseFragment;
import com.cnwir.pedometer.utils.RequestUrl;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by heaven on 2015/7/31.
 */
public class TodayRankFragment extends BaseFragment {

    private PtrClassicFrameLayout mPtrFrameLayout;

    private ListView mListView;

    private List<Friend> datas;

    private CircleListViewAdaper adaper;

    private Activity mActivity;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.generallistlayout, container, false);
        System.out.println("TodayRankFragment");
        initView(view);
        return view;
    }

    private void initView(View view) {

        // list view
        mListView = (ListView) view.findViewById(R.id.load_more_list_view);


        mPtrFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.load_more_list_view_ptr_frame);
        mPtrFrameLayout.setLastUpdateTimeRelateObject(mActivity);
        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                getData();


            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        mPtrFrameLayout.setResistance(1.7f);
        mPtrFrameLayout.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrameLayout.setDurationToClose(200);
        mPtrFrameLayout.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrameLayout.setPullToRefresh(false);
        // default is true
        mPtrFrameLayout.setKeepHeaderWhenRefresh(true);
        mPtrFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrameLayout.autoRefresh();

            }
        }, 100);



    }

    private void getData() {

        Map<String, String> map = new HashMap<String, String>();

        map.put("orangekeyId", App.getInstance().getUser().getOrangekeyId());

        NormalPostRequest request = new NormalPostRequest(RequestUrl.getRank(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                System.out.println(jsonObject.toString());
                try {
                    if (jsonObject == null) {
                        return;
                    }

                    if (jsonObject.getInt("ret") != 0) {
                        return;
                    }
                    datas = new ArrayList<Friend>();
                    JSONArray array = jsonObject.getJSONArray("today");
                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {
                        Friend friend = new Friend();
                        friend = gson.fromJson(array.getJSONObject(i).toString(), Friend.class);
                        datas.add(friend);

                    }

                    adaper = new CircleListViewAdaper(mActivity, datas);
                    mListView.setAdapter(adaper);
                    adaper.notifyDataSetChanged();
                    mPtrFrameLayout.refreshComplete();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                System.out.println(volleyError.toString());

            }
        }, map);


        executeRequest(request);
    }
}
