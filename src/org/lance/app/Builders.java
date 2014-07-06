package org.lance.app;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * ����ĶԻ���
 * 
 * @author lance
 * 
 */
public class Builders {
	// ��������
	private int padding = 16;;
	// �����С
	private int textSize = 16;
	// �Ի������ڵ�������
	private Context context;
	// �Ի���
	private AlertDialog dialog;
	// �Ի������
	private String title;
	// ��ʾ��Ϣ
	private String message;
	// �����Զ���View���ݵ���ʾ��Ϣ
	private String content;
	// ��ʾͼ��--���
	private Drawable icon;
	// �Ի����Զ��岼��
	private View view;
	// ͷ���Զ��岼��
	private View headView;
	// ��ʾ��ʾ��Ϣ�Ŀؼ�
	private TextView hintView;
	// ȷ����ť
	private Button positive;
	// ȡ����ť
	private Button negative;
	// ���԰�ť
	private Button neutral;
	// ȷ����ť��ֵ
	private String positiveValue;
	// ȡ����ť��ֵ
	private String negativeValue;
	// ���԰�ť��ֵ
	private String neutralValue;
	// ȷ����ť������---�������ٶԻ��򼰴�����Ӧ��ҵ���߼�
	private OnClickListener positiveButton;
	// ȡ����ť������---�������ٶԻ��򼰴�����Ӧ��ҵ���߼�
	private OnClickListener negativeButton;
	// ���԰�ť������---�������ٶԻ��򼰴�����Ӧ��ҵ���߼�
	private OnClickListener neutralButton;
	// ȷ���Ի���İ�ť������
	private DialogInterface.OnClickListener positiveButtonListener;
	// ȡ���Ի���İ�ť������
	private DialogInterface.OnClickListener negativeButtonListener;
	// ���ԶԻ���İ�ť������
	private DialogInterface.OnClickListener neutralButtonListener;

	public Builders(Context context) {
		this.context = context;
	}

