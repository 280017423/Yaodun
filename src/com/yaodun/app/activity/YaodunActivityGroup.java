
package com.yaodun.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.yaodun.app.R;

/**
 * 关于界面
 * 
 * @author zou.sq
 */
public class YaodunActivityGroup extends YaodunActivityBase implements OnClickListener {

    RelativeLayout mRlContainer;
    Intent classifyIntent, searchIntent;
    View classifyView, searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yaodun);
        initView();
    }

    private void initView() {
        mRlContainer = (RelativeLayout) findViewById(R.id.layout_container);
        classifyIntent = new Intent(this, YaodunClassifyActivity.class);
        searchIntent = new Intent(this, YaodunSearchActivity.class);
        classifyView = getLocalActivityManager().startActivity(
                classifyIntent.getComponent().getShortClassName(), classifyIntent)
                .getDecorView();
        searchView = getLocalActivityManager().startActivity(
                searchIntent.getComponent().getShortClassName(), searchIntent)
                .getDecorView();

        mRlContainer.removeAllViews();
        mRlContainer.addView(classifyView, new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public void goClassify() {
        mRlContainer.removeAllViews();
        mRlContainer.addView(classifyView, new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }
    public void goSearch() {
        mRlContainer.removeAllViews();
        mRlContainer.addView(searchView, new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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
