package jp.inc.arouse.keyboardtestapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.common.collect.Lists;
import com.omega.keyboard.sdk.KeyboardSDK;
import com.omega.keyboard.sdk.callback.CreateThemeCallback;
import com.omega.keyboard.sdk.model.CustomTheme;

import bolts.Continuation;
import bolts.Task;
import bolts.TaskCompletionSource;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardSDKTest
 * <p>
 * Created by yuta on 2017/11/09.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class SplashFragment extends Fragment {

	private static final long SPLASH_TIME = 2 * DateUtils.SECOND_IN_MILLIS;

	private KeyboardSDK keyboardSDK;

	public static SplashFragment newInstance() {
		SplashFragment fragment = new SplashFragment();

		Bundle args = new Bundle();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		keyboardSDK = KeyboardSDK.sharedInstance(getContext());
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_splash, container, false);
		}
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		Task.whenAll(Lists.newArrayList(
				Task.delay(SPLASH_TIME),
				createPresetThemes()
		))
				.continueWith(new Continuation<Void, Void>() {
					@Override
					public Void then(Task<Void> task) throws Exception {
						transition();
						return null;
					}
				}, Task.UI_THREAD_EXECUTOR);
	}


	private Task<Void> createPresetThemes() {
		final TaskCompletionSource<Void> tcs = new TaskCompletionSource<>();

		if (keyboardSDK.getNumCustomThemes(CustomTheme.TYPE_PRESET_IMAGE) > 0) {
			tcs.setResult(null);
			return tcs.getTask();
		}

		createTheme(R.drawable.default_keyboard_001_background, R.drawable.default_keyboard_001_thumbnail, Color.DKGRAY, Color.DKGRAY)
				.continueWith(new Continuation<CustomTheme, Void>() {
					@Override
					public Void then(Task<CustomTheme> task) throws Exception {
						keyboardSDK.setCurrentCustomTheme(task.getResult());
						return null;
					}
				})
				.continueWithTask(new Continuation<Void, Task<CustomTheme>>() {
					@Override
					public Task<CustomTheme> then(Task<Void> task) throws Exception {
						return createTheme(R.drawable.default_keyboard_002_background, R.drawable.default_keyboard_002_thumbnail, Color.WHITE, Color.WHITE);
					}
				})
				.continueWith(new Continuation<CustomTheme, Void>() {
					@Override
					public Void then(Task<CustomTheme> task) throws Exception {
						tcs.setResult(null);
						return null;
					}
				});

		return tcs.getTask();
	}

	private Task<CustomTheme> createTheme(int background, int thumbnail, int textColor, int lineColor) {
		final TaskCompletionSource<CustomTheme> tcs = new TaskCompletionSource<>();

		keyboardSDK.addPresetTheme(background, thumbnail, textColor, lineColor, new CreateThemeCallback() {
			@Override
			public void onComplete(boolean success, CustomTheme customTheme) {
				tcs.setResult(customTheme);
			}
		});

		return tcs.getTask();
	}

	private void transition() {
		Intent intent = new Intent(getContext(), MainActivity.class);
		startActivity(intent);

		getActivity().finish();
	}

}
