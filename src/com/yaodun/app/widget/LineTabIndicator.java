package com.yaodun.app.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LineTabIndicator extends HorizontalScrollView {

	public interface OnTabSelectedListener {

		void onTabSelected(int position);
	}

	public OnPageChangeListener mOnPageChangeListener;
	private OnTabSelectedListener mTabSelectedListener;

	private LinearLayout mTabsContainer;
	private ViewPager mPager;

	private int mTabCount;
	private int mCurrentPosition;
	private float mCurrentPositionOffset;

	private Paint mLinePaint;
	private Paint mDiviPaint;

	private int mUnderlineColor = 0xFFD1D1D1;
	private int mIndicatorColor = 0xFF6EBE23;
	private int mDividerColor = 0xFFD1D1D1;
	private int mTextSelectedColor = 0xFF6EBE23;
	private int mTextUnselectColor = 0xFF666666;

	private boolean mEnableExpand = true;
	private boolean mEnableDivider = true;
	private boolean mIndicatorOnTop;
	private boolean mViewPagerScrollWithAnimation = true;

	private int mTabTextSize = 16;
	private int mScrollOffset = 52;
	private float mIndicatorHeight = 1.5f;
	private float mUnderlineHeight = 1.0f;
	private int mDividerPadding;
	private int mTabPadding = 24;
	private int mDividerWidth = 1;
	private int mLastScrollX;

	public LineTabIndicator(Context context) {
		this(context, null);
	}

	public LineTabIndicator(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LineTabIndicator(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		setFillViewport(true);
		setWillNotDraw(false);

		DisplayMetrics dm = getResources().getDisplayMetrics();

		mTabsContainer = new LinearLayout(context);
		mTabsContainer.setOrientation(LinearLayout.HORIZONTAL);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, dm);
		mTabsContainer.setLayoutParams(params);
		addView(mTabsContainer);

		mScrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mScrollOffset, dm);
		mDividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerPadding, dm);
		mTabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mTabPadding, dm);
		mDividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mDividerWidth, dm);
		mIndicatorHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mIndicatorHeight, dm);
		mUnderlineHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mUnderlineHeight, dm);

		mLinePaint = new Paint();
		mLinePaint.setAntiAlias(true);
		mLinePaint.setStyle(Style.FILL);

		mDiviPaint = new Paint();
		mDiviPaint.setAntiAlias(true);
		mDiviPaint.setStrokeWidth(mDividerWidth);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (isInEditMode() || mTabCount == 0) {
			return;
		}

		final int height = getHeight();

		mLinePaint.setColor(mUnderlineColor);
		if (mIndicatorOnTop) {
			canvas.drawRect(0, 0, mTabsContainer.getWidth(), mUnderlineHeight, mLinePaint);
		} else {
			canvas.drawRect(0, height - mUnderlineHeight, mTabsContainer.getWidth(), height, mLinePaint);
		}

		mLinePaint.setColor(mIndicatorColor);

		View currentTab = mTabsContainer.getChildAt(mCurrentPosition);
		float lineLeft = currentTab.getLeft();
		float lineRight = currentTab.getRight();

		if (mCurrentPositionOffset > 0f && mCurrentPosition < mTabCount - 1) {
			View nextTab = mTabsContainer.getChildAt(mCurrentPosition + 1);
			final float nextTabLeft = nextTab.getLeft();
			final float nextTabRight = nextTab.getRight();

			lineLeft = (mCurrentPositionOffset * nextTabLeft + (1f - mCurrentPositionOffset) * lineLeft);
			lineRight = (mCurrentPositionOffset * nextTabRight + (1f - mCurrentPositionOffset) * lineRight);
		}

		if (mIndicatorOnTop) {
			canvas.drawRect(lineLeft, 0, lineRight, mIndicatorHeight, mLinePaint);
		} else {
			canvas.drawRect(lineLeft, height - mIndicatorHeight, lineRight, height, mLinePaint);
		}

		if (mEnableDivider) {
			mDiviPaint.setColor(mDividerColor);
			for (int i = 0; i < mTabCount - 1; i++) {
				View tab = mTabsContainer.getChildAt(i);
				canvas.drawLine(tab.getRight(), mDividerPadding, tab.getRight(), height - mDividerPadding, mDiviPaint);
			}
		}
	}

	public void setViewPager(ViewPager pager) {
		this.mPager = pager;

		if (pager.getAdapter() == null) {
			throw new IllegalStateException("ViewPager does not have adapter instance.");
		}

		pager.setOnPageChangeListener(new PageListener());

		notifyDataSetChanged();
	}

	public void setOnPageChangeListener(OnPageChangeListener listener) {
		this.mOnPageChangeListener = listener;
	}

	public void setOnTabReselectedListener(OnTabSelectedListener listener) {
		mTabSelectedListener = listener;
	}

	public void notifyDataSetChanged() {

		mTabsContainer.removeAllViews();

		mTabCount = mPager.getAdapter().getCount();

		for (int i = 0; i < mTabCount; i++) {
			addTab(i, mPager.getAdapter().getPageTitle(i).toString());
		}

		updateTabStyles();
	}

	private class TabView extends RelativeLayout {
		private TextView mTabText;

		public TabView(Context context) {
			super(context);
			init();
		}

		public TabView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		public TabView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		private void init() {
			mTabText = new TextView(getContext());
			mTabText.setTextAppearance(getContext(), android.R.attr.textAppearanceMedium);
			mTabText.setTextSize(mTabTextSize / getResources().getConfiguration().fontScale);
			mTabText.setSingleLine(true);
			mTabText.setGravity(Gravity.CENTER);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			this.addView(mTabText, params);

		}

		public TextView getTextView() {
			return mTabText;
		}
	}

	private void addTab(final int position, String title) {
		TabView tab = new TabView(getContext());
		tab.getTextView().setText(title);
		tab.setFocusable(true);
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final int oldSelected = mPager.getCurrentItem();
				if (oldSelected != position && mTabSelectedListener != null) {
					mTabSelectedListener.onTabSelected(position);
				}

				mPager.setCurrentItem(position, mViewPagerScrollWithAnimation);
			}
		});

		if (!mEnableExpand) {
			tab.setPadding(mTabPadding, 0, mTabPadding, 0);
		}
		mTabsContainer.addView(tab, position, mEnableExpand ? new LinearLayout.LayoutParams(
				0, LayoutParams.MATCH_PARENT, 1.0f) : new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
	}

	public void setTabText(int position, String text) {
		if (position < 0 || position > (mTabsContainer.getChildCount() - 1))
			throw new RuntimeException("tabs does not have this position.");

		View tab = mTabsContainer.getChildAt(position);
		if (tab instanceof TextView) {
			((TextView) tab).setText(text);
		}
	}

	public boolean isIndicatorOnTop() {
		return mIndicatorOnTop;
	}

	public void setIndicatorOnTop(boolean indicatorOnTop) {
		this.mIndicatorOnTop = indicatorOnTop;
	}

	public boolean isEnableExpand() {
		return mEnableExpand;
	}

	public void setEnableExpand(boolean enableExpand) {
		this.mEnableExpand = enableExpand;
	}

	public boolean isEnableDivider() {
		return mEnableDivider;
	}

	public void setEnableDivider(boolean enableDivider) {
		this.mEnableDivider = enableDivider;
	}

	public void setViewPagerScrollWithAnimation(boolean enable) {
		this.mViewPagerScrollWithAnimation = enable;
	}

	public boolean getViewPagerScrollWithAnimation() {
		return this.mViewPagerScrollWithAnimation;
	}

	public void setCurrentItem(int item) {
		mPager.setCurrentItem(item, mViewPagerScrollWithAnimation);
	}

	private void tabSelect(int index) {
		final int tabCount = mTabsContainer.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabsContainer.getChildAt(i);
			final boolean isSelected = (i == index);
			child.setSelected(isSelected);
			if (isSelected) {
				((TabView) child).getTextView().setTextColor(mTextSelectedColor);
			} else {
				((TabView) child).getTextView().setTextColor(mTextUnselectColor);
			}
		}
	}

	private void updateTabStyles() {
		for (int i = 0; i < mTabCount; i++) {
			View v = mTabsContainer.getChildAt(i);
			v.setBackgroundColor(Color.TRANSPARENT);
		}
		tabSelect(mPager.getCurrentItem());
	}

	private void scrollToChild(int position, int offset) {
		if (mTabCount == 0) {
			return;
		}

		int newScrollX = mTabsContainer.getChildAt(position).getLeft() + offset;

		if (position > 0 || offset > 0) {
			newScrollX -= mScrollOffset;
		}

		if (newScrollX != mLastScrollX) {
			mLastScrollX = newScrollX;
			scrollTo(newScrollX, 0);
		}
	}

	private class PageListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			mCurrentPosition = position;
			mCurrentPositionOffset = positionOffset;

			scrollToChild(position, (int) (positionOffset * mTabsContainer.getChildAt(position).getWidth()));

			invalidate();

			if (mOnPageChangeListener != null) {
				mOnPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				scrollToChild(mPager.getCurrentItem(), 0);
			}

			if (mOnPageChangeListener != null) {
				mOnPageChangeListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position) {
			tabSelect(position);

			if (mOnPageChangeListener != null) {
				mOnPageChangeListener.onPageSelected(position);
			}
		}
	}
}