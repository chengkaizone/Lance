package org.lance.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lance.adapters.CommentDetailAdapter;
import org.lance.anim.Rotate3DAnimation;
import org.lance.anim.Rotate3DAnimation.Model;
import org.lance.anim.Rotate3DAnimation.OnSwapListener;
import org.lance.entity.CommentResult;
import org.lance.main.R;
import org.lance.util.HtmlService;

import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

/**
 * 3D旋转实现
 * 
 * @author Administrator
 * 
 */
public class D3AnimationDemo extends BaseActivity implements OnSwapListener{
	private static final int SOURCE = 0;// 显示原文的视图
	private static final int BACK = 1;// 显示跟帖的视图
	// 显示新闻
	String newsHtml = "file:///android_asset/shownews.html";
	// 不分开
	String blankHtml = "file:///android_asset/blank.html";
	private int page = 0;
	private FrameLayout container, root1, root2;
	private WebView web1, web2;
	private ListView list;
	private Button switcher1, switcher2;
	private int[] location = new int[2];
	private InputMethodManager inputMethod;
	private ClipboardManager clip;
	private LayoutInflater inflater;
	private View toolbar;
	private PopupWindow hint;
	private int itemLoc = 0;
	private ImageButton imgbtn;
	private EditText input;
	private LinearLayout inputlay;
	private Button replybtn;
	private LinearLayout copy, replyroot;
	private boolean isShowInput = false;
	private List<CommentResult> data = new ArrayList<CommentResult>();

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.new_comment);
		inputMethod = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		clip = (ClipboardManager) this
				.getSystemService(Context.CLIPBOARD_SERVICE);
		inflater = LayoutInflater.from(this);
		toolbar = inflater.inflate(R.layout.mycomment_toolbar, null);
		hint = new PopupWindow(toolbar, -2, -2);
		hint.setOutsideTouchable(true);// 设置未触摸也销毁自己
		hint.setOnDismissListener(new OnDismissListener() {
			public void onDismiss() {
				// 当窗口被销毁时回调此方法
				// System.out.println("窗口被销毁");
			}
		});
		init();
	}

	OnClickListener inputListener = new OnClickListener() {
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.reply_img_button:
				inputlay.setVisibility(View.VISIBLE);
				inputMethod.showSoftInput(input, 0);
				isShowInput = true;
				break;
			case R.id.toolbar_copy_text:
				clip.setText("需要剪贴的内容---甘成凯");
				hint.dismiss();
				Toast.makeText(D3AnimationDemo.this, "已复制到剪贴板",
						Toast.LENGTH_SHORT).show();
				break;
			case R.id.reply_button:
				inputlay.setVisibility(View.GONE);
				break;
			}
		}
	};

	private void init() {
		Random random = new Random();
		View view = inflater.inflate(R.layout.mycomment_toolbar, null);
		replyroot = (LinearLayout) findViewById(R.id.reply_layout_frame);
		copy = (LinearLayout) view.findViewById(R.id.toolbar_copy_text);
		container = (FrameLayout) findViewById(R.id.new_comment_container);
		root1 = (FrameLayout) findViewById(R.id.new_comment_root1);
		root2 = (FrameLayout) findViewById(R.id.new_comment_root2);
		web1 = (WebView) findViewById(R.id.new_comment_web1);
		web2 = (WebView) findViewById(R.id.new_comment_web2);
		list = (ListView) findViewById(R.id.new_comment_listview);
		switcher1 = (Button) findViewById(R.id.new_comment_btn1);
		switcher2 = (Button) findViewById(R.id.new_comment_btn2);
		imgbtn = (ImageButton) findViewById(R.id.reply_img_button);
		input = (EditText) findViewById(R.id.reply_edittext);
		inputlay = (LinearLayout) findViewById(R.id.reply_edittext_layout);
		replybtn = (Button) findViewById(R.id.reply_button);
		replybtn.setOnClickListener(inputListener);
		copy.setOnClickListener(inputListener);
		imgbtn.setOnClickListener(inputListener);
		switcher1.setOnClickListener(switcherListener);
		switcher2.setOnClickListener(switcherListener);
		for (int i = 1; i <= 15; i++) {
			CommentResult result = new CommentResult();
			result.setContent("神鼎飞丹砂会计法昆仑山顶！" + i);
			result.setCount(random.nextInt(1000));
			result.setTime("13小时前！" + i);
			result.setUser("张三！" + i);
			data.add(result);
		}
		CommentDetailAdapter adapter = new CommentDetailAdapter(this, data);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获取当前点击的item在窗口的位置赋值到指定数组
				// 提示窗口需要显示一次之后才会计算它的宽高
				view.getLocationInWindow(location);
				int tmpHeight = location[1] - 50;// 在他的上方50像素的地方显示
				int tmpWidth = toolbar.getWidth();
				int i = Gravity.TOP | Gravity.RIGHT;
				if (hint.isShowing()) {
					if (position == itemLoc) {
						hint.dismiss();
					} else {
						hint.dismiss();
						hint.showAtLocation(view, i, 0, tmpHeight);
						itemLoc = position;
					}
				} else {
					hint.showAtLocation(view, i, 0, tmpHeight);
					itemLoc = position;
				}
			}
		});
		String head="世界里程最长的城际高速";
		String content = "	2月1日上午8时，铁路上海虹桥站至南京站G5000次、南京站至上海虹桥站G5001次列车同时相向"
				+ "发车，这标志着目前我国乃至世界上标准最高、里程最长、运营速度最快的沪宁城际高速铁路正式投入运营。中共中央政治局委"
				+ "员、上海市委书记俞正声，江苏省委书记梁保华，铁道部部长刘志军出席了在铁路上海虹桥站的通车典礼仪式。沪宁城际高速铁"
				+ "路运营里程301公里，列车最高时速350公里，从铁路南京站至上海站或上海虹桥站，最快仅需73分钟。全线铺设无砟轨"
				+ "道，采用世界上最先进的国产“和谐号”高速动车组和牵引供电、列车控制、行车指挥等系统";
		String encoding="utf-8";
		// 设置滚动条不可见
		web1.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		//如果要显示图片可以写webView.loadDataWithBaseURL(baseUrl, string, "text/html", "utf-8", null);
		web1.getSettings().setDefaultTextEncodingName(encoding);
		String html = HtmlService.getHtmlOverString(head,
				"2012-02-01", content, true,encoding);
		//web1.loadData(html, "text/html", "utf-8");官方bug
		web1.loadDataWithBaseURL(null, html, "text/html", encoding, null);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (page == BACK) {
				inputMethod.hideSoftInputFromInputMethod(
						input.getWindowToken(), 0);
				container.startAnimation(Rotate3DAnimation.getPreAnim(container,
						Model.ROTATE_Y,D3AnimationDemo.this));
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	OnClickListener switcherListener = new OnClickListener() {
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.new_comment_btn1:
				if (inputlay.getVisibility() == View.VISIBLE) {
					inputMethod.hideSoftInputFromWindow(input.getWindowToken(),
							0);
					inputlay.setVisibility(View.GONE);
				}
				container.startAnimation(Rotate3DAnimation
						.getDefaultPreAnim(container,Model.ROTATE_Y,D3AnimationDemo.this));

				break;
			case R.id.new_comment_btn2:
				if (inputlay.getVisibility() == View.VISIBLE) {
					inputMethod.hideSoftInputFromWindow(input.getWindowToken(),
							0);
					inputlay.setVisibility(View.GONE);
				}
				if (hint.isShowing()) {
					hint.dismiss();
				}
				container.startAnimation(Rotate3DAnimation.getPreAnim(container,
						Model.ROTATE_Y,D3AnimationDemo.this));

				break;
			}
		}
	};

	// 重写分派事件
	public boolean dispatchKeyEvent(KeyEvent event) {

		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onSwap(boolean flag) {
		if(flag){
			// 显示原文按钮
			switcher2.setVisibility(View.GONE);
			switcher1.setVisibility(View.VISIBLE);
			// 显示web页面
			root2.setVisibility(View.GONE);
			root1.setVisibility(View.VISIBLE);
			// 去掉底部控件
			replyroot.setVisibility(View.GONE);
		}else{
			// 显示跟帖页面
			root2.setVisibility(View.VISIBLE);
			root1.setVisibility(View.GONE);
			// 显示跟帖按钮
			switcher2.setVisibility(View.VISIBLE);
			switcher1.setVisibility(View.GONE);
			// 显示底部控件
			replyroot.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onSwapFinished(boolean isNext) {
		
	}

}
