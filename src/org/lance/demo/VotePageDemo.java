package org.lance.demo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import org.lance.main.R;
import org.lance.widget.HorizontalScroller;

/**
 * 投票页面--横向切屏技术
 * 
 * @author Administrator
 * 
 */
public class VotePageDemo extends BaseActivity {
	Button btn;
	private TextView text;
	HorizontalScroller scroller;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.vote_main);
		btn = (Button) findViewById(R.id.vote_btn);
		scroller = (HorizontalScroller) findViewById(R.id.vote_main_scroll);
		scroller.setAllowOutside(true);
		scroller.setOutsideRate(0.4f);
		text = (TextView) findViewById(R.id.vote_text);
		text.setText("sdfsd\ndsfs\nsfsdf\nwerwerq\nwe\ns"
				+ "dfsd\nsdfsd\nsdfsd");
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(VotePageDemo.this,
						VoteSubmitDemo.class));
			}
		});
	}
}
