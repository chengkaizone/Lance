package org.lance.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.lance.main.R;

/**
 * @author chengkai
 */
public class WebPageDemo extends Activity implements OnClickListener {
	private TextView title;
	private WebView webView;
	private ProgressBar bar;
	private String info;
	private EditText input;
	private Button search;
	private String url_head = "http://";
	private InputMethodManager imManager;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.webview);
		Intent intent = getIntent();
		info = intent.getStringExtra("info");
		init();
	}

	private void init() {
		title = (TextView) findViewById(R.id.base_top_title);
		bar = (ProgressBar) findViewById(R.id.web_tab_bar);
		webView = (WebView) findViewById(R.id.webview);
		input = (EditText) findViewById(R.id.web_input);
		search = (Button) findViewById(R.id.web_search);
		imManager = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		search.setOnClickListener(this);
		title.setText("查看svg图片");
		initWeb();
	}

	private void initWeb() {
		// 请求获焦
		webView.requestFocus();
		// 设置滚动条风格
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		int screenDensity = getResources().getDisplayMetrics().densityDpi;
		// 屏幕分辨率
		WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM;
		switch (screenDensity) {
		case DisplayMetrics.DENSITY_LOW:
			zoomDensity = WebSettings.ZoomDensity.CLOSE;
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			zoomDensity = WebSettings.ZoomDensity.MEDIUM;
			break;
		case DisplayMetrics.DENSITY_HIGH:
			zoomDensity = WebSettings.ZoomDensity.FAR;
			break;
		}
		WebSettings set = webView.getSettings();
		// 根据屏幕分辨率设置默认的缩放方式
		set.setDefaultZoom(zoomDensity);
		// 设置javascript支持
		set.setJavaScriptEnabled(true);
		// 设置双击放大
		set.setUseWideViewPort(true); // 实现双击放大缩小
		set.setLoadWithOverviewMode(true);
		// 设置缩放支持
		set.setSupportZoom(true);
		// 设置显示缩放控制器
		set.setBuiltInZoomControls(true);
		// 设置缓存模式
		set.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		//
		//set.setAllowContentAccess(true);

		// 允许访问本地文件
		set.setAllowFileAccess(true);

		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				System.out.println(url);
				input.setText(url);
				webView.loadUrl(url);
				return super.shouldOverrideUrlLoading(view, url);
			}
		});
		webView.setWebChromeClient(new WebChromeClient() {
			// 显示加载进度
			public void onProgressChanged(WebView view, int newProgress) {
				bar.setVisibility(View.VISIBLE);
				bar.setProgress(newProgress);
				if (newProgress >= 100) {
					bar.setVisibility(View.GONE);
				}
				super.onProgressChanged(view, newProgress);
			}

			@Override
			public void onReceivedTitle(WebView view, String title) {
				getWindow().setTitle(title);
				super.onReceivedTitle(view, title);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webView.canGoBack()) {
				webView.goBack();
			} else {
				finish();
			}
		}
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		webView.loadUrl(info);
		// final String mimeType = "text/html";
		// final String encoding = "utf-8";
		// final String html =
		// "<p><img height=\"600px\" width=\"600px \"src=\"file:///android_asset/lock.svg\" /></p>";
		// webView.loadDataWithBaseURL("fake://not/needed", html, mimeType,
		// encoding, "");
	}

	@Override
	public void onClick(View v) {
		if (imManager.isActive()) {
			imManager.hideSoftInputFromWindow(input.getWindowToken(), 0);
		}
		if ("".equals(input.getText().toString().trim())) {
			Toast.makeText(WebPageDemo.this, "请输入网址", Toast.LENGTH_SHORT)
					.show();
		} else {
			webView.loadUrl(url_head + input.getText().toString().trim());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "百度主页");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		System.out.println("点击菜单");
		if (item.getItemId() == 0) {
			webView.loadUrl("http://www.baidu.com");
		}
		return super.onOptionsItemSelected(item);
	}

}
