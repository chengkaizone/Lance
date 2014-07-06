package org.lance.demo;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import barcode.lance.client.camera.CameraManager;
import barcode.lance.client.widget.ViewFinder;
import barcode.lance.client.widget.ViewFinder.ViewfinderResultPointCallback;
import barcode.lance.assist.BarcodeFormat;
import barcode.lance.assist.BinaryBitmap;
import barcode.lance.assist.DecodeHintType;
import barcode.lance.assist.MultiFormatReader;
import barcode.lance.assist.ReaderException;
import barcode.lance.assist.Result;
import barcode.lance.assist.ResultMetadataType;
import barcode.lance.assist.ResultPointCallback;
import barcode.lance.client.common.BeepManager;
import barcode.lance.client.common.DecodeFormatManager;
import barcode.lance.client.common.InactivityTimer;
import barcode.lance.client.common.Intents;
import barcode.lance.client.common.PlanarYUVLuminanceSource;
import barcode.lance.client.common.ResultPointHelper;
import barcode.lance.client.handler.BarcodePreferences;
import barcode.lance.client.handler.HandleMessage;
import barcode.lance.client.handler.ResultHandler;
import barcode.lance.client.handler.ResultHandlerFactory;
import barcode.lance.client.handler.supplement.SupplementalInfoRetriever;
import barcode.lance.common.HybridBinarizer;

import org.lance.main.R;

