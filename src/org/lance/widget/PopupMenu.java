package org.lance.widget;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * �Զ���˵�
 * 
 * @author lance
 */
public class PopupMenu {

	private PopupWindow mPopup;
	private Context mContext;

	/**
	 * ͼƬ��Դ
	 */
	private int[] mImgRes = new int[0];

	/**
	 * ������Դ
	 */
	private String[] mTexts = new String[0];

	/**
	 * �˵�����
	 */
	private int mBg;

	/**
	 * �˵���ʾ��ʧ�Ķ���
	 */
	private int mAnimStyle;

	/**
	 * ���ִ�С
	 */
	private float mTxtSize = -1;

	/**
	 * ������ɫ
	 */
	private int mTxtColor = -1;

	/**
	 * �ı����ͼƬ�Ķ��뷽ʽ
	 */
	private int mAlign = MenuItem.TEXT_BOTTOM;

	/**
	 * �˵���ѡ�е�Ч��
	 */
	private int mSelector = -1;

	/**
	 * �˵��Ŀ�
	 */
	private int mWidth;

	/**
	 * �˵��ĸ�
	 */
	private int mHeight;

	/**
	 * ��Ų˵���
	 */
	private GridView mGridView;

	/**
	 * �������ֵ���󳤶ȣ����������"..."���
	 */
	private int mMaxStrLength = 4;

	/**
	 * �˵������¼�
	 */
	private OnMenuItemClickListener mListener;

	/**
	 * �Ƿ�Թ����ַ�����ȡ�Ż�
	 */
	private boolean mIsOptimizeTxt = true;

	/**
	 * ����˵���
	 */
	private ArrayList<MenuItem> mMenuItems = new ArrayList<MenuItem>();

	public PopupMenu(Context context) {
		if (null == context) {
			throw new IllegalArgumentException();
		}

		mContext = context;
	}

	/**
	 * ����ͼƬ��Դ
	 * 
	 * @param imgRes
	 */
	public void setMenuIcons(int[] icons) {
		if (null != icons) {
			mImgRes = icons;
		}
	}

	/**
	 * ���ò˵�����
	 * 
	 * @param bgRes
	 */
	public void setBackgroundResource(int bgRes) {
		mBg = bgRes;
	}

	/**
	 * ���ò˵��������
	 * 
	 * @param txtRes
	 *            ��Դ����
	 */
	public void setMenuTexts(int[] txtRes) {
		if (null == txtRes) {
			return;
		}

		final Resources res = mContext.getResources();
		final int length = txtRes.length;
		mTexts = new String[length];
		for (int i = 0; i < length; i++) {
			mTexts[i] = res.getString(txtRes[i]);
		}
	}

	/**
	 * ���ò˵��������
	 * 
	 * @param txtRes
	 */
	public void setMenuTexts(String[] texts) {
		mTexts = texts;
	}

	/**
	 * �������ִ�С
	 * 
	 * @param txtSize
	 */
	public void setTextSize(float txtSize) {
		mTxtSize = txtSize;
	}

	/**
	 * ����������ɫ
	 * 
	 * @param color
	 */
	public void setTextColor(int color) {
		mTxtColor = color;
	}

	/**
	 * �����ı����ͼƬ�Ķ��뷽ʽ
	 * 
	 * @param align
	 */
	public void setTextAlign(int align) {
		mAlign = align;
	}

	/**
	 * �����ı�����󳤶�
	 * 
	 * @param length
	 */
	public void setMaxTextLength(int length) {
		mMaxStrLength = length;
	}

	/**
	 * �����Ƿ�Թ����ı������Ż�
	 * 
	 * @param isOptimize
	 */
	public void isOptimizeText(boolean isOptimize) {
		mIsOptimizeTxt = isOptimize;
	}

	/**
	 * ���ò˵�����
	 * 
	 * @param animStyle
	 */
	public void setAnimStyle(int animStyle) {
		mAnimStyle = animStyle;
	}

	/**
	 * ���ò˵��Ŀ��
	 * 
	 * @param width
	 */
	public void setWidth(int width) {
		mWidth = width;
	}

