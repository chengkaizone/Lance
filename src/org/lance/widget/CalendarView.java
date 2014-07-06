package org.lance.widget;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout.LayoutParams;

import org.lance.demo.CalendarDemo;

/**
 * 日历控件视图
 * 
 * @author lance
 * 
 */
public class CalendarView extends View {
	// 字体大小
	private static final int fTextSize = 28;

	// 基本元素
	// 日历监听器
	private OnCalendarListener listener = null;
	private Paint paint = new Paint();
	// 矩形类---和Rect类似只是精度不一样
	private RectF rect = new RectF();
	private String sDate = "";

	// 当前日期
	private int year = 0;
	private int month = 0;
	private int date = 0;

	// 布尔变量
	private boolean bSelected = false;
	private boolean bIsActiveMonth = false;
	private boolean bToday = false;
	private boolean bTouchedDown = false;
	private boolean bHoliday = false;
	private boolean hasRecord = false;
	// 动画执行时间
	public static int ANIM_ALPHA_DURATION = 100;

	/**
	 * 创建日历控件
	 * 
	 * @param context
	 * @param width
	 *            控件宽
	 * @param height
	 *            控件高
	 */
	public CalendarView(Context context, int width, int height) {
		super(context);
		setFocusable(true);
		setLayoutParams(new LayoutParams(width, height));
	}

	/**
	 * 获取当前时间的日历对象
	 * 
	 * @return
	 */
	public Calendar getCurCalendar() {
		Calendar calDate = Calendar.getInstance();
		calDate.clear();
		calDate.set(Calendar.YEAR, year);
		calDate.set(Calendar.MONTH, month);
		calDate.set(Calendar.DAY_OF_MONTH, date);
		return calDate;
	}

	// 设置变量值
	public void setData(int iYear, int iMonth, int iDay, Boolean bToday,
			Boolean bHoliday, int iActiveMonth, boolean hasRecord) {
		year = iYear;
		month = iMonth;
		date = iDay;

		this.sDate = Integer.toString(date);
		this.bIsActiveMonth = (month == iActiveMonth);
		this.bToday = bToday;
		this.bHoliday = bHoliday;
		this.hasRecord = hasRecord;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		rect.set(0, 0, this.getWidth(), this.getHeight());
		rect.inset(1, 1);

		final boolean bFocused = IsViewFocused();

		drawDayView(canvas, bFocused);
		drawDayNumber(canvas);
	}

	public boolean IsViewFocused() {
		return (this.isFocused() || bTouchedDown);
	}

	/**
	 * 绘制日历方格
	 * 
	 * @param canvas
	 * @param bFocused
	 */
	private void drawDayView(Canvas canvas, boolean bFocused) {
		if (bSelected || bFocused) {
			LinearGradient lGradBkg = null;
			if (bFocused) {
				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
						0xffaa5500, 0xffffddbb, Shader.TileMode.CLAMP);
			}
			if (bSelected) {
				lGradBkg = new LinearGradient(rect.left, 0, rect.right, 0,
						0xff225599, 0xffbbddff, Shader.TileMode.CLAMP);
			}

			if (lGradBkg != null) {
				paint.setShader(lGradBkg);
				canvas.drawRect(rect, paint);
			}

			paint.setShader(null);

		} else {
			paint.setColor(getColorBkg(bHoliday, bToday));
			canvas.drawRect(rect, paint);
		}

