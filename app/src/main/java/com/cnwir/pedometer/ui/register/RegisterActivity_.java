package com.cnwir.pedometer.ui.register;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cnwir.pedometer.App;
import com.cnwir.pedometer.AppManager;
import com.cnwir.pedometer.MainActivity;
import com.cnwir.pedometer.R;
import com.cnwir.pedometer.domain.User;
import com.cnwir.pedometer.service.NormalPostRequest;
import com.cnwir.pedometer.ui.BaseActivity;
import com.cnwir.pedometer.ui.history.HistoryRecordFragment;
import com.cnwir.pedometer.ui.home.HomeFragment;
import com.cnwir.pedometer.ui.login.LoginActivity;
import com.cnwir.pedometer.utils.DESPlus;
import com.cnwir.pedometer.utils.RequestUrl;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class RegisterActivity_ extends BaseActivity implements View.OnClickListener {

    private TextView topBar;

    private ImageView returnBtn;

    private EditText et_userName, et_password, et_orangekey, et_nickName, et_phoneNum;

    private String userName, password, orangekey, nickname, phoneNum;

    private Button btn_register;

    private TextView tv_login;

    private ProgressDialog pd;

    private RadioButton rb_1, rb_2;

    private int sex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);
        AppManager.getAppManager().addActivity(this);

        initView();


    }


    public void initView() {


        topBar = (TextView) findViewById(R.id.top_bar_title);
        topBar.setText("用户注册");
        returnBtn = (ImageView) findViewById(R.id.top_bar_letf_icon);
        et_userName = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        et_orangekey = (EditText) findViewById(R.id.et_orangekey);
        et_nickName = (EditText) findViewById(R.id.et_nickname);
        et_phoneNum = (EditText) findViewById(R.id.et_phonenum);

        btn_register = (Button) findViewById(R.id.btn_register);
        tv_login = (TextView) findViewById(R.id.tv_login);
        returnBtn.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);

        rb_1 = (RadioButton) findViewById(R.id.register_rb_1);
        rb_2 = (RadioButton) findViewById(R.id.register_rb_2);
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
                    sex = 1;
                    break;
                case R.id.home_rb_2:
                    sex = 2;
                    break;
                default:
                    break;
            }

        }


    };

    private void toMainAcitivty(User user) {

        App.getInstance().setUser(user);
        System.out.println("user = " + user.toString());

        Intent intent = new Intent(RegisterActivity_.this, MainActivity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        finish();


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.top_bar_letf_icon:
                break;

            case R.id.btn_register:
                if (checkFormat()) {

                    register();
                }
                break;
            case R.id.tv_login:
                toLogin();
                break;

            default:
                break;


        }

    }

    private void toLogin() {
        Intent intent = new Intent(RegisterActivity_.this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        finish();
    }

    private void register() {

        pd = new ProgressDialog(RegisterActivity_.this);

        pd.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);
        pd.setMessage("请稍候...");
        pd.show();
        Map<String, String> map = new HashMap<String, String>();

        map.put("username", userName);
        map.put("password", password);
        map.put("orangekey", orangekey);
        map.put("nickname", nickname);
        map.put("sex", sex + "");

        map.put("phone", phoneNum);

        NormalPostRequest request = new NormalPostRequest(RequestUrl.register(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {

                    pd.dismiss();

                    JSONObject obj = new JSONObject(jsonObject.toString());

                    if (obj.getInt("ret") != 0) {

                        Toast.makeText(RegisterActivity_.this, "注册失败,请重试", Toast.LENGTH_SHORT).show();
                        return;
                    }else{

                        JSONObject objData = obj.getJSONObject("data");
                        Gson gson = new Gson();
                        User user = new User();
                        user = gson.fromJson(objData.toString(), User.class);
                        toMainAcitivty(user);

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity_.this, "注册失败,请重试", Toast.LENGTH_SHORT).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                pd.dismiss();
                Toast.makeText(RegisterActivity_.this, "注册失败,请重试", Toast.LENGTH_SHORT).show();
            }
        }, map);

        executeRequest(request);


    }

    private boolean checkFormat() {
        userName = et_userName.getText().toString().trim();
        password = et_password.getText().toString().trim();
        orangekey = et_orangekey.getText().toString().trim();

        nickname = et_nickName.getText().toString().trim();
        phoneNum = et_phoneNum.getText().toString().trim();
        if (TextUtils.isEmpty(userName)) {

            Toast.makeText(RegisterActivity_.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {

            Toast.makeText(RegisterActivity_.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(orangekey)) {

            Toast.makeText(RegisterActivity_.this, "推荐码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(nickname)) {

            Toast.makeText(RegisterActivity_.this, "真实姓名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;


    }
}
