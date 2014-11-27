package com.zxing.decoding;

import java.io.IOException;
import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.yaodun.app.R;
import com.yaodun.app.activity.YaodunActivityBase;
import com.zxing.camera.CameraManager;
import com.zxing.view.ViewfinderView;

public class QrCodeActivityBase extends YaodunActivityBase implements Callback {
	protected final String TAG = getClass().getSimpleName();

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private TextView txtResult;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;

	private Intent intent;
	private Camera camera;
	private boolean hasStartPreview = false;
	private Parameters parameter;
	private ImageView btnCancel; // 取消
	private TextView btnManual; // 手动输入
	private Button btnOpen; // 开灯
	private boolean isOpen = true; // 控制开关灯
	
	public static final String KEY_BARCODE_RESULT = "barcode_result";
	public static final String KEY_CAPTURE_FOR = "capture_for";
	public static final int CAPTURE_FOR_NONE = 10;
	public static final int CAPTURE_FOR_COMMENT = 11;
	public static final int CAPTURE_FOR_SEARCH = 12;
	public static final int CAPTURE_FOR_BARCODE = 13;
	private int captureFor = CAPTURE_FOR_COMMENT;
	
	//搜到能属于我们自己的产品
	public static final String ACTION_CAPTURE_HAS_MATCH_PRODUCT = "capture_has_matched_product";
	//搜到产品信息，但是不是我们的面膜产品
	public static final String ACTION_CAPTURE_HAS_NOTMATCH_PRODUCT = "capture_has_not_match_product";
	//搜不到产品
	public static final String ACTION_CAPTURE_NO_PRODUCT = "capture_has_product";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);

		captureFor = getIntent().getIntExtra(KEY_CAPTURE_FOR, CAPTURE_FOR_NONE);

		// 初始化 CameraManager
		CameraManager.init(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		txtResult = (TextView) findViewById(R.id.txtResult);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);

		RelativeLayout header = (RelativeLayout) findViewById(R.id.header);
		btnCancel = (ImageView) header.findViewById(R.id.left_iv);
		btnCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				back();
			}
		});
		btnManual = (TextView) header.findViewById(R.id.right_iv);
		btnManual.setOnClickListener(manualInputListener);

		btnOpen = (Button) findViewById(R.id.capture_manual_open);
		btnOpen.setOnClickListener(openListener);
	}

	protected void hideCancelBtn(){
		if(btnCancel != null){
			btnCancel.setVisibility(View.GONE);
		}
	}
	@Override
	public void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	public void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
		Log.d(TAG, "onDestroy");
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
		if (camera != null) {
			CameraManager.stopPreview();
		}
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private String mBarcode;
	private Bitmap mBarcodeImg;

	public void handleDecode(Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		// viewfinderView.drawResultBitmap(barcode);
		// playBeepSoundAndVibrate();
		// txtResult.setText(obj.getBarcodeFormat().toString() + ":"
		// + obj.getText());
		mBarcode = obj.getText();
		mBarcodeImg = barcode;
		onDecode(mBarcode, mBarcodeImg);
	}

	/**
	 * 子类重载此方法，处理扫描结果
	 * @param barCode
	 * @param barCodeImg
	 */
	protected void onDecode(String barCode, Bitmap barCodeImg){}
	
	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	/**
	 * 开灯
	 */
	private OnClickListener openListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			camera = CameraManager.getCamera();
			parameter = camera.getParameters();
			if (isOpen) {
				btnOpen.setBackgroundResource(R.drawable.capture_led_close);
				parameter.setFlashMode(Parameters.FLASH_MODE_TORCH);
				camera.setParameters(parameter);
				isOpen = false;
			} else { // 关灯
				btnOpen.setBackgroundResource(R.drawable.capture_led_open);
				parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
				camera.setParameters(parameter);
				isOpen = true;
			}
		}
	};

	/**
	 * 手工输入
	 */
	private OnClickListener manualInputListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
//			intent = new Intent(CaptureActivity.this, ManualInputActivity.class);
//			startActivity(intent);
		}
	};

	@Override
	public void onBackPressed() {
		back();
	}

	public boolean back() {
		finish();
		return true;
	}

}