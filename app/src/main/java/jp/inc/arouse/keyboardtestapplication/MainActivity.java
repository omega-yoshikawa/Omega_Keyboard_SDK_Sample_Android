package jp.inc.arouse.keyboardtestapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.omega.keyboard.sdk.KeyboardSDK;

public class MainActivity extends AppCompatActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(getApplicationContext());
		keyboardSDK.setupInActivity();

		MainFragment fragment = MainFragment.newInstance();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getName());
		fragmentTransaction.commit();
	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		Fragment fragment = getSupportFragmentManager().findFragmentByTag(MainFragment.class.getName());
		if (fragment != null && fragment instanceof MainFragment) {
			MainFragment mainFragment = (MainFragment)fragment;
			mainFragment.onWindowFocusChanged(hasFocus);
		}
	}
}
