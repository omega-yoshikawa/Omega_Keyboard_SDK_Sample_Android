package jp.inc.arouse.keyboardtestapplication;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.omega.keyboard.sdk.KeyboardSDK;
import com.omega.keyboard.sdk.callback.CheckNewNoticeCallback;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardSDKTest
 * <p>
 * Created by yuta on 2017/11/24.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class NoticeFragment extends Fragment {

	private KeyboardSDK keyboardSDK;

	private LinearLayout newNoticeLayout;
	private WebView webView;
	private ContentLoadingProgressBar loadingProgressBar;

	public static NoticeFragment newInstance() {
		NoticeFragment fragment = new NoticeFragment();

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
			view = inflater.inflate(R.layout.fragment_notice, container, false);
		}
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		newNoticeLayout = view.findViewById(R.id.new_notice_layout);
		newNoticeLayout.setVisibility(View.GONE);

		// 新着お知らせ既読
		view.findViewById(R.id.read_new_notice_button)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						webView.loadUrl(keyboardSDK.getNewNoticeURL());

						// 新着お知らせを既読にする
						keyboardSDK.readNewNotice();
						newNoticeLayout.setVisibility(View.GONE);
					}
				});

		// 新着お知らせ確認
		view.findViewById(R.id.check_new_notice_button)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						checkNewNotice();
					}
				});

		// 最新お知らせ表示
		view.findViewById(R.id.show_new_notice_button)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						webView.loadUrl(keyboardSDK.getNewNoticeURL());
					}
				});

		// お知らせ一覧表示
		view.findViewById(R.id.show_notice_list_button)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						webView.loadUrl(keyboardSDK.getNoticeListURL());
					}
				});

		// LoadingProgressBar
		loadingProgressBar = view.findViewById(R.id.loading_progress_bar);
		loadingProgressBar.hide();

		// WebView
		webView = view.findViewById(R.id.notice_web_view);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);

				if (loadingProgressBar != null) {
					loadingProgressBar.show();
				}
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				if (loadingProgressBar != null) {
					loadingProgressBar.show();
				}
			}
		});
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		getActivity().setTitle("お知らせ");

		checkNewNotice();
	}

	private void checkNewNotice() {
		keyboardSDK.checkNewNotice(new CheckNewNoticeCallback() {
			@Override
			public void onSuccess(boolean hasNewNotice, String newNoticeURL) {
				if (hasNewNotice) {
					newNoticeLayout.setVisibility(View.VISIBLE);
					Toast.makeText(getContext(), "新着のお知らせがあります\n" + newNoticeURL, Toast.LENGTH_SHORT).show();
				}
				else {
					newNoticeLayout.setVisibility(View.GONE);
					Toast.makeText(getContext(), "新着のお知らせはありませんでした", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFailure() {
				Toast.makeText(getContext(), "お知らせ情報の取得に失敗しました", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
