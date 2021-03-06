package com.pregnancy.app.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.pregnancy.app.R;
import com.pregnancy.app.adapter.KnowledgeAdapter;
import com.pregnancy.app.authentication.ActionProcessor;
import com.pregnancy.app.authentication.ActionResult;
import com.pregnancy.app.listener.IActionListener;
import com.pregnancy.app.model.KnowledgeModel;
import com.pregnancy.app.req.KnowledgeReq;
import com.pregnancy.app.util.ConstantSet;
import com.qianjiang.framework.imageloader.core.ImageLoader;
import com.qianjiang.framework.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.qianjiang.framework.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import com.qianjiang.framework.widget.pulltorefresh.PullToRefreshListView;

public class ContentFragment extends YaodunFragmentBase implements OnItemClickListener {
	private static final String KEY_TYPE = "KEY_TYPE";
	private static final int GET_DATA_SUCCESSED = 0;
	private static final int GET_DATA_FAIL = 1;
	private static final int PULL_DOWN = 0;
	private static final int PULL_UP = 1;
	private PullToRefreshListView mPullToRefreshListView;
	private int mType;
	private KnowledgeAdapter mAdapter;
	private ImageLoader mImageLoader = ImageLoader.getInstance();
	private List<KnowledgeModel> mKnowledgeModels;
	private int mPage;
	private boolean mIsGettingData;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mPullToRefreshListView.onRefreshComplete();
			ActionResult result = (ActionResult) msg.obj;
			if (result != null) {
				switch (msg.what) {
					case GET_DATA_SUCCESSED:
						List<KnowledgeModel> moreList = (List<KnowledgeModel>) result.ResultObject;
						if (moreList != null) {
							if (PULL_DOWN == msg.arg1) {
								mKnowledgeModels.clear();
								mKnowledgeModels.addAll(0, moreList);
							} else {
								mPage++;
								mKnowledgeModels.addAll(moreList);
							}
							if (moreList.size() < ConstantSet.INFO_NUM_IN_ONE_PAGE) {
								mPullToRefreshListView.setNoMoreData();
								mPullToRefreshListView.onRefreshComplete(false);
							} else {
								mPullToRefreshListView.onRefreshComplete(true);
							}
							mPullToRefreshListView.setMode(Mode.BOTH);
						}
						if (mAdapter != null) {
							mAdapter.notifyDataSetChanged();
						}
						break;
					case GET_DATA_FAIL:
						showErrorMsg((ActionResult) result);
						break;
					default:
						break;
				}
			}
			mIsGettingData = false;
		}
	};

	/**
	 * @Name ContentFragment
	 * @param type
	 *            解决方法：自定义的fragment最好有一个Public的参数为空的构造函数，若需要传入一个参数，可以使用下面的方法</br
	 *            缺陷： Unable to instantiate fragment
	 *            com.yaodun.app.activity.ContentFragment: make sure class name
	 *            exists, is public, and has an empty constructor that is public
	 */
	public static ContentFragment getInstance(int type) {
		ContentFragment fileViewFragment = new ContentFragment();
		fileViewFragment.mType = type;
		return fileViewFragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_TYPE)) {
			mType = savedInstanceState.getInt(KEY_TYPE, 0);
		}
		mKnowledgeModels = new ArrayList<KnowledgeModel>();
		mAdapter = new KnowledgeAdapter(getActivity(), mKnowledgeModels, mImageLoader);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = View.inflate(getActivity(), R.layout.fragment_medicine_knowledge, null);
		mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.lv_medicine_knowledge);
		ListView listView = mPullToRefreshListView.getRefreshableView();
		listView.setAdapter(mAdapter);
		listView.setSelector(new BitmapDrawable());
		listView.setOnItemClickListener(this);
		listView.setCacheColorHint(getResources().getColor(R.color.transparent));
		listView.setFadingEdgeLength(0);
		mPullToRefreshListView.setOnRefreshListener(new OnRefreshListener2() {
			@Override
			public void onPullDownToRefresh() {
				mPage = 1;
				getKnowledgeModels(PULL_DOWN);
			}

			@Override
			public void onPullUpToRefresh() {
				getKnowledgeModels(PULL_UP);
			}
		});
		mPullToRefreshListView.setHeaderVisible(true);
		mPullToRefreshListView.setMode(Mode.BOTH);
		mPullToRefreshListView.setIsShowHeaderFresh(true);
		return view;
	}

	private void getKnowledgeModels(final int pullStatus) {

		if (mIsGettingData) {
			return;
		}
		mIsGettingData = true;
		new ActionProcessor().startAction(getActivity(), new IActionListener() {

			@Override
			public void onSuccess(ActionResult result) {
				sendHandler(GET_DATA_SUCCESSED, pullStatus, result);
			}

			@Override
			public void onError(ActionResult result) {
				sendHandler(GET_DATA_FAIL, pullStatus, result);
			}

			@Override
			public ActionResult onAsyncRun() {
				return KnowledgeReq.getKnowledgeList(mType, mPage);
			}
		});
	}

	private void sendHandler(int what, int status, ActionResult result) {
		Message msg = mHandler.obtainMessage();
		msg.what = what;
		msg.arg1 = status;
		msg.obj = result;
		mHandler.sendMessage(msg);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(KEY_TYPE, mType);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		KnowledgeModel model = (KnowledgeModel) parent.getAdapter().getItem(position);
		if (null != model) {
			Intent intent = new Intent(getActivity(), KnowledgeDetailActivity.class);
			intent.putExtra(ConstantSet.EXTRA_KNOWLEDGEMODEL, model);
			startActivity(intent);
		}
	}
}