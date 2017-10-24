package jp.inc.arouse.keyboardtestapplication;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.preference.Preference;

import com.google.common.collect.Lists;
import com.omega.keyboard.sdk.fragment.dialog.alert.AlertDialogFragment;
import com.omega.keyboard.sdk.fragment.settings.SettingsFragment;
import com.omega.keyboard.sdk.mozc.preference.PreferencePage;

import java.util.ArrayList;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardSDKTest
 * <p>
 * Created by yuta on 2017/10/24.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class CustomSettingsFragment extends SettingsFragment {

	public static CustomSettingsFragment newInstance() {
		CustomSettingsFragment fragment = new CustomSettingsFragment();

		ArrayList<Integer> addContents = Lists.newArrayList(
				R.xml.pref_custom_settings_002
		);

		Bundle args = new Bundle();
		args.putIntegerArrayList(SettingsFragment.ARG_ADD_CONTENTS, addContents);

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	protected void updatePreference() {
		super.updatePreference();

		Preference preference = findPreference("pref_custom_settings_006");
		if (preference != null) {
			preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					new AlertDialogFragment.Builder()
							.title("カスタム設定 [6]")
							.message("SettingsFragment テスト")
							.positive("OK")
							.negative("キャンセル")
							.build()
							.show(CustomSettingsFragment.this);
					return true;
				}
			});
		}

		preference = findPreference("pref_custom_settings_007");
		if (preference != null) {
			preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					SettingsFragment nextFragment = SettingsFragment.newInstance(PreferencePage.SDK_SOFTWARE_KEYBOARD, Lists.newArrayList(R.xml.pref_custom_settings_001));

					FragmentTransaction transaction = getFragmentManager().beginTransaction();
					transaction.replace(R.id.container, nextFragment, "pref_custom_settings_007");
					transaction.addToBackStack("pref_custom_settings_007");
					transaction.commit();
					return true;
				}
			});
		}
	}

}
