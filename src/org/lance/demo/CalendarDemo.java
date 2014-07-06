package org.lance.demo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import org.lance.main.R;
import org.lance.widget.CalendarView;
import org.lance.widget.CalendarView.CalendarHeader;
import org.lance.widget.CalendarView.DayStyle;

/**
 * 日历控件实现
 * 
 * @author chengkai
 * 
 */
public class CalendarDemo extends BaseActivity {
	// 生成日历，外层容器
	private LinearLayout layContent = null;
	private ArrayList<CalendarView> days = new ArrayList<CalendarView>();

	// 日期变量
	public static Calendar calStartDate = Calendar.getInstance();
	private Calendar calToday = Calendar.getInstance();
	private Calendar calCalendar = Calendar.getInstance();
	private Calendar calSelected = Calendar.getInstance();

	// 当前操作日期
	private int curMonth = 0;
	private int curYear = 0;
	private int iFirstDayOfWeek = Calendar.MONDAY;

	private int Calendar_Width = 0;
	private int Cell_Width = 0;

	// 页面控件
	TextView Top_Date = null;
	Button btn_pre_month = null;
	Button btn_next_month = null;
	TextView arrange_text = null;
	LinearLayout mainLayout = null;
	LinearLayout arrange_layout = null;

	// 数据源
	ArrayList<String> Calendar_Source = null;
	Hashtable<Integer, Integer> calendar_Hashtable = new Hashtable<Integer, Integer>();
	Boolean[] flag = null;
	Calendar startDate = null;
	Calendar endDate = null;
	int dayvalue = -1;

	public static int Calendar_WeekBgColor = 0;
	public static int Calendar_DayBgColor = 0;
	public static int isHoliday_BgColor = 0;
	public static int unPresentMonth_FontColor = 0;
	public static int isPresentMonth_FontColor = 0;
	public static int isToday_BgColor = 0;
	public static int special_Reminder = 0;
	public static int common_Reminder = 0;
	public static int Calendar_WeekFontColor = 0;

	String UserName = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// 获得屏幕宽和高，并計算出屏幕寬度分七等份的大小
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		int screenWidth = display.getWidth();
		Calendar_Width = screenWidth;
		Cell_Width = Calendar_Width / 7 + 1;

		// 制定布局文件，并设置属性
		mainLayout = (LinearLayout) getLayoutInflater().inflate(
				R.layout.calendar_main, null);
		// mainLayout.setPadding(2, 0, 2, 0);
		setContentView(mainLayout);

		// 声明控件，并绑定事件
		Top_Date = (TextView) findViewById(R.id.Top_Date);
		btn_pre_month = (Button) findViewById(R.id.btn_pre_month);
		btn_next_month = (Button) findViewById(R.id.btn_next_month);
		btn_pre_month.setOnClickListener(new Pre_MonthOnClickListener());
		btn_next_month.setOnClickListener(new Next_MonthOnClickListener());

		// 计算本月日历中的第一天(一般是上月的某天)，并更新日历
		calStartDate = getCalendarStartDate();
		mainLayout.addView(generateCalendarMain());
		CalendarView daySelected = updateCalendar();

		if (daySelected != null)
			daySelected.requestFocus();

