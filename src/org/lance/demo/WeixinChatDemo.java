package org.lance.demo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.lance.adapters.ChatAdapter;
import org.lance.entity.ChatEntity;
import org.lance.main.R;

public class WeixinChatDemo extends BaseActivity {
	private Button sendButton = null;
	private EditText contentEditText = null;
	private ListView chatListView = null;
	private List<ChatEntity> chatList = null;
	private ChatAdapter chatAdapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weixin_main);
		init();
	}

	private void init() {
		contentEditText = (EditText) this.findViewById(R.id.et_content);
		sendButton = (Button) this.findViewById(R.id.btn_send);
		chatListView = (ListView) this.findViewById(R.id.listview);
		chatList = new ArrayList<ChatEntity>();
		ChatEntity chatEntity = null;
		// 初始化聊天内容
		for (int i = 0; i < 2; i++) {
			chatEntity = new ChatEntity();
			if (i % 2 == 0) {
				chatEntity.setComeMsg(false);
				chatEntity.setContent("Hello");
				chatEntity.setChatTime("2012-10-06 14:12:32");
			} else {
				chatEntity.setComeMsg(true);
				chatEntity.setContent("Hello,nice to meet you!");
				chatEntity.setChatTime("2012-10-02 15:13:32");
			}
			chatList.add(chatEntity);
		}
		chatAdapter = new ChatAdapter(this, chatList);
		chatListView.setAdapter(chatAdapter);
		sendButton.setOnClickListener(new onClickListenerImpl());
	}

	/**
	 * 发送消息事件
	 * 
	 * @author JERRY
	 * 
	 */
	private class onClickListenerImpl implements OnClickListener {
		@Override
		public void onClick(View v) {
			if (!contentEditText.getText().toString().equals("")) {
				// 发送消息
				send();
			} else {
				Toast.makeText(WeixinChatDemo.this, "内容不能为空",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 发送消息
	 */
	private void send() {
		ChatEntity chatEntity = new ChatEntity();
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		chatEntity.setChatTime(sdf.format(now));
		chatEntity.setContent(contentEditText.getText().toString());
		chatEntity.setComeMsg(false);
		chatList.add(chatEntity);
		chatAdapter.notifyDataSetChanged();
		chatListView.setSelection(chatList.size() - 1);
		contentEditText.setText("");
	}
}