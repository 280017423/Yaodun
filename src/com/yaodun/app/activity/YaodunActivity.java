package com.yaodun.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.yaodun.app.R;

/**
 * 关于界面
 * 
 * @author zou.sq
 */
public class YaodunActivity extends YaodunActivityBase implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_yaodao);
		initView();
	}

	private void initView() {
        Button btnPeople = (Button) findViewById(R.id.btn_people);
        btnPeople.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_people:
                break;
            default:
                break;
        }
    }
}
