package com.cnwir.pedometer.ui.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.cnwir.pedometer.App;
import com.cnwir.pedometer.AppManager;
import com.cnwir.pedometer.MainActivity;
import com.cnwir.pedometer.R;
import com.cnwir.pedometer.domain.User;
import com.cnwir.pedometer.service.NormalPostRequest;
import com.cnwir.pedometer.ui.BaseActivity;
import com.cnwir.pedometer.ui.register.RegisterActivity_;
import com.cnwir.pedometer.utils.RequestUrl;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by heaven on 2015/7/28.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{


    private EditText et_username, et_password;

    private Button btn_login;

    private String userName, password;


    private TextView tv_register;

    private ProgressDialog pd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppManager.getAppManager().addActivity(this);
        initView();
    }

    private void initView(){
        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

           case R.id.btn_login:

            if(checkFormat()){
                login();
            }

            break;

            case R.id.tv_register:

                register();
                break;
           default:
               break;
        }

    }

    private void login() {
        pd = new ProgressDialog(LoginActivity.this);
        pd.setProgressStyle(ProgressDialog.THEME_HOLO_LIGHT);
        pd.setMessage("正在登录...");
        pd.show();
        Map<String, String> map = new HashMap<String, String>();

        map.put("username", userName);
        map.put("password", password);
        NormalPostRequest request = new NormalPostRequest(RequestUrl.login(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                pd.dismiss();
                try {
                    JSONObject obj = new JSONObject(jsonObject.toString());
                    if(obj.getInt("ret") != 0){
                        Toast.makeText(LoginActivity.this, "登录失败，请重新登录", Toast.LENGTH_SHORT).show();
                        return ;
                    }

                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    JSONObject objData = obj.getJSONObject("data");
                    Gson gson = new Gson();
                    User user = new User();
                    user = gson.fromJson(objData.toString(), User.class);
                    System.out.println("登录成功返回数据 " + user.getOrangekey());
                    App.getInstance().setUser(user);
                    toMainActivity(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

                pd.dismiss();
                Toast.makeText(LoginActivity.this, "登录失败，请重新登录", Toast.LENGTH_SHORT).show();
            }
        }, map);

        executeRequest(request);
    }

    private void toMainActivity(User user) {

        App.getInstance().setUser(user);
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        finish();

    }

    private void register(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity_.class);

        startActivity(intent);
        overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
        finish();
    }

    private boolean checkFormat(){
        userName = et_username.getText().toString().trim();
        password = et_password.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {

            Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(password)) {

            Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
}
