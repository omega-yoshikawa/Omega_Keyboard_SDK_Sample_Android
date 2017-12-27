package jp.inc.arouse.keyboardtestapplication;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omega.keyboard.sdk.KeyboardSDK;
import com.omega.keyboard.sdk.callback.LoadImageCallback;
import com.omega.keyboard.sdk.model.CustomTheme;
import com.orhanobut.logger.Logger;

import java.util.List;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardTestApplication
 * <p>
 * Created by yuta on 2017/09/15.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class CustomThemeAdapter extends RecyclerView.Adapter<CustomThemeAdapter.ViewHolder> {

	private final Context context;
	private final KeyboardSDK keyboardSDK;
	private final float scaledDensity;

	private int type;
	private CustomTheme.Sort sort;
	private boolean editing = false;
	private List<CustomTheme> customThemes;

	private OnItemClickListener onItemClickListener;


	public CustomThemeAdapter(Context context) {
		super();

		this.context = context;
		this.keyboardSDK = KeyboardSDK.sharedInstance(context);

		this.scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;

		refresh(CustomTheme.TYPE_PRESET_IMAGE, CustomTheme.Sort.ASCENDING);
	}

	public boolean isEditing() {
		return editing;
	}

	public void setEditing(boolean editing) {
		if (this.editing == editing) {
			return;
		}
		this.editing = editing;
		notifyItemRangeChanged(0, getItemCount());
	}

	public void refresh() {
		refresh(type, sort);
	}

	public void refresh(int type, CustomTheme.Sort sort) {
		this.type = type;
		this.sort = sort;
		customThemes = keyboardSDK.getCustomThemeList(type, sort);
		notifyDataSetChanged();
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

	@Override
	public int getItemCount() {
		return customThemes == null ? 0 : customThemes.size();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View itemView = LayoutInflater.from(context).inflate(R.layout.item_custom_theme, parent, false);
		return new ViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final CustomTheme customTheme = customThemes.get(position);
		holder.bind(customTheme);
		holder.setEditing(editing);
		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
					onItemClickListener.onClickItem(customTheme);
				}
			}
		});
	}

	@Override
	public void onViewRecycled(ViewHolder holder) {
		super.onViewRecycled(holder);

		holder.recycle();
	}


	/**
	 * OnItemClick
	 */
	interface OnItemClickListener {

		void onClickItem(CustomTheme customTheme);
	}


	/**
	 * ViewHolder
	 */
	class ViewHolder extends RecyclerView.ViewHolder {

		private final AppCompatImageView thumbImageView;
		private final AppCompatTextView infoTextView;
		private final Animator editingAnimator;
		private CustomTheme customTheme;


		ViewHolder(final View itemView) {
			super(itemView);

			View backgroundView = itemView.findViewById(R.id.background_view);
			StateListDrawable backgroundDrawable = new StateListDrawable();
			backgroundDrawable.addState(new int[]{
					-android.R.attr.state_pressed,
					-android.R.attr.state_selected
			}, new ColorDrawable(Color.parseColor("#FFFFFF")));
			backgroundDrawable.addState(new int[]{
					android.R.attr.state_pressed,
					-android.R.attr.state_selected
			}, new ColorDrawable(Color.parseColor("#DDDDDD")));
			backgroundDrawable.addState(new int[]{
					-android.R.attr.state_pressed,
					android.R.attr.state_selected
			}, new ColorDrawable(Color.parseColor("#FFCCCC")));
			backgroundDrawable.addState(new int[]{
					android.R.attr.state_pressed,
					android.R.attr.state_selected
			}, new ColorDrawable(Color.parseColor("#FFAAAA")));
			backgroundView.setBackground(backgroundDrawable);
			thumbImageView = (AppCompatImageView)itemView.findViewById(R.id.thumb_image_view);
			infoTextView = (AppCompatTextView)itemView.findViewById(R.id.info_text_view);

			editingAnimator = AnimatorInflater.loadAnimator(context, R.animator.item_custom_theme_editing);
			editingAnimator.setTarget(thumbImageView);
			editingAnimator.addListener(new Animator.AnimatorListener() {
				@Override
				public void onAnimationStart(Animator animation) {
				}

				@Override
				public void onAnimationEnd(Animator animation) {
					itemView.setRotation(0);
				}

				@Override
				public void onAnimationCancel(Animator animation) {
				}

				@Override
				public void onAnimationRepeat(Animator animation) {
				}
			});
		}

		void bind(CustomTheme customTheme) {
			this.customTheme = customTheme;
			// thumbnail
			keyboardSDK.loadThumbnailImage(customTheme, new LoadImageCallback() {
				@Override
				public void onLoadImage(boolean success, Bitmap bitmap) {
					if (success) {
						thumbImageView.setImageBitmap(bitmap);
					}
					else {
						thumbImageView.setImageResource(android.R.drawable.stat_notify_error);
					}
				}
			});

			// info
			SpannableStringBuilder infoBuilder = new SpannableStringBuilder();
			// ID
			infoBuilder.append("ID : ").append(customTheme.getId()).append("\n");
			// LineColor
			infoBuilder.append("LINE : ");
			ColorStateList lineColorStateList = new ColorStateList(new int[][]{
					new int[]{ -android.R.attr.state_pressed },
					new int[]{ android.R.attr.state_pressed }
			},
					new int[]{
							customTheme.getLineColor(),
							customTheme.getLineColor()
					});
			int lineTextSize = (int)Math.ceil(14 * scaledDensity);
			TextAppearanceSpan lineSpan = new TextAppearanceSpan("monospace", Typeface.BOLD, lineTextSize, lineColorStateList, null);
			int start = infoBuilder.length();
			infoBuilder.append("■");
			int end = infoBuilder.length();
			infoBuilder.setSpan(lineSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			infoBuilder.append("\n");
			// TextColor
			infoBuilder.append("TEXT : ");
			ColorStateList textColorStateList = new ColorStateList(new int[][]{
					new int[]{ -android.R.attr.state_pressed },
					new int[]{ android.R.attr.state_pressed }
			},
					new int[]{
							customTheme.getTextColor(),
							customTheme.getTextColor()
					});
			int textTextSize = (int)Math.ceil(14 * scaledDensity);
			TextAppearanceSpan textSpan = new TextAppearanceSpan("monospace", Typeface.BOLD, textTextSize, textColorStateList, null);
			start = infoBuilder.length();
			infoBuilder.append("■");
			end = infoBuilder.length();
			infoBuilder.setSpan(textSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

			infoTextView.setText(infoBuilder);

			itemView.setSelected(customTheme.isSelected());
		}

		void setEditing(boolean editing) {
			if (customTheme.isPreset() || customTheme.isSelected()) {
				if (editingAnimator.isRunning()) {
					editingAnimator.end();
				}
				return;
			}

			if (editing) {
				editingAnimator.start();
			}
			else {
				editingAnimator.end();
				itemView.setRotation(0);
			}
		}

		void recycle() {
			itemView.setOnClickListener(null);
			itemView.setSelected(false);
			thumbImageView.setImageBitmap(null);
			infoTextView.setText(null);
		}
	}
}