	/**
	 * ���ò˵��ĸ߶�
	 * 
	 * @param height
	 */
	public void setHeight(int height) {
		mHeight = height;
	}

	/**
	 * ���ò˵����ѡ�е�Ч��
	 * 
	 * @param selector
	 */
	public void setSelector(int selector) {
		mSelector = selector;
	}

	/**
	 * ����װ�ز˵����ݵ�����
	 * 
	 * @param view
	 */
	public void setMenuConentView(GridView view) {
		mGridView = view;
	}

	/**
	 * ��ʾ�˵�
	 * 
	 * @return ��ʾ�ɹ�����true, ʧ�ܷ���false
	 */
	public boolean show() {
		if (hide()) {
			return false;
		}

		final Context context = mContext;
		final int length = mImgRes.length;
		final int txtLength = mTexts.length;
		Point point = new Point();
		if (length != 0 && txtLength != 0) {
			Point p1 = getTextMaxDimenstion(mTexts);
			Point p2 = getImageMaxDimension(mImgRes);
			switch (mAlign) {
			case MenuItem.TEXT_BOTTOM:
			case MenuItem.TEXT_TOP:
				point.x = Math.max(p1.x, p2.x);
				point.y = p1.y + p2.y;
				break;
			case MenuItem.TEXT_LEFT:
			case MenuItem.TEXT_RIGHT:
				point.x = p1.x + p2.x;
				point.y = Math.max(p1.y, p2.y);
				break;
			}
		} else {
			if (length != 0) {
				point = getImageMaxDimension(mImgRes);
			} else if (txtLength != 0) {
				point = getTextMaxDimenstion(mTexts);
			}
		}

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		int width = mWidth == 0 ? metrics.widthPixels : mWidth;
		float density = metrics.density;
		int imgWidth = point.x;
		int height = point.y;
		// ��ȥ5dp�ļ��һ�����ܰڷ�ͼƬ�ĸ���
		int columns = (int) ((width - 5 * density) / (imgWidth + 5 * density));

		int leng = length != 0 ? length : txtLength;
		int rows = columns == 0 ? 0 : leng / columns;
		if (columns * rows < leng) {
			rows += 1;
		}

		final LinearLayout layout = initLayout(context);
		GridView gridView = mGridView;
		if (null == gridView) {
			gridView = getContentView(context, columns);
		} else {
			setContentViewListener(gridView);
		}

		layout.addView(gridView, new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		// �Ը߶Ƚ�������
		int h = 0;
		if (mAlign == MenuItem.TEXT_LEFT || mAlign == MenuItem.TEXT_RIGHT) {
			h = (int) (height * rows + 8 * density);
		} else if (mAlign == MenuItem.TEXT_BOTTOM
				|| mAlign == MenuItem.TEXT_TOP) {
			h = (int) ((height + 8 * density) * rows);
		}
		if (txtLength != 0) {
			h += 10 * density;
		}
		mPopup = new PopupWindow(context);
		mPopup.setWidth(width);
		mPopup.setHeight(mHeight == 0 ? h : mHeight);

		mPopup.setContentView(layout);
		mPopup.setFocusable(true);
		mPopup.setOutsideTouchable(true);
		mPopup.setTouchable(true);
		// ���ñ���Ϊnull���Ͳ�����ֺ�ɫ�����������ؼ�PopupWindow�ͻ���ʧ
		mPopup.setBackgroundDrawable(null);
		if (mAnimStyle != 0) {
			mPopup.setAnimationStyle(mAnimStyle);
		}
		mPopup.showAtLocation(layout, Gravity.BOTTOM | Gravity.CENTER, 0, 0);
		return true;
	}

	private LinearLayout initLayout(Context context) {
		LinearLayout layout = new LinearLayout(context);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setFadingEdgeLength(0);
		layout.setGravity(Gravity.CENTER);

		layout.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					hide();
				}
				return false;
			}

		});

		return layout;
	}

	/**
	 * ��ʼ���ݣ������ݼ��ص���Ӧ��View��
	 */
	private void initData() {
		MenuItem item = new MenuItem(mContext);
		item.setTextAlign(mAlign);
		item.setTextColor(mTxtColor);
		item.setTextSize(mTxtColor);
		int txtLength = mTexts.length;
		int imgLength = mImgRes.length;
		if (txtLength != 0 && imgLength != 0) {
			for (int i = 0; i < imgLength; i++) {
				MenuItem menuItem = new MenuItem(mContext, item);
				menuItem.setImageRes(mImgRes[i]);
				menuItem.setText(mTexts[i]);
				mMenuItems.add(menuItem);
			}

		} else {
			if (txtLength != 0) {
				for (int i = 0; i < txtLength; i++) {
					MenuItem menuItem = new MenuItem(mContext, item);
					menuItem.setText(mTexts[i]);
					mMenuItems.add(menuItem);
				}
			} else if (imgLength != 0) {
				for (int i = 0; i < imgLength; i++) {
					MenuItem menuItem = new MenuItem(mContext, item);
					menuItem.setImageRes(mImgRes[i]);
					mMenuItems.add(menuItem);
				}
			}
		}
	}

	/**
	 * ��ʼ���˵��������
	 * 
	 * @param context
	 * @param columns
	 *            �˵�������
	 * @return
	 */
	private GridView getContentView(Context context, int columns) {
		if (mMenuItems.isEmpty()) {
			initData();
		}

		if (null != mGridView) {
			return mGridView;
		}
		GridView gridView = new GridView(context);
		gridView.setLayoutParams(new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
		gridView.setAdapter(new MenuAdapter(mMenuItems));
		gridView.setVerticalSpacing(0);
		gridView.setNumColumns(columns);
		gridView.setGravity(Gravity.CENTER);
		gridView.setVerticalScrollBarEnabled(false);
		if (mBg != 0) {
			gridView.setBackgroundResource(mBg);
		}
		if (mSelector != -1) {
			gridView.setSelector(mSelector);
		}
		gridView.setHorizontalScrollBarEnabled(false);
		setContentViewListener(gridView);
		return gridView;
	}

	/**
	 * ע���¼�
	 * 
	 * @param gridView
	 */
	private void setContentViewListener(GridView gridView) {
		if (null == gridView.getOnItemClickListener()) {
			gridView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (null != mListener) {
						mListener.onMenuItemClick(parent, view, position);
					}
					hide();
				}

			});
		}

		gridView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					switch (keyCode) {
					case KeyEvent.KEYCODE_BACK:
					case KeyEvent.KEYCODE_MENU:
						hide();
						break;
					}
				}
				return false;
			}

		});
	}

	/**
	 * ��ȡ����ͼƬ�����Ŀ�͸�
	 * 
	 * @param imgRes
	 * @return
	 */
	private Point getImageMaxDimension(int[] imgRes) {
		final Point point = new Point();
		for (int i = 0, length = imgRes.length; i < length; i++) {
			Bitmap tmp = BitmapFactory.decodeResource(mContext.getResources(),
					imgRes[i]);
			int width = tmp.getWidth();
			int height = tmp.getHeight();
			tmp.recycle();
			tmp = null;
			if (point.x < width) {
				point.x = width;
			}
			if (point.y < height) {
				point.y = height;
			}
		}

		return point;
	}

	/**
	 * �����ı�����󳤶�
	 * 
	 * @param txts
	 * @return
	 */
	private Point getTextMaxDimenstion(String[] txts) {
		final Point point = new Point();
		final Rect bounds = new Rect();
		final Paint paint = new Paint();
		float size = mTxtSize != -1 ? mTxtSize : mContext.getResources()
				.getDisplayMetrics().density * 16;
		paint.setTextSize(size);
		paint.setColor(mTxtColor != -1 ? mTxtColor : Color.BLACK);
		if (mIsOptimizeTxt) {
			for (int i = 0, length = txts.length; i < length; i++) {
				String str = txts[i];
				if (null == str) {
					str = "";
				} else if (str.length() > mMaxStrLength) {
					// ���ַ������Ƚ��п���
					str = new StringBuilder()
							.append(str.substring(0, mMaxStrLength))
							.append("...").toString();
				}

				txts[i] = str;
				paint.getTextBounds(str, 0, str.length(), bounds);
				compareDimension(point, bounds.width(), bounds.height());
			}
		} else {
			for (int i = 0, length = txts.length; i < length; i++) {
				String str = txts[i];
				if (null == str) {
					str = "";
				}

				txts[i] = str;
				paint.getTextBounds(str, 0, str.length(), bounds);
				compareDimension(point, bounds.width(), bounds.height());
			}
		}
		return point;
	}

	/**
	 * �Ƚϲ��ı����ߴ�
	 * 
	 * @param point
	 *            �������ߴ�Ķ���
	 * @param width
	 *            ��
	 * @param height
	 *            ��
	 */
	private void compareDimension(Point point, int width, int height) {
		if (point.x < width) {
			point.x = width;
		}

		if (point.y < height) {
			point.y = height;
		}
	}

	/**
	 * ���ز˵�
	 * 
	 * @return ���سɹ�����true��ʧ�ܷ���false
	 */
	public boolean hide() {
		if (null != mPopup && mPopup.isShowing()) {
			mPopup.dismiss();
			mPopup = null;
			// if (null != mListener) {
			// mListener.hideMenu();
			// }
			return true;
		}
		return false;
	}

	// public void dismiss() {
	// mMenuItems.clear();
	// mGridView = null;
	// mTexts = new String[0];
	// mImgRes = new int[0];
	// mWidth = 0;
	// mHeight = 0;
	// }

	/**
	 * ���ò˵��ѡ�м�����
	 * 
	 * @param listener
	 */
	public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
		mListener = listener;
	}

	/**
	 * �˵���Ŀѡ�м�����
	 * 
	 * @author maylian.mei
	 * 
	 */
	public interface OnMenuItemClickListener {
		/**
		 * �˵������Ļ���õķ���
		 * 
		 * @param parent
		 * @param view
		 * @param position
		 */
		public void onMenuItemClick(AdapterView<?> parent, View view,
				int position);

		/**
		 * �˵����ػ���õķ���
		 */
		public void hideMenu();
	}

	public static class MenuItem {
		private LinearLayout mLayout;

		/**
		 * �ı���ͼƬ���·���ʾ
		 */
		public static final int TEXT_BOTTOM = 0x0;

		/**
		 * �ı���ͼƬ���Ϸ���ʾ
		 */
		public static final int TEXT_TOP = 0x1;

		/**
		 * �ı���ͼƬ�������ʾ
		 */
		public static final int TEXT_LEFT = 0x2;

		/**
		 * �ı���ͼƬ���ұ���ʾ
		 */
		public static final int TEXT_RIGHT = 0x3;

		/**
		 * �ı��Ķ��뷽ʽ
		 */
		private int mAlign = TEXT_BOTTOM;

		/**
		 * �ı�
		 */
		private String mText;

		/**
		 * �ı���ɫ
		 */
		private int mTextColor;

		/**
		 * �ı���С
		 */
		private int mTextSize;

		/**
		 * ͼƬ����ԴID
		 */
		private int mImgRes;
		private Context mContext;

		public MenuItem(Context context) {
			this(context, 0, null, 0, 0, TEXT_BOTTOM);
		}

		public MenuItem(Context context, int imgRes, String text,
				int textColor, int textSize, int align) {
			mImgRes = imgRes;
			mText = text;
			mTextColor = textColor;
			mTextSize = textSize;
			mAlign = align;
			mContext = context;
		}

		public MenuItem(Context context, MenuItem item) {
			this(context, 0, null, item.getTextColor(), item.getTextSize(),
					item.getAlign());
		}

		/**
		 * ��ʼ���˵���
		 * 
		 * @param context
		 */
		private void initlayout() {
			Context context = mContext;
			mLayout = new LinearLayout(context);
			mLayout.setLayoutParams(new AbsListView.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT));
			mLayout.setGravity(Gravity.CENTER);
			TextView textView = getTextView(context);
			ImageView imageView = getImageView(context);
			if (null != textView && null != imageView) {
				Point point = getImageDimension(context, mImgRes);
				switch (mAlign) {
				case TEXT_BOTTOM: // �ı�����
					mLayout.setOrientation(LinearLayout.VERTICAL);
					mLayout.addView(imageView, new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, point.y));
					mLayout.addView(textView, new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT));
					break;
				case TEXT_TOP:// �ı�����
					mLayout.setOrientation(LinearLayout.VERTICAL);
					mLayout.addView(textView, new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT));
					mLayout.addView(imageView, new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT, point.y));
					break;
				case TEXT_LEFT:// �ı�����
					mLayout.setOrientation(LinearLayout.HORIZONTAL);
					mLayout.addView(textView, new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.MATCH_PARENT));
					mLayout.addView(imageView, new ViewGroup.LayoutParams(
							point.x, ViewGroup.LayoutParams.MATCH_PARENT));
					break;
				case TEXT_RIGHT:// �ı�����
					mLayout.setOrientation(LinearLayout.HORIZONTAL);
					mLayout.addView(imageView, new ViewGroup.LayoutParams(
							point.x, ViewGroup.LayoutParams.MATCH_PARENT));
					mLayout.addView(textView, new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.WRAP_CONTENT,
							ViewGroup.LayoutParams.MATCH_PARENT));
					break;
				}
			} else {
				if (null != textView) {
					mLayout.addView(textView, new ViewGroup.LayoutParams(
							ViewGroup.LayoutParams.MATCH_PARENT,
							ViewGroup.LayoutParams.MATCH_PARENT));
				} else if (null != imageView) {
					Point point = getImageDimension(context, mImgRes);
					mLayout.addView(imageView, new ViewGroup.LayoutParams(
							point.x, point.y));
				}
			}
		}

		/**
		 * ��ȡͼƬ�ĳߴ�
		 * 
		 * @param context
		 * @param res
		 * @return
		 */
		private Point getImageDimension(Context context, int res) {
			Point point = new Point();
			Bitmap bm = BitmapFactory.decodeResource(context.getResources(),
					mImgRes);
			point.x = bm.getWidth();
			point.y = bm.getHeight();
			bm.recycle();
			bm = null;
			return point;
		}

		/**
		 * ����ͼƬ��Դ
		 * 
		 * @param imgRes
		 */
		public void setImageRes(int imgRes) {
			mImgRes = imgRes;
		}

		/**
		 * �����ı���С
		 * 
		 * @param size
		 */
		public void setTextSize(int size) {
			mTextSize = size;
		}

		/**
		 * �����ı���ɫ
		 * 
		 * @param color
		 */
		public void setTextColor(int color) {
			mTextColor = color;
		}

		/**
		 * �����ı�����
		 * 
		 * @param text
		 */
		public void setText(String text) {
			mText = text;
		}

		/**
		 * �����ı��Ķ��뷽ʽ
		 * 
		 * @param align
		 */
		public void setTextAlign(int align) {
			mAlign = align;
		}

		public String getText() {
			return mText;
		}

		public int getTextColor() {
			return mTextColor;
		}

		public int getTextSize() {
			return mTextSize;
		}

		/**
		 * ����TextView
		 * 
		 * @param context
		 * @return
		 */
		private TextView getTextView(Context context) {
			if (TextUtils.isEmpty(mText)) {
				return null;
			}
			TextView txtView = new TextView(context);
			if (mTextColor != 0) {
				txtView.setTextColor(mTextColor);
			}

			if (mTextSize != 0) {
				txtView.setTextSize(mTextSize);
			}
			txtView.setText(mText);
			txtView.setGravity(Gravity.CENTER);
			return txtView;
		}

		public int getAlign() {
			return mAlign;
		}

		/**
		 * ����ImageView
		 * 
		 * @param context
		 * @return
		 */
		private ImageView getImageView(Context context) {
			if (mImgRes == 0) {
				return null;
			}
			ImageView view = new ImageView(context);
			view.setImageResource(mImgRes);
			return view;
		}

		/**
		 * ��ȡ�˵���Ŀ������
		 * 
		 * @return
		 */
		public View getView() {
			initlayout();
			return mLayout;
		}

	}

	public class MenuAdapter extends BaseAdapter {
		private ArrayList<MenuItem> mMenuItems;

		public MenuAdapter(ArrayList<MenuItem> menuItems) {
			mMenuItems = menuItems;
		}

		@Override
		public int getCount() {
			return mMenuItems.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = mMenuItems.get(position).getView();
			}

			return convertView;
		}

	}

}