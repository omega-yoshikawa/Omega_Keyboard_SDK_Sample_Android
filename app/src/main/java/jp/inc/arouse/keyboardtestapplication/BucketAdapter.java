package jp.inc.arouse.keyboardtestapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Task;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardTestApplication
 * <p>
 * Created by yuta on 2017/09/19.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class BucketAdapter extends BaseAdapter {

	private final Context context;

	private List<BucketInfo> buckets = Lists.newArrayList();
	private ContentResolver contentResolver;


	public BucketAdapter(Context context) {
		this.context = context;
		this.contentResolver = context.getContentResolver();

		updateBuckets();
	}

	private void updateBuckets() {
		this.buckets.clear();

		Cursor cursor = contentResolver.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[]{
						MediaStore.Images.Media.BUCKET_ID,
						MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
				},
				null,
				null,
				null
		);

		LinkedHashSet<BucketInfo> buckets = Sets.newLinkedHashSet();
		buckets.add(new BucketInfo(0, "すべての写真"));

		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					buckets.add(new BucketInfo(cursor.getLong(0), cursor.getString(1)));
				}
				while (cursor.moveToNext());
			}
			cursor.close();

			cursor.registerDataSetObserver(new DataSetObserver() {
				@Override
				public void onChanged() {
					super.onChanged();

					Task.call(new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							updateBuckets();
							return null;
						}
					}, Task.UI_THREAD_EXECUTOR);
				}

				@Override
				public void onInvalidated() {
					super.onInvalidated();

					Task.call(new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							updateBuckets();
							return null;
						}
					}, Task.UI_THREAD_EXECUTOR);
				}
			});
			this.buckets.addAll(buckets);
		}

		notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		return buckets.get(position).getId();
	}

	@Override
	public int getCount() {
		return buckets.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder viewHolder;
		if (convertView == null) {
			view = LayoutInflater.from(context).inflate(R.layout.item_gallery_bucket, parent, false);
			viewHolder = new ViewHolder(view);
			view.setTag(viewHolder);
		}
		else {
			view = convertView;
			viewHolder = (ViewHolder)convertView.getTag();
		}

		BucketInfo info = (BucketInfo)getItem(position);
		viewHolder.textView.setText(info.getName());

		return view;
	}

	@Override
	public Object getItem(int position) {
		return buckets.get(position);
	}


	private class ViewHolder {

		private AppCompatTextView textView;

		ViewHolder(View rootView) {
			textView = (AppCompatTextView)rootView.findViewById(R.id.text_view);
		}
	}
}
