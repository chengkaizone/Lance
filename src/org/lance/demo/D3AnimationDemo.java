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
 * 3D��תʵ��
 * 
 * @author Administrator
 * 
 */
public class D3AnimationDemo extends BaseActivity implements OnSwapListener{
	private static final int SOURCE = 0;// ��ʾԭ�ĵ���ͼ
	private static final int BACK = 1;// ��ʾ��������ͼ
	// ��ʾ����
	String newsHtml = "file:///android_asset/shownews.html";
	// ���ֿ�
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
		hint.setOutsideTouchable(true);// ����δ����Ҳ�����Լ�
		hint.setOnDismissListener(new OnDismissListener() {
			public void onDismiss() {
				// �����ڱ�����ʱ�ص��˷���
				// System.out.println("���ڱ�����");
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
				clip.setText("��Ҫ����������---�ʳɿ�");
				hint.dismiss();
				Toast.makeText(D3AnimationDemo.this, "�Ѹ��Ƶ�������",
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
			result.setContent("�񶦷ɵ�ɰ��Ʒ�����ɽ����" + i);
			result.setCount(random.nextInt(1000));
			result.setTime("13Сʱǰ��" + i);
			result.setUser("������" + i);
			data.add(result);
		}
		CommentDetailAdapter adapter = new CommentDetailAdapter(this, data);
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// ��ȡ��ǰ�����item�ڴ��ڵ�λ�ø�ֵ��ָ������
				// ��ʾ������Ҫ��ʾһ��֮��Ż�������Ŀ��
				view.getLocationInWindow(location);
				int tmpHeight = location[1] - 50;// �������Ϸ�50���صĵط���ʾ
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
		String head="���������ĳǼʸ���";
		String content = "	2��1������8ʱ����·�Ϻ�����վ���Ͼ�վG5000�Ρ��Ͼ�վ���Ϻ�����վG5001���г�ͬʱ����"
				+ "���������־��Ŀǰ�ҹ����������ϱ�׼��ߡ���������Ӫ�ٶ����Ļ����Ǽʸ�����·��ʽͶ����Ӫ���й��������ξ�ί"
				+ "Ա���Ϻ���ί���������������ʡί�����������������������־����ϯ������·�Ϻ�����վ��ͨ��������ʽ�������Ǽʸ�����"
				+ "·��Ӫ���301����г����ʱ��350�������·�Ͼ�վ���Ϻ�վ���Ϻ�����վ��������73���ӡ�ȫ���������Ĺ�"
				+ "�����������������Ƚ��Ĺ�������г�š����ٶ������ǣ�����硢�г����ơ��г�ָ�ӵ�ϵͳ";
		String encoding="utf-8";
		// ���ù��������ɼ�
		web1.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		//���Ҫ��ʾͼƬ����дwebView.loadDataWithBaseURL(baseUrl, string, "text/html", "utf-8", null);
		web1.getSettings().setDefaultTextEncodingName(encoding);
		String html = HtmlService.getHtmlOverString(head,
				"2012-02-01", content, true,encoding);
		//web1.loadData(html, "text/html", "utf-8");�ٷ�bug
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

	// ��д�����¼�
	public boolean dispatchKeyEvent(KeyEvent event) {

		return super.dispatchKeyEvent(event);
	}

	@Override
	public void onSwap(boolean flag) {
		if(flag){
			// ��ʾԭ�İ�ť
			switcher2.setVisibility(View.GONE);
			switcher1.setVisibility(View.VISIBLE);
			// ��ʾwebҳ��
			root2.setVisibility(View.GONE);
			root1.setVisibility(View.VISIBLE);
			// ȥ���ײ��ؼ�
			replyroot.setVisibility(View.GONE);
		}else{
			// ��ʾ����ҳ��
			root2.setVisibility(View.VISIBLE);
			root1.setVisibility(View.GONE);
			// ��ʾ������ť
			switcher2.setVisibility(View.VISIBLE);
			switcher1.setVisibility(View.GONE);
			// ��ʾ�ײ��ؼ�
			replyroot.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onSwapFinished(boolean isNext) {
		
	}

}
