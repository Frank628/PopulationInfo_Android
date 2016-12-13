package com.jinchao.population.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

import com.jinchao.population.R;

public class LoadMoreListView extends ListView implements OnScrollListener{
	private LayoutInflater mInflater;
	private View footView,newFootView;
	private LinearLayout mFootLayout;
	private OnLoadMoreListener mOnLoadMoreListener;
	private OnGoClickListener mOnGoClickListener;
	private int totalNum = 0;
	private int lastVisibleItem;
	private boolean isLoading = true;
	private boolean canLoad = true;
	private boolean isgoButtonVisiable=true;
	private String info="";
	
	public LoadMoreListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	
	public LoadMoreListView(Context context, AttributeSet pAttrs) {
		super(context, pAttrs);
		init(context);
	}

	public LoadMoreListView(Context context, AttributeSet pAttrs, int pDefStyle) {
		super(context, pAttrs, pDefStyle);
		init(context);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		lastVisibleItem = firstVisibleItem + visibleItemCount - 1;
		if(totalNum == getCount() - 1){
			mFootLayout.setVisibility(View.INVISIBLE);
			canLoad = false;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
			if(getLastVisiblePosition() == totalNum){
				mFootLayout.setVisibility(View.INVISIBLE);
			}else{
				if(getLastVisiblePosition() == getCount() - 1  &&  canLoad){
					mFootLayout.setVisibility(View.VISIBLE);
					if(mOnLoadMoreListener != null){
						if(!isLoading){
							mOnLoadMoreListener.onLoadMore();
							isLoading = true;
						}
					}else{
						try {
							throw new Exception("OnLoadMoreListener is Null");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			
		}
	}

	private void init(Context mContext) {
//		setCacheColorHint(mContext.getResources().getColor(R.color.transaction));
		info="暂无数据";
		mInflater = LayoutInflater.from(mContext);
		addFootView();
		setOnScrollListener(this);
	}
	public void setNodata(String info,boolean isgoButtonshow,OnGoClickListener mOnGoClickListener){
		this.info=info;
		this.isgoButtonVisiable=isgoButtonshow;
		this.mOnGoClickListener = mOnGoClickListener;
	}
	private void addFootView(){
		footView = (LinearLayout) mInflater.inflate(R.layout.listview_footer, null);
		mFootLayout = (LinearLayout) footView.findViewById(R.id.ll_footlayout);
		addFooterView(footView,null,false);
	}
	private void addNetErrorFootView(){
		footView = (LinearLayout) mInflater.inflate(R.layout.listview_footer, null);
		mFootLayout = (LinearLayout) footView.findViewById(R.id.ll_footlayout);
		addFooterView(footView,null,false);
	}
	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
		this.isLoading = false;
		if(totalNum == 0){
			canLoad = false;
			removeFooterView(footView);
			if(newFootView != null){
				removeFooterView(newFootView);
			}else{
				newFootView = (LinearLayout) mInflater.inflate(R.layout.layout_nodata, null);
				((TextView)newFootView.findViewById(R.id.tv_content)).setText(info);
				if (isgoButtonVisiable) {
					newFootView.findViewById(R.id.tv_gosearch).setVisibility(View.VISIBLE);
					newFootView.findViewById(R.id.tv_gosearch).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							mOnGoClickListener.onClick(v);
						}
					});
				}else{
					newFootView.findViewById(R.id.tv_gosearch).setVisibility(View.GONE);
				}
			}
			addFooterView(newFootView, null, false);
		}else{
			canLoad = true;
			if(newFootView != null){
				removeFooterView(newFootView);
			}
			removeFooterView(footView);
			addFooterView(footView, null, false);
		}
	}
	public interface OnLoadMoreListener {
		public void onLoadMore();
	}
	public interface OnGoClickListener {
		public void onClick(View v);
	}
	
	public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
		this.mOnLoadMoreListener = mOnLoadMoreListener;
	}
}
