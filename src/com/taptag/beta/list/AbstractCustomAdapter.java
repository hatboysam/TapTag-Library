package com.taptag.beta.list;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public abstract class AbstractCustomAdapter<T> extends ArrayAdapter<T> {
	
	public Context context;
	public int textViewResourceId;
	public int viewId;
	public T[] originalData;
	public T[] data;
	
	public AbstractCustomAdapter(Context context, int viewId, int textViewResourceId, T[] objects) {
		super(context, viewId, textViewResourceId, objects);
		this.context = context;
		this.textViewResourceId = textViewResourceId;
		this.viewId = viewId;
		this.data = objects;
		this.originalData = objects;
	}
	
	@Override 
	public abstract View getView(int position, View convertView, ViewGroup parent);
	
	@Override
	public int getCount() {
		return data.length;
	}
	
	@Override
	public T getItem(int position) {
		if (position >= 0) {
			return data[position];
		} else {
			return null;
		}
	}
	
	@Override
	public void notifyDataSetChanged() {
		sortData();
		super.notifyDataSetChanged();
	}
	
	public abstract void sortData();
}
