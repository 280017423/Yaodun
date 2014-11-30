package com.yaodun.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yaodun.app.activity.ContentFragment;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
	private static final String[] CONTENT = new String[] { "热点", "育儿", "两性", "情感", "专题" };
	private static final String[] TITLE = new String[] { "热点", "育儿", "两性", "情感", "专题" };

	public TabFragmentPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return ContentFragment.newInstance(CONTENT[position % CONTENT.length]);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLE[position];
	}

	@Override
	public int getCount() {
		return CONTENT.length;
	}
}