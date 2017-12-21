package jp.inc.arouse.keyboardtestapplication;

import lombok.Value;

/**
 * jp.inc.arouse.keyboardtestapplication<br>
 * KeyboardTestApplication
 * <p>
 * Created by yuta on 2017/09/19.<br>
 * Copyright © 2017 arouse, inc. All Rights Reserved.
 */

@Value
public class ImageInfo {

	private long id;
	private int orientation;
	private String data;

}
