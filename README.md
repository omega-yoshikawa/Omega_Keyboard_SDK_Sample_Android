# Omega Keyboard SDK

-----

## 最新バージョン

**1.4.1**

## 必要条件

* Android Studio（最新のもの推奨）
* Android SDK
* JDK7以降
* API Level 16（Android 4.1 Jelly Bean）以降

-----

# スタートガイド

1. 導入方法
1. 使用方法
1. SDKの機能

-----

# 1. 導入方法

## Gradleで導入

1． モジュールの **build.gradle** *(app/build.gradle)* の `android` へ下記項目を追記する  

```
#!gradle
repositories {
    maven {
        url 'https://raw.githubusercontent.com/OMEGA-Inc/Omega_Adnetwork_SDK_Android/master'
    }
    maven {
        url 'https://raw.githubusercontent.com/OMEGA-Inc/Omega_Keyboard_SDK_Mozc_Android/master'
    }
    maven {
        url 'https://raw.githubusercontent.com/OMEGA-Inc/Omega_Keyboard_SDK_Android/master'
    }
}
```

2． モジュールの **build.gradle** *(app/build.gradle)* の `dependencies` へ下記項目を追記する  

```
#!gradle
dependencies {
    implementation 'com.android.support:exifinterface:27.0.2' // SDKでテーマを作成する場合
	implementation 'com.google.android.gms:play-services-ads:11.6.0'
	implementation 'com.omega_adnetwork.sdk:OmegaAdnetworkSDK:X.X.X' (X.X.Xは広告SDKのバージョン)
	implementation 'org.mozc.android.inputmethod.japanese:MozcJNI:1.0.0'
	implementation 'com.omega.keyboard.sdk:OmegaKeyboardSDK:X.X.X' (X.X.XはキーボードSDKのバージョン)
}
```

※SDKの更新がアプリに反映されない場合は、Gradle cacheを削除してください。  
Gradle cacheの場所は以下となります。  

| OS | Path |
| :--- | :--- |
| Windows | %USER_HOME%.gradle/caches |
| Mac/Unix | $HOME/.gradle/caches/ |


## 手動で導入

1. Android Studioのメニューで **File → New → New Module...** を選択する
1. **Import .Jar/.AAR Package** を選択し、 **Next** を押す
1. **File Name:** の **…** を押し、 **OmegaKeyboardSDK-X.X.X.aar** を選択する (X.X.XはキーボードSDKのバージョン)
1. **Subproject name:** で任意のモジュール名を入力し、 **Finish** を押す
1. Android Studioのメニューで **File → New → New Module...** を選択する
1. **Import .Jar/.AAR Package** を選択し、 **Next** を押す
1. **File Name:** の **…** を押し、 **OmegaAdnetworkSDK-X.X.X.aar** を選択する (X.X.Xは広告SDKのバージョン)
1. **Subproject name:** で任意のモジュール名を入力し、 **Finish** を押す
1. Android Studioのメニューで **File → New → New Module...** を選択する
1. **Import .Jar/.AAR Package** を選択し、 **Next** を押す
1. **File Name:** の **…** を押し、 **MozcJNI-X.X.X.aar** を選択する (X.X.XはMozcJNI SDKのバージョン)
1. **Subproject name:** で任意のモジュール名を入力し、 **Finish** を押す
1. メインモジュールの　**build.gradle(app/build.gradle)** の `repositories` と `dependencies` へ追記する

```
#!gradle
repositories {
	google()
}

dependencies {
    implementation 'com.android.support:appcompat-v7:27.0.2'
    implementation 'com.android.support:exifinterface:27.0.2' // SDKでテーマを作成する場合
    implementation 'com.google.android.gms:play-services-ads:11.6.0'
    implementation 'com.google.protobuf:protobuf-java:3.4.0'

    implementation project(':OmegaKeyboardSDK') // 4で指定したもの
    implementation project(':OmegaAdnetworkSDK') // 8で指定したもの
    implementation project(':MozcJNI') // 12で指定したもの
}
```

-----

## 2. 使用方法

### AndroidManifest

`application` タグ内に使用する *Activity* 、 *Service* 、 *Provider* を追加する  


#### TutorialActivity

チュートリアルのActivity  
使用する場合は追加する  
`android:screenOrientation` で *portrait* を指定する  
*ActionBar* は非表示推奨 

###### SampleCode

```
#!xml
<activity
	android:name="com.omega.keyboard.sdk.activity.TutorialActivity"
	android:screenOrientation="portrait"
	android:theme="@style/AppTheme.NoActionBar" />

```

#### ActivateKeyboardActivity

パーミッション・キーボードの有効化・キーボードの選択のActivity  
使用する場合は追加する  
`android:screenOrientation` で *portrait* を指定する  
*ActionBar* は非表示推奨 

###### SampleCode

```
#!xml
<activity
	android:name="com.omega.keyboard.sdk.activity.ActivateKeyboardActivity"
	android:screenOrientation="portrait"
	android:theme="@style/AppTheme.NoActionBar" />
```

#### FirstSettingsActivity

初期設定のActivity  
使用する場合は追加する  
`android:screenOrientation` で *portrait* を指定する  
*ActionBar* は非表示推奨 

###### SampleCode

```
#!xml
<activity
	android:name="com.omega.keyboard.sdk.activity.FirstSettingsActivity"
	android:screenOrientation="portrait"
	android:theme="@style/AppTheme.NoActionBar" />
```

#### SettingsActivity

設定のActivity  
必ず追加する  
`android:screenOrientation` で *portrait* を指定する  
`android:theme` で *KeyboardTheme* 、 *KeyboardTheme.NoActionBar* を指定する  
*intent-filter* は下記の通りにする  
*ActionBar* は表示推奨 

###### SampleCode

```
#!xml
<activity
	android:name="com.omega.keyboard.sdk.activity.SettingsActivity"
	android:screenOrientation="portrait"
	android:theme="@style/KeyboardTheme" />
```

#### CreateThemeActivity

カスタムテーマ作成のActivity  
使用する場合は追加する  
`android:screenOrientation` で *portrait* を指定する  
*ActionBar* は表示推奨  
appモジュールの *build.gradle* の `dependencies` へ  
`implementation 'com.android.support:exifinterface:27.0.2'` を追加する  


###### SampleCode

```
#!xml
<activity
	android:name="com.omega.keyboard.sdk.activity.CreateThemeActivity"
	android:screenOrientation="portrait"
	android:theme="@style/AppTheme" />
```

#### UserDictionaryToolActivity

ユーザー辞書設定のActivity  
必ず追加する  
`android:screenOrientation` で *portrait* を指定する  
*ActionBar* は表示推奨 
`android:theme` で *KeyboardTheme* 、 *KeyboardTheme.NoActionBar* を指定する  
*intent-filter* は下記の通りにする  

###### SampleCode

```
#!xml
<activity
	android:name="com.omega.keyboard.sdk.activity.UserDictionaryToolActivity"
	android:screenOrientation="portrait"
	android:theme="@style/KeyboardTheme">

	<intent-filter>
		<action android:name="android.intent.action.SEND" />
		<action android:name="android.intent.action.VIEW" />

		<category android:name="android.intent.category.DEFAULT" />

		<data android:mimeType="text/plain" />
		<data android:mimeType="application/zip" />
		<data android:scheme="file" />
	</intent-filter>
</activity>
```

#### InputMethodService

