package org.lance.demo;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.lance.main.R;
import org.lance.widget.GifView;

public class GifViewDemo extends BaseActivity implements OnClickListener {
	private GifView gifView;
	private Button btn1;
	boolean flag = true;

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.gifview);
		gifView = (GifView) findViewById(R.id.gifview_gif);
		btn1 = (Button) findViewById(R.id.gifview_btn1);
		gifView.setGifImage(R.drawable.switcher);
		gifView.setOnClickListener(this);
		btn1.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (flag) {
			gifView.showAnimation();
			flag = !flag;
		} else {
			gifView.showCover();
			flag = !flag;
		}
	}
	// public static class Builder {
	// private final AlertController.AlertParams P;
	// public Builder(Context context) {
	// P = new AlertController.AlertParams(context);
	// }
	// public Builder setTitle(int titleId) {
	// P.mTitle = P.mContext.getText(titleId);
	// return this;
	// }
	// public Builder setTitle(CharSequence title) {
	// P.mTitle = title;
	// return this;
	// }
	// public Builder setCustomTitle(View customTitleView) {
	// P.mCustomTitleView = customTitleView;
	// return this;
	// }
	// public Builder setMessage(int messageId) {
	// P.mMessage = P.mContext.getText(messageId);
	// return this;
	// }
	// public Builder setMessage(CharSequence message) {
	// P.mMessage = message;
	// return this;
	// }
	// public Builder setIcon(int iconId) {
	// P.mIconId = iconId;
	// return this;
	// }
	// public Builder setIcon(Drawable icon) {
	// P.mIcon = icon;
	// return this;
	// }
	// public Builder setPositiveButton(int textId, final OnClickListener
	// listener) {
	// P.mPositiveButtonText = P.mContext.getText(textId);
	// P.mPositiveButtonListener = listener;
	// return this;
	// }
	// public Builder setPositiveButton(CharSequence text, final OnClickListener
	// listener) {
	// P.mPositiveButtonText = text;
	// P.mPositiveButtonListener = listener;
	// return this;
	// }
	// public Builder setNegativeButton(int textId, final OnClickListener
	// listener) {
	// P.mNegativeButtonText = P.mContext.getText(textId);
	// P.mNegativeButtonListener = listener;
	// return this;
	// }
	// public Builder setNegativeButton(CharSequence text, final OnClickListener
	// listener) {
	// P.mNegativeButtonText = text;
	// P.mNegativeButtonListener = listener;
	// return this;
	// }
	// public Builder setNeutralButton(int textId, final OnClickListener
	// listener) {
	// P.mNeutralButtonText = P.mContext.getText(textId);
	// P.mNeutralButtonListener = listener;
	// return this;
	// }
	// public Builder setNeutralButton(CharSequence text, final OnClickListener
	// listener) {
	// P.mNeutralButtonText = text;
	// P.mNeutralButtonListener = listener;
	// return this;
	// }
	// public Builder setCancelable(boolean cancelable) {
	// P.mCancelable = cancelable;
	// return this;
	// }
	// public Builder setOnCancelListener(OnCancelListener onCancelListener) {
	// P.mOnCancelListener = onCancelListener;
	// return this;
	// }
	// public Builder setOnKeyListener(OnKeyListener onKeyListener) {
	// P.mOnKeyListener = onKeyListener;
	// return this;
	// }
	// public Builder setItems(int itemsId, final OnClickListener listener) {
	// P.mItems = P.mContext.getResources().getTextArray(itemsId);
	// P.mOnClickListener = listener;
	// return this;
	// }
	// public Builder setItems(CharSequence[] items, final OnClickListener
	// listener) {
	// P.mItems = items;
	// P.mOnClickListener = listener;
	// return this;
	// }
	// public Builder setAdapter(final ListAdapter adapter, final
	// OnClickListener listener) {
	// P.mAdapter = adapter;
	// P.mOnClickListener = listener;
	// return this;
	// }
	// public Builder setCursor(final Cursor cursor, final OnClickListener
	// listener,
	// String labelColumn) {
	// P.mCursor = cursor;
	// P.mLabelColumn = labelColumn;
	// P.mOnClickListener = listener;
	// return this;
	// }
	// public Builder setMultiChoiceItems(int itemsId, boolean[] checkedItems,
	// final OnMultiChoiceClickListener listener) {
	// P.mItems = P.mContext.getResources().getTextArray(itemsId);
	// P.mOnCheckboxClickListener = listener;
	// P.mCheckedItems = checkedItems;
	// P.mIsMultiChoice = true;
	// return this;
	// }
	// public Builder setMultiChoiceItems(CharSequence[] items, boolean[]
	// checkedItems,
	// final OnMultiChoiceClickListener listener) {
	// P.mItems = items;
	// P.mOnCheckboxClickListener = listener;
	// P.mCheckedItems = checkedItems;
	// P.mIsMultiChoice = true;
	// return this;
	// }
	// public Builder setMultiChoiceItems(Cursor cursor, String isCheckedColumn,
	// String labelColumn,
	// final OnMultiChoiceClickListener listener) {
	// P.mCursor = cursor;
	// P.mOnCheckboxClickListener = listener;
	// P.mIsCheckedColumn = isCheckedColumn;
	// P.mLabelColumn = labelColumn;
	// P.mIsMultiChoice = true;
	// return this;
	// }
	// public Builder setSingleChoiceItems(int itemsId, int checkedItem,
	// final OnClickListener listener) {
	// P.mItems = P.mContext.getResources().getTextArray(itemsId);
	// P.mOnClickListener = listener;
	// P.mCheckedItem = checkedItem;
	// P.mIsSingleChoice = true;
	// return this;
	// }
	// public Builder setSingleChoiceItems(Cursor cursor, int checkedItem,
	// String labelColumn,
	// final OnClickListener listener) {
	// P.mCursor = cursor;
	// P.mOnClickListener = listener;
	// P.mCheckedItem = checkedItem;
	// P.mLabelColumn = labelColumn;
	// P.mIsSingleChoice = true;
	// return this;
	// }
	// public Builder setSingleChoiceItems(CharSequence[] items, int
	// checkedItem, final OnClickListener listener) {
	// P.mItems = items;
	// P.mOnClickListener = listener;
	// P.mCheckedItem = checkedItem;
	// P.mIsSingleChoice = true;
	// return this;
	// }
	// public Builder setSingleChoiceItems(ListAdapter adapter, int checkedItem,
	// final OnClickListener listener) {
	// P.mAdapter = adapter;
	// P.mOnClickListener = listener;
	// P.mCheckedItem = checkedItem;
	// P.mIsSingleChoice = true;
	// return this;
	// }
	// public Builder setOnItemSelectedListener(final
	// AdapterView.OnItemSelectedListener listener) {
	// P.mOnItemSelectedListener = listener;
	// return this;
	// }
	// public Builder setView(View view) {
	// P.mView = view;
	// P.mViewSpacingSpecified = false;
	// return this;
	// }
	// public Builder setView(View view, int viewSpacingLeft, int
	// viewSpacingTop,
	// int viewSpacingRight, int viewSpacingBottom) {
	// P.mView = view;
	// P.mViewSpacingSpecified = true;
	// P.mViewSpacingLeft = viewSpacingLeft;
	// P.mViewSpacingTop = viewSpacingTop;
	// P.mViewSpacingRight = viewSpacingRight;
	// P.mViewSpacingBottom = viewSpacingBottom;
	// return this;
	// }
	// public Builder setInverseBackgroundForced(boolean useInverseBackground) {
	// P.mForceInverseBackground = useInverseBackground;
	// return this;
	// }
	// public Builder setRecycleOnMeasureEnabled(boolean enabled) {
	// P.mRecycleOnMeasure = enabled;
	// return this;
	// }
	// public AlertDialog create() {
	// final AlertDialog dialog = new AlertDialog(P.mContext);
	// P.apply(dialog.mAlert);
	// dialog.setCancelable(P.mCancelable);
	// dialog.setOnCancelListener(P.mOnCancelListener);
	// if (P.mOnKeyListener != null) {
	// dialog.setOnKeyListener(P.mOnKeyListener);
	// }
	// return dialog;
	// }
	// public AlertDialog show() {
	// AlertDialog dialog = create();
	// dialog.show();
	// return dialog;
	// }
	// }
}
