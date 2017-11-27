package jp.inc.arouse.keyboardtestapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.omega.keyboard.sdk.KeyboardSDK;
import com.omega.keyboard.sdk.fragment.settings.SettingsFragment;
import com.omega.keyboard.sdk.model.CustomTheme;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardTestApplication
 * <p>
 * Created by yuta on 2017/09/15.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class MainFragment extends Fragment {

	private static final int REQUEST_CODE_CREATE_THEME = 100;
	private static final int REQUEST_CODE_ACTIVATE_PERMISSION = REQUEST_CODE_CREATE_THEME + 1;
	private static final int REQUEST_CODE_KEYBOARD_ENABLED = REQUEST_CODE_ACTIVATE_PERMISSION + 1;
	private static final int REQUEST_CODE_KEYBOARD_SELECTED = REQUEST_CODE_KEYBOARD_ENABLED + 1;
	private static final int REQUEST_CODE_TUTORIAL = REQUEST_CODE_KEYBOARD_SELECTED + 1;
	private static final int REQUEST_CODE_FIRST_SETTINGS = REQUEST_CODE_TUTORIAL + 1;
	private static final int REQUEST_CODE_SETTINGS = REQUEST_CODE_FIRST_SETTINGS + 1;

	private KeyboardSDK keyboardSDK;

	private AppCompatSpinner typeSpinner;
	private ArrayAdapter<String> typeAdapter;
	private RecyclerView recyclerView;
	private CustomThemeAdapter customThemeAdapter;
	private AppCompatButton editButton;


	public static MainFragment newInstance() {
		Bundle args = new Bundle();

		MainFragment fragment = new MainFragment();
		fragment.setArguments(args);

		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		keyboardSDK = KeyboardSDK.sharedInstance(getContext());

		EventBus.getDefault().register(this);

		setHasOptionsMenu(true);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		EventBus.getDefault().unregister(this);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_main, container, false);
		}
		return view;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		inflater.inflate(R.menu.main, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_activate:
				ActivateFragment activateFragment = ActivateFragment.newInstance();
				showFragment(activateFragment);
				return true;

			case R.id.menu_settings:
				keyboardSDK.startSettingsActivityForResult(MainFragment.this, REQUEST_CODE_SETTINGS);
				return true;

			case R.id.menu_custom_settings_001:
				ArrayList<Integer> addContents = Lists.newArrayList(
						R.xml.pref_custom_settings_001
				);
				SettingsFragment settingsFragment = SettingsFragment.newInstance(R.id.container, addContents);
				showFragment(settingsFragment);
				return true;

			case R.id.menu_custom_settings_002:
				CustomSettingsFragment customSettingsFragment = CustomSettingsFragment.newInstance();
				showFragment(customSettingsFragment);
				return true;

			case R.id.menu_first_settings:
				keyboardSDK.startFirstSettingsActivityForResult(MainFragment.this, REQUEST_CODE_FIRST_SETTINGS);
				return true;

			case R.id.menu_tutorial:
				keyboardSDK.startTutorialActivityForResult(MainFragment.this, REQUEST_CODE_TUTORIAL);
				return true;

			case R.id.menu_create_theme_in_app:
				CreateThemeFragment createThemeFragment = CreateThemeFragment.newInstance();
				showFragment(createThemeFragment);
				return true;

			case R.id.menu_create_theme_in_sdk:
				List<Integer> textColors = Lists.newArrayList(
						Color.BLACK,
						Color.WHITE,
						Color.RED,
						Color.GREEN,
						Color.BLUE,
						Color.CYAN,
						Color.MAGENTA,
						Color.YELLOW,
						Color.parseColor("#77FF00FF"),
						Color.parseColor("#7700FFFF"),
						Color.parseColor("#77FFFF00"),
						Color.parseColor("#770000FF"),
						Color.parseColor("#7700FF00"),
						Color.parseColor("#77FF0000"),
						Color.parseColor("#77000000"),
						Color.parseColor("#77FFFFFF")
				);
				List<Integer> lineColors = Lists.newArrayList(
						Color.BLACK,
						Color.WHITE,
						Color.RED,
						Color.GREEN,
						Color.BLUE,
						Color.CYAN,
						Color.MAGENTA,
						Color.YELLOW,
						Color.parseColor("#77FF00FF"),
						Color.parseColor("#7700FFFF"),
						Color.parseColor("#77FFFF00"),
						Color.parseColor("#770000FF"),
						Color.parseColor("#7700FF00"),
						Color.parseColor("#77FF0000"),
						Color.parseColor("#77000000"),
						Color.parseColor("#77FFFFFF"),
						Color.TRANSPARENT
				);
				keyboardSDK.startCreateThemeActivityForResult(MainFragment.this, REQUEST_CODE_CREATE_THEME, textColors, lineColors);
				return true;

			case R.id.menu_movie_reward:
				MovieRewardFragment movieRewardFragment = MovieRewardFragment.newInstance();
				showFragment(movieRewardFragment);
				return true;

			case R.id.menu_notice:
				NoticeFragment noticeFragment = NoticeFragment.newInstance();
				showFragment(noticeFragment);
				return true;

			default:
				return false;
		}
	}

	private <F extends Fragment> void showFragment(F fragment) {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.replace(R.id.container, fragment, fragment.getClass().getName());
		transaction.addToBackStack(fragment.getClass().getName());
		transaction.commit();
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle("Home");
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		typeSpinner = findViewById(R.id.type_spinner);
		typeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{
				"プリセット",
				"カスタム"
		});
		typeSpinner.setAdapter(typeAdapter);
		typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				switch (position) {
					case 0:
						customThemeAdapter.refresh(CustomTheme.TYPE_PRESET_IMAGE);
						break;

					case 1:
						customThemeAdapter.refresh(CustomTheme.TYPE_USER_IMAGE);
						break;

					default:
						break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		editButton = findViewById(R.id.edit_button);
		editButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// 編集 → 選択
				if (customThemeAdapter.isEditing()) {
					customThemeAdapter.setEditing(false);
					editButton.setText("編集");
				}
				// 選択 → 編集
				else {
					if (!keyboardSDK.canRemoveCustomTheme()) {
						Toast.makeText(getContext(), "テーマを削除できません", Toast.LENGTH_SHORT).show();
						return;
					}
					customThemeAdapter.setEditing(true);
					editButton.setText("完了");
					Toast.makeText(getContext(), "タップして削除", Toast.LENGTH_SHORT).show();
				}
			}
		});

		recyclerView = findViewById(R.id.recycler_view);
		GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 3, GridLayoutManager.VERTICAL, false);
		recyclerView.setLayoutManager(layoutManager);
		customThemeAdapter = new CustomThemeAdapter(getContext());
		customThemeAdapter.setOnItemClickListener(new CustomThemeAdapter.OnItemClickListener() {
			@Override
			public void onClickItem(CustomTheme customTheme) {
				// 編集
				if (customThemeAdapter.isEditing()) {
					if (customTheme.isPreset() || customTheme.isSelected()) {
						Toast.makeText(getContext(), "テーマを削除できません", Toast.LENGTH_SHORT).show();
						return;
					}
					keyboardSDK.removeCustomTheme(customTheme);
					if (keyboardSDK.canRemoveCustomTheme()) {
						Toast.makeText(getContext(), "カスタムテーマを削除しました", Toast.LENGTH_SHORT).show();
					}
					else {
						customThemeAdapter.setEditing(false);
						editButton.setText("編集");
						Toast.makeText(getContext(), "カスタムテーマを削除しました\nこれ以上テーマを削除できません。", Toast.LENGTH_SHORT).show();
					}
				}
				// 選択
				else {
					keyboardSDK.setCurrentCustomTheme(customTheme);
					Toast.makeText(getContext(), "カスタムテーマを変更しました", Toast.LENGTH_SHORT).show();
				}
				customThemeAdapter.refresh();
			}
		});
		recyclerView.setAdapter(customThemeAdapter);
	}

	@SuppressWarnings("unchecked")
	private <V extends View> V findViewById(@IdRes int id) {
		if (getView() == null) {
			return null;
		}
		return getView().findViewById(id);
	}

	@SuppressWarnings("unused")
	@Subscribe
	public void onReceiveCreateThemeEvent(CreateCustomThemeEvent event) {
		switch (event.getCustomTheme().getType()) {
			case CustomTheme.TYPE_PRESET_IMAGE:
				typeSpinner.setSelection(0);
				customThemeAdapter.refresh(CustomTheme.TYPE_PRESET_IMAGE);
				break;

			case CustomTheme.TYPE_USER_IMAGE:
				typeSpinner.setSelection(1);
				customThemeAdapter.refresh(CustomTheme.TYPE_USER_IMAGE);
				break;

			default:
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQUEST_CODE_CREATE_THEME:
				switch (resultCode) {
					case Activity.RESULT_OK:
						String createdThemeId = data.getStringExtra(KeyboardSDK.CUSTOM_THEME_ID);
						Toast.makeText(getContext(), "カスタムテーマを作成しました [ " + createdThemeId + "]", Toast.LENGTH_SHORT).show();
						new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
							@Override
							public void run() {
								typeSpinner.setSelection(1);
								customThemeAdapter.refresh(CustomTheme.TYPE_USER_IMAGE);
							}
						}, 100);
						break;

					case Activity.RESULT_CANCELED:
						Toast.makeText(getContext(), "カスタムテーマ作成を中断しました", Toast.LENGTH_SHORT).show();
						break;

					default:
						break;
				}
				break;

			case REQUEST_CODE_ACTIVATE_PERMISSION:
				switch (resultCode) {
					case Activity.RESULT_OK:
						Toast.makeText(getContext(), "アクセスを許可しました", Toast.LENGTH_SHORT).show();
						break;

					case Activity.RESULT_CANCELED:
						Toast.makeText(getContext(), "アクセスを許可しませんでした", Toast.LENGTH_SHORT).show();
						break;

					default:
						break;
				}
				break;

			case REQUEST_CODE_KEYBOARD_ENABLED:
				switch (resultCode) {
					case Activity.RESULT_OK:
						Toast.makeText(getContext(), "キーボードを有効化しました", Toast.LENGTH_SHORT).show();
						break;

					case Activity.RESULT_CANCELED:
						Toast.makeText(getContext(), "キーボードを有効化しませんでした", Toast.LENGTH_SHORT).show();
						break;

					default:
						break;
				}
				break;

			case REQUEST_CODE_KEYBOARD_SELECTED:
				switch (resultCode) {
					case Activity.RESULT_OK:
						Toast.makeText(getContext(), "キーボードを選択しました", Toast.LENGTH_SHORT).show();
						break;

					case Activity.RESULT_CANCELED:
						Toast.makeText(getContext(), "キーボードを選択しませんでした", Toast.LENGTH_SHORT).show();
						break;

					default:
						break;
				}
				break;

			case REQUEST_CODE_FIRST_SETTINGS:
				switch (resultCode) {
					case Activity.RESULT_OK:
						Toast.makeText(getContext(), "初期設定が完了しました", Toast.LENGTH_SHORT).show();
						break;

					case Activity.RESULT_CANCELED:
						Toast.makeText(getContext(), "初期設定を中断しました", Toast.LENGTH_SHORT).show();
						break;

					default:
						break;
				}
				break;

			case REQUEST_CODE_TUTORIAL:
				switch (resultCode) {
					case Activity.RESULT_OK:
						Toast.makeText(getContext(), "チュートリアルが終了しました", Toast.LENGTH_SHORT).show();
						break;

					case Activity.RESULT_CANCELED:
						Toast.makeText(getContext(), "チュートリアルを中断しました", Toast.LENGTH_SHORT).show();
						break;

					default:
						break;
				}
				break;

			case REQUEST_CODE_SETTINGS:
				Toast.makeText(getContext(), "設定画面を閉じました", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
		}
	}

}