*KeyboardBaseService* を継承したクラスを作成し、追加する  
`android:permission` で *android.permission.BIND_INPUT_METHOD* を指定する
必ず追加する  
`intent-filter` 、 `meta-data` は下記の通りにする  

###### SampleCode

```
#!xml
<service
	android:name=".KeyboardService"
	android:permission="android.permission.BIND_INPUT_METHOD">
	<intent-filter>
		<action android:name="android.view.InputMethod" />
	</intent-filter>
	<meta-data
		android:name="android.view.im"
		android:resource="@xml/method" />
</service>
```

#### 辞書エクスポート (File Provider)

辞書エクスポートのFileProvider  
`android:authorities` は他のアプリと重複するとエラーになるため、ユニークな値を指定する  
`android:exported` で *false* を指定する  
`android:grantUriPermissions` で *true* を指定する  
`meta-data` は *SampleCode* の通りにする  

###### SampleCode

```
#!xml
<provider
	android:name="android.support.v4.content.FileProvider"
	android:authorities="@string/user_dictionary_export_provider"
	android:exported="false"
	android:grantUriPermissions="true">
	<meta-data
		android:name="android.support.FILE_PROVIDER_PATHS"
		android:resource="@xml/export_dictionary_paths" />
</provider>
```

-----

### Theme

*SettingsActivity* 、 *UserDictionaryToolsActivity* では `android:theme` へ *KeyboardTheme* 、 *KeyboardTheme.NoActionBar* を指定する  

#### スタイルをカスタムする

##### SDKのstyleを拡張する

`parent` を *KeyboardTheme* 、 *KeyboardTheme.NoActionBar* にする

###### SampleCode

```
#!xml
<resources>
	<style name="AppTheme.Keyboard" parent="KeyboardTheme">
		<item name="colorPrimary">@color/colorPrimary</item>
		<item name="colorPrimaryDark">@color/colorPrimaryDark</item>
		<item name="colorAccent">@color/colorAccent</item>
		<item name="actionBarStyle">@style/Widget.ActionBar</item>
	</style>

	<style name="AppTheme.Keyboard.NoActionBar" parent="KeyboardTheme.NoActionBar">
		<item name="colorPrimary">@color/colorPrimary</item>
		<item name="colorPrimaryDark">@color/colorPrimaryDark</item>
		<item name="colorAccent">@color/colorAccent</item>
		<item name="actionBarStyle">@style/Widget.ActionBar</item>
	</style>
</resources>
```

##### 独自のstyleを作成する場合

必ず`preferenceTheme`を含んだテーマにする

###### SampleCode

```
#!xml
<style name="AppTheme" parent="Theme.AppCompat.Light.DarkActionBar">
	<item name="colorPrimary">@color/colorPrimary</item>
	<item name="colorPrimaryDark">@color/colorPrimaryDark</item>
	<item name="colorAccent">@color/colorAccent</item>
	<item name="preferenceTheme">@style/PreferenceThemeOverlay.v14.Material.Fix</item>
</style>

<style name="AppTheme.NoActionBar" parent="Theme.AppCompat.Light.NoActionBar">
	<item name="colorPrimary">@color/colorPrimary</item>
	<item name="colorPrimaryDark">@color/colorPrimaryDark</item>
	<item name="colorAccent">@color/colorAccent</item>
	<item name="preferenceTheme">@style/PreferenceThemeOverlay.v14.Material.Fix</item>
</style>
```

-----

### Application

*Application* クラスは *KeyboardApplication* を継承したクラスを使用する  
`KeyboardApplication#getInitializeParameter()` で *InitializeParameter* を作成し、returnする  
`KeyboardApplication#onInitSDK(KeyboardSDK)` でSDKの初期化を行う  

| パラメータ | 内容 |
| :--- | :--- |
| SYMBOL | 別途通知されたもの |
| TOKEN | 別途通知されたもの |
| APP NAME | アプリ名 |
| ORGANIZATION NAME | 開発者名 |
| USER DICTIONARY EXPORT PROVIDER NAME | AndroidMainfestで指定した値と同じ値 |
| MEDIA_ID | 動画リワードのメディアID |
| ZONE_ID | 動画リワードのゾーンID |
| SDK_KEY | 動画リワードのSDKキー |
| isDebug | SDKのデバッグフラグ |

###### SampleCode

```
#!java
public class MainApplication extends KeyboardApplication {

	@Override
	public void onCreate() {
		supoer.onCreate();

		// アプリ初期化
	}

	@Override
	protected void InitializeParameter getInitializeParameter() {
		return InitializeParameter.newBuilder()
				.applicationInfo("APP NAME", "ORGANIZATION NAME", "USER DICTIONARY EXPORT PROVIDER NAME") // 必須
				.adInfo("SYMBOL", "TOKEN") // 必須
				.mediaId("MEDIA ID") // 動画リワードを使用する場合は必須
				.ZoneId("ZONE ID") // 動画リワードでゾーンIDの指定がある場合は入力
				.sdkKey("SDK KEY") // 動画リワードを使用する場合は必須
				.isDebug(BuildConfig.DEBUG) // 推奨 (デフォルトはfalse)
				.build();
	}

	@Override
	protected void onInitSDK(KeyboardSDK keyboardSDK) {
		// KeyboardSDKの初期設定（プリセットテーマの追加等）
	}
}
```

#### KeyboardApplicationを継承しない場合

*MultiDexApplication* を継承した *Application* クラスの `onCreate()` 内で `KeyboardSDK#initialize(InitializeParameter, InitializeTransaction)` を呼び、KeyboardSDKの初期化を行う

###### SampleCode

```
#!java
public class MainApplication extends MultiDexApplication {

	@Override
	public void onCreate() {
		super.onCreate();

		InitializeParameter initParam = InitializeParameter.newBuilder()
				.applicationInfo("APP NAME", "ORGANIZATION NAME", "USER DICTIONARY EXPORT PROVIDER NAME") // 必須
				.adInfo("SYMBOL", "TOKEN") // 必須
				.mediaId("MEDIA ID") // 動画リワードを使用する場合は必須
				.ZoneId("ZONE ID") // 動画リワードでゾーンIDの指定がある場合は入力
				.sdkKey("SDK KEY") // 動画リワードを使用する場合は必須
				.isDebug(BuildConfig.DEBUG) // 推奨 (デフォルトはfalse)
				.build();

		KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
		keyboardSDK.initialize(initParam, new InitializeTransaction() {
			@Override
			public void init(KeyboardSDK keyboardSDK) {
				// 初期設定（プリセットテーマの追加等）
			}
		});
	}
}
```

-----

### Activity

最初に起動する *Activity* クラスの `onCreate(Bundle)` 内で　`KeyboardSDK#setupInActivity` を呼ぶ

```
#!java
public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
		keyboardSDK.setupInActivity();
	}
}
```

-----

### InputMethodService

*KeyboardBaseService* を継承したクラスを使用する  
`getLaunchActivity()` でキーボードから起動する *Activity* のクラスをreturnする  
`setupAdView(AdView)` で *AdView* の初期化を行う ( **OmegaAdnetworkSDK** 参照)  
キーボード上にテキストダイアログを表示できる (テキストのTypeface等を変更する場合は *SpannableString* を使用する)  

+ KeyboardBaseService#showTextDialog(CharSequence)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | CharSequence | 表示するテキスト |

#### ボーナスについて

