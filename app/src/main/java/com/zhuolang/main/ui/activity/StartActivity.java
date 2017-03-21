package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.utils.SharedPrefsUtil;

/**
 * Created by Administrator on 2016/11/22.
 */
public class StartActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean is_login = SharedPrefsUtil.getValue(StartActivity.this, APPConfig.IS_LOGIN,false);
                Intent intent = new Intent();
                if (is_login)
                    intent.setClass(StartActivity.this, MainActivity.class);
                else
                    intent.setClass(StartActivity.this, LoginActivity.class);
                intent.putExtra("Flagf", "false");
                startActivity(intent);
                finish();
            }
        }, 2000); //停留2秒钟
    }
}
