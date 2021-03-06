package com.lyw.health.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class LuAdapter<T> extends BaseAdapter{
	protected Context context;
	protected List<T> datas;
	private final int mItemLayoutId;
	public LuAdapter(Context context,List<T> datas,int mItemLayoutId){
		this.context=context;
		this.datas=datas;
		this.mItemLayoutId=mItemLayoutId;
	}
	
	public void setDatas(List<T> datas){
		this.datas=datas;
	}
	
	@Override
	public int getCount() {
		return (datas == null)?0:datas.size();
	}

	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return (datas == null)?null:datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder=getViewHolder(convertView, parent, position);
		convert(viewHolder,getItem(position));
		convert(viewHolder,position);
		return viewHolder.getConverView();
	}
	public  void convert(ViewHolder helper, T item){};
	public  void convert(ViewHolder helper, int position){};
		
//	};
	protected ViewHolder getViewHolder(View convertView, ViewGroup parent, int position){
		return ViewHolder.get(context, convertView, parent,mItemLayoutId, position);
	}
}