*KeyboardSDK* でユーザーIDを設定すると、自動で処理する。  
ポイント獲得時はテキストダイアログが表示される。  

##### ログインボーナス

1日1回ログインボーナスを獲得する処理を行う。 (毎日午前4時にリセット)  
ログインボーナスのダイアログの文言をカスタムする場合は `string` 内に定義する。 ( `%d` を含むと獲得したポイント数を表示）  
ログインボーナスのダイアログの表示をカスタムする場合は `showLoginBonusDialog(int)` をOverrideする。  

+ KeyboardBaseService#showLoginBonusDialog(int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | int | 獲得したポイント |

###### ポイント獲得時の文言

| リソース名 | デフォルトの文言 |
| :--- | :--- |
| keyboard_sdk_get_login_bonus_text | ログインボーナス！\nXXポイント獲得しました！ |

##### 抽選ポイント

1時間毎に表示した広告の数をもとに抽選を行う。  
抽選ポイントのダイアログの文言をカスタムする場合は `string` 内に定義する。 ( `%d` を含むと獲得したポイント数を表示）  
抽選ポイントのダイアログの表示をカスタムする場合は `showLoginBonusDialog(int)` をOverrideする。  

+ KeyboardBaseService#showLotteryDialog(int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | int | 獲得したポイント |

###### ポイント獲得時の文言

| リソース名 | デフォルトの文言 |
| :--- | :--- |
| keyboard_sdk_get_lottery_text | 特別ボーナス！\nXXポイント獲得しました！ |

###### SampleCode

```
#!java
public class KeyboardService extends KeyboardBaseService {

	@NonNull
	@Override
	protected Class<? extends Activity> getLaunchActivity() {
		return MainActivity.class;
	}

	@Override
	protected void setupAdView(AdView adView) {
		// AdViewの初期化
	}

	public void textDialog() {
		// テキストダイアログの表示
		showTextDialog("メッセージ");
	}

	@Override
	public void showLoginBonusDialog(int point) {
		showTextDialog("ログインボーナス！\n" + point + "ポイント獲得しました！")
	}

	@Override
	public void showLotteryDialog(int point) {
		showTextDialog("特別ボーナス！\n" + point + "ポイント獲得しました！")
	}
}
```

-----

## 3. SDKの機能

1. チュートリアル画面
1. パーミッション
1. キーボードの有効化
1. キーボードの選択
1. 初期設定画面
1. 設定画面
1. カスタム設定画面
1. プリセットテーマ作成
1. カスタムテーマ作成
1. カスタムテーマの取得
1. 現在のカスタムテーマの取得
1. カスタムテーマの選択
1. カスタムテーマの削除
1. カスタムテーマ一覧取得
1. カスタムテーマの数取得
1. カスタムテーマが削除可能か確認
1. カスタムテーマの背景画像取得
1. カスタムテーマのサムネイル画像取得
1. CustomThemeクラスのパラメータ
1. カスタムテーマ作成画面
1. 各種ボーナス
1. 動画リワード
1. キーボード上にView表示

-----

### 1. チュートリアル画面

パーミッションの許可、キーボードの有効化、キーボードの選択、キーボードの初期設定を行うチュートリアル画面を表示する。  
結果を受け取る場合はリクエストコードを指定。  
各ページで表示するリソースを用意する。  
*※使用する場合はManifestへ  **com.omega.keyboard.sdk.activity.TutorialActivity** を追加*  

+ KeyboardSDK#startTutorialActivity(Activity)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示するActivity |

+ KeyboardSDK#startTutorialActivityForResult(Activity, int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示するActivity |
| 2 | int | リクエストコード |

+ KeyboardSDK#startTutorialActivity(Fragment)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示するFragment |

+ KeyboardSDK#startTutorialActivityForResult(Fragment, int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示するFragment |
| 2 | int | リクエストコード |

#### 各ページで表示するリソース

##### 1ページ目 : アクセスの許可

| リソースの内容 | リソースの種類 | リソース名 | デフォルトの値 | 必須 |
| :--- | :--- | :--- | :--- | :---: |
| アクセスの許可についての画像 (全画面) | drawable | keyboard_sdk_request_permissions_image | --- | ○ |
| アクセスの許可を求めるボタンのテキスト | string | keyboard_sdk_request_permission_button_text | アクセスを許可する | × |

##### 2ページ目 : キーボードの有効化

| リソースの内容 | リソースの種類 | リソース名 | デフォルトの値 | 必須 |
| :--- | :--- | :--- | :--- | :---: |
| キーボードの有効化についての画像 (全画面) | drawable | keyboard_sdk_request_enabled_image | --- | ○ |
| キーボードの有効化をするボタンのテキスト | string | keyboard_sdk_request_enabled_button_text | キーボードを有効化する | × |

##### 3ページ目 : キーボードの選択

| リソースの内容 | リソースの種類 | リソース名 | デフォルトの値 | 必須 |
| :--- | :--- | :--- | :--- | :---: |
| キーボードの選択についての画像 (全画面) | drawable | keyboard_sdk_request_selected_image | --- | ○ |
| キーボードの選択をするボタンのテキスト | string | keyboard_sdk_request_selected_button_text | キーボードを選択する | × |

##### 4ページ目 : 入力方法の設定

| リソースの内容 | リソースの種類 | リソース名 | デフォルトの値 | 必須 |
| :--- | :--- | :--- | :--- | :---: |
| 入力方法を設定するボタンのテキスト | string | keyboard_sdk_first_keyboard_setting_button_text | 設定する | × |

##### 5ページ目 : チュートリアル終了

| リソースの内容 | リソースの種類 | リソース名 | デフォルトの値 | 必須 |
| :--- | :--- | :--- | :--- | :---: |
| チュートリアル終了の画像 (全画面) | drawable | keyboard_sdk_finish_tutorial_image | --- | ○ |
| チュートリアルを終了するボタンのテキスト | string | keyboard_sdk_finish_tutorial_button_text | はじめる | × |

###### SampleCode

```
#!java
public MainActivity extends AppCompatActivity {

	public void showTutorial() {
		KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
		keyboardSDK.startTutorialActivityForResult(this, REQUEST_CODE_TUTORIAL);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQUEST_CODE_TUTORIAL:
				switch (resultCode) {
					case RESULT_OK:
						// チュートリアル完了
						break;

					case RESULT_CANCELED:
						// チュートリアル中断
						break;
				}
				break;

			default:
				break;
		}
	}
}
```

-----

### 2. パーミッション

#### 必須パーミッション

+ android.permission.READ_EXTERNAL_STORAGE
+ android.permission.WRITE_EXTERNAL_STORAGE

#### パーミッションの許可確認

+ PermissionUtil#isPermit()

###### SampleCode

```
#!java
public void checkPermissions() {
	PermissionUtil permissionUtil = PermissionUtil.sharedInstance(this);
	if (permissionUtil.isPermit()) {
		// 許可済み
	}
	else {
		// 未許可
	}
}
```

#### キーボードSDKで必要なパーミッションをリクエスト

##### SDKの画面を使用する場合

結果を受け取る場合はリクエストコードを指定。  
表示するリソースを用意する。  
*※使用する場合はManifestへ  **com.omega.keyboard.sdk.activity.ActivateKeyboardActivity** を追加*  

+ KeyboardSDK#startActivateKeyboardActivityForResult(Activity, ActivateKeyboardActivity.Type)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示するActivity |
| 2 | ActivateKeyboardActivity.Type | タイプ |

+ KeyboardSDK#startActivateKeyboardActivityForResult(Activity, ActivateKeyboardActivity.Type, int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示するActivity |
| 2 | ActivateKeyboardActivity.Type | タイプ |
| 3 | int | リクエストコード |

+ KeyboardSDK#startActivateKeyboardActivityForResult(Fragment, ActivateKeyboardActivity.Type)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示するFragment |
| 2 | ActivateKeyboardActivity.Type | タイプ |

+ KeyboardSDK#startActivateKeyboardActivityForResult(Fragment, ActivateKeyboardActivity.Type, int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示するFragment |
| 2 | ActivateKeyboardActivity.Type | タイプ |
| 3 | int | リクエストコード |

###### 表示するリソース

| リソースの内容 | リソースの種類 | リソース名 | デフォルトの値 | 必須 |
| :--- | :--- | :--- | :--- | :---: |
| ActionBarに表示するタイトル | string | keyboard_sdk_request_permissions_title | --- | × |
| アクセスの許可についての画像 (全画面) | drawable | keyboard_sdk_request_permissions_image | --- | ○ |
| アクセスの許可を求めるボタンのテキスト | string | keyboard_sdk_request_permission_button_text | アクセスを許可する | × |

###### SampleCode

```
#!java
public MainActivity extends Activity {

	public void showPermissionCheck() {
		KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
		keyboardSDK.startActivateKeyboardActivityForResult(this, ActivateKeyboardActivity.Type.PERMISSIONS, REQUEST_CODE_PERMISSIONS);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQUEST_CODE_PERMISSIONS:
				switch (resultCode) {
					case RESULT_OK:
						// パーミッション許可
						break;

					case RESULT_CANCELED:
						// パーミッション未許可
						break;
				}
				break;

			default:
				break;
		}
	}
}

```

##### SDKの画面を使用しない場合

+ PermissionUtil#requestPermissions(Activity activity, PermissionUtil.RequestPermissionCallback)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示中のActivity |
| 2 | PermissionUtil.RequestPermissionCallback | リクエストのCallback |

+ PermissionUtil#requestPermissions(Fragment fragment, PermissionUtil.RequestPermissionCallback)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示中のFragment |
| 2 | PermissionUtil.RequestPermissionCallback | リクエストのCallback |

こちらを使用する場合は、 `onRequestPermissionsResult(int, String[], int[])` をOverrideし、下記を呼ぶ

+ PermissionUtil#onRequestPermissionsResult(int, String[], int[])

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | int | requestCode |
| 2 | String[] | permissions |
| 3 | int[] | grantResults |

###### SampleCode

```
#!java
public MainActivity extends AppCompatActivity {

	private PermissionUtil permissionUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.permissionUtil = PermissionUtil.sharedInstance(this);
	}

	public void showPermissionCheck() {
		this.permissionUtil.requestPermissions(this, new PermissionUtil.RequestPermissionCallback() {
			@Override
			public void onRequestPermission(boolean isPermit) {
				if (isPermit) {
					// パーミッション許可
				}
				else {
					// パーミッション未許可
				}
			}
		});
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);

		if (this.permissionUtil != null) {
			this.permissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}
```

-----

### 3. キーボードの有効化

#### キーボードの有効化確認

+ KeyboardUtil#isEnabled()

###### SampleCode

```
#!java
public void checkKeyboardEnabled() {
	KeyboardUtil keyboardUtil = KeyboardUtil.sharedInstance(this);
	if (keyboardUtil.isEnabled()) {
		// キーボード有効
	}
	else {
		// キーボード無効
	}
}
```

#### キーボードの有効化をリクエスト

##### SDKの画面を使用する場合

結果を受け取る場合はリクエストコードを指定。  
表示するリソースを用意する。  
*※使用する場合はManifestへ  **com.omega.keyboard.sdk.activity.ActivateKeyboardActivity** を追加*  

+ KeyboardSDK#startActivateKeyboardActivityForResult(Activity, ActivateKeyboardActivity.Type)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示するActivity |
| 2 | ActivateKeyboardActivity.Type | タイプ |

+ KeyboardSDK#startActivateKeyboardActivityForResult(Activity, ActivateKeyboardActivity.Type, int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示するActivity |
| 2 | ActivateKeyboardActivity.Type | タイプ |
| 3 | int | リクエストコード |

+ KeyboardSDK#startActivateKeyboardActivityForResult(Fragment, ActivateKeyboardActivity.Type)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示するFragment |
| 2 | ActivateKeyboardActivity.Type | タイプ |

+ KeyboardSDK#startActivateKeyboardActivityForResult(Fragment, ActivateKeyboardActivity.Type, int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示するFragment |
| 2 | ActivateKeyboardActivity.Type | タイプ |
| 3 | int | リクエストコード |

###### 表示するリソース

| リソースの内容 | リソースの種類 | リソース名 | デフォルトの値 | 必須 |
| :--- | :--- | :--- | :--- | :---: |
| ActionBarに表示するタイトル | string | keyboard_sdk_request_enabled_title | --- | × |
| キーボードの有効化についての画像 (全画面) | drawable | keyboard_sdk_request_enabled_image | --- | ○ |
| キーボードの有効化をするボタンのテキスト | string | keyboard_sdk_request_enabled_button_text | キーボードを有効化する | × |

###### SampleCode

```
#!java
public MainActivity extends Activity {

	public void requestPermissions() {
		KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
		keyboardSDK.startActivateKeyboardActivityForResult(this, ActivateKeyboardActivity.Type.ENABLED, REQUEST_CODE_KEYBOARD_ENABLED);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQUEST_CODE_KEYBOARD_ENABLED:
				switch (resultCode) {
					case RESULT_OK:
						// キーボード有効
						break;

					case RESULT_CANCELED:
						// キーボード無効
						break;
				}
				break;

			default:
				break;
		}
	}
}

```

##### SDKの画面を使用しない場合

+ KeyboardUtil#requestEnabled(Activity activity, KeyboardUtil.RequestEnabledCallback)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示中のActivity |
| 2 | KeyboardUtil.RequestEnabledCallback | リクエストのCallback |

+ KeyboardUtil#requestEnabled(Fragment fragment, KeyboardUtil.RequestEnabledCallback)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示中のFragment |
| 2 | KeyboardUtil.RequestEnabledCallback | リクエストのCallback |

こちらを使用する場合は、 `onActivityResult(int, int, Intent)` をOverrideし、下記を呼ぶ

+ KeyboardUtil#onActivityResult(int, int, Intent)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | int | requestCode |
| 2 | int | resultCode |
| 3 | Intent | data |

###### SampleCode

```
#!java
public MainActivity extends AppCompatActivity {

	private KeyboardUtil keyboardUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.keyboardUtil = KeyboardUtil.sharedInstance(this);
	}

	public void requestKeyboardEnabled() {
		this.keyboardUtil.requestEnabled(this, new KeyboardUtil.RequestEnabledCallback() {
			@Override
			public void onEnabled(boolean isEnabled) {
				if (isEnabled) {
					// 有効
				}
				else {
					// 無効
				}
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (this.keyboardUtil != null) {
			this.keyboardUtil.onActivityResult(requestCode, resultCode, data);
		}
	}
}
```

-----

### 4. キーボードの選択

#### キーボードの選択確認

+ KeyboardUtil#isSelected()

###### SampleCode

```
#!java
public void checkKeyboardEnabled() {
	KeyboardUtil keyboardUtil = KeyboardUtil.sharedInstance(this);
	if (keyboardUtil.isSelected()) {
		// キーボード選択
	}
	else {
		// キーボード未選択
	}
}
```

#### キーボードの選択をリクエスト

##### SDKの画面を使用する場合

結果を受け取る場合はリクエストコードを指定。  
表示するリソースを用意する。  
*※使用する場合はManifestへ  **com.omega.keyboard.sdk.activity.ActivateKeyboardActivity** を追加*  

+ KeyboardSDK#startActivateKeyboardActivityForResult(Activity, ActivateKeyboardActivity.Type)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示するActivity |
| 2 | ActivateKeyboardActivity.Type | タイプ |

+ KeyboardSDK#startActivateKeyboardActivityForResult(Activity, ActivateKeyboardActivity.Type, int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示するActivity |
| 2 | ActivateKeyboardActivity.Type | タイプ |
| 3 | int | リクエストコード |

+ KeyboardSDK#startActivateKeyboardActivityForResult(Fragment, ActivateKeyboardActivity.Type)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示するFragment |
| 2 | ActivateKeyboardActivity.Type | タイプ |

+ KeyboardSDK#startActivateKeyboardActivityForResult(Fragment, ActivateKeyboardActivity.Type, int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示するFragment |
| 2 | ActivateKeyboardActivity.Type | タイプ |
| 3 | int | リクエストコード |

###### 表示するリソース

| リソースの内容 | リソースの種類 | リソース名 | デフォルトの値 | 必須 |
| :--- | :--- | :--- | :--- | :---: |
| ActionBarに表示するタイトル | string | keyboard_sdk_request_selected_title | --- | × |
| キーボードの選択についての画像 (全画面) | drawable | keyboard_sdk_request_selected_image | --- | ○ |
| キーボードの選択をするボタンのテキスト | string | keyboard_sdk_request_selected_button_text | キーボードを選択する | × |

###### SampleCode

```
#!java
public MainActivity extends Activity {

	public void requestPermissions() {
		KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
		keyboardSDK.startActivateKeyboardActivityForResult(this, ActivateKeyboardActivity.Type.SELECTED, REQUEST_CODE_KEYBOARD_SELECTED);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQUEST_CODE_KEYBOARD_SELECTED:
				switch (resultCode) {
					case RESULT_OK:
						// キーボード選択
						break;

					case RESULT_CANCELED:
						// キーボード未選択
						break;
				}
				break;

			default:
				break;
		}
	}
}

```

##### SDKの画面を使用しない場合

+ KeyboardUtil#requestSelected(KeyboardUtil.RequestSelectedCallback)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | KeyboardUtil.RequestSelectedCallback | 選択リクエストのCallback |

こちらを使用する場合は、 `onWindowFocusChanged(boolean)` をOverrideし、下記を呼ぶ

+ KeyboardUtil#onWindowFocusChanged(boolean hasFocus)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | boolean | hasFocus |

###### SampleCode

```
#!java
public MainActivity extends AppCompatActivity {

	private KeyboardUtil keyboardUtil;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.keyboardUtil = KeyboardUtil.sharedInstance(this);
	}

	public void requestKeyboardEnabled() {
		this.keyboardUtil.requestEnabled(this, new KeyboardUtil.RequestEnabledCallback() {
			@Override
			public void onEnabled(boolean isEnabled) {
				if (isEnabled) {
					// 有効
				}
				else {
					// 無効
				}
			}
		});
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);

		if (this.keyboardUtil != null) {
			this.keyboardUtil.onWindowFocusChanged(hasFocus);
		}
	}
}
```

-----

### 5. 初期設定画面

キーボードの初期設定画面を表示する。  
結果を受け取る場合はリクエストコードを指定。  
表示するリソースを用意する。  
*※使用する場合はManifestへ  **com.omega.keyboard.sdk.activity.FirstSettingsActivity** を追加*  

+ KeyboardSDK#startFirstSettingsActivity(Activity)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示するActivity | 

+ KeyboardSDK#startFirstSettingsActivityForResult(Activity, int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示するActivity |
| 2 | int | リクエストコード |

+ KeyboardSDK#startFirstSettingsActivity(Fragment)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示するFragment | 

+ KeyboardSDK#startFirstSettingsActivityForResult(Fragment, int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示するFragment | 
| 2 | int | リクエストコード |

#### 表示するリソース

| リソースの内容 | リソースの種類 | リソース名 | デフォルトの値 | 必須 |
| :--- | :--- | :--- | :--- | :---: |
| 入力方法を設定するボタンのテキスト | string | keyboard_sdk_first_keyboard_setting_button_text | 設定する | × |

###### SampleCode

```
#!java
public void MainActivity extends AppCompatActivity {

	public void showFirstSettings() {
		KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
		keyboardSDK.startFirstSettingsActivityForResult(this, REQUEST_CODE_FIRST_SETTINGS);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQUEST_CODE_FIRST_SETTINGS:
				switch (resultCode) {
					case RESULT_OK:
						// 初期設定完了
						break;

					case RESULT_CANCELED:
						// 初期設定中断
						break;
				}
				break;

			default:
				break;
		}
	}
}
```

-----

### 6. 設定画面

キーボードの設定画面を表示する。  
終了を受け取る場合はリクエストコードを指定。  
*※Manifestへ  **com.omega.keyboard.sdk.activity.SettingsActivity** ・ **com.omega.keyboard.sdk.activity.UserDictionaryToolActivity** を追加*  

+ KeyboardSDK#startSettingsActivity(Activity)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示するActivity | 

+ KeyboardSDK#startSettingsActivityForResult(Activity, int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Activity | 表示するActivity |
| 2 | int | リクエストコード |

+ KeyboardSDK#startSettingsActivity(Fragment)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示するFragment |

+ KeyboardSDK#startSettingsActivityForResult(Fragment, int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | Fragment | 表示するFragment |
| 2 | int | リクエストコード |

###### SampleCode

```
#!java
public void MainActivity extends AppCompatActivity {

	public void showSettings() {
		KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
		keyboardSDK.startSettingsActivityForResult(this, REQUEST_CODE_SETTINGS);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQUEST_CODE_SETTINGS:
				// 設定画面終了
				break;

			default:
				break;
		}
	}
}
```

-----

### 7. カスタム設定画面

xmlで *PreferenceScreen* を作成し、キーボードの設定画面の下部に追加する。  
*SharedPreference* でデータを管理している場合は *SettingsFragment* を使用する。  
カスタムのPreferenceやそれ以外の方法でデータを管理している場合は *SettingsFragment* を継承したクラスを使用する。  
表示するページは *PreferencePage* で指定する。指定しなかった場合は設定トップ画面を表示する。  

#### PreferencePage

| PreferencePage | 内容 |
| :--- | :--- |
| SDK_KEYBOARD | 設定トップ画面 |
| SDK_SOFTWARE_KEYBOARD | ソフトウェアキーボードの詳細設定 |
| SDK_CLEAR_DICTIONARY | 辞書のクリア |

#### SharedPreferenceで管理されているデータの場合

+ SettingsFragment#newInstance(int)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | int | レイアウトするViewのID |

+ SettingsFragment#newInstance(int, PreferencePage)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | int | レイアウトするViewのID |
| 2 | PreferencePage | 表示する画面 |

+ SettingsFragment#newInstance(int, ArrayList<Integer>)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | int | レイアウトするViewのID |
| 2 | ArrayList<Integer> | 追加するPreferenceScreenのxmlのリソースID |

+ SettingsFragment#newInstance(int, PreferencePage, ArrayList<Integer>)

| 引数 | クラス | 内容 |
| :---: | :--- | :--- |
| 1 | int | レイアウトするViewのID |
| 2 | PreferencePage | 表示する画面 |
| 3 | ArrayList<Integer> | 追加するPreferenceScreenのxmlのリソースID |

###### SampleCode

```
#!java
public void MainActivity extends AppCompatActivity {
	
	public void showCustomSettings() {
		ArrayList<Integer> addContents = new ArrayList<>();
		addContents.add(R.xml.custom_pref);
		SettingsFragment settingsFragment = SettingsFragment.newInstance(R.id.container, addContents);

		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.container, fragment, "TAG");
		fragmentTransaction.commit();
	}
}
```

#### カスタムのデータの場合

`updatePreferences()` をOverrideし、項目をクリックした時の処理等を行う。  
パラメータは下記で指定する。  

| 定数名 | 内容 | 備考 |
| :--- | :--- | :--- |
| SettingsFragment#ARG_CONTAINER_ID | レイアウトするViewのID | 必須 |
| SettingsFragment#ARG_PREFERENCE_PAGE | 表示する画面 | 指定しない場合はSDK_KEYBOARD |
| SettingsFragment#ARG_ADD_CONTENTS | 追加する設定項目 |  |

###### SampleCode

```
#!java
public class CustomSettingsFragment extends SettingsFragment {

	public static CustomSettingsFragment newInstance() {
		CustomSettingsFragment fragment = new CustomSettingsFragment();

		ArrayList<Integer> addContents = new ArrayList<>();
		addContents.add(R.xml.custom_pref_01);
		addContents.add(R.xml.custom_pref_02);

		Bundle args = new Bundle();
        args.putInt(SettingsFragment.ARG_CONTAINER_ID, R.id.container);
		args.putSerializable(SettingsFragment.ARG_PREFERENCE_PAGE, PreferencePage.SDK_KEYBOARD);
		args.putIntegerArrayList(SettingsFragment.ARG_ADD_CONTENTS, addContents);

		fragment.setArguments(args);
		return fragment;
	}

	@Override
	protected void updatePreferences() {
		Preference customPref = findPreference("custom_pref_key");
		if (customPref != null) {
			customPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
				@Override
				public boolean onPreferenceClick(Preference preference) {
					// クリック時の処理
					return true;
				}
			});
		}
	}
}
```

-----

### 8. プリセットテーマ作成

プリセットのカスタムテーマを作成する。

+ KeyboardSDK#addPresetTheme(int, int, int, CreateThemeCallback)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | int | 背景画像のDrawableのリソースID (サムネイル画像は自動生成) |
| 2 | int | 文字の色 |
| 3 | int | 線の色 |
| 4 | CreateThemeCallback | カスタムテーマ作成Callback (受け取らない場合はnull) |

+ KeyboardSDK#addPresetTheme(int, int, int, int, CreateThemeCallback)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | int | 背景画像のDrawableのリソースID |
| 2 | int | サムネイル画像のDrawableのリソースID |
| 3 | int | 文字の色 |
| 4 | int | 線の色 |
| 5 | CreateThemeCallback | カスタムテーマ作成Callback (受け取らない場合はnull) |

+ KeyboardSDK#addPresetTheme(int, Bitmap, int, int, CreateThemeCallback)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | int | 背景画像のDrawableのリソースID |
| 2 | Bitmap | サムネイル画像のBitmap |
| 3 | int | 文字の色 |
| 4 | int | 線の色 |
| 5 | CreateThemeCallback | カスタムテーマ作成Callback (受け取らない場合はnull) |

+ KeyboardSDK#addPresetTheme(Bitmap, int, int, CreateThemeCallback) 

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | Bitmap | 背景画像のBitmap (サムネイル画像は自動生成) |
| 2 | int | 文字の色 |
| 3 | int | 線の色 |
| 4 | CreateThemeCallback | カスタムテーマ作成Callback (受け取らない場合はnull) |

+ KeyboardSDK#addPresetTheme(Bitmap, int, int, int, CreateThemeCallback) 

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | Bitmap | 背景画像のBitmap |
| 2 | int | サムネイル画像のDrawableのリソースID |
| 3 | int | 文字の色 |
| 4 | int | 線の色 |
| 5 | CreateThemeCallback | カスタムテーマ作成Callback (受け取らない場合はnull) |

+ KeyboardSDK#addPresetTheme(Bitmap, Bitmap, int, int, CreateThemeCallback) 

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | Bitmap | 背景画像のBitmap |
| 2 | Bitamp | サムネイル画像のBitmap |
| 3 | int | 文字の色 |
| 4 | int | 線の色 |
| 5 | CreateThemeCallback | カスタムテーマ作成Callback (受け取らない場合はnull) |

###### SampleCode

```
#!java
public void addPresetTheme() {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	keyboardSDK.addPresetTheme(R.drawable.default_keyboard_background, Color.GREEN, Color.RED, new CreateThemeCallback() {
		@Override
		public void onComplete(boolean success, CustomTheme customTheme) {
			if (success) {
				// 作成成功
			}
			else {
				// 作成失敗
			}
		}
	});
}
```

-----

### 9. カスタムテーマ作成

カスタムテーマを作成する

+ KeyboardSDK#addCustomTheme(int, int, int, CreateThemeCallback)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | int | 背景画像のDrawableのリソースID (サムネイル画像は自動生成) |
| 2 | int | 文字の色 |
| 3 | int | 線の色 |
| 4 | CreateThemeCallback | カスタムテーマ作成Callback (受け取らない場合はnull) |

+ KeyboardSDK#addCustomTheme(int, int, int, int, CreateThemeCallback)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | int | 背景画像のDrawableのリソースID |
| 2 | int | サムネイル画像のDrawableのリソースID |
| 3 | int | 文字の色 |
| 4 | int | 線の色 |
| 5 | CreateThemeCallback | カスタムテーマ作成Callback (受け取らない場合はnull) |

+ KeyboardSDK#addCustomTheme(int, Bitmap, int, int, CreateThemeCallback)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | int | 背景画像のDrawableのリソースID |
| 2 | Bitmap | サムネイル画像のBitmap |
| 3 | int | 文字の色 |
| 4 | int | 線の色 |
| 5 | CreateThemeCallback | カスタムテーマ作成Callback (受け取らない場合はnull) |

+ KeyboardSDK#addCustomTheme(Bitmap, int, int, CreateThemeCallback) 

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | Bitmap | 背景画像のBitmap (サムネイル画像は自動生成) |
| 2 | int | 文字の色 |
| 3 | int | 線の色 |
| 4 | CreateThemeCallback | カスタムテーマ作成Callback (受け取らない場合はnull) |

+ KeyboardSDK#addCustomTheme(Bitmap, int, int, int, CreateThemeCallback) 

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | Bitmap | 背景画像のBitmap |
| 2 | int | サムネイル画像のDrawableのリソースID |
| 3 | int | 文字の色 |
| 4 | int | 線の色 |
| 5 | CreateThemeCallback | カスタムテーマ作成Callback (受け取らない場合はnull) |

+ KeyboardSDK#addCustomTheme(Bitmap, Bitmap | int, int, CreateThemeCallback) 

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | Bitmap | 背景画像のBitmap |
| 2 | Bitmap | サムネイル画像のBitmap |
| 3 | int | 文字の色 |
| 4 | int | 線の色 |
| 5 | CreateThemeCallback | カスタムテーマ作成Callback (受け取らない場合はnull) |

###### SampleCode

```
#!java
public void addCustomTheme() {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	keyboardSDK.addCustomTheme(R.drawable.keyboard_background, Color.BLUE, Color.YELLOW, new CreateThemeCallback() {
		@Override
		public void onComplete(boolean success, CustomTheme customTheme) {
			if (success) {
				// 作成成功
			}
			else {
				// 作成失敗
			}
		}
	});
}
```

-----

### 10. カスタムテーマの取得

+ KeyboardSDK#getCustomTheme(String)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | String | カスタムテーマのID |

###### SampleCode

```
#!java
public void getCustomTheme(String customThemeId) {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	CustomTheme customTheme = keyboardSDK.getCustomTheme(customThemeId);
	if (customTheme != null) {
		// 取得成功
	}
	else {
		// 取得失敗
	}
}
```

-----

### 11. 現在のカスタムテーマの取得

+ KeyboardSDK#getCurrentCustomTheme()

###### SampleCode

```
#!java
public void getCurrentCustomTheme() {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	CustomTheme = customTheme = keyboardSDK.getCurrentCustomTheme();
	if (customTheme != null) {
		// 取得成功
	}
	else {
		// 取得失敗
	}
}
```

-----

### 12. カスタムテーマの選択

テーマを選択する

+ KeyboardSDK#setCurrentCustomTheme(CustomTheme)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | CustomTheme | カスタムテーマ |

+ KeyboardSDK#setCurrentCustomTheme(String)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | String | カスタムテーマのID |

###### SampleCode

```
#!java
public void setCurrentCustomTheme(CustomTheme customTheme) {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	keyboardSDK.setCurrentCustomTheme(customTheme);
}

public void setCurrentCustomTheme(String customThemeId) {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	keyboardSDK.setCurrentCustomTheme(customThemeId);
}
```

-----

### 13. カスタムテーマの削除

+ KeyboardSDK#removeCustomTheme(CustomTheme)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | CustomTheme | カスタムテーマ |

+ KeyboardSDK#removeCustomTheme(String)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | String | カスタムテーマのID |

###### SampleCode

```
#!java
public void removeCustomTheme(CustomTheme customTheme) {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	keyboardSDK.removeCustomTheme(customTheme);
}

public void removeCustomTheme(String customThemeId) {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	keyboardSDK.removeCustomTheme(customThemeId);
}
```

-----

### 14. カスタムテーマ一覧取得

+ KeyboardSDK#getCustomThemeList(int)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | int | カスタムテーマの種類 |

+ KeyboardSDK#getCustomThemeList(int, CustomTheme.Sort)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | int | カスタムテーマの種類 |
| 2 | CustomTheme.Sort | リストの順序 |

| カスタムテーマの種類 | 定数 |
| :--- | :--- |
| プリセットテーマ | CustomTheme.TYPE_PRESET_IMAGE |
| ユーザー作成テーマ | CustomTheme.TYPE_USER_IMAGE |

| 定数 | ソート |
| :--- | :--- |
| CustomTheme.Sort.ASCENDING | 登録した順 |
| CustomTheme.Sort.DESCENDING | 新しいもの順 |

###### SampleCode

```
#!java
public void getPresetThemeList() {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	List<CustomTheme> customThemes = keyboardSDK.getCustomThemeList(CustomTheme.TYPE_PRESET_IMAGE);
	if (customThemes != null) {
		// 取得成功
	}
	else {
		// 取得失敗
	}
}

public void getCustomThemeList() {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	List<CustomTheme> customThemes = keyboardSDK.getCustomThemeList(CustomTheme.TYPE_USER_IMAGE, CustomTheme.Sort.DESCENDING);
	if (customThemes != null) {
		// 取得成功
	}
	else {
		// 取得失敗
	}
}
```

-----

### 15. カスタムテーマの数取得

+ KeyboardSDK#getNumCustomThemes(int type)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | int | カスタムテーマの種類 |

| カスタムテーマの種類 | 定数 |
| :--- | :--- |
| プリセットテーマ | CustomTheme.TYPE_PRESET_IMAGE |
| ユーザー作成テーマ | CustomTheme.TYPE_USER_IMAGE |

###### SampleCode

```
#!java
public void getNumPresetThemes() {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	long numThemes = keyboardSDK.getNumCustomThemes(CustomTheme.TYPE_PRESET_IMAGE);
}

public void getNumCustomThemes() {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	long numThemes = keyboardSDK.getNumCustomThemes(CustomTheme.TYPE_USER_IMAGE);
}
```

-----

### 16. カスタムテーマが削除可能か確認

カスタムテーマが削除可能かチェックする。  
条件はプリセットではないカスタムテーマの数が1件以上かつ、それが選択されていない。  

+ KeyboardSDK#canRemoveCustomTheme()

+ KeyboardSDK#canRemoveCustomThemes(int)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | int | 削除するテーマの数 (1以上) |

###### SampleCode

```
#!java
public void checkCanRemoveCustomTheme() {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	if (keyboardSDK.canRemoveCustomTheme()) {
		// 削除可能
	}
	else {
		// 削除不可
	}
}

public void checkCanRemoveCustomTheme() {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	if (keyboardSDK.canRemoveCustomTheme(2)) {
		// 2件削除可能
	}
	else {
		// 2件削除不可
	}
}
```

-----

### 17. カスタムテーマの背景画像取得

+ KeyboardSDK#loadThemeImage(CustomTheme, LoadImageCallback)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | CustomTheme | カスタムテーマ |
| 2 | LoadImageCallback | 画像取得Callback |

###### SampleCode

```
#!java
public void loadThemeImage() {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	keyboardSDK.loadThemeImage(customTheme, new LoadImageCallback() {
		@Override
		public void onLoadImage(boolean success, Bitmap bitmap) {
			if (success) {
				// 取得成功
			}
			else {
				// 取得失敗
			}
		}
	});
}
```

-----

### 18. カスタムテーマのサムネイル画像取得

+ KeyboardSDK#loadThumbnailImage(CustomTheme, LoadImageCallback)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | CustomTheme | カスタムテーマ |
| 2 | LoadImageCallback | 画像取得Callback |

###### SampleCode

```
#!java
public void loadThumbnailImage() {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	keyboardSDK.loadThumbnailImage(customTheme, new LoadImageCallback() {
		@Override
		public void onLoadImage(boolean success, Bitmap bitmap) {
			if (success) {
				// 取得成功
			}
			else {
				// 取得失敗
			}
		}
	});
}
```

-----

### 19. CustomThemeクラスのパラメータ

| パラメータ | クラス |  内容 |
| :--- | :--- | :--- |
| getId() | String | テーマID |
| getType() | int | テーマの種類 |
| isPreset() | boolean | プリセットテーマフラグ |
| isSelected() | boolean | 選択フラグ |
| getTextColor() | int | 文字の色 |
| getLineColor() | int | 線の色 |
| getCreated() | Date | 作成日時 |
| getModified() | Date | 最終編集日時 |

-----

### 20. カスタムテーマ作成画面

カスタムテーマ作成画面を表示。  
結果を受け取る場合はリクエストコードを指定。  
作成したカスタムテーマのIDは `data` から `KeyboardSDK#CUSTOM_THEME_ID` をキーにして取得。  
*※使用する場合はManifestへ  **com.omega.keyboard.sdk.activity.CreateThemeActivity** を追加*  

+ KeyboardSDK#startCreateThemeActivity(Activity, List<Integer>, List<Integer>)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | Activity | 表示するActivity |
| 2 | List<Integer> | 選択できる文字の色のリスト |
| 3 | List<Integer> | 選択できる線の色のリスト |

+ KeyboardSDK#startCreateThemeActivity(Activity, int, List<Integer>, List<Integer>)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | Activity | 表示するActivity |
| 2 | int | リクエストコード |
| 3 | List<Integer> | 選択できる文字の色のリスト |
| 4 | List<Integer> | 選択できる線の色のリスト |

+ KeyboardSDK#startCreateThemeActivity(Fragment, List<Integer>, List<Integer>)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | Fragment | 表示するFragment |
| 2 | List<Integer> | 選択できる文字の色のリスト |
| 3 | List<Integer> | 選択できる線の色のリスト |

+ KeyboardSDK#startCreateThemeActivity(Fragment, int, List<Integer>, List<Integer>)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | Fragment | 表示するFragment |
| 2 | int | リクエストコード |
| 3 | List<Integer> | 選択できる文字の色のリスト |
| 4 | List<Integer> | 選択できる線の色のリスト |

###### SampleCode

```
#!java
public MainActivity extends AppCompatActivity {

	public void showCreateTheme() {
		List<Integer> textColors = new ArrayList<>();
		textColors.add(Color.BLACK);
		textColors.add(Color.WHITE);
		textColors.add(Color.RED);
		textColors.add(Color.GREEN);
		textColors.add(Color.BLUE);
		
		List<Integer> lineColors = new ArrayList<>();
		lineColors.add(Color.BLACK);
		lineColors.add(Color.WHITE);
		lineColors.add(Color.RED);
		lineColors.add(Color.GREEN);
		lineColors.add(Color.BLUE);
		lineColors.add(Color.TRANSPARENT);

		KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
		keyboardSDK.startCreateThemeActivityForResult(this, REQUEST_CODE_CREATE_THEME, textColors, lineColors);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
			case REQUEST_CODE_CREATE_THEME:
				switch (resultCode) {
					case RESULT_OK:
						// カスタムテーマ作成成功
						String customThemeId = data.getStringExtra(KeyboardSDK.CUSTOM_THEME_ID);
						break;

					case RESULT_CANCELED:
						// カスタムテーマ作成中断
						break;
				}
				break;

			default:
				break;
		}
	}
}
```

-----

### 21. 各種ボーナス  

*KeyboardSDK* でユーザーIDを設定すると、ログインボーナス・抽選ポイントの処理を行う。  
ポイント獲得時はキーボード上にテキストダイアログが表示される。  

#### ユーザーID設定

+ KeyboardSDK#setUserId(String)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | String | ユーザーID (nullを指定すると削除) |

#### ユーザーID削除

+ KeyboardSDK#setUserId(String)


##### ログインボーナスについて

1日1回ログインボーナスを獲得する処理を行う。 (毎日午前4時にリセット)  

###### 既に取得したかどうかはを確認する

+ KeyboardSDK#didGetLoginBonus()


##### 抽選ポイントについて

1時間毎に表示した広告の数をもとに抽選を行う。  

-----


### 22. 動画リワード

動画リワードを再生し、ポイントを獲得する。
*KeyboardSDK* でユーザーIDと動画リワードのメディアID・SDKキーを設定する必要がある。
メディアID・SDKキーは別途通知します。

#### 動画リワード初期化

動画リワードを再生する *Activity* の `onCreate(Bundle)` 、もしくは *Fragment* の `onActivityCreate(Bundle)` で初期化をする。

+ KeyboardSDK#setupMovieReward(Activity, MovieRewardListener)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | Activity | 動画リワードを再生するActivity |
| 2 | MovieRewardListener | 動画リワードのListener |

###### SampleCode

```
#!java
public MainActivity extends AppCompatActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
		keyboardSDK.setupMovieReward(this, new MovieRewardListener() {

			@Override
			public void onChangeCanShow(boolean canShow) {
				if (canShow) {
					// 動画リワードの準備が完了し、再生が可能となった
				}
				else {
					// 動画リワードの取得に失敗したか、再生できる動画リワードがなくなった
				}
			}
		});
	}

}
```

#### 動画リワード再生準備確認

動画リワードの再生準備が完了したか確認する。

+ KeyboardSDK#isReadyMovieReward()


#### 動画リワード再生

+ KeyboardSDK#showMovieReward(MovieRewardPlayCallback)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | MovieRewardPlayCallback | 動画リワード再生Callback |

###### SampleCode

```
#!java
public void showMovieReward() {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	keyboardSDK.showMovieReward(new MovieRewardPlayCallback() {

		@Override
		public void onFinishPlay() {
			// 動画リワード再生完了
		}

		@Override
		public void onCancel() {
			// 動画リワードの再生を途中でキャンセルした
		}

		@Override
		public void onFailure(MovieRewardPlayFailureReason reason) {
			// 動画リワード再生失敗
			switch (reason) {
				case NOT_INIT_SDK:
					// SDKの初期化が行われていない
					break;

				case NOT_LOGIN:
					// SDKにユーザーIDが設定されていない
					break;

				case EMPTY_ADS:
					// 再生可能な動画リワードがない
					break;

				case OTHER:
					// 通信エラー等の理由で再生できなかった
					break;
			}
		}
	});
}
```

#### 動画リワード達成ポイント取得

動画リワードの再生が完了した時にポイントを獲得する。

+ KeyboardSDK#getMovieRewardPoint(MovieRewardGetPointCallback)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | MovieRewardPlayCallback | 動画リワード達成ポイント獲得Callback |

###### SampleCode

```
#!java
public void getMovieRewardPoint() {
	KeyboardSDK keyboardSDK = KeyboardSDK.sharedInstance(this);
	keyboardSDK.getMovieRewardPoint(new MovieRewardGetPointCallback() {

		@Override
		public void onSuccess(int getPoint) {
			// ポイント獲得
		}

		@Override
		public void onLimitPlay() {
			// 動画リワードの再生回数の上限に達した
		}

		@Override
		public void onFailure() {
			// 通信エラー等で動画リワードの達成ポイントを取得できなかった
		}
	});
}
```

-----

### 23. キーボード上にView表示

キーボードの文字盤と背景の間にViewを表示する。
アニメーションやダイアログなどを表示する際に使用する。


#### View表示

+ KeyboardBaseService#setAnimationView(V view)

| 引数 | クラス | 内容 |
| :--- | :--- | :--- |
| 1 | V extends View | 表示するView |

#### View削除

+ KeyboardBaseService#finishAnimation()

※表示が完了した際に必ず呼ぶ

###### SampleCode

```
#!java
public void showAnimation() {
	AnimationView animationView = new AnimationView(this);
	setAnimationView(animationView);
	animationView.start(new AnimationCallback() {
		@Override
		public void onFinish() {
			finishAnimation();
		}
	});
}
```

-----
