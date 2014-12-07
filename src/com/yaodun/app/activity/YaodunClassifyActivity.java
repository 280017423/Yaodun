
package com.yaodun.app.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.yaodun.app.R;
import com.yaodun.app.model.QueryType;

/**
 * 药盾-分类
 * 
 * @author zou.sq
 */
public class YaodunClassifyActivity extends YaodunActivityBase implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yaodun_classify);
        initView();
    }

    private void initView() {
        View btnPeople = findViewById(R.id.iv_people);
        btnPeople.setOnClickListener(this);
        View layoutKid = findViewById(R.id.layout_kid);
        layoutKid.setOnClickListener(this);
        View layoutPregnant = findViewById(R.id.layout_pregnant);
        layoutPregnant.setOnClickListener(this);
        View layoutOld = findViewById(R.id.layout_old);
        layoutOld.setOnClickListener(this);
        View layoutRepeat = findViewById(R.id.layout_repeat);
        layoutRepeat.setOnClickListener(this);
        View layoutAvoid = findViewById(R.id.layout_avoid);
        layoutAvoid.setOnClickListener(this);
        View layoutAntibiotic = findViewById(R.id.layout_antibiotic);
        layoutAntibiotic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int queryType = 0;
        switch (v.getId()) {
            case R.id.iv_people:
                queryType = QueryType.medicine_dazhong;
                break;
            case R.id.layout_kid:
                queryType = QueryType.medicine_ertong;
                break;
            case R.id.layout_avoid:
                queryType = QueryType.medicine_jinji;
                break;
            case R.id.layout_antibiotic:
                queryType = QueryType.medicine_kangshengsu;
                break;
            case R.id.layout_old:
                queryType = QueryType.medicine_laoren;
                break;
            case R.id.layout_repeat:
                queryType = QueryType.medicine_chongfu;
                break;
            case R.id.layout_pregnant:
                queryType = QueryType.medicine_yunfu;
                break;
            default:
                break;
        }
        goSearch(queryType);
    }

    void goSearch(int queryType) {
        YaodunActivityGroup parent = (YaodunActivityGroup) getParent();
        parent.goSearch(queryType);
    }
}
