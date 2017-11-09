package jp.inc.arouse.keyboardtestapplication;

import android.support.annotation.NonNull;

import com.omega.keyboard.sdk.InitializeParameter;
import com.omega.keyboard.sdk.KeyboardApplication;
import com.omega.keyboard.sdk.KeyboardSDK;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.LogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardTestApplication
 * <p>
 * Created by yuta on 2017/09/14.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class MainApplication extends KeyboardApplication {

	@Override
	public void onCreate() {
		super.onCreate();

		initLogger();
	}

	private void initLogger() {
		FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
				.tag("KeyboardSDKTest")
				.methodCount(3)
				.methodOffset(2)
				.build();
		LogAdapter logAdapter = new AndroidLogAdapter(formatStrategy) {
			@Override
			public boolean isLoggable(int priority, String tag) {
				if (BuildConfig.DEBUG) {
					return priority >= Logger.VERBOSE;
				}
				else {
					return priority >= Logger.ERROR;
				}
			}
		};
		Logger.addLogAdapter(logAdapter);
	}

	@Override
	protected void onInitSDK(@NonNull final KeyboardSDK keyboardSDK) {
	}

	@NonNull
	@Override
	protected InitializeParameter getInitializeParameter() {
		return InitializeParameter.newBuilder()
				.applicationInfo(getString(R.string.app_name), "arouse, inc.", getString(R.string.user_dictionary_export_provider))
				.adInfo(Define.SYMBOL, Define.TOKEN)
				.isDebug(true)
				.build();
	}
}
