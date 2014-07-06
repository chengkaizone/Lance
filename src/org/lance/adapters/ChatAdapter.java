package org.lance.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.lance.entity.ChatEntity;
import org.lance.main.R;

public class ChatAdapter extends BaseAdapter {
	private Context context = null;
	private List<ChatEntity> chatList = null;
	private LayoutInflater inflater = null;
	private int COME_MSG = 0;
	private int TO_MSG = 1;

	public ChatAdapter(Context context, List<ChatEntity> chatList) {
		this.context = context;
		this.chatList = chatList;

	}

	@Override
	public int getCount() {
		return chatList.size();
	}

	@Override
	public Object getItem(int position) {
		return chatList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		ChatEntity entity = chatList.get(position);
		if (entity.isComeMsg()) {
			return COME_MSG;
		} else {
			return TO_MSG;
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatHolder chatHolder = null;
		inflater = LayoutInflater.from(this.context);
		if (convertView == null) {
			chatHolder = new ChatHolder();
			if (chatList.get(position).isComeMsg()) {
				convertView = inflater.inflate(R.layout.chat_from_item, null);
			} else {
				convertView = inflater.inflate(R.layout.chat_to_item, null);
			}
			chatHolder.setTimeTextView((TextView) convertView
					.findViewById(R.id.tv_time));
			chatHolder.setContentTextView((TextView) convertView
					.findViewById(R.id.tv_content));
			chatHolder.setUserImageView((ImageView) convertView
					.findViewById(R.id.iv_user_image));
			convertView.setTag(chatHolder);
		} else {
			chatHolder = (ChatHolder) convertView.getTag();
		}

		chatHolder.getTimeTextView().setText(
				chatList.get(position).getChatTime());
		chatHolder.getContentTextView().setText(
				chatList.get(position).getContent());
		chatHolder.getUserImageView().setImageResource(
				chatList.get(position).getUserImage());

		return convertView;
	}

	private class ChatHolder {
		private TextView timeTextView;
		private ImageView userImageView;
		private TextView contentTextView;

		public TextView getTimeTextView() {
			return timeTextView;
		}

		public void setTimeTextView(TextView timeTextView) {
			this.timeTextView = timeTextView;
		}

		public ImageView getUserImageView() {
			return userImageView;
		}

		public void setUserImageView(ImageView userImageView) {
			this.userImageView = userImageView;
		}

		public TextView getContentTextView() {
			return contentTextView;
		}

		public void setContentTextView(TextView contentTextView) {
			this.contentTextView = contentTextView;
		}
	}
}