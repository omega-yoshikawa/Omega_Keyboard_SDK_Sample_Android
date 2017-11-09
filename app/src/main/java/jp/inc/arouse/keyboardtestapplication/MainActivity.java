package jp.inc.arouse.keyboardtestapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		MainFragment fragment = MainFragment.newInstance();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getName());
		fragmentTransaction.commit();
	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		Fragment fragment = getSupportFragmentManager().findFragmentByTag(ActivateFragment.class.getName());
		if (fragment != null && fragment instanceof ActivateFragment) {
			ActivateFragment activateFragment = (ActivateFragment)fragment;
			activateFragment.onWindowFocusChanged(hasFocus);
		}
	}
}
