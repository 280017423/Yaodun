package com.pregnancy.app.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.pregnancy.app.activity.ContentFragment;

public class TabFragmentPagerAdapter extends FragmentPagerAdapter {
	private static final String[] TITLE = new String[] { "妇女", "抗生素" };

	public TabFragmentPagerAdapter(Context context, FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		if (0 == position) {
			return new ContentFragment(1);
		} else {
			return new ContentFragment(4);
		}
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return TITLE[position];
	}

	@Override
	public int getCount() {
		return TITLE.length;
	}
}