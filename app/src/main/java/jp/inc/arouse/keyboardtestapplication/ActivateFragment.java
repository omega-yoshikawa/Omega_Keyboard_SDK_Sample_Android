package jp.inc.arouse.keyboardtestapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.omega.keyboard.sdk.KeyboardSDK;
import com.omega.keyboard.sdk.activity.ActivateKeyboardActivity;
import com.omega.keyboard.sdk.util.KeyboardUtil;
import com.omega.keyboard.sdk.util.PermissionUtil;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardSDKTest
 * <p>
 * Created by yuta on 2017/10/13.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class ActivateFragment extends Fragment {

	private static final int REQUEST_CODE_CHECK_PERMISSIONS = 100;
	private static final int REQUEST_CODE_CHECK_ENABLED = REQUEST_CODE_CHECK_PERMISSIONS + 1;
	private static final int REQUEST_CODE_CHECK_SELECTED = REQUEST_CODE_CHECK_ENABLED + 1;

	private PermissionUtil permissionUtil;
	private KeyboardUtil keyboardUtil;
	private KeyboardSDK keyboardSDK;


	public static ActivateFragment newInstance() {
		ActivateFragment fragment = new ActivateFragment();
		
		Bundle args = new Bundle();
		
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.keyboardSDK = KeyboardSDK.sharedInstance(getContext());
		this.permissionUtil = PermissionUtil.sharedInstance(getContext());
		this.keyboardUtil = KeyboardUtil.sharedInstance(getContext());
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_activate, container, false);
		}
		return view;
	}

	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// パーミッションチェック
		findViewById(R.id.check_permissions_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (permissionUtil.isPermit()) {
					showToast("既に許可しています");
					return;
				}

				permissionUtil.requestPermissions(ActivateFragment.this, new PermissionUtil.RequestPermissionCallback() {
					@Override
					public void onRequestPermission(boolean isPermit) {
						String message = isPermit ? "許可しました" : "許可しませんでした";
						showToast(message);
					}
				});
			}
		});

		// キーボード有効化チェック
		findViewById(R.id.check_enabled_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (keyboardUtil.isEnabled()) {
					showToast("既に有効化されてます");
					return;
				}
				keyboardUtil.requestEnabled(ActivateFragment.this, new KeyboardUtil.RequestEnabledCallback() {
					@Override
					public void onEnabled(boolean isEnabled) {
						String message = isEnabled ? "有効化しました" : "有効化をキャンセルしました";
						showToast(message);
					}
				});
			}
		});

		// キーボード選択チェック
		findViewById(R.id.check_selected_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (keyboardUtil.isSelected()) {
					showToast("既に選択されています");
					return;
				}
				keyboardUtil.requestSelected(new KeyboardUtil.RequestSelectedCallback() {
					@Override
					public void onSelected(boolean isSelected) {
						String message = isSelected ? "選択しました" : "選択をキャンセルしました";
						showToast(message);
					}
				});
			}
		});


		// パーミッションチェック画面表示
		findViewById(R.id.check_permissions_activity_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (permissionUtil.isPermit()) {
					showToast("既に許可しています");
					return;
				}
				keyboardSDK.startActivateKeyboardActivityForResult(ActivateFragment.this, ActivateKeyboardActivity.Type.PERMISSIONS, REQUEST_CODE_CHECK_PERMISSIONS);
			}
		});

		// キーボード有効化チェック画面表示
		findViewById(R.id.check_enabled_activity_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (keyboardUtil.isEnabled()) {
					showToast("既に有効化されてます");
					return;
				}
				keyboardSDK.startActivateKeyboardActivityForResult(ActivateFragment.this, ActivateKeyboardActivity.Type.ENABLED, REQUEST_CODE_CHECK_ENABLED);
			}
		});

		// キーボード選択チェック画面表示
		findViewById(R.id.check_selected_activity_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (keyboardUtil.isSelected()) {
					showToast("既に選択されています");
					return;
				}
				keyboardSDK.startActivateKeyboardActivityForResult(ActivateFragment.this, ActivateKeyboardActivity.Type.SELECTED, REQUEST_CODE_CHECK_SELECTED);
			}
		});
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle("Activate");
	}

	private <V extends View> V findViewById(int id) {
		return (V)getView().findViewById(id);
	}

	private void showToast(String message) {
		Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (keyboardUtil != null) {
			keyboardUtil.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (permissionUtil != null) {
			permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	public void onWindowFocusChanged(boolean hasFocus) {
		if (keyboardUtil != null) {
			keyboardUtil.onWindowFocusChanged(hasFocus);
		}
	}
}
