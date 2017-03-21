package com.zhuolang.main.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.zhuolang.main.R;
import com.zhuolang.main.common.APPConfig;
import com.zhuolang.main.utils.SharedPrefsUtil;

/**
 * Created by Administrator on 2016/11/22.
 */
public class UpdateBookActivity extends Activity implements View.OnClickListener{

    private ImageView iv_findbook;
    private ImageView iv_addbook;
    private ImageView iv_delbook;
    private ImageView iv_updatebook;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatebook);
        iv_addbook = (ImageView) this.findViewById(R.id.image_updateboook_add);
        iv_updatebook = (ImageView) this.findViewById(R.id.image_updateboook_update);
        iv_delbook = (ImageView) this.findViewById(R.id.image_updateboook_delete);
        iv_back = (ImageView) this.findViewById(R.id.image_updatebook_back);
        iv_findbook = (ImageView) this.findViewById(R.id.image_updateboook_find);
        iv_findbook.setOnClickListener(this);
        iv_addbook.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_updatebook.setOnClickListener(this);
        iv_delbook.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        clickImage(v);
    }
    private void clickImage(View v){
        switch (v.getId()){
            case R.id.image_updateboook_add:
                Intent intent = new Intent();
                intent.setClass(UpdateBookActivity.this, AddBookActivity.class);
                this.startActivity(intent);
//                this.overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            case R.id.image_updateboook_update:
                Intent intent1 = new Intent();
                intent1.setClass(UpdateBookActivity.this, UpdateBookListActivity.class);
                this.startActivity(intent1);
//                this.overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            case R.id.image_updateboook_delete:
                Intent intent2 = new Intent();
                intent2.setClass(UpdateBookActivity.this, DelBookListActivity.class);
                this.startActivity(intent2);
//                this.overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            case R.id.image_updatebook_back:

                finish();
                break;
            case R.id.image_updateboook_find:
                Intent intent3 = new Intent();
                intent3.setClass(UpdateBookActivity.this, BookListActivity.class);
                startActivity(intent3);
//                this.overridePendingTransition(R.anim.enter_anim, R.anim.exit_anim);
                break;
            default:
                break;
        }
    }


}
