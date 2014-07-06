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
 * �����ؼ���ͼ
 * 
 * @author lance
 * 
 */
public class CalendarView extends View {
	// �����С
	private static final int fTextSize = 28;

	// ����Ԫ��
	// ����������
	private OnCalendarListener listener = null;
	private Paint paint = new Paint();
	// ������---��Rect����ֻ�Ǿ��Ȳ�һ��
	private RectF rect = new RectF();
	private String sDate = "";

	// ��ǰ����
	private int year = 0;
	private int month = 0;
	private int date = 0;

	// ��������
	private boolean bSelected = false;
	private boolean bIsActiveMonth = false;
	private boolean bToday = false;
	private boolean bTouchedDown = false;
	private boolean bHoliday = false;
	private boolean hasRecord = false;
	// ����ִ��ʱ��
	public static int ANIM_ALPHA_DURATION = 100;

	/**
	 * ���������ؼ�
	 * 
	 * @param context
	 * @param width
	 *            �ؼ���
	 * @param height
	 *            �ؼ���
	 */
	public CalendarView(Context context, int width, int height) {
		super(context);
		setFocusable(true);
		setLayoutParams(new LayoutParams(width, height));
	}

	/**
	 * ��ȡ��ǰʱ�����������
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

	// ���ñ���ֵ
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
	 * ������������
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
	 * ���������е�����
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
		// �����ı�
		canvas.drawText(sDate, iPosX, iPosY, paint);
		// �����»���
		paint.setUnderlineText(false);
	}

	// �õ�����߶�
	private int getTextHeight() {
		return (int) (-paint.ascent() + paint.descent());
	}

	// �����������ز�ͬ��ɫֵ
	public static int getColorBkg(boolean bHoliday, boolean bToday) {
		if (bToday)
			return CalendarDemo.isToday_BgColor;
		// if (bHoliday) //������ĩ�����ⱳ��ɫ����ȥ��ע��
		// return Calendar_TestActivity.isHoliday_BgColor;
		return CalendarDemo.Calendar_DayBgColor;
	}

	// �����Ƿ�ѡ��
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

	// ����¼�
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

	// ����¼�
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean bResult = super.onKeyDown(keyCode, event);
		if ((keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
				|| (keyCode == KeyEvent.KEYCODE_ENTER)) {
			dolistener();
		}
		return bResult;
	}

	// ��͸���Ƚ���
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
			vec[Calendar.SUNDAY] = "����";
			vec[Calendar.MONDAY] = "��һ";
			vec[Calendar.TUESDAY] = "�ܶ�";
			vec[Calendar.WEDNESDAY] = "����";
			vec[Calendar.THURSDAY] = "����";
			vec[Calendar.FRIDAY] = "����";
			vec[Calendar.SATURDAY] = "����";
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
		// �����С
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

			// ���þ��δ�С
			rect.set(0, 0, this.getWidth(), this.getHeight());
			rect.inset(1, 1);

			// ��������ͷ��
			drawDayHeader(canvas);
		}

		private void drawDayHeader(Canvas canvas) {
			// �����Σ������þ��λ��ʵ���ɫ
			paint.setColor(CalendarDemo.Calendar_WeekBgColor);
			canvas.drawRect(rect, paint);

			// д������ͷ�������û��ʲ���
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

		// �õ�����߶�
		private int getTextHeight() {
			return (int) (-paint.ascent() + paint.descent());
		}

		/**
		 * �õ�һ���ڵĵڼ�����ı����
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