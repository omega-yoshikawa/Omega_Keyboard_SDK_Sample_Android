package jp.inc.arouse.keyboardtestapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.omega.keyboard.sdk.KeyboardSDK;
import com.omega.keyboard.sdk.callback.MovieRewardCallback;
import com.omega.keyboard.sdk.config.MovieRewardFailureReason;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardSDKTest
 * <p>
 * Created by yuta on 2017/11/20.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class MovieRewardFragment extends Fragment {

	private KeyboardSDK keyboardSDK;

	public static MovieRewardFragment newInstance() {
		MovieRewardFragment fragment = new MovieRewardFragment();

		Bundle args = new Bundle();

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		keyboardSDK = KeyboardSDK.sharedInstance(getContext());
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = super.onCreateView(inflater, container, savedInstanceState);
		if (view == null) {
			view = inflater.inflate(R.layout.fragment_movie_reward, container, false);
		}
		return view;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		keyboardSDK.setupMovieReward(getActivity());
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		String userId = PreferenceManager.getDefaultSharedPreferences(getContext())
				.getString(SharedPrefKey.USER_ID, null);

		final AppCompatEditText userIdEditText = view.findViewById(R.id.user_id_edit_text);
		userIdEditText.setText(userId);

		view.findViewById(R.id.user_id_decide_button)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						String userId = userIdEditText.getText().toString();

						PreferenceManager.getDefaultSharedPreferences(getContext())
								.edit()
								.putString(SharedPrefKey.USER_ID, userId)
								.apply();

						Toast.makeText(getContext(), "ユーザーIDを設定しました", Toast.LENGTH_SHORT).show();
					}
				});

		view.findViewById(R.id.show_reward_button)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						keyboardSDK.showMovieReward(new MovieRewardCallback() {
							@Override
							public void onSuccess(int getPoint) {
								Toast.makeText(getContext(), "動画リワード 完了 [" + getPoint + "pt]", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onCancel() {
								Toast.makeText(getContext(), "動画リワード キャンセル", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onFailure(MovieRewardFailureReason reason) {
								String message;
								switch (reason) {
									case OTHER:
										message = "通信エラー等";
										break;

									case NOT_LOGIN:
										message = "ログインしていない";
										break;

									case NOT_INIT_SDK:
										message = "パラメータが設定されていない";
										break;

									case EMPTY_ADS:
										message = "再生できるリワードがない";
										break;

									case PLAY_LIMIT:
										message = "再生回数上限";
										break;

									default:
										message = "Unknown";
										break;
								}
								Toast.makeText(getContext(), "動画リワード 失敗 [" + message + "]", Toast.LENGTH_SHORT).show();
							}
						});
					}
				});
	}
}
