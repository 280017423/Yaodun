
package com.yaodun.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qianjiang.framework.util.EvtLog;
import com.yaodun.app.R;

/**
 * 药盾--查询页面
 * 
 * @author zou.sq
 */
public class YaodunSearchActivity extends YaodunActivityBase implements OnClickListener {

    TextView mTvTitle;
    EditText mEtName;

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
        mTvTitle = (TextView) findViewById(R.id.title_with_back_title_btn_mid);
        mTvTitle.setText("大众用药查询");
        
        mEtName = (EditText) findViewById(R.id.et_name);
        ImageView ivQrcode = (ImageView) findViewById(R.id.iv_qrcode);
        ivQrcode.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_with_back_title_btn_left:
                goClassify();
                break;
            case R.id.iv_qrcode:
                startActivity(QrCodeActivity.getStartActIntent(mContext));
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
