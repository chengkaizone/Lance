package org.lance.demo;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;

import org.lance.main.R;
import org.lance.util.MoveBg;
import org.lance.widget.ToastImageView;

/**
 * 主页面--控制选项卡
 * 
 * @author Administrator
 * 
 */
public class TabDemo extends TabActivity {
	TabHost tabHost;
	TabHost.TabSpec tabSpec;
	RadioGroup radioGroup;
	RelativeLayout bottom_layout;
	ImageView img;
	int startLeft;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabs_main);
        
        stub = (ViewStub) findViewById(R.id.tab_host_stub);
        bottom_layout = (RelativeLayout) findViewById(R.id.layout_bottom);
        
        tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("news").setIndicator("News").setContent(new Intent(this, TabNewsActivity.class)));
        tabHost.addTab(tabHost.newTabSpec("topic").setIndicator("Topic").setContent(new Intent(this, PullRefreshDemo.class)));
        tabHost.addTab(tabHost.newTabSpec("picture").setIndicator("Picture").setContent(new Intent(this, SelectCityDemo.class)));
        tabHost.addTab(tabHost.newTabSpec("follow").setIndicator("Follow").setContent(new Intent(this, D3AnimationDemo.class)));
        tabHost.addTab(tabHost.newTabSpec("vote").setIndicator("Vote").setContent(new Intent(this, VotePageDemo.class)));
        
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOnCheckedChangeListener(checkedChangeListener);
        
        img = new ImageView(this);
        img.setImageResource(R.drawable.tab_front_bg);
        bottom_layout.addView(img);
    }
    
	private OnCheckedChangeListener checkedChangeListener = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.radio_news:
				tabHost.setCurrentTabByTag("news");
//				moveFrontBg(img, startLeft, 0, 0, 0);
				MoveBg.moveFrontBg(img, startLeft, 0, 0, 0);
				startLeft = 0;
				break;
			case R.id.radio_topic:
				tabHost.setCurrentTabByTag("topic");
				MoveBg.moveFrontBg(img, startLeft, img.getWidth(), 0, 0);
				startLeft = img.getWidth();
				break;
			case R.id.radio_pic:
				tabHost.setCurrentTabByTag("picture");
				MoveBg.moveFrontBg(img, startLeft, img.getWidth() * 2, 0, 0);
				startLeft = img.getWidth() * 2;
				break;
			case R.id.radio_follow:
				tabHost.setCurrentTabByTag("follow");
				MoveBg.moveFrontBg(img, startLeft, img.getWidth() * 3, 0, 0);
				startLeft = img.getWidth() * 3;
				break;
			case R.id.radio_vote:
				tabHost.setCurrentTabByTag("vote");
				MoveBg.moveFrontBg(img, startLeft, img.getWidth() * 4, 0, 0);
				startLeft = img.getWidth() * 4;
				break;

			default:
				break;
			}
		}
	};
	private ViewStub stub;
	private View guide;
	
	@Override
	public void onResume() {
		super.onResume();
		SharedPreferences share = this.getSharedPreferences("bools",
				Context.MODE_PRIVATE);
		boolean b = share.getBoolean("isFirst", false);
		if (!b) {
			guide = stub.inflate();
			guide.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					v.setVisibility(View.GONE);
				}
			});
			SharedPreferences.Editor editor = share.edit();
			editor.putBoolean("isFirst", true);
			editor.commit();// 提交数据--将数据写入应用程序中
		}
	}

}
