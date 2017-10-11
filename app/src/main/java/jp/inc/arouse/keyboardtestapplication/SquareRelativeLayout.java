package jp.inc.arouse.keyboardtestapplication;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardTestApplication
 * <p>
 * Created by yuta on 2017/09/19.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class SquareRelativeLayout extends RelativeLayout {

	public SquareRelativeLayout(Context context) {
		super(context);
	}

	public SquareRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public SquareRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);

		if (width == 0 && height == 0) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
		else if (width == 0 || height == 0) {
			if (width > 0) {
				super.onMeasure(widthMeasureSpec, widthMeasureSpec);
			}
			else {
				super.onMeasure(heightMeasureSpec, heightMeasureSpec);
			}
		}
		else {
			if (width > height) {
				super.onMeasure(heightMeasureSpec, heightMeasureSpec);
			}
			else {
				super.onMeasure(widthMeasureSpec, widthMeasureSpec);
			}
		}
	}
}
