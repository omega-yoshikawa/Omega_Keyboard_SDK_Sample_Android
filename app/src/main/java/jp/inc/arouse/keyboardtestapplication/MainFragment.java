package jp.inc.arouse.keyboardtestapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.common.collect.Lists;
import com.omega.keyboard.sdk.KeyboardSDK;
import com.omega.keyboard.sdk.model.CustomTheme;
import com.omega.keyboard.sdk.util.KeyboardUtil;
import com.omega.keyboard.sdk.util.PermissionUtil;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardTestApplication
 * <p>
 * Created by yuta on 2017/09/15.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class MainFragment extends Fragment {

	private KeyboardSDK keyboardSDK;
	private PermissionUtil permissionUtil;
	private KeyboardUtil keyboardUtil;

	private Toolbar toolbar;
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
		permissionUtil = PermissionUtil.sharedInstance(getContext());
		keyboardUtil = KeyboardUtil.sharedInstance(getContext());

		EventBus.getDefault().register(this);
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
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		toolbar = findViewById(R.id.toolbar);
		toolbar.setTitle(R.string.app_name);
		toolbar.inflateMenu(R.menu.main);
		toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {
					case R.id.menu_settings:
						keyboardSDK.startSettingsActivity(getActivity());
						return true;

					case R.id.menu_create_theme_in_app:
						CreateThemeFragment createThemeFragment = CreateThemeFragment.newInstance();
						FragmentTransaction transaction = getFragmentManager().beginTransaction();
						transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
						transaction.replace(R.id.container, createThemeFragment, createThemeFragment.getClass().getName());
						transaction.addToBackStack(createThemeFragment.getClass().getName());
						transaction.commit();
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
						keyboardSDK.startCreateThemeActivity(MainFragment.this, Define.REQUEST_CODE_CREATE_THEME, textColors, lineColors);
						return true;

					default:
						return false;
				}
			}
		});

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
						return;
					}
					keyboardSDK.removeCustomTheme(customTheme);
					Toast.makeText(getContext(), "カスタムテーマを削除しました", Toast.LENGTH_SHORT).show();
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
		return (V)getView().findViewById(id);
	}

	@SuppressWarnings("unused")
	@Subscribe(sticky = true)
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
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		permissionUtil.requestPermissions(this, new PermissionUtil.RequestPermissionCallback() {
			@Override
			public void onRequestPermission(boolean isPermit) {
				if (isPermit) {
					keyboardUtil.requestEnabled(getActivity(), new KeyboardUtil.RequestEnabledCallback() {
						@Override
						public void onEnabled(boolean isEnabled) {
							if (isEnabled) {
								keyboardUtil.requestSelected(new KeyboardUtil.RequestSelectedCallback() {
									@Override
									public void onSelected(boolean isSelected) {

									}
								});
							}
						}
					});
				}
			}
		});
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case Define.REQUEST_CODE_CREATE_THEME:
				if (Activity.RESULT_OK == resultCode) {
					new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(getContext(), "カスタムテーマ作成 : " + data.getStringExtra(KeyboardSDK.CUSTOM_THEME_ID), Toast.LENGTH_SHORT).show();
							typeSpinner.setSelection(1);
							customThemeAdapter.refresh(CustomTheme.TYPE_USER_IMAGE);
						}
					}, 100);
				}
				break;

			default:
				keyboardUtil.onActivityResult(requestCode, resultCode, data);
				break;
		}
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		keyboardUtil.onWindowFocusChanged(hasFocus);
	}
}
