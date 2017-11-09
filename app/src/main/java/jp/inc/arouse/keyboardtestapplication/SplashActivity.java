package jp.inc.arouse.keyboardtestapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.omega.keyboard.sdk.KeyboardSDK;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardSDKTest
 * <p>
 * Created by yuta on 2017/11/09.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class SplashActivity extends AppCompatActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);


		KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(getApplicationContext());
		keyboardSDK.setupInActivity();

		SplashFragment fragment = SplashFragment.newInstance();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getName());
		fragmentTransaction.commit();
	}
}
