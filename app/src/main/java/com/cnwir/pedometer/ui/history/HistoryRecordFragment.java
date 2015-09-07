package com.cnwir.pedometer.ui.history;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cnwir.pedometer.App;
import com.cnwir.pedometer.MainActivity;
import com.cnwir.pedometer.R;
import com.cnwir.pedometer.domain.Step;
import com.cnwir.pedometer.domain.User;
import com.cnwir.pedometer.service.NormalPostRequest;
import com.cnwir.pedometer.ui.BaseFragment;
import com.cnwir.pedometer.ui.register.RegisterActivity_;
import com.cnwir.pedometer.utils.DateFormatUtils;
import com.cnwir.pedometer.utils.RequestUrl;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heaven on 2015/7/18.
 */
public class HistoryRecordFragment extends BaseFragment {

    private MainActivity mActivity;

    private LineChart mChart;

    private List<Step> steps;

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();

        view = inflater.inflate(R.layout.history_record_layout, container, false);


        getSevenDayData();


        return view;

    }

    private void initView(View view) {

        mChart = (LineChart) view.findViewById(R.id.history_chart);
        mChart.setBackgroundColor(Color.argb(40, 255, 255, 255));
        mChart.getAxisRight().setEnabled(false);
        mChart.setDescription(null);
        mChart.setTouchEnabled(false);
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setAxisLineColor(Color.RED);
        xAxis.setAxisLineWidth(2f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        YAxis yAxis = mChart.getAxisLeft();
//        yAxis.setTypeface(...); // set a different font
        yAxis.setTextSize(12f); // set the textsize

        int maxValue = 10;

        if(steps != null){

            for(int i = 0; i < steps.size(); i++){
                if(steps.get(i).getNumber() > maxValue){

                    maxValue = steps.get(i).getNumber();
                }
            }
            maxValue = maxValue / 100 * 100 + 100;
        }
        yAxis.setAxisMaxValue(maxValue); // the axis maximum is 100
        yAxis.setTextColor(Color.BLACK);
        yAxis.setAxisLineColor(Color.RED);
        yAxis.setAxisLineWidth(2f);
//        yAxis.setValueFormatter(new MyValueFormatter());
        setData(steps.size(), App.getInstance().getUser().getGoal());

    }

    private void setData(int count, int range) {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < steps.size(); i++) {


            System.out.println(steps.get(i).getDate() + " ---- " +DateFormatUtils.getDayByDate(steps.get(i).getDate()));
            xVals.add(DateFormatUtils.getDayByDate(steps.get(i).getDate()));
//            xVals.add(i + "");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {

            yVals.add(new Entry(steps.get(i).getNumber(), i, "步"));
//            yVals.add(new Entry(2000, i, "步"));
        }

        // create a dataset and give it a type
//
        LineDataSet set1 = new LineDataSet(yVals, DateFormatUtils.getMonthByDate(steps.get(0).getDate()));
        // set1.setFillAlpha(110);
        set1.setFillColor(Color.RED);
        set1.setValueTextColor(Color.BLACK);

        // set the line to be drawn like this "- - - - - -"
//        set1.enableDashedLine(10f, 5f, 0f);
        set1.setColor(Color.RED);
        set1.setCircleColor(Color.RED);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.RED);
//        set1.setDrawFilled(true);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);
    }

    private void getSevenDayData() {

        Map<String, String> map = new HashMap<String, String>();

        map.put("token", App.getInstance().getUser().getToken());

        NormalPostRequest request = new NormalPostRequest(RequestUrl.get7DayData(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    if (jsonObject == null) {

                        return;
                    }

                    JSONObject obj = null;

                    obj = new JSONObject(jsonObject.toString());


                    if (obj.getInt("ret") != 0) {

                        return;
                    } else {
                        steps = new ArrayList<Step>();
                        JSONArray array = obj.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {

                            Step step = new Step();

                            step.setNumber(array.getJSONObject(i).getInt("realcount"));
                            step.setDate(array.getJSONObject(i).getString("day"));


                            steps.add(step);
                        }

                        initView(view);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                System.out.println("获取数据失败" + volleyError.toString());

            }
        }, map);

        executeRequest(request);


    }
}
