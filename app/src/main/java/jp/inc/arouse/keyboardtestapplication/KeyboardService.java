package jp.inc.arouse.keyboardtestapplication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.preference.PreferenceManager;
import android.text.TextUtils;

import com.omega.keyboard.sdk.KeyboardBaseService;
import com.omega_adnetwork.sdk.AdView;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardTestApplication
 * <p>
 * Created by Nomura on 2017/07/28.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class KeyboardService extends KeyboardBaseService {

	private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
			if (TextUtils.equals(SharedPrefKey.AD_VIEW_BACKGROUND_COLOR, key)) {
				int backgroundColor = sharedPreferences.getInt(key, Color.WHITE);
				getAdView().setBackgroundColor(backgroundColor);
			}
			else if (TextUtils.equals(SharedPrefKey.AD_VIEW_TEXT_COLOR, key)) {
				int textColor = sharedPreferences.getInt(key, Color.BLACK);
				getAdView().setTextColor(textColor);
			}
			else if (TextUtils.equals(SharedPrefKey.AD_VIEW_SUGGEST_COUNT, key)) {
				int suggestCount = Integer.valueOf(sharedPreferences.getString(SharedPrefKey.AD_VIEW_SUGGEST_COUNT, "3"));
				getAdView().setNumShowSuggestItem(suggestCount);
			}
		}
	};

	@Override
	public void onCreate() {
		super.onCreate();

		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPreferences.unregisterOnSharedPreferenceChangeListener(onSharedPreferenceChangeListener);
	}

	@Override
	public void onWindowShown() {
		super.onWindowShown();

		setAdViewParams(getAdView());
	}

	@Override
	public void onWindowHidden() {
		super.onWindowHidden();
	}

	@NonNull
	@Override
	protected Class<? extends Activity> getLaunchActivity() {
		return MainActivity.class;
	}

	@Override
	protected void setupAdView(AdView adView) {
		setAdViewParams(adView);
	}

	private void setAdViewParams(AdView adView) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		int backgroundColor = sharedPreferences.getInt(SharedPrefKey.AD_VIEW_BACKGROUND_COLOR, Color.WHITE);
		adView.setBackgroundColor(backgroundColor);
		int textColor = sharedPreferences.getInt(SharedPrefKey.AD_VIEW_TEXT_COLOR, Color.BLACK);
		adView.setTextColor(textColor);
		int suggestCount = Integer.valueOf(sharedPreferences.getString(SharedPrefKey.AD_VIEW_SUGGEST_COUNT, "3"));
		adView.setNumShowSuggestItem(suggestCount);
	}
}