public class BarcodeDemo extends BaseActivity implements
		SurfaceHolder.Callback, OnClickListener {

	// opera浏览器包名
	private String operaPack = "com.opera.browser";
	private String dolpinPack = "com.dolphin.browser.cn";
	// 下载opera浏览器的链接
	private String operaUrl = "http://gdown.baidu.com/data/wisegame/"
			+ "a446100751dd9bfd/OperaMobile.apk";
	private String dolpinUrl = "http://gdown.baidu.com/data/wisegame/"
			+ "df1a1e1ecd151af0/haitunliulanqi.apk";
	private final long RESULT_DURATION = 1500L;
	private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;

	private static final String PRODUCT_SEARCH_URL_PREFIX = "http://www.google";
	private static final String PRODUCT_SEARCH_URL_SUFFIX = "/m/products/scan";
	private static final String ZXING_URL = "http://zxing.appspot.com/scan";
	private static final String RETURN_CODE_PLACEHOLDER = "{CODE}";
	private static final String RETURN_URL_PARAM = "ret";
	private String codeInfo;

	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private String sourceUrl;
	private String returnUrlTemplate;

	private ViewFinder viewFinder;
	private View resultView;
	private ImageView barcodeImage;
	private TextView formatText;
	private TextView typeText;
	private TextView timeText;
	private TextView metaText;
	private TextView contentsText;
	private TextView contentsSupplementText;
	private TextView hint;
	private Button btn;

	private DateFormat formatter;
	private Result lastResult;
	private boolean hasSurface;
	private BeepManager beepManager;
	private InactivityTimer inactivityTimer;

	private Source source;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private OtherHandler handler;
	private static final Set<ResultMetadataType> METADATA_TYPES;
	static {
		METADATA_TYPES = new HashSet<ResultMetadataType>(5);
		METADATA_TYPES.add(ResultMetadataType.ISSUE_NUMBER);
		METADATA_TYPES.add(ResultMetadataType.SUGGESTED_PRICE);
		METADATA_TYPES.add(ResultMetadataType.ERROR_CORRECTION_LEVEL);
		METADATA_TYPES.add(ResultMetadataType.POSSIBLE_COUNTRY);
	}

	private enum Source {
		NATIVE_APP_INTENT, PRODUCT_SEARCH_LINK, ZXING_LINK, NONE
	}

	public void onCreate(Bundle save) {
		super.onCreate(save);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.other_main);
		initView();
		CameraManager.init(getApplication());
		formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT);
		handler = null;
		lastResult = null;
		hasSurface = false;
		// historyManager = new HistoryManager(this);
		// historyManager.trimHistory();
		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this, R.raw.beep);

	}

	private void initView() {
		resultView = findViewById(R.id.result_view);
		viewFinder = (ViewFinder) findViewById(R.id.viewfinder_view);

		barcodeImage = (ImageView) findViewById(R.id.barcode_image_view);
		formatText = (TextView) findViewById(R.id.format_text_view);
		typeText = (TextView) findViewById(R.id.type_text_view);
		timeText = (TextView) findViewById(R.id.time_text_view);
		metaText = (TextView) findViewById(R.id.meta_text_view);
		contentsText = (TextView) findViewById(R.id.contents_text_view);
		contentsSupplementText = (TextView) findViewById(R.id.contents_supplement_text_view);
		hint = (TextView) findViewById(R.id.other_hint);
		btn = (Button) findViewById(R.id.other_button);
		btn.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		resetStatusView();

		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		Intent intent = getIntent();
		String action = intent == null ? null : intent.getAction();
		String dataString = intent == null ? null : intent.getDataString();
		if (intent != null && action != null) {
			if (action.equals(Intents.Scan.ACTION)) {
				System.out.println("action--->1");
				source = Source.NATIVE_APP_INTENT;
				decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);
			} else if (dataString != null
					&& dataString.contains(PRODUCT_SEARCH_URL_PREFIX)
					&& dataString.contains(PRODUCT_SEARCH_URL_SUFFIX)) {
				System.out.println("action--->2");
				source = Source.PRODUCT_SEARCH_LINK;
				sourceUrl = dataString;
				decodeFormats = DecodeFormatManager.PRODUCT_FORMATS;
			} else if (dataString != null && dataString.startsWith(ZXING_URL)) {
				System.out.println("action--->3");
				source = Source.ZXING_LINK;
				sourceUrl = dataString;
				Uri inputUri = Uri.parse(sourceUrl);
				returnUrlTemplate = inputUri
						.getQueryParameter(RETURN_URL_PARAM);
				decodeFormats = DecodeFormatManager
						.parseDecodeFormats(inputUri);
			} else {
				System.out.println("action--->4");
				source = Source.NONE;
				decodeFormats = null;
			}
			characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);
		} else {
			source = Source.NONE;
			decodeFormats = null;
			characterSet = null;
		}
		beepManager.updatePrefs();
		inactivityTimer.onResume();
	}

	public void drawViewFinder() {
		viewFinder.drawViewFinder();
	}

	public Handler getHandler() {
		return handler;
	}

	public ViewFinder getViewFinder() {
		return viewFinder;
	}

	/**
	 * 处理解码结果
	 * 
	 * @param rawResult
	 * @param barcode
	 */
	public void handleDecode(Result rawResult, Bitmap barcode) {
		inactivityTimer.onActivity();
		lastResult = rawResult;
		if (barcode == null) {
			handleDecodeInternally(rawResult, null);
		} else {
			beepManager.playBeepSoundAndVibrate();
			ResultPointHelper.drawResultPoints(barcode, rawResult);
			handleDecodeExternally(rawResult, barcode);
			switch (source) {
			case NATIVE_APP_INTENT:
			case PRODUCT_SEARCH_LINK:
				handleDecodeExternally(rawResult, barcode);
				break;
			case ZXING_LINK:
				if (returnUrlTemplate == null) {
					handleDecodeInternally(rawResult, barcode);
				} else {
					handleDecodeExternally(rawResult, barcode);
				}
				break;
			case NONE:
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(this);
				if (prefs.getBoolean(BarcodePreferences.BULK_MODE, false)) {
					Toast.makeText(this, "批量扫描：找到条码，已保存", Toast.LENGTH_SHORT)
							.show();
					if (handler != null) {
						handler.sendEmptyMessageDelayed(
								HandleMessage.RESTART_PREVIEW,
								BULK_MODE_SCAN_DELAY_MS);
					}
					resetStatusView();
				} else {
					handleDecodeInternally(rawResult, barcode);
				}
				break;
			}
		}
	}

	// Put up our own UI for how to handle the decoded contents.
	private void handleDecodeInternally(Result rawResult, Bitmap barcode) {
		// 隐藏扫描取景框
		viewFinder.setVisibility(View.GONE);
		// 设置结果视图可见
		resultView.setVisibility(View.VISIBLE);
		hint.setVisibility(View.GONE);
		btn.setVisibility(View.VISIBLE);
		if (barcode == null) {
			barcodeImage.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.ic_launcher));
		} else {
			barcodeImage.setImageBitmap(barcode);
		}
		System.out
				.println("date--->" + rawResult.getBarcodeFormat().toString());
		// 设置日期
		formatText.setText(rawResult.getBarcodeFormat().toString());
		// 根据结果返回相应的结果处理器
		ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(
				this, rawResult);
		System.out.println("type--->" + resultHandler.getType().toString());
		// 设置结果类型
		typeText.setText(resultHandler.getType().toString());
		System.out.println("time--->"
				+ formatter.format(new Date(rawResult.getTimestamp())));
		// 设置扫描时间
		timeText.setText(formatter.format(new Date(rawResult.getTimestamp())));

		metaText.setVisibility(View.GONE);
		// metaTextViewLabel.setVisibility(View.GONE);

		Map<ResultMetadataType, Object> metadata = (Map<ResultMetadataType, Object>) rawResult
				.getResultMetadata();
		if (metadata != null) {
			StringBuilder metadataText = new StringBuilder(20);
			for (Map.Entry<ResultMetadataType, Object> entry : metadata
					.entrySet()) {
				if (METADATA_TYPES.contains(entry.getKey())) {
					metadataText.append(entry.getValue()).append('\n');
				}
			}
			System.out.println("matadata--->" + metadataText.toString());
			if (metadataText.length() > 0) {
				metadataText.setLength(metadataText.length() - 1);
				metaText.setText(metadataText);
				metaText.setVisibility(View.VISIBLE);
				// metaTextViewLabel.setVisibility(View.VISIBLE);
			}
		}

		CharSequence displayContents = resultHandler.getDisplayContents();
		codeInfo = displayContents.toString();
		// 得到扫描的内容
		System.out.println("disContent--->" + displayContents);
		contentsText.setText(displayContents);
		// Crudely scale betweeen 22 and 32 -- bigger font for shorter text
		int scaledSize = Math.max(22, 32 - displayContents.length() / 4);
		contentsText.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);

		contentsSupplementText.setText("");
		contentsSupplementText.setOnClickListener(null);
		if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
				BarcodePreferences.SUPPLEMENTAL, true)) {
			SupplementalInfoRetriever.maybeInvokeRetrieval(
					contentsSupplementText, resultHandler.getResult(), handler,
					this);
		}
	}

	/**
	 * 处理外在的解码
	 * 
	 * @param rawResult
	 * @param barcode
	 */
	private void handleDecodeExternally(Result rawResult, Bitmap barcode) {
		viewFinder.drawResultBitmap(barcode);
		ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(
				this, rawResult);
		if (source == Source.NATIVE_APP_INTENT) {
			Intent intent = new Intent(getIntent().getAction());
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			intent.putExtra(Intents.Scan.RESULT, rawResult.toString());
			intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult
					.getBarcodeFormat().toString());
			byte[] rawBytes = rawResult.getRawBytes();
			if (rawBytes != null && rawBytes.length > 0) {
				intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes);
			}
			Message message = Message.obtain(handler,
					HandleMessage.RETURN_SCAN_RESULT);
			message.obj = intent;
			handler.sendMessageDelayed(message, RESULT_DURATION);
		} else if (source == Source.PRODUCT_SEARCH_LINK) {
			Message message = Message.obtain(handler,
					HandleMessage.LAUNCH_PRODUCT_QUERY);
			int end = sourceUrl.lastIndexOf("/scan");
			message.obj = sourceUrl.substring(0, end) + "?q="
					+ resultHandler.getDisplayContents().toString()
					+ "&source=zxing";
			handler.sendMessageDelayed(message, RESULT_DURATION);
		} else if (source == Source.ZXING_LINK) {
			Message message = Message.obtain(handler,
					HandleMessage.LAUNCH_PRODUCT_QUERY);
			message.obj = returnUrlTemplate.replace(RETURN_CODE_PLACEHOLDER,
					resultHandler.getDisplayContents().toString());
			handler.sendMessageDelayed(message, RESULT_DURATION);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		CameraManager.get().closeDriver();
	}

	/**
	 * 重写返回键
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (source == Source.NATIVE_APP_INTENT) {
				setResult(RESULT_CANCELED);
				finish();
				return true;
			} else if ((source == Source.NONE || source == Source.ZXING_LINK)
					&& lastResult != null) {
				resetStatusView();
				if (handler != null) {
					handler.sendEmptyMessage(HandleMessage.RESTART_PREVIEW);
				}
				return true;
			}
		} else if (keyCode == KeyEvent.KEYCODE_FOCUS
				|| keyCode == KeyEvent.KEYCODE_CAMERA) {
			// Handle these events so they don't launch the Camera app
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 初始化相机设备
	 * 
	 * @param surfaceHolder
	 */
	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			// displayFrameworkBugMessageAndExit();
			return;
		} catch (RuntimeException e) {
			// displayFrameworkBugMessageAndExit();
			return;
		}
		if (handler == null) {
			handler = new OtherHandler(this, decodeFormats, characterSet);
		}
	}

	private void resetStatusView() {
		resultView.setVisibility(View.GONE);
		viewFinder.setVisibility(View.VISIBLE);
		btn.setVisibility(View.GONE);
		hint.setVisibility(View.VISIBLE);
		lastResult = null;
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	private class OtherHandler extends Handler {
		private final BarcodeDemo activity;
		private final DecodeThread decodeThread;
		private final int PREVIEW = 0x101;
		private final int SUCCESS = 0x102;
		private final int DONE = 0x103;

		private int state = SUCCESS;

		OtherHandler(BarcodeDemo activity, Vector<BarcodeFormat> decodeFormats,
				String characterSet) {
			this.activity = activity;
			decodeThread = new DecodeThread(activity, decodeFormats,
					characterSet, new ViewfinderResultPointCallback(
							activity.getViewFinder()));
			decodeThread.start();
			state = SUCCESS;
			// Start ourselves capturing previews and decoding.
			CameraManager.get().startPreview();
			restartPreviewAndDecode();
		}

		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case HandleMessage.AUTO_FOCUS:
				// 如果失败将请求再次获焦
				if (state == PREVIEW) {
					CameraManager.get().requestAutoFocus(this,
							HandleMessage.AUTO_FOCUS);
				}
				break;
			case HandleMessage.RESTART_PREVIEW:
				// 请求重新启动解码
				restartPreviewAndDecode();
				break;
			// 由DecodeHandler发送消息过来
			case HandleMessage.DECODE_SUCCESSED:
				// 解码成功
				state = SUCCESS;
				Bundle bundle = message.getData();
				// 取出解码得到的位图对象---二维码图片
				Bitmap barcode = bundle == null ? null : (Bitmap) bundle
						.getParcelable(DecodeThread.BARCODE_BITMAP);
				// 处理解码
				activity.handleDecode((Result) message.obj, barcode);
				break;
			case HandleMessage.DECODE_FAILED:
				// We're decoding as fast as possible, so when one decode fails,
				// start another.解码失败再次请求预览状态改为预览状态
				state = PREVIEW;
				// 请求预览---最终还是交给解码处理器
				CameraManager.get().requestPreviewFrame(
						decodeThread.getHandler(), HandleMessage.DECODE);
				break;

			case HandleMessage.RETURN_SCAN_RESULT:
				// 浏览结果
				activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
				activity.finish();
				break;
			case HandleMessage.LAUNCH_PRODUCT_QUERY:
				// 启动产品查询
				String url = (String) message.obj;
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				activity.startActivity(intent);
				break;
			}
		}

		/**
		 * 退出时先停止camera
		 */
		public void quitSynchronously() {
			state = DONE;
			CameraManager.get().stopPreview();
			Message quit = Message.obtain(decodeThread.getHandler(),
					HandleMessage.QUIT);
			quit.sendToTarget();
			try {
				decodeThread.join();
			} catch (InterruptedException e) {
				// continue
			}
			// Be absolutely sure we don't send any queued up messages
			removeMessages(HandleMessage.DECODE_SUCCESSED);
			removeMessages(HandleMessage.DECODE_FAILED);
		}

		private void restartPreviewAndDecode() {
			System.out.println("restartPreview---->");
			if (state == SUCCESS) {
				state = PREVIEW;
				CameraManager.get().requestPreviewFrame(
						decodeThread.getHandler(), HandleMessage.DECODE);
				CameraManager.get().requestAutoFocus(this,
						HandleMessage.AUTO_FOCUS);
				activity.drawViewFinder();
			}
		}
	}

	public final class DecodeThread extends Thread {

		public static final String BARCODE_BITMAP = "barcode_bitmap";

		private final BarcodeDemo activity;
		private final Hashtable<DecodeHintType, Object> hints;
		private Handler handler;
		private final CountDownLatch handlerInitLatch;

		public DecodeThread(BarcodeDemo activity,
				Vector<BarcodeFormat> decodeFormats, String characterSet,
				ResultPointCallback resultPointCallback) {

			this.activity = activity;
			handlerInitLatch = new CountDownLatch(1);

			hints = new Hashtable<DecodeHintType, Object>(3);

			// The prefs can't change while the thread is running, so pick them
			// up once here.
			if (decodeFormats == null || decodeFormats.isEmpty()) {
				SharedPreferences prefs = PreferenceManager
						.getDefaultSharedPreferences(activity);
				decodeFormats = new Vector<BarcodeFormat>();
				if (prefs.getBoolean(BarcodePreferences.DECODE_1D, true)) {
					decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
				}
				if (prefs.getBoolean(BarcodePreferences.DECODE_QR, true)) {
					decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
				}
				if (prefs.getBoolean(BarcodePreferences.DECODE_DATA_MATRIX,
						true)) {
					decodeFormats
							.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
				}
			}
			hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

			if (characterSet != null) {
				hints.put(DecodeHintType.CHARACTER_SET, characterSet);
			}

			hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
					resultPointCallback);
		}

		public Handler getHandler() {
			try {
				// 会导致当前线程中断；闭锁（门闩）降为0
				handlerInitLatch.await();
			} catch (InterruptedException ie) {
				// continue?
			}
			return handler;
		}

		@Override
		public void run() {
			// 大多数集成有消息循环是通过处理程序类
			Looper.prepare();
			// 启动线程时创建解码处理器
			handler = new DecodeHandler(activity, hints);
			// 因计数的门闩,如果计数为0释放所有等待的线程
			handlerInitLatch.countDown();
			// 运行消息队列；开始循环处理
			Looper.loop();
		}

	}

	public final class DecodeHandler extends Handler {
		private final BarcodeDemo activity;
		private final MultiFormatReader multiFormatReader;

		public DecodeHandler(BarcodeDemo activity,
				Hashtable<DecodeHintType, Object> hints) {
			multiFormatReader = new MultiFormatReader();
			multiFormatReader.setHints(hints);
			this.activity = activity;
		}

		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case HandleMessage.DECODE:
				// 处理解码请求
				// Log.d(TAG, "Got decode message");
				decode((byte[]) message.obj, message.arg1, message.arg2);
				break;
			case HandleMessage.QUIT:
				// 退出请求
				Looper.myLooper().quit();
				break;
			}
		}

		/**
		 * 处理消息--解码
		 * 
		 * @param data
		 * @param width
		 * @param height
		 */
		private void decode(byte[] data, int width, int height) {
			long start = System.currentTimeMillis();
			Result rawResult = null;
			PlanarYUVLuminanceSource source = CameraManager.get()
					.buildLuminanceSource(data, width, height);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			try {
				rawResult = multiFormatReader.decodeWithState(bitmap);
			} catch (ReaderException re) {
				// continue
			} finally {
				multiFormatReader.reset();
			}
			// 解码成功
			if (rawResult != null) {
				long end = System.currentTimeMillis();
				System.out.println("耗时：" + (end - start) + "毫秒");
				Message message = Message.obtain(activity.getHandler(),
						HandleMessage.DECODE_SUCCESSED, rawResult);
				Bundle bundle = new Bundle();
				// 将位图对象放入消息中
				bundle.putParcelable(DecodeThread.BARCODE_BITMAP,
						source.renderCroppedGreyscaleBitmap());
				message.setData(bundle);
				// 发送到目标处理器CodeActivityHandler中
				message.sendToTarget();
			} else {
				// 解码失败
				Message message = Message.obtain(activity.getHandler(),
						HandleMessage.DECODE_FAILED);
				message.sendToTarget();
			}
		}

	}

	@Override
	public void onClick(View v) {
		test();
	}

	private void test() {
		String ver = android.os.Build.VERSION.SDK;
		int version = Integer.parseInt(ver);
		if (version <= 10) {
			try {
				PackageManager pManager = getPackageManager();
				pManager.getPackageInfo(operaPack, 0);
				openUrl(codeInfo);
			} catch (NameNotFoundException e) {
				new AlertDialog.Builder(BarcodeDemo.this)
						.setTitle("温馨提示!")
						.setMessage("抱歉，没有找到opera浏览器，是否下载？")
						.setPositiveButton("确认",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										Uri uri = Uri.parse(operaUrl);
										Intent intent = new Intent(
												Intent.ACTION_VIEW, uri);
										startActivity(intent);
									}
								}).setNegativeButton("取消", null).show();
			}
		} else {
			openUrl(codeInfo);
		}
	}

	// 打开链接
	private void openUrl(String url) {
		Intent intent = new Intent();
		//intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		String ver = android.os.Build.VERSION.SDK;
		int version = Integer.parseInt(ver);
		System.out.println("version===>" + version);
		if (version <= 10) {
			System.out.println("系统版本：" + version + "不支持svg");
			// 打开浏览器解析svg图片
			ComponentName componentName = new ComponentName(operaPack,
					"com.opera.Opera");
			// 设置intent的Action属性
			intent.setComponent(componentName);
			// 设置intent的data属性。
			intent.setData(Uri.parse(codeInfo));
		} else {
			System.out.println("系统版本：" + version + "支持svg");
			intent.setClass(this, WebPageDemo.class);
			intent.putExtra("info", codeInfo);
		}
		// 跳转
		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "打开链接");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		codeInfo = "http://ariya.github.com/svg/tiger.svg";
		if (item.getItemId() == 0) {
			test();
		}
		return super.onOptionsItemSelected(item);
	}
}
