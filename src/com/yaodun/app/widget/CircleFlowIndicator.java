package com.yaodun.app.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.qianjiang.framework.util.ImageUtil;
import com.yaodun.app.R;

/**
 * 
 * Description the class 自定义指示器
 * 
 * @version 1.0
 * @author zou.sq
 * 
 */
public class CircleFlowIndicator extends View {
	private float mSpace;
	private float mRadius;
	private Paint mPaint;
	private Bitmap mNormalBitmap;
	private Bitmap mSelectedBitmap;
	private int mCount;
	private int mPointNormalColor;
	private int mPointSeletedColor;
	private int mSeletedIndex;

	public CircleFlowIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FlowIndicator);

		mCount = a.getInteger(R.styleable.FlowIndicator_count, 4);
		mSpace = a.getDimension(R.styleable.FlowIndicator_space, 9);
		mRadius = a.getDimension(R.styleable.FlowIndicator_point_radius, 9);

		mPointNormalColor = a.getColor(R.styleable.FlowIndicator_point_normal_color, 0x000000);
		mPointSeletedColor = a.getColor(R.styleable.FlowIndicator_point_seleted_color, 0xffff07);
		Drawable normal_drawable = a.getDrawable(R.styleable.FlowIndicator_point_normal_drawable);
		Drawable seleted_drawable = a.getDrawable(R.styleable.FlowIndicator_point_seleted_drawable);
		if (null != normal_drawable) {
			mNormalBitmap = ImageUtil.drawable2Bitmap(normal_drawable);
		}
		if (null != seleted_drawable) {
			mSelectedBitmap = ImageUtil.drawable2Bitmap(seleted_drawable);
		}
		a.recycle();
		mPaint = new Paint();
	}

	public void setSeletion(int index) {
		this.mSeletedIndex = index;
		invalidate();
	}

	public void setCount(int count) {
		this.mCount = count;
		invalidate();
	}

	public void next() {
		if (mSeletedIndex < mCount - 1) {
			mSeletedIndex++;
		} else {
			mSeletedIndex = 0;
		}
		invalidate();
	}

	public void previous() {
		if (mSeletedIndex > 0) {
			mSeletedIndex--;
		} else {
			mSeletedIndex = mCount - 1;
		}
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		mPaint.setAntiAlias(true);
		float width = (getWidth() - ((mRadius * 2 * mCount) + (mSpace * (mCount - 1)))) / 2.f;
		for (int i = 0; i < mCount; i++) {
			if (null != mNormalBitmap && null != mSelectedBitmap) {
				if (i == mSeletedIndex) {
					canvas.drawBitmap(mSelectedBitmap, width + getPaddingLeft() + i * (mSpace + mRadius + mRadius), 0,
							mPaint);
				} else {
					canvas.drawBitmap(mNormalBitmap, width + getPaddingLeft() + i * (mSpace + mRadius + mRadius), 0,
							mPaint);
				}
			} else {
				if (i == mSeletedIndex) {
					mPaint.setColor(mPointSeletedColor);
				} else {
					mPaint.setColor(mPointNormalColor);
				}
				canvas.drawCircle(width + getPaddingLeft() + mRadius + i * (mSpace + mRadius + mRadius),
						getHeight() / 2, mRadius, mPaint);
			}

		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
	}

	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (int) (getPaddingLeft() + getPaddingRight() + (mCount * 2 * mRadius) + (mCount - 1) * mRadius + 1);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else {
			result = (int) (2 * mRadius + getPaddingTop() + getPaddingBottom() + 1);
			if (specMode == MeasureSpec.AT_MOST) {
				result = Math.min(result, specSize);
			}
		}
		return result;
	}

}