	public AlertDialog create() {
		Builder builder = new AlertDialog.Builder(context);
		this.dialog = builder.create();
		if (view == null) {
			if (headView != null) {
				dialog.setCustomTitle(headView);
				if (message != null) {
					dialog.setMessage(message);
				}
				if (positiveButtonListener != null) {
					dialog.setButton(positiveValue,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									positiveButtonListener.onClick(dialog,
											which);
								}
							});
				} else {
					dialog.setButton(positiveValue,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
				}
				if (negativeButtonListener != null) {
					dialog.setButton2(negativeValue,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									negativeButtonListener.onClick(dialog,
											which);
								}
							});
				} else {
					dialog.setButton2(negativeValue,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
				}
				if (neutralButtonListener != null) {
					dialog.setButton3(neutralValue,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									neutralButtonListener
											.onClick(dialog, which);
								}
							});
				} else {
					dialog.setButton3(neutralValue,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
				}
			} else {
				if (icon != null) {
					dialog.setIcon(icon);
				}
				if (title != null) {
					dialog.setTitle(title);
				}
				if (message != null) {
					dialog.setMessage(message);
				}
			}
		} else {
			if (hintView != null) {
				hintView.setPadding(padding, padding, padding, padding);
				hintView.setTextSize(textSize);
				hintView.setText(content);
			}
			if (positiveButton != null) {
				if (positive != null) {
					positive.setText(positiveValue);
					positive.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
							positiveButton.onClick(v);
						}
					});
				}
			} else {
				if (positive != null) {
					positive.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				}
			}
			if (negativeButton != null) {
				if (negative != null) {
					negative.setText(negativeValue);
					negative.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
							negativeButton.onClick(v);
						}
					});
				}
			} else {
				if (negative != null) {
					negative.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				}
			}
			if (neutralButton != null) {
				if (neutral != null) {
					neutral.setText(neutralValue);
					neutral.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
							neutralButton.onClick(v);
						}
					});
				}
			} else {
				if (neutral != null) {
					neutral.setOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				}
			}
		}
		return dialog;
	}

	public AlertDialog show() {
		dialog = create();
		dialog.show();
		if (view != null) {
			dialog.setContentView(view);
		}
		return dialog;
	}

	public static Builders show(Context context, String title, String message,
			Drawable icon) {
		Builders builders = new Builders(context);
		builders.setTitle(title).setMessage(message).setIcon(icon);
		return builders;
	}

	public static Builders show(Context context, String title, String message,
			int icon) {
		Builders builders = new Builders(context);
		builders.setTitle(title).setMessage(message).setIcon(icon);
		return builders;
	}

	public static Builders show(Context context, int titleId, String message,
			Drawable icon) {
		Builders builders = new Builders(context);
		builders.setTitle(titleId).setMessage(message).setIcon(icon);
		return builders;
	}

	public static Builders show(Context context, int titleId, String message,
			int iconId) {
		Builders builders = new Builders(context);
		builders.setTitle(titleId).setMessage(message).setIcon(iconId);
		return builders;
	}

	public Builders setPositiveButton(String value,
			final OnClickListener positiveButton) {
		this.positiveValue = value;
		this.positiveButton = positiveButton;
		return this;
	}

	public Builders setPositiveButton(int valueId,
			final OnClickListener positiveButton) {
		this.positiveValue = context.getResources().getString(valueId);
		this.positiveButton = positiveButton;
		return this;
	}

	public Builders setNegativeButton(String value,
			final OnClickListener negativeButton) {
		this.negativeValue = value;
		this.negativeButton = negativeButton;
		return this;
	}

	public Builders setNegativeButton(int valueId,
			final OnClickListener negativeButton) {
		this.negativeValue = context.getResources().getString(valueId);
		this.negativeButton = negativeButton;
		return this;
	}

	public Builders setNeutralButton(String value,
			final OnClickListener neutralButton) {
		this.neutralValue = value;
		this.neutralButton = neutralButton;
		return this;
	}

	public Builders setNeutralButton(int valueId,
			final OnClickListener neutralButton) {
		this.neutralValue = context.getResources().getString(valueId);
		this.neutralButton = neutralButton;
		return this;
	}

	public Builders setPositiveButtonListener(String value,
			final DialogInterface.OnClickListener positiveButtonListener) {
		this.positiveValue = value;
		this.positiveButtonListener = positiveButtonListener;
		return this;
	}

	public Builders setPositiveButtonListener(int valueId,
			final DialogInterface.OnClickListener positiveButtonListener) {
		this.positiveValue = context.getResources().getString(valueId);
		this.positiveButtonListener = positiveButtonListener;
		return this;
	}

	public Builders setNegativeButtonListener(String value,
			final DialogInterface.OnClickListener negativeButtonListener) {
		this.negativeValue = value;
		this.negativeButtonListener = negativeButtonListener;
		return this;
	}

	public Builders setNegativeButtonListener(int valueId,
			final DialogInterface.OnClickListener negativeButtonListener) {
		this.negativeValue = context.getResources().getString(valueId);
		this.negativeButtonListener = negativeButtonListener;
		return this;
	}

	public Builders setNeutralButtonListener(String value,
			final DialogInterface.OnClickListener neutralButtonListener) {
		this.neutralValue = value;
		this.neutralButtonListener = neutralButtonListener;
		return this;
	}

	public Builders setNeutralButtonListener(int valueId,
			final DialogInterface.OnClickListener neutralButtonListener) {
		this.neutralValue = context.getResources().getString(valueId);
		this.neutralButtonListener = neutralButtonListener;
		return this;
	}

	public View getHeadView() {
		return headView;
	}

	public Builders setHeadView(View headView) {
		this.headView = headView;
		this.title = null;
		this.icon = null;
		return this;
	}

	public Builders setHeadView(int headViewId) {
		this.headView = LayoutInflater.from(context).inflate(headViewId, null);
		this.title = null;
		this.icon = null;
		return this;
	}

	public View getView() {
		return view;
	}

	public Builders setView(View view) {
		this.view = view;
		return this;
	}

	public Builders setView(View view, TextView textView) {
		this.view = view;
		this.hintView = textView;
		return this;
	}

	public Builders setView(View mView, TextView textView, Button positive) {
		this.view = mView;
		this.hintView = textView;
		this.positive = positive;
		return this;
	}

	public Builders setView(View mView, TextView textView, Button positive,
			Button negative) {
		this.view = mView;
		this.hintView = textView;
		this.positive = positive;
		this.negative = negative;
		return this;
	}

	public Builders setView(View mView, TextView textView, Button positive,
			Button negative, Button neutral) {
		this.view = mView;
		this.hintView = textView;
		this.positive = positive;
		this.negative = negative;
		this.neutral = neutral;
		return this;
	}

	public Builders setView(int viewId, int textViewId) {
		this.view = LayoutInflater.from(context).inflate(viewId, null);
		this.hintView = (TextView) this.view.findViewById(textViewId);
		return this;
	}

	public Builders setView(int viewId, int textViewId, int positiveId) {
		this.view = LayoutInflater.from(context).inflate(viewId, null);
		this.hintView = (TextView) this.view.findViewById(textViewId);
		this.positive = (Button) this.view.findViewById(positiveId);
		return this;
	}

	public Builders setView(int viewId, int textViewId, int positiveId,
			int negativeId) {
		this.view = LayoutInflater.from(context).inflate(viewId, null);
		this.hintView = (TextView) this.view.findViewById(textViewId);
		this.positive = (Button) this.view.findViewById(positiveId);
		this.negative = (Button) this.view.findViewById(negativeId);
		return this;
	}

	public Builders setView(int viewId, int textViewId, int positiveId,
			int negativeId, int neutralId) {
		this.view = LayoutInflater.from(context).inflate(viewId, null);
		this.hintView = (TextView) this.view.findViewById(textViewId);
		this.positive = (Button) this.view.findViewById(positiveId);
		this.negative = (Button) this.view.findViewById(negativeId);
		this.neutral = (Button) this.view.findViewById(neutralId);
		return this;
	}

	public Builders setView(View view, int textViewId) {
		this.view = view;
		this.hintView = (TextView) this.view.findViewById(textViewId);
		return this;
	}

	public Builders setView(View view, int textViewId, int positiveId) {
		this.view = view;
		this.hintView = (TextView) this.view.findViewById(textViewId);
		this.positive = (Button) this.view.findViewById(positiveId);
		return this;
	}

	public Builders setView(View view, int textViewId, int positiveId,
			int negativeId) {
		this.view = view;
		this.hintView = (TextView) this.view.findViewById(textViewId);
		this.positive = (Button) this.view.findViewById(positiveId);
		this.negative = (Button) this.view.findViewById(negativeId);
		return this;
	}

	public Builders setView(View view, int textViewId, int positiveId,
			int negativeId, int neutralId) {
		this.view = view;
		this.hintView = (TextView) this.view.findViewById(textViewId);
		this.positive = (Button) this.view.findViewById(positiveId);
		this.negative = (Button) this.view.findViewById(negativeId);
		this.neutral = (Button) this.view.findViewById(neutralId);
		return this;
	}

	public String getContent() {
		return content;
	}

	public Builders setContent(String content) {
		this.content = content;
		if (hintView != null) {
			hintView.setText(content);
		}
		return this;
	}

	public Builders setContent(int contentId) {
		this.content = context.getResources().getString(contentId);
		if (hintView != null) {
			hintView.setText(content);
		}
		return this;
	}

	public String getTitle() {
		return title;
	}

	public Builders setTitle(String title) {
		this.title = title;
		return this;
	}

	public Builders setTitle(int titleId) {
		this.title = context.getResources().getString(titleId);
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Builders setMessage(String message) {
		this.message = message;
		return this;
	}

	public Builders setMessage(int messageId) {
		this.message = context.getResources().getString(messageId);
		return this;
	}

	public Drawable getIcon() {
		return icon;
	}

	public Builders setIcon(Drawable icon) {
		this.icon = icon;
		return this;
	}

	public Builders setIcon(int iconId) {
		this.icon = context.getResources().getDrawable(iconId);
		return this;
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public int getTextSize() {
		return textSize;
	}

	public void setTextSize(int textSize) {
		this.textSize = textSize;
	}

}
