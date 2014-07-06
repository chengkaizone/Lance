package org.lance.demo;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import org.lance.adapter.SingleAdapter;
import org.lance.main.R;

public class AnimationsDemo extends BaseActivity {
	private ListView list;
	private SingleAdapter sAdapter;
	private String[] srr = { "后退式进入","前进式退出","左退式进入","右进式退出","水波式进入",
			"右进左出","左进右出","心跳式效果",
			"淡入淡出", "放大淡出", "转动淡出", "左上角展开淡出", "压缩抛出淡出",
			"下往上推进入", "左右交错", "缩小淡出", "上下交错","",""};

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.animations_layout);
		list = (ListView) findViewById(R.id.anims_listview);
		init();
	}

	private void init() {
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < srr.length; i++) {
			data.add(srr[i]);
		}
		sAdapter = new SingleAdapter(this, R.layout.textview,
				R.id.animation_text, data);
		list.setAdapter(sAdapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {
				startActivity(new Intent(AnimationsDemo.this,
						CustomColumnDemo.class));
				switch (position) {
				case 0:// 后退式进入
					overridePendingTransition(R.anim.slide_in_from_right,
							R.anim.stack_push_out);
					break;
				case 1:// 前进式退出
					overridePendingTransition(R.anim.stack_pop_in,
							R.anim.slide_out_to_right);
					break;
				case 2://左退式进入
					overridePendingTransition(R.anim.anim_window_in,
							R.anim.anim_window_out);
					break;
				case 3://右进式退出
					overridePendingTransition(R.anim.anim_window_close_in,
							R.anim.anim_window_close_out);
					break;
				case 4:// 水波式进入
					overridePendingTransition(R.anim.lightbox_fade_in,
							R.anim.lightbox_fade_out);
					break;
				case 5:// 右进左出
					overridePendingTransition(R.anim.slide_in_from_right,
							R.anim.slide_out_to_left);
					break;
				case 6:// 左进右出
					overridePendingTransition(R.anim.slide_in_from_left,
							R.anim.slide_out_to_right);
					break;
				case 7:// 心跳式
					overridePendingTransition(R.anim.pulse_animation,
							R.anim.pulse_disappear);
					break;
				
				case 8:// 淡入淡出效果---第一个动画使用于新进来的界面
					overridePendingTransition(R.anim.anim_fade_in,
							R.anim.anim_fade_out);
					break;
				case 9:// 放大淡出效果
					overridePendingTransition(R.anim.anim_scale_alpha_in,
							R.anim.anim_alpha_out);
					break;
				case 10:// 转动淡出效果
					overridePendingTransition(
							R.anim.anim_scale_translate_rotate_in,
							R.anim.anim_alpha_out);
					break;
				case 11:// 左上角展开淡出
					overridePendingTransition(R.anim.anim_scale_in,
							R.anim.anim_alpha_out);
					break;
				case 12:// 压缩抛出淡出
					overridePendingTransition(R.anim.anim_hyperspace_in,
							R.anim.anim_hyperspace_out);
					break;
				case 13:// 下往上推出效果
					overridePendingTransition(R.anim.anim_push_up_in,
							R.anim.anim_push_up_out);
					break;
				case 14:// 左右交错效果
					overridePendingTransition(R.anim.anim_slide_left_in,
							R.anim.anim_slide_right_out);
					break;
				case 15:// 缩小右下角效果
					overridePendingTransition(R.anim.anim_zoom_in,
							R.anim.anim_zoom_out);
					break;
				case 16:// 上下交错效果
					overridePendingTransition(R.anim.anim_slide_up_in,
							R.anim.anim_slide_down_out);
					break;
				}
			}
		});
	}
}
