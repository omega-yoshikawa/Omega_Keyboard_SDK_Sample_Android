package jp.inc.arouse.keyboardtestapplication;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.naver.android.helloyako.imagecrop.view.ImageCropView;
import com.omega.keyboard.sdk.KeyboardSDK;
import com.omega.keyboard.sdk.callback.CreateThemeCallback;
import com.omega.keyboard.sdk.graphics.KeyboardPreviewDrawable;
import com.omega.keyboard.sdk.model.CustomTheme;
import com.omega.keyboard.sdk.mozc.keyboard.Keyboard;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardTestApplication
 * <p>
 * Created by yuta on 2017/09/19.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class CreateThemeFragment extends Fragment {

	private Toolbar toolbar;
	private ImageCropView imageCropView;
	private AppCompatImageView previewKeyboardImageView;

	private GalleryAdapter galleryAdapter;
	private BucketAdapter bucketAdapter;

	private KeyboardPreviewDrawable previewDrawable;

	private int textColor = Color.BLUE;
	private int lineColor = Color.YELLOW;

	private boolean isLoading = false;
	private ProgressBar progressBar;


	public static CreateThemeFragment newInstance() {
		Bundle args = new Bundle();

		CreateThemeFragment fragment = new CreateThemeFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_create_theme, container, false);
		}
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle("カスタムテーマ作成");
		toolbar.inflateMenu(R.menu.create_theme);
		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.menu_create:
						if (isLoading) {
							return false;
						}
						else {
							createTheme();
							return true;
						}

					default:
						return false;
				}
			}
		});

		progressBar = findViewById(R.id.preview_progress_bar);

		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDimensionPixelSize(R.dimen.input_frame_height);

		imageCropView = findViewById(R.id.crop_image_view);
		imageCropView.setAspectRatio(width, height);
		imageCropView.setScaleEnabled(true);
		imageCropView.setDoubleTapEnabled(true);
		imageCropView.setDoubleTapListener(new ImageCropView.OnImageViewTouchDoubleTapListener() {
			@Override
			public void onDoubleTap() {
				imageCropView.resetMatrix();
			}
		});

		previewKeyboardImageView = findViewById(R.id.preview_keyboard_image_view);
		Keyboard.KeyboardSpecification keyboardSpecification = Keyboard.KeyboardSpecification.TWELVE_KEY_FLICK_KANA;
		previewDrawable = new KeyboardPreviewDrawable(getResources(), keyboardSpecification, textColor, lineColor);
		previewKeyboardImageView.setImageDrawable(previewDrawable);

		bucketAdapter = new BucketAdapter(getContext());
		final AppCompatSpinner bucketSpinner = findViewById(R.id.bucket_spinner);
		bucketSpinner.setAdapter(this.bucketAdapter);
		bucketSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				BucketInfo bucketInfo = (BucketInfo)bucketAdapter.getItem(position);
				galleryAdapter.setBucketInfo(bucketInfo);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		RecyclerView recyclerView = findViewById(R.id.recycler_view);
		GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 4);
		recyclerView.setLayoutManager(layoutManager);

		galleryAdapter = new GalleryAdapter(getContext());
		galleryAdapter.setOnItemClickListener(new GalleryAdapter.OnItemClickListener() {
			@Override
			public void onClickItem(ImageInfo imageInfo) {
				loadImage(imageInfo);
			}
		});
		recyclerView.setAdapter(galleryAdapter);

		final View textColorView = findViewById(R.id.text_color_view);
		textColorView.setBackgroundColor(textColor);
		textColorView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPickerDialogBuilder.with(getContext())
						.setTitle("テキストカラー")
						.initialColor(textColor)
						.wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
						.density(15)
						.setPickerCount(1)
						.showAlphaSlider(true)
						.showColorPreview(true)
						.showLightnessSlider(true)
						.setPositiveButton("OK", new ColorPickerClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int selectedColor, Integer[] allColors) {
								textColor = selectedColor;
								textColorView.setBackgroundColor(selectedColor);
								previewDrawable.setTextColor(selectedColor);
							}
						})
						.setNegativeButton("キャンセル", null)
						.build()
						.show();
			}
		});

		final View lineColorView = findViewById(R.id.line_color_view);
		lineColorView.setBackgroundColor(lineColor);
		lineColorView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPickerDialogBuilder.with(getContext())
						.setTitle("ラインカラー")
						.initialColor(lineColor)
						.wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
						.density(15)
						.setPickerCount(1)
						.showAlphaSlider(true)
						.showColorPreview(true)
						.showLightnessSlider(true)
						.setPositiveButton("OK", new ColorPickerClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int selectedColor, Integer[] allColors) {
								lineColor = selectedColor;
								lineColorView.setBackgroundColor(selectedColor);
								previewDrawable.setLineColor(selectedColor);
							}
						})
						.setNegativeButton("キャンセル", null)
						.build()
						.show();
			}
		});

		loadImage(galleryAdapter.getFirstImageInfo());
	}

	@SuppressWarnings("unchecked")
	private <V extends View> V findViewById(@IdRes int id) {
		if (getView() == null) {
			return null;
		}
		return (V)getView().findViewById(id);
	}

	private void loadImage(final ImageInfo imageInfo) {
		if (isLoading) {
			return;
		}
		if (imageInfo == null || imageInfo.getId() < 0) {
			Toast.makeText(getContext(), "画像の読み込みに失敗しました [E01]", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		isLoading = true;
		progressBar.setVisibility(View.VISIBLE);

		Task.callInBackground(new Callable<Bitmap>() {
			@Override
			public Bitmap call() throws Exception {
				Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, imageInfo.getId());
				return MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
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
						isLoading = false;
						progressBar.setVisibility(View.GONE);

						if (task.isFaulted()) {
							Toast.makeText(getContext(), "画像の読み込みに失敗しました [E02]", Toast.LENGTH_SHORT)
									.show();
						}
						else {
							imageCropView.setImageBitmap(task.getResult());
						}
						return null;
					}
				}, Task.UI_THREAD_EXECUTOR);
	}

	private void createTheme() {
		final ProgressDialog progressDialog = ProgressDialog.show(getContext(), null, "作成中…", false, false);

		cropImage()
				.continueWith(new Continuation<Bitmap, Void>() {
					@Override
					public Void then(Task<Bitmap> task) throws Exception {
						if (task.isFaulted()) {
							progressDialog.dismiss();
							Toast.makeText(getContext(), task.getError().getMessage(), Toast.LENGTH_SHORT).show();
						}
						else {
							KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(getContext());
							keyboardSDK.addCustomTheme(task.getResult(), textColor, lineColor, new CreateThemeCallback() {
								@Override
								public void onComplete(boolean success, CustomTheme customTheme) {
									progressDialog.dismiss();
									if (success) {
										Toast.makeText(getContext(), "カスタムテーマを作成しました", Toast.LENGTH_SHORT).show();
										EventBus.getDefault().postSticky(new CreateCustomThemeEvent(customTheme));
										getFragmentManager().popBackStack();
									}
									else {
										Toast.makeText(getContext(), "カスタムテーマの作成に失敗しました", Toast.LENGTH_SHORT).show();
									}
								}
							});
						}
						return null;
					}
				});
	}

	private Task<Bitmap> cropImage() {
		final TaskCompletionSource<Bitmap> tcs = new TaskCompletionSource<>();

		Task.callInBackground(new Callable<Bitmap>() {
			@Override
			public Bitmap call() throws Exception {
				return imageCropView.getCroppedImage();
			}
		})
				.continueWith(new Continuation<Bitmap, Void>() {
					@Override
					public Void then(Task<Bitmap> task) throws Exception {
						if (task.isFaulted() || task.getResult() == null) {
							tcs.setError(new Exception("画像の編集に失敗しました"));
						}
						else {
							tcs.setResult(task.getResult());
						}
						return null;
					}
				}, Task.UI_THREAD_EXECUTOR);

		return tcs.getTask();
	}
}
