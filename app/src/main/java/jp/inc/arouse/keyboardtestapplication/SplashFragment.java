package jp.inc.arouse.keyboardtestapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.omega.keyboard.sdk.KeyboardSDK;
import com.omega.keyboard.sdk.callback.CreateThemeCallback;
import com.omega.keyboard.sdk.model.CustomTheme;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardSDKTest
 * <p>
 * Created by yuta on 2017/11/09.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class SplashFragment extends Fragment {

	private static final long SPLASH_TIME = 2 * DateUtils.SECOND_IN_MILLIS;

	private static final int PRESET_THEME_COUNT = 1;

	private KeyboardSDK keyboardSDK;
	private int createThemeCount;
	private boolean finishSplashTime;


	public static SplashFragment newInstance() {
		SplashFragment fragment = new SplashFragment();

		Bundle args = new Bundle();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		createThemeCount = 0;
		finishSplashTime = false;

		keyboardSDK = KeyboardSDK.sharedInstance(getContext());

		createPresetThemes();
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

		Handler handler = new Handler(Looper.getMainLooper());
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				finishSplashTime = true;
				checkTransition();
			}
		}, SPLASH_TIME);
	}


	private void createPresetThemes() {
		keyboardSDK.addPresetTheme(R.drawable.default_keyboard_background, Color.BLUE, Color.RED, new CreateThemeCallback() {
			@Override
			public void onComplete(boolean success, CustomTheme customTheme) {
				++createThemeCount;
				keyboardSDK.setCurrentCustomTheme(customTheme);

				checkTransition();
			}
		});
	}

	private void checkTransition() {
		if (!finishSplashTime) {
			return;
		}

		if (createThemeCount < PRESET_THEME_COUNT) {
			return;
		}

		Intent intent = new Intent(getContext(), MainActivity.class);
		startActivity(intent);

		getActivity().finish();
	}

}
