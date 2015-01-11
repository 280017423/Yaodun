package com.pregnancy.app.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.pregnancy.app.util.ConstantSet;
import com.qianjiang.framework.util.EvtLog;
import com.zxing.decoding.QrCodeActivityBase;

public class QrCodeActivity extends QrCodeActivityBase {

    public static Intent getStartActIntent(Context from){
        Intent intent = new Intent(from, QrCodeActivity.class);
        return intent;
    }
    
    @Override
    protected void onDecode(String barCode, Bitmap barCodeImg) {
        super.onDecode(barCode, barCodeImg);
        EvtLog.d(TAG, "onDecode, "+barCode);
        Intent data = new Intent(ConstantSet.ACTION_QRCODE_OK);
        data.putExtra(ConstantSet.EXTRA_QRCODE, barCode);
        sendBroadcast(data);
        finish();
    }
}
