//package com.cnwir.pedometer.ui.circle;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ListView;
//
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.cnwir.pedometer.R;
//import com.cnwir.pedometer.ui.BaseFragment;
//import com.google.gson.Gson;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import in.srain.cube.views.ptr.PtrDefaultHandler;
//import in.srain.cube.views.ptr.PtrFrameLayout;
//import in.srain.cube.views.ptr.PtrHandler;
//
///**
// * Created by heaven on 2015/7/31.
// */
//public class GeneralListFragment extends BaseFragment {
//
//    private PtrFrameLayout mPtrFrameLayout;
//
//    private ListView mListView;
//
//
//    public static GeneralListFragment newInstance(String url) {
//
//        GeneralListFragment fragment = new GeneralListFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("url", url);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.generallistlayout, container, false);
//
//        initView(view);
//        return view;
//
//    }
//
//    private void initView(View view) {
//        mPtrFrameLayout = (PtrFrameLayout) view.findViewById(R.id.load_more_list_view_ptr_frame);
//        mPtrFrameLayout.setLoadingMinTime(1000);
//        mPtrFrameLayout.setPtrHandler(new PtrHandler() {
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//
//                // here check list view, not content.
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
//            }
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//
//                mPtrFrameLayout.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        // ptr
//                        mPtrFrameLayout.refreshComplete();
//
//                    }
//                }, 2000);
//            }
//
//        });
//
//        // list view
//        mListView = (ListView) view.findViewById(R.id.load_more_list_view);
//
//    }
//
//    private void getData(String url) {
//
//        JsonObjectRequest request = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//                try {
//                    if (jsonObject == null) {
//                        return;
//                    }
//
//                    if (jsonObject.getInt("ret") != 0) {
//                        return;
//                    }
//                    JSONArray array = jsonObject.getJSONArray("data");
//                    Gson gson = new gson();
//                    for(int i = 0; i < array.length(); i++){
//
//
//                    }
//
//
//                } catch (JSONExceptionption e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//
//            }
//        });
//    }
//}
