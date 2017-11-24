package jp.inc.arouse.keyboardtestapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.omega.keyboard.sdk.KeyboardSDK;
import com.omega.keyboard.sdk.callback.MovieRewardGetPointCallback;
import com.omega.keyboard.sdk.callback.MovieRewardListener;
import com.omega.keyboard.sdk.callback.MovieRewardPlayCallback;
import com.omega.keyboard.sdk.config.MovieRewardPlayFailureReason;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardSDKTest
 * <p>
 * Created by yuta on 2017/11/20.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class MovieRewardFragment extends Fragment {

	private KeyboardSDK keyboardSDK;

	private AppCompatButton playMovieRewardButton;

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

		keyboardSDK.setupMovieReward(getActivity(), new MovieRewardListener() {
			@Override
			public void onChangeCanShow(boolean canShow) {
				if (canShow) {
					Toast.makeText(getContext(), "動画リワード再生が可能になりました", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getContext(), "動画リワード再生ができなくなりました", Toast.LENGTH_SHORT).show();
				}

				if (playMovieRewardButton != null) {
					playMovieRewardButton.setEnabled(canShow);
				}
			}
		});
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

		playMovieRewardButton = view.findViewById(R.id.show_reward_button);
		playMovieRewardButton.setEnabled(false);
		playMovieRewardButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				playReward();
			}
		});
	}

	private void playReward() {
		keyboardSDK.showMovieReward(new MovieRewardPlayCallback() {
			@Override
			public void onFinishPlay() {
				Toast.makeText(getContext(), "動画リワード 再生完了", Toast.LENGTH_SHORT).show();

				getRewardPoint();
			}

			@Override
			public void onCancel() {
				Toast.makeText(getContext(), "動画リワード 再生キャンセル", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure(MovieRewardPlayFailureReason reason) {
				String reasonMessage;
				switch (reason) {
					case NOT_INIT_SDK:
						reasonMessage = "SDKが初期化されていない";
						break;

					case NOT_LOGIN:
						reasonMessage = "ログインしていない";
						break;

					case EMPTY_ADS:
						reasonMessage = "再生できる動画がない";
						break;

					default:
						reasonMessage = "通信エラー等";
						break;
				}

				Toast.makeText(getContext(), "動画リワード 再生失敗 [" + reasonMessage + "]", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void getRewardPoint() {
		keyboardSDK.getMovieRewardPoint(new MovieRewardGetPointCallback() {
			@Override
			public void onSuccess(int point) {
				Toast.makeText(getContext(), "動画リワード ポイント獲得 [" + point + "pt]", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLimitPlay() {
				Toast.makeText(getContext(), "動画リワード ポイント獲得失敗 [再生回数上限]", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFailure() {
				Toast.makeText(getContext(), "動画リワード ポイント獲得失敗 [通信エラー等]", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
