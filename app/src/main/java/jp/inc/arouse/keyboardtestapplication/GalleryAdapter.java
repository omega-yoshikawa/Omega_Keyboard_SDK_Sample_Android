package jp.inc.arouse.keyboardtestapplication;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardTestApplication
 * <p>
 * Created by yuta on 2017/09/19.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

	private final Context context;
	private final ContentResolver contentResolver;
	private Cursor cursor;

	private boolean isDataValid;

	private int idColumn;
	private int orientationColumn;
	private int dataColumn;

	private OnItemClickListener onItemClickListener;
	private DataSetObserver dataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			super.onChanged();

			Task.call(new Callable<Void>() {
				@Override
				public Void call() throws Exception {
					isDataValid = true;
					notifyItemRangeChanged(0, cursor.getCount());
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
					isDataValid = true;
					notifyDataSetChanged();
					return null;
				}
			}, Task.UI_THREAD_EXECUTOR);
		}
	};

	public GalleryAdapter(Context context) {
		this.context = context;
		this.contentResolver = context.getContentResolver();
		this.cursor = contentResolver.query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				null,
				null,
				null,
				MediaStore.Images.ImageColumns.DATE_MODIFIED + " ASC"
		);

		if (cursor == null) {
			isDataValid = false;
			return;
		}

		isDataValid = true;
		idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
		orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
		dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
		cursor.registerDataSetObserver(dataSetObserver);
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	public void setBucketInfo(BucketInfo bucketInfo) {
		cursor.close();

		if (bucketInfo.getId() == 0) {
			cursor = contentResolver.query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					null,
					null,
					null,
					MediaStore.Images.ImageColumns.DATE_MODIFIED + " ASC"
			);

			if (cursor == null) {
				isDataValid = false;
				return;
			}

			isDataValid = true;
			idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
			orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
			dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
			cursor.registerDataSetObserver(dataSetObserver);
		}
		else {
			cursor = contentResolver.query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					null,
					MediaStore.Images.Media.BUCKET_ID + " = ?",
					new String[]{
							String.valueOf(bucketInfo.getId())
					},
					MediaStore.Images.ImageColumns.DATE_MODIFIED + " ASC"
			);

			if (cursor == null) {
				isDataValid = false;
				return;
			}

			isDataValid = true;
			idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);
			orientationColumn = cursor.getColumnIndex(MediaStore.Images.Media.ORIENTATION);
			dataColumn = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
			cursor.registerDataSetObserver(dataSetObserver);
		}

		notifyDataSetChanged();
	}

	@Override
	public long getItemId(int position) {
		if (isDataValid && cursor != null) {
			if (cursor.moveToPosition(position)) {
				return cursor.getLong(idColumn);
			}
		}
		return super.getItemId(position);
	}

	public ImageInfo getFirstImageInfo() {
		if (!isDataValid || cursor == null) {
			return null;
		}
		if (cursor.moveToFirst()) {
			return new ImageInfo(cursor.getLong(idColumn), cursor.getInt(orientationColumn), cursor.getString(dataColumn));
		}
		else {
			return null;
		}
	}

	@Override
	public void setHasStableIds(boolean hasStableIds) {
		super.setHasStableIds(true);
	}

	@Override
	public int getItemCount() {
		if (isDataValid && cursor != null) {
			return cursor.getCount();
		}
		else {
			return 0;
		}
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = View.inflate(context, R.layout.item_gallery, null);
		final ViewHolder viewHolder = new ViewHolder(itemView);
		viewHolder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					onItemClickListener.onClickItem(viewHolder.imageInfo);
				}
			}
		});
		return viewHolder;
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		if (cursor.moveToPosition(position)) {
			holder.onBind(new ImageInfo(cursor.getLong(idColumn), cursor.getInt(orientationColumn), cursor.getString(dataColumn)));
		}
		else {
			holder.onError();
		}
	}

	@Override
	public void onViewRecycled(ViewHolder holder) {
		super.onViewRecycled(holder);

		holder.onRecycle();
	}


	/**
	 * OnItemClickListener
	 */
	public interface OnItemClickListener {

		void onClickItem(ImageInfo imageInfo);
	}

	/**
	 * ViewHolder
	 */
	class ViewHolder extends RecyclerView.ViewHolder {

		private AppCompatImageView imageView;
		private ProgressBar progressBar;

		private ImageInfo imageInfo;


		ViewHolder(View itemView) {
			super(itemView);

			imageView = (AppCompatImageView)itemView.findViewById(R.id.image_view);
			progressBar = (ProgressBar)itemView.findViewById(R.id.progress_bar);
		}

		void onBind(final ImageInfo imageInfo) {
			this.imageInfo = imageInfo;

			progressBar.setVisibility(View.VISIBLE);

			Task.callInBackground(new Callable<Bitmap>() {
				@Override
				public Bitmap call() throws Exception {
					return MediaStore.Images.Thumbnails.getThumbnail(contentResolver, imageInfo.getId(), MediaStore.Images.Thumbnails.MINI_KIND, null);
				}
			})
					.onSuccess(new Continuation<Bitmap, Bitmap>() {
						@Override
						public Bitmap then(Task<Bitmap> task) throws Exception {
							Bitmap src = task.getResult();

							if (imageInfo.getOrientation() == 0) {
								return src;
							}

							Matrix matrix = new Matrix();
							matrix.postRotate(imageInfo.getOrientation());

							Bitmap result = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);

							src.recycle();

							return result;
						}
					})
					.continueWith(new Continuation<Bitmap, Void>() {
						@Override
						public Void then(Task<Bitmap> task) throws Exception {
							progressBar.setVisibility(View.GONE);
							if (task.isFaulted()) {
								onError();
							}
							else {
								imageView.setImageBitmap(task.getResult());
							}
							return null;
						}
					}, Task.UI_THREAD_EXECUTOR);
		}

		void onError() {
			imageInfo = null;
			imageView.setImageResource(android.R.drawable.stat_notify_error);
		}

		void setOnClickListener(View.OnClickListener listener) {
			itemView.setOnClickListener(listener);
		}

		void onRecycle() {
			imageInfo = null;
			imageView.setImageBitmap(null);
			progressBar.setVisibility(View.GONE);
		}
	}

}
