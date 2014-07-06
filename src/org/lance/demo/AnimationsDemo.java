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
	private String[] srr = { "����ʽ����","ǰ��ʽ�˳�","����ʽ����","�ҽ�ʽ�˳�","ˮ��ʽ����",
			"�ҽ����","����ҳ�","����ʽЧ��",
			"���뵭��", "�Ŵ󵭳�", "ת������", "���Ͻ�չ������", "ѹ���׳�����",
			"�������ƽ���", "���ҽ���", "��С����", "���½���","",""};

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
				case 0:// ����ʽ����
					overridePendingTransition(R.anim.slide_in_from_right,
							R.anim.stack_push_out);
					break;
				case 1:// ǰ��ʽ�˳�
					overridePendingTransition(R.anim.stack_pop_in,
							R.anim.slide_out_to_right);
					break;
				case 2://����ʽ����
					overridePendingTransition(R.anim.anim_window_in,
							R.anim.anim_window_out);
					break;
				case 3://�ҽ�ʽ�˳�
					overridePendingTransition(R.anim.anim_window_close_in,
							R.anim.anim_window_close_out);
					break;
				case 4:// ˮ��ʽ����
					overridePendingTransition(R.anim.lightbox_fade_in,
							R.anim.lightbox_fade_out);
					break;
				case 5:// �ҽ����
					overridePendingTransition(R.anim.slide_in_from_right,
							R.anim.slide_out_to_left);
					break;
				case 6:// ����ҳ�
					overridePendingTransition(R.anim.slide_in_from_left,
							R.anim.slide_out_to_right);
					break;
				case 7:// ����ʽ
					overridePendingTransition(R.anim.pulse_animation,
							R.anim.pulse_disappear);
					break;
				
				case 8:// ���뵭��Ч��---��һ������ʹ�����½����Ľ���
					overridePendingTransition(R.anim.anim_fade_in,
							R.anim.anim_fade_out);
					break;
				case 9:// �Ŵ󵭳�Ч��
					overridePendingTransition(R.anim.anim_scale_alpha_in,
							R.anim.anim_alpha_out);
					break;
				case 10:// ת������Ч��
					overridePendingTransition(
							R.anim.anim_scale_translate_rotate_in,
							R.anim.anim_alpha_out);
					break;
				case 11:// ���Ͻ�չ������
					overridePendingTransition(R.anim.anim_scale_in,
							R.anim.anim_alpha_out);
					break;
				case 12:// ѹ���׳�����
					overridePendingTransition(R.anim.anim_hyperspace_in,
							R.anim.anim_hyperspace_out);
					break;
				case 13:// �������Ƴ�Ч��
					overridePendingTransition(R.anim.anim_push_up_in,
							R.anim.anim_push_up_out);
					break;
				case 14:// ���ҽ���Ч��
					overridePendingTransition(R.anim.anim_slide_left_in,
							R.anim.anim_slide_right_out);
					break;
				case 15:// ��С���½�Ч��
					overridePendingTransition(R.anim.anim_zoom_in,
							R.anim.anim_zoom_out);
					break;
				case 16:// ���½���Ч��
					overridePendingTransition(R.anim.anim_slide_up_in,
							R.anim.anim_slide_down_out);
					break;
				}
			}
		});
	}
}
