package jp.inc.arouse.keyboardtestapplication;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.omega.keyboard.sdk.KeyboardBaseService;
import com.omega.keyboard.sdk.mozc.MozcBaseService;
import com.omega_adnetwork.sdk.AdView;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardTestApplication
 * <p>
 * Created by Nomura on 2017/07/28.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class KeyboardService extends KeyboardBaseService {

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onWindowShown() {
		super.onWindowShown();
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
	}
}
