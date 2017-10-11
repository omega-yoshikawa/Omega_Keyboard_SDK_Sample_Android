package jp.inc.arouse.keyboardtestapplication;

import com.omega.keyboard.sdk.model.CustomTheme;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardTestApplication
 * <p>
 * Created by yuta on 2017/09/19.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

public class CreateCustomThemeEvent {

	private final CustomTheme customTheme;

	public CreateCustomThemeEvent(CustomTheme customTheme) {
		this.customTheme = customTheme;
	}

	public CustomTheme getCustomTheme() {
		return customTheme;
	}
}
