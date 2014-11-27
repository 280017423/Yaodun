package com.yaodun.app.activity;

import com.qianjiang.framework.util.EvtLog;
import com.zxing.decoding.QrCodeActivityBase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.Tag;
import android.util.Log;

public class QrCodeActivity extends QrCodeActivityBase {

    public static Intent getStartActIntent(Context from){
        Intent intent = new Intent(from, QrCodeActivity.class);
        return intent;
    }
    
    @Override
    protected void onDecode(String barCode, Bitmap barCodeImg) {
        super.onDecode(barCode, barCodeImg);
        EvtLog.d(TAG, "onDecode, "+barCode);
        
        finish();
    }
}
