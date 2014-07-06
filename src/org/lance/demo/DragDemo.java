package org.lance.demo;

import java.util.ArrayList;

import org.lance.adapters.DateAdapter;
import org.lance.main.R;
import org.lance.widget.Configure;
import org.lance.widget.DragGridView;
import org.lance.widget.DragGridView.OnDragGridViewListener;
import org.lance.widget.ScrollLayout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/** 网格拖动排序 */
public class DragDemo extends BaseActivity implements OnDragGridViewListener{
	
	/** GridView. */
	private RelativeLayout relate;
	private DragGridView gridView;
	private ScrollLayout scrollLay;
	private TextView tv_page;// int oldPage=1;
	private ImageView runImage, delImage;
	LinearLayout.LayoutParams param;
	private String[] add_items={"-ex_1","-ex_2","-ex_3","-ex_4","-ex_5"};

	TranslateAnimation left, right;
	Animation up, down;

	public static final int PAGE_SIZE = 8;
	//launcher包含多个网格视图
	ArrayList<DragGridView> gridviews = new ArrayList<DragGridView>();

	ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();// 全部数据的集合集lists.size()==countpage;
	ArrayList<String> lstDate = new ArrayList<String>();// 每一页的数据

	boolean isClean = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.drag_main);
		for (int i = 0; i < 22; i++) {
			lstDate.add("" + i);
		}

		init();
		initData();

		for (int i = 0; i < Configure.countPages; i++) {
			scrollLay.addView(addGridView(i));
		}

		scrollLay.setPageListener(new ScrollLayout.PageListener() {
			@Override
			public void page(int page) {
				setCurPage(page);
			}
		});

		runImage = (ImageView) findViewById(R.id.run_image);
		runAnimation();
		delImage = (ImageView) findViewById(R.id.dels);
		relate.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				return false;
			}
		});

	}

	public void init() {
		relate = (RelativeLayout) findViewById(R.id.relate);
		scrollLay = (ScrollLayout) findViewById(R.id.views);
		tv_page = (TextView) findViewById(R.id.tv_page);
		tv_page.setText("1");
		Configure.init(this);
		param = new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		param.rightMargin = 100;
		param.leftMargin = 20;
		if (gridView != null) {
			scrollLay.removeAllViews();
		}
	}

	public void initData() {
		Configure.countPages = (int) Math.ceil(lstDate.size()
				/ (float) PAGE_SIZE);

		lists = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < Configure.countPages; i++) {
			lists.add(new ArrayList<String>());
			for (int j = PAGE_SIZE * i; j < (PAGE_SIZE * (i + 1) > lstDate
					.size() ? lstDate.size() : PAGE_SIZE * (i + 1)); j++)
				lists.get(i).add(lstDate.get(j));
		}
		boolean isLast = true;
		for (int i = lists.get(Configure.countPages - 1).size(); i < PAGE_SIZE; i++) {
			if (isLast) {
				lists.get(Configure.countPages - 1).add(null);
				isLast = false;
			} else
				lists.get(Configure.countPages - 1).add("none");
		}
	}

	public void CleanItems() {
		lstDate = new ArrayList<String>();
		for (int i = 0; i < lists.size(); i++) {
			for (int j = 0; j < lists.get(i).size(); j++) {
				if (lists.get(i).get(j) != null
						&& !lists.get(i).get(j).equals("none")) {
					lstDate.add(lists.get(i).get(j).toString());
					System.out.println("-->" + lists.get(i).get(j).toString());
				}
			}
		}
		System.out.println(lstDate.size());
		initData();
		scrollLay.removeAllViews();
		gridviews = new ArrayList<DragGridView>();
		for (int i = 0; i < Configure.countPages; i++) {
			scrollLay.addView(addGridView(i));
		}
		isClean = false;
		scrollLay.snapToScreen(0);
	}

	public int getFristNonePosition(ArrayList<String> array) {
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i) != null && array.get(i).toString().equals("none")) {
				return i;
			}
		}
		return -1;
	}

	public int getFristNullPosition(ArrayList<String> array) {
		for (int i = 0; i < array.size(); i++) {
			if (array.get(i) == null) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * 添加网格视图
	 * @param i
	 * @return
	 */
	public LinearLayout addGridView(int index) {
		LinearLayout lay = new LinearLayout(this);
		gridView = new DragGridView(this);
		DateAdapter dateAdapter=new DateAdapter(this, lists.get(index));
		gridView.setAdapter(dateAdapter);
		gridView.setOnGridViewItemListener(dateAdapter);
		gridView.setSelector(R.drawable.drag_grid_light);
		gridView.setOnDragListener(this);
		gridView.setNumColumns(2);
		gridView.setHorizontalSpacing(10);
		gridView.setVerticalSpacing(10);
		final int ii = index;
		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				if (lists.get(ii).get(arg2) == null) {
					new AlertDialog.Builder(DragDemo.this)
							.setTitle("添加")
							.setItems(add_items,
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											lists.get(ii).add(arg2,
													add_items[which]);
											lists.get(ii).remove(arg2 + 1);

											if (getFristNonePosition(lists
													.get(ii)) > 0
													&& getFristNullPosition(lists
															.get(ii)) < 0) {
												lists.get(ii)
														.set(getFristNonePosition(lists
																.get(ii)), null);
											}
											if (getFristNonePosition(lists
													.get(ii)) < 0
													&& getFristNullPosition(lists
															.get(ii)) < 0) {
												System.out.println("===");
												if (ii == Configure.countPages - 1
														|| (getFristNullPosition(lists.get(lists
																.size() - 1)) < 0 && getFristNonePosition(lists.get(lists
																.size() - 1)) < 0)) {
													lists.add(new ArrayList<String>());
													lists.get(lists.size() - 1)
															.add(null);
													for (int i = 1; i < PAGE_SIZE; i++)
														lists.get(
																lists.size() - 1)
																.add("none");

													scrollLay
															.addView(addGridView(Configure.countPages));
													Configure.countPages++;
												} else if (getFristNonePosition(lists
														.get(lists.size() - 1)) > 0
														&& getFristNullPosition(lists.get(lists
																.size() - 1)) < 0) {
													lists.get(lists.size() - 1)
															.set(getFristNonePosition(lists
																	.get(lists
																			.size() - 1)),
																	null);
													((DateAdapter) ((gridviews
															.get(lists.size() - 1))
															.getAdapter()))
															.notifyDataSetChanged();
												}
											}
											((DateAdapter) ((gridviews.get(ii))
													.getAdapter()))
													.notifyDataSetChanged();
										}
									})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									}).show();
				}
			}
		});
		
		gridviews.add(gridView);
		lay.addView(gridviews.get(index), param);
		return lay;
	}

	private final int runTime=25000;
	//背景动画
	public void runAnimation() {
		down = AnimationUtils.loadAnimation(this,
				R.anim.drag_del_down);
		up = AnimationUtils.loadAnimation(this, R.anim.drag_del_up);
		down.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				delImage.setVisibility(View.GONE);
			}
		});

		right = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0f,
				Animation.RELATIVE_TO_PARENT, -1f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f);
		left = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, -1f,
				Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT,
				0f, Animation.RELATIVE_TO_PARENT, 0f);
		right.setDuration(runTime);
		left.setDuration(runTime);
		right.setFillAfter(true);
		left.setFillAfter(true);

		right.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				runImage.startAnimation(left);
			}
		});
		left.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				runImage.startAnimation(right);
			}
		});
		runImage.startAnimation(right);
	}

	public void setCurPage(final int page) {
		Animation a = AnimationUtils.loadAnimation(this,
				R.anim.drag_scale_in);
		a.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				tv_page.setText((page + 1) + "");
				tv_page.startAnimation(AnimationUtils.loadAnimation(DragDemo.this, R.anim.drag_scale_out));
			}
		});
		tv_page.startAnimation(a);

	}

	@Override
	public void onPage(int cases, int page) {
		switch (cases) {
		case DragGridView.SLIDING:// 这里处理交换页面的逻辑
			scrollLay.snapToScreen(page);
			setCurPage(page);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Configure.isChangingPage = false;
				}
			}, 800);
			break;
		case DragGridView.DEL_APPEARE:// 删除按钮上来
			delImage.setBackgroundResource(R.drawable.drag_del);
			delImage.setVisibility(0);
			delImage.startAnimation(up);
			break;
		case DragGridView.DEL_PREPARE:// 删除按钮变深
			delImage.setBackgroundResource(R.drawable.drag_del_check);
			Configure.isDelDark = true;
			break;
		case DragGridView.DEL_CONFIRM:// 删除按钮变淡
			delImage.setBackgroundResource(R.drawable.drag_del);
			Configure.isDelDark = false;
			break;
		case DragGridView.DEL_DISAPPEARE:// 删除按钮下去
			delImage.startAnimation(down);
			break;
		case DragGridView.DRAG_UNDO:// 松手动作
			delImage.startAnimation(down);
			// Configure.isDelRunning = false;
			lists.get(Configure.curentPage).add(Configure.removeItem,null);
			lists.get(Configure.curentPage).remove(Configure.removeItem + 1);
			((DateAdapter) ((gridviews.get(Configure.curentPage)).getAdapter())).notifyDataSetChanged();
			break;
		}
	}

	@Override
	public void onChange(int from, int to, int count) {
		//计算当前页面
		String toString = lists.get(Configure.curentPage - count).get(from);
		
		lists.get(Configure.curentPage - count).add(from,lists.get(Configure.curentPage).get(to));
		lists.get(Configure.curentPage - count).remove(from + 1);
		lists.get(Configure.curentPage).add(to, toString);
		lists.get(Configure.curentPage).remove(to + 1);

		((DateAdapter) ((gridviews.get(Configure.curentPage - count)).getAdapter())).notifyDataSetChanged();
		((DateAdapter) ((gridviews.get(Configure.curentPage)).getAdapter())).notifyDataSetChanged();
	}

}