		if (hasRecord) {
			CreateReminder(canvas, CalendarDemo.special_Reminder);
		}
	}

	/**
	 * 绘制日历中的数字
	 * 
	 * @param canvas
	 */
	public void drawDayNumber(Canvas canvas) {
		paint.setTypeface(null);
		paint.setAntiAlias(true);
		paint.setShader(null);
		paint.setFakeBoldText(true);
		paint.setTextSize(fTextSize);
		paint.setColor(CalendarDemo.isPresentMonth_FontColor);
		paint.setUnderlineText(false);

		if (!bIsActiveMonth)
			paint.setColor(CalendarDemo.unPresentMonth_FontColor);
		if (bToday)
			paint.setUnderlineText(true);
		final int iPosX = (int) rect.left + ((int) rect.width() >> 1)
				- ((int) paint.measureText(sDate) >> 1);
		final int iPosY = (int) (this.getHeight()
				- (this.getHeight() - getTextHeight()) / 2 - paint
				.getFontMetrics().bottom);
		// 绘制文本
		canvas.drawText(sDate, iPosX, iPosY, paint);
		// 设置下划线
		paint.setUnderlineText(false);
	}

	// 得到字体高度
	private int getTextHeight() {
		return (int) (-paint.ascent() + paint.descent());
	}

	// 根据条件返回不同颜色值
	public static int getColorBkg(boolean bHoliday, boolean bToday) {
		if (bToday)
			return CalendarDemo.isToday_BgColor;
		// if (bHoliday) //如需周末有特殊背景色，可去掉注释
		// return Calendar_TestActivity.isHoliday_BgColor;
		return CalendarDemo.Calendar_DayBgColor;
	}

	// 设置是否被选中
	@Override
	public void setSelected(boolean bEnable) {
		if (this.bSelected != bEnable) {
			this.bSelected = bEnable;
			this.invalidate();
		}
	}

	public void setListener(OnCalendarListener listener) {
		this.listener = listener;
	}

	public void dolistener() {
		if (listener != null)
			listener.OnClick(this);
	}

	// 点击事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean bHandled = false;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			bHandled = true;
			bTouchedDown = true;
			invalidate();
			startAlphaAnimIn(CalendarView.this);
		}
		if (event.getAction() == MotionEvent.ACTION_CANCEL) {
			bHandled = true;
			bTouchedDown = false;
			invalidate();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			bHandled = true;
			bTouchedDown = false;
			invalidate();
			dolistener();
		}
		return bHandled;
	}

	// 点击事件
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyDown(keyCode, event);
		if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
				|| (keyCode == KeyEvent.KEYCODE_ENTER)) {
			dolistener();
		}
		return bResult;
	}

	// 不透明度渐变
	public static void startAlphaAnimIn(View view) {
		AlphaAnimation anim = new AlphaAnimation(0.5F, 1);
		anim.setDuration(ANIM_ALPHA_DURATION);
		anim.startNow();
		view.startAnimation(anim);
	}

	public void CreateReminder(Canvas canvas, int Color) {
		paint.setStyle(Paint.Style.FILL_AND_STROKE);
		paint.setColor(Color);
		Path path = new Path();
		path.moveTo(rect.right - rect.width() / 4, rect.top);
		path.lineTo(rect.right, rect.top);
		path.lineTo(rect.right, rect.top + rect.width() / 4);
		path.lineTo(rect.right - rect.width() / 4, rect.top);
		path.close();
		canvas.drawPath(path, paint);
	}

	public static class DayStyle {
		private final static String[] vecStrWeekDayNames = getWeekDayNames();

		private static String[] getWeekDayNames() {
			String[] vec = new String[10];
			vec[Calendar.SUNDAY] = "周日";
			vec[Calendar.MONDAY] = "周一";
			vec[Calendar.TUESDAY] = "周二";
			vec[Calendar.WEDNESDAY] = "周三";
			vec[Calendar.THURSDAY] = "周四";
			vec[Calendar.FRIDAY] = "周五";
			vec[Calendar.SATURDAY] = "周六";
			return vec;
		}

		public static String getWeekDayName(int iDay) {
			return vecStrWeekDayNames[iDay];
		}

		public static int getWeekDay(int index, int iFirstDayOfWeek) {
			int iWeekDay = -1;

			if (iFirstDayOfWeek == Calendar.MONDAY) {
				iWeekDay = index + Calendar.MONDAY;

				if (iWeekDay > Calendar.SATURDAY)
					iWeekDay = Calendar.SUNDAY;
			}

			if (iFirstDayOfWeek == Calendar.SUNDAY) {
				iWeekDay = index + Calendar.SUNDAY;
			}

			return iWeekDay;
		}
	}

	public static class CalendarHeader extends View {
		// 字体大小
		private final static int fTextSize = 22;
		private Paint paint = new Paint();
		private RectF rect = new RectF();
		private int iWeekDay = -1;

		public CalendarHeader(Context context, int iWidth, int iHeight) {
			super(context);
			setLayoutParams(new LayoutParams(iWidth, iHeight));
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			// 设置矩形大小
			rect.set(0, 0, this.getWidth(), this.getHeight());
			rect.inset(1, 1);

			// 绘制日历头部
			drawDayHeader(canvas);
		}

		private void drawDayHeader(Canvas canvas) {
			// 画矩形，并设置矩形画笔的颜色
			paint.setColor(CalendarDemo.Calendar_WeekBgColor);
			canvas.drawRect(rect, paint);

			// 写入日历头部，设置画笔参数
			paint.setTypeface(null);
			paint.setTextSize(fTextSize);
			paint.setAntiAlias(true);
			paint.setFakeBoldText(true);
			paint.setColor(CalendarDemo.Calendar_WeekFontColor);

			// draw day name
			final String sDayName = DayStyle.getWeekDayName(iWeekDay);
			final int iPosX = (int) rect.left + ((int) rect.width() >> 1)
					- ((int) paint.measureText(sDayName) >> 1);
			final int iPosY = (int) (this.getHeight()
					- (this.getHeight() - getTextHeight()) / 2 - paint
					.getFontMetrics().bottom);
			canvas.drawText(sDayName, iPosX, iPosY, paint);
		}

		// 得到字体高度
		private int getTextHeight() {
			return (int) (-paint.ascent() + paint.descent());
		}

		/**
		 * 得到一星期的第几天的文本标记
		 * 
		 * @param iWeekDay
		 */
		public void setData(int iWeekDay) {
			this.iWeekDay = iWeekDay;
		}
	}

	public interface OnCalendarListener {
		public void OnClick(CalendarView item);
	}
}