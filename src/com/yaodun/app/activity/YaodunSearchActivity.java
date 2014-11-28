
package com.yaodun.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.qianjiang.framework.util.EvtLog;
import com.yaodun.app.R;

/**
 * 关于界面
 * 
 * @author zou.sq
 */
public class YaodunSearchActivity extends YaodunActivityBase implements OnClickListener {

    TextView mTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yaodun_search);
        initView();
    }

    private void initView() {
        // title
        View left = findViewById(R.id.title_with_back_title_btn_left);
        left.setOnClickListener(this);
        // TextView tvLeft = (TextView) findViewById(R.id.tv_title_with_back_left);
        left.setBackgroundResource(R.drawable.btn_back_bg);
        mTitleTv = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
        mTitleTv.setText("大众用药查询");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_with_back_title_btn_left:
                goClassify();
                break;
            default:
                break;
        }
    }

    void goClassify() {
        YaodunActivityGroup parent = (YaodunActivityGroup) getParent();
        parent.goClassify();
    }
}