		LinearLayout.LayoutParams Param1 = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);

		ScrollView view = new ScrollView(this);
		arrange_layout = createLayout(LinearLayout.VERTICAL);
		arrange_layout.setPadding(5, 2, 0, 0);
		arrange_text = new TextView(this);
		mainLayout.setBackgroundColor(Color.WHITE);
		arrange_text.setTextColor(Color.BLACK);
		arrange_text.setTextSize(18);
		arrange_layout.addView(arrange_text);

		startDate = getStartDate();
		calToday = getTodayDate();

		endDate = getEndDate(startDate);
		view.addView(arrange_layout, Param1);
		mainLayout.addView(view);

		// 新建线程
		new Thread() {
			@Override
			public void run() {
				int day = GetNumFromDate(calToday, startDate);

				if (calendar_Hashtable != null
						&& calendar_Hashtable.containsKey(day)) {
					dayvalue = calendar_Hashtable.get(day);
				}
			}

		}.start();

		Calendar_WeekBgColor = this.getResources().getColor(
				R.color.Calendar_WeekBgColor);
		Calendar_DayBgColor = this.getResources().getColor(
				R.color.Calendar_DayBgColor);
		isHoliday_BgColor = this.getResources().getColor(
				R.color.isHoliday_BgColor);
		unPresentMonth_FontColor = this.getResources().getColor(
				R.color.unPresentMonth_FontColor);
		isPresentMonth_FontColor = this.getResources().getColor(
				R.color.isPresentMonth_FontColor);
		isToday_BgColor = this.getResources().getColor(R.color.isToday_BgColor);
		special_Reminder = this.getResources()
				.getColor(R.color.specialReminder);
		common_Reminder = this.getResources().getColor(R.color.commonReminder);
		Calendar_WeekFontColor = this.getResources().getColor(
				R.color.Calendar_WeekFontColor);
	}

	protected String GetDateShortString(Calendar date) {
		String returnString = date.get(Calendar.YEAR) + "/";
		returnString += date.get(Calendar.MONTH) + 1 + "/";
		returnString += date.get(Calendar.DAY_OF_MONTH);

		return returnString;
	}

	// 得到当天在日历中的序号
	private int GetNumFromDate(Calendar now, Calendar returnDate) {
		Calendar cNow = (Calendar) now.clone();
		Calendar cReturnDate = (Calendar) returnDate.clone();
		setTimeToMidnight(cNow);
		setTimeToMidnight(cReturnDate);

		long todayMs = cNow.getTimeInMillis();
		long returnMs = cReturnDate.getTimeInMillis();
		long intervalMs = todayMs - returnMs;
		int index = millisecondsToDays(intervalMs);

		return index;
	}

	private int millisecondsToDays(long intervalMs) {
		return Math.round((intervalMs / (1000 * 86400)));
	}

	private void setTimeToMidnight(Calendar calendar) {
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * 创建线性布局
	 * 
	 * @param iOrientation
	 * @return
	 */
	private LinearLayout createLayout(int iOrientation) {
		LinearLayout lay = new LinearLayout(this);
		lay.setLayoutParams(new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		lay.setOrientation(iOrientation);

		return lay;
	}

	/**
	 * 创建日历头部
	 * 
	 * @return
	 */
	private View generateCalendarHeader() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		for (int iDay = 0; iDay < 7; iDay++) {
			CalendarHeader day = new CalendarHeader(this, Cell_Width, 35);
			final int iWeekDay = DayStyle.getWeekDay(iDay, iFirstDayOfWeek);
			day.setData(iWeekDay);
			layRow.addView(day);
		}
		return layRow;
	}

	/**
	 * 创建日历的主体部分
	 * 
	 * @return
	 */
	private View generateCalendarMain() {
		layContent = createLayout(LinearLayout.VERTICAL);
		layContent.setBackgroundColor(Color.argb(255, 105, 105, 103));
		layContent.addView(generateCalendarHeader());
		days.clear();
		for (int iRow = 0; iRow < 6; iRow++) {
			layContent.addView(generateCalendarRow());
		}
		return layContent;
	}

	// 生成日历中的一行，仅画矩形
	private View generateCalendarRow() {
		LinearLayout layRow = createLayout(LinearLayout.HORIZONTAL);
		for (int iDay = 0; iDay < 7; iDay++) {
			CalendarView dayCell = new CalendarView(this, Cell_Width,
					Cell_Width);
			dayCell.setListener(dayListener);
			days.add(dayCell);
			layRow.addView(dayCell);
		}
		return layRow;
	}

	// 设置当天日期和被选中日期
	private Calendar getCalendarStartDate() {
		calToday.setTimeInMillis(System.currentTimeMillis());
		calToday.setFirstDayOfWeek(iFirstDayOfWeek);
		if (calSelected.getTimeInMillis() == 0) {
			calStartDate.setTimeInMillis(System.currentTimeMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		} else {
			calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
			calStartDate.setFirstDayOfWeek(iFirstDayOfWeek);
		}
		UpdateStartDateForMonth();
		return calStartDate;
	}

	// 由于本日历上的日期都是从周一开始的，此方法可推算出上月在本月日历中显示的天数
	private void UpdateStartDateForMonth() {
		curMonth = calStartDate.get(Calendar.MONTH);
		curYear = calStartDate.get(Calendar.YEAR);
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.HOUR_OF_DAY, 0);
		calStartDate.set(Calendar.MINUTE, 0);
		calStartDate.set(Calendar.SECOND, 0);
		update();
		int iDay = 0;
		int iStartDay = iFirstDayOfWeek;

		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}

		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}

		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
	}

	// 更新日历
	private CalendarView updateCalendar() {
		CalendarView daySelected = null;
		boolean bSelected = false;
		final boolean bIsSelection = (calSelected.getTimeInMillis() != 0);
		final int iSelectedYear = calSelected.get(Calendar.YEAR);
		final int iSelectedMonth = calSelected.get(Calendar.MONTH);
		final int iSelectedDay = calSelected.get(Calendar.DAY_OF_MONTH);
		calCalendar.setTimeInMillis(calStartDate.getTimeInMillis());

		for (int i = 0; i < days.size(); i++) {
			final int iYear = calCalendar.get(Calendar.YEAR);
			final int iMonth = calCalendar.get(Calendar.MONTH);
			final int iDay = calCalendar.get(Calendar.DAY_OF_MONTH);
			final int iDayOfWeek = calCalendar.get(Calendar.DAY_OF_WEEK);
			CalendarView dayCell = days.get(i);

			// 判断是否当天
			boolean bToday = false;

			if (calToday.get(Calendar.YEAR) == iYear) {
				if (calToday.get(Calendar.MONTH) == iMonth) {
					if (calToday.get(Calendar.DAY_OF_MONTH) == iDay) {
						bToday = true;
					}
				}
			}

			// check holiday
			boolean bHoliday = false;
			if ((iDayOfWeek == Calendar.SATURDAY)
					|| (iDayOfWeek == Calendar.SUNDAY))
				bHoliday = true;
			if ((iMonth == Calendar.JANUARY) && (iDay == 1))
				bHoliday = true;

			// 是否被选中
			bSelected = false;

			if (bIsSelection)
				if ((iSelectedDay == iDay) && (iSelectedMonth == iMonth)
						&& (iSelectedYear == iYear)) {
					bSelected = true;
				}

			dayCell.setSelected(bSelected);

			// 是否有记录
			boolean hasRecord = false;

			if (flag != null && flag[i] == true && calendar_Hashtable != null
					&& calendar_Hashtable.containsKey(i)) {
				// hasRecord = flag[i];
				hasRecord = Calendar_Source.get(calendar_Hashtable.get(i))
						.contains(UserName);
			}

			if (bSelected)
				daySelected = dayCell;

			dayCell.setData(iYear, iMonth, iDay, bToday, bHoliday, curMonth,
					hasRecord);

			calCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}

		layContent.invalidate();

		return daySelected;
	}

	// 更新日历标题上显示的年月
	private void update() {
		String date = calStartDate.get(Calendar.YEAR) + "年"
				+ (calStartDate.get(Calendar.MONTH) + 1) + "月";
		Top_Date.setText(date);
	}

	// 点击上月按钮，触发事件
	class Pre_MonthOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			arrange_text.setText("");
			calSelected.setTimeInMillis(0);
			curMonth--;

			if (curMonth == -1) {
				curMonth = 11;
				curYear--;
			}

			calStartDate.set(Calendar.DAY_OF_MONTH, 1);
			calStartDate.set(Calendar.MONTH, curMonth);
			calStartDate.set(Calendar.YEAR, curYear);
			calStartDate.set(Calendar.HOUR_OF_DAY, 0);
			calStartDate.set(Calendar.MINUTE, 0);
			calStartDate.set(Calendar.SECOND, 0);
			calStartDate.set(Calendar.MILLISECOND, 0);
			UpdateStartDateForMonth();

			startDate = (Calendar) calStartDate.clone();
			endDate = getEndDate(startDate);

			// 新建线程
			new Thread() {
				@Override
				public void run() {

					int day = GetNumFromDate(calToday, startDate);

					if (calendar_Hashtable != null
							&& calendar_Hashtable.containsKey(day)) {
						dayvalue = calendar_Hashtable.get(day);
					}
				}
			}.start();

			updateCalendar();
		}

	}

	// 点击下月按钮，触发事件
	class Next_MonthOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			arrange_text.setText("");
			calSelected.setTimeInMillis(0);
			curMonth++;

			if (curMonth == 12) {
				curMonth = 0;
				curYear++;
			}

			calStartDate.set(Calendar.DAY_OF_MONTH, 1);
			calStartDate.set(Calendar.MONTH, curMonth);
			calStartDate.set(Calendar.YEAR, curYear);
			UpdateStartDateForMonth();

			startDate = (Calendar) calStartDate.clone();
			endDate = getEndDate(startDate);

			// 新建线程
			new Thread() {
				@Override
				public void run() {
					int day = 5;
					if (calendar_Hashtable != null
							&& calendar_Hashtable.containsKey(day)) {
						dayvalue = calendar_Hashtable.get(day);
					}
				}
			}.start();
			updateCalendar();
		}
	}

	// 点击日历，触发事件
	private CalendarView.OnCalendarListener dayListener = new CalendarView.OnCalendarListener() {
		public void OnClick(CalendarView item) {
			calSelected
					.setTimeInMillis(item.getCurCalendar().getTimeInMillis());
			int day = GetNumFromDate(calSelected, startDate);

			if (calendar_Hashtable != null
					&& calendar_Hashtable.containsKey(day)) {
				arrange_text.setText(Calendar_Source.get(calendar_Hashtable
						.get(day)));
			} else {
				arrange_text.setText("暂无数据记录");
			}

			item.setSelected(true);
			updateCalendar();
		}
	};

	public Calendar getTodayDate() {
		Calendar cal_Today = Calendar.getInstance();
		cal_Today.set(Calendar.HOUR_OF_DAY, 0);
		cal_Today.set(Calendar.MINUTE, 0);
		cal_Today.set(Calendar.SECOND, 0);
		cal_Today.setFirstDayOfWeek(Calendar.MONDAY);

		return cal_Today;
	}

	/**
	 * 得到当前日历中的第一天
	 */
	public Calendar getStartDate() {
		int iDay = 0;
		Calendar now = Calendar.getInstance();
		now.set(Calendar.DAY_OF_MONTH, 1);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		// 设置一周中的第一天为星期一
		now.setFirstDayOfWeek(Calendar.MONDAY);
		iDay = now.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
		if (iDay < 0) {
			iDay = 6;
		}
		now.add(Calendar.DAY_OF_WEEK, -iDay);
		return now;
	}

	public Calendar getEndDate(Calendar startDate) {
		// Calendar end = GetStartDate(enddate);
		Calendar endDate = Calendar.getInstance();
		endDate = (Calendar) startDate.clone();
		endDate.add(Calendar.DAY_OF_MONTH, 41);
		return endDate;
	}
}