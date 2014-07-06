package org.lance.widget;

import org.lance.main.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * ���϶����������
 * @author lance
 *
 */
public class DragGridView extends GridView {
	private final String TAG="DragGridView";
	/** ��һ���϶���λ�� */
	private int dragPosition;
	/** ���µ�λ�� */
	private int dropPosition;

	private ViewGroup fromView;
	private Animation AtoB, BtoA, DelDone;
	private int stopCount = 0;
	private int moveNum;
	private int startX;
	private int startY;
	private int padding=0;
	private int statusBarHeight;
	private Rect startDragRect;//��ʼ�϶�������
	private int startDragPosition;//��ʼ�϶�ʱ��item
	private View lastView;//����ƶ�����ͼ
	
	private WindowManager windowManager;
	private WindowManager.LayoutParams windowParams;
	
	private ImageView iv_drag;
	boolean flag = false;
	private OnDragGridViewListener onDragListener;
	private OnGridViewItemListener onGridViewItemListener;//λ�øı������
	

	public void setOnDragListener(OnDragGridViewListener onDragListener) {
		this.onDragListener = onDragListener;
	}

	public void setOnGridViewItemListener(
			OnGridViewItemListener onGridViewItemListener) {
		this.onGridViewItemListener = onGridViewItemListener;
	}


	public DragGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public DragGridView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context){
		windowManager = (WindowManager) getContext().getSystemService(
				Context.WINDOW_SERVICE);// "window"
	}
	
	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {
		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
		Rect rect= new Rect(); 
		((Activity)getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(rect); 
		
		WindowManager.LayoutParams attrs = ((Activity)getContext()).getWindow().getAttributes();
		if((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN){
			statusBarHeight = 0;
		}else{
			statusBarHeight = rect.top;
		}
	}
	public void setLongFlag(boolean temp) {
		flag = temp;
	}

	public boolean setOnItemLongClickListener(final MotionEvent event) {
		this.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Configure.isMove = true;
				//���ڿ��ƶ�״̬ ��ʼ״̬����λ����ͬ
				dragPosition = dropPosition = position;

				if (dragPosition == AdapterView.INVALID_POSITION) {
					return false;
				}
				startDragPosition=position;
				//�����Ƶ�item��Զ����Ч��λ��
				fromView = (ViewGroup) getChildAt(dragPosition - getFirstVisiblePosition());
				int[] locs=new int[2];
				fromView.getLocationInWindow(locs);
				startDragRect=new Rect(locs[0], locs[1], 
						fromView.getLeft()+fromView.getWidth(), fromView.getTop()+fromView.getHeight());
				fromView.destroyDrawingCache();
				fromView.setDrawingCacheEnabled(true);
				fromView.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
				
				windowParams = new WindowManager.LayoutParams();
				windowParams.gravity = Gravity.TOP | Gravity.LEFT;
				
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
				windowParams.x = locs[0]+padding;
				windowParams.y = locs[1]+padding-statusBarHeight;
				windowParams.alpha = 0.7f;
				windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
				windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
				Bitmap src = Bitmap.createBitmap(fromView.getDrawingCache());

				Bitmap dest = Bitmap.createBitmap(src, padding,padding,
						src.getWidth() - padding*2, src.getHeight() - padding*2);
				
				startDrag(dest);
				return false;
			};
		});
		return super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			return setOnItemLongClickListener(ev);
		}
		return super.onInterceptTouchEvent(ev);
	}

	//��ʼ�϶�������λͼ
	private void startDrag(final Bitmap dest) {
		Animation disappear = AnimationUtils.loadAnimation(getContext(),
				R.anim.drag_out);//����ʧ,�����󸡶�����ִ�ж���
		disappear.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				fromView.setVisibility(View.INVISIBLE);
				stopDrag();
				iv_drag = new ImageView(getContext());
				iv_drag.setImageBitmap(dest);
				windowManager.addView(iv_drag, windowParams);
			}
		});
		fromView.startAnimation(disappear);
		if(onDragListener!=null){
			onDragListener.onPage(DEL_APPEARE, moveNum);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (iv_drag != null && dragPosition != AdapterView.INVALID_POSITION) {
			int x=(int)event.getX();
			int y=(int)event.getY();
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				// ��ȡ���µ�ʱ���������Ļ���Ͻǵ�����
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
			case MotionEvent.ACTION_MOVE:
				int newx = (int) event.getRawX();
				int newy = (int) event.getRawY();
				int dx = newx - startX;
				int dy = newy - startY;
				if (iv_drag != null) {
					windowParams.x += dx
							- moveNum * Configure.screenWidth;
					windowParams.y +=dy;
					windowManager.updateViewLayout(iv_drag, windowParams);
				}
				
				// �Գ�ʼ�������¸�ֵ
				startX = (int) event.getRawX();
				startY = (int) event.getRawY();
				onDrag(x, y);
				break;
			case MotionEvent.ACTION_UP:
				stopDrag();//�Ƴ���������
				onDrop(x, y);
				break;
			}
		}
		return super.onTouchEvent(event);
	}

	//�������϶��Ĵ���
	private void onDrag(int x, int y) {
		//�����ж��Ƿ���Ҫɾ������
		if ((x > (Configure.screenWidth / 2 - 100) && x < (Configure.screenWidth / 2 + 100))
				&& (y > Configure.screenHeight - 200)) {
			if(onDragListener!=null){
				onDragListener.onPage(DEL_PREPARE, moveNum);
			}
			return;
		}
		if (Configure.isDelDark) {//ɾ��ȷ��
			if(onDragListener!=null){
				onDragListener.onPage(DEL_CONFIRM, moveNum);
			}
		}
		if (moveNum > 0) {
			if ((x >= (moveNum + 1) * Configure.screenWidth - 20
					* Configure.screenDensity || x <= moveNum
					* Configure.screenWidth)
					&& !Configure.isChangingPage){
				stopCount++;
			} else{
				stopCount = 0;
			}
			if (stopCount > 10) {
				stopCount = 0;
				if (x >= (moveNum + 1) * Configure.screenWidth - 20
						* Configure.screenDensity
						&& Configure.curentPage < Configure.countPages - 1) {
					Configure.isChangingPage = true;
					if(onDragListener!=null){
						onDragListener.onPage(SLIDING, ++Configure.curentPage);
					}
					moveNum++;
				} else if (x <= moveNum * Configure.screenWidth
						&& Configure.curentPage > 0) {
					Configure.isChangingPage = true;
					if(onDragListener!=null){
						onDragListener.onPage(SLIDING, --Configure.curentPage);
					}
					moveNum--;
				}
			}
		} else {
			if ((x >= (moveNum + 1) * Configure.screenWidth - 20
					* Configure.screenDensity || x <= moveNum
					* Configure.screenWidth)
					&& !Configure.isChangingPage){
				stopCount++;
			} else{
				stopCount = 0;
			}
			if (stopCount > 10) {
				stopCount = 0;
				if (x >= (moveNum + 1) * Configure.screenWidth - 20
						* Configure.screenDensity
						&& Configure.curentPage < Configure.countPages - 1) {
					Configure.isChangingPage = true;
					if(onDragListener!=null){
						onDragListener.onPage(SLIDING, ++Configure.curentPage);
					}
					moveNum++;
				} else if (x <= moveNum * Configure.screenWidth
						&& Configure.curentPage > 0) {
					Configure.isChangingPage = true;
					if(onDragListener!=null){
						onDragListener.onPage(SLIDING, --Configure.curentPage);
					}
					moveNum--;
				}
			}
		}
		int curPos=0;
		//�õ��������Ӧ���µ�λ��
		if(x>startDragRect.left && x<startDragRect.right && y>startDragRect.top && y<startDragRect.bottom){
			curPos = startDragPosition;
		}else{
			curPos = pointToPosition(x,y);
		}
		
		if (curPos == AdapterView.INVALID_POSITION||curPos==dropPosition) {
			return;
		}else{
			if(curPos!=dragPosition){
				lastView = getChildAt(curPos - getFirstVisiblePosition());
			}
			if (curPos % 2 == 0) {//�����жϵ���item����˫��item
				if (curPos!=dropPosition){
					//System.out.println("����ǰ:"+curPos+"   "+dragPosition+"   "+dropPosition);
					dragPosition=dropPosition;//
					dropPosition = curPos;
					//System.out.println("���к�:"+curPos+"   "+dragPosition+"   "+dropPosition);
					AnimationListener listener=new AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
							
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							if(onGridViewItemListener!=null){
								onGridViewItemListener.onExchange(dragPosition, dropPosition);
							}
						}
					};
					fromView = (ViewGroup) getChildAt(dragPosition - getFirstVisiblePosition());
					int[] locs=new int[2];
					fromView.getLocationInWindow(locs);
					int[] toLocs=new int[2];
					lastView.getLocationInWindow(toLocs);
					lastView.startAnimation(slidingAnimation(locs[0]-toLocs[0],locs[1]-toLocs[1],listener));
				}
			} else {
				if (curPos!=dropPosition){
					System.out.println("˫��ǰ:"+curPos+"   "+dragPosition+"   "+dropPosition);
					dragPosition=dropPosition;
					dropPosition = curPos;
					System.out.println("˫�к�:"+curPos+"   "+dragPosition+"   "+dropPosition);
					AnimationListener listener=new AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
							
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							if(onGridViewItemListener!=null){
								onGridViewItemListener.onExchange(dragPosition, dropPosition);
							}
						}
					};
					fromView = (ViewGroup) getChildAt(dragPosition - getFirstVisiblePosition());
					int[] locs=new int[2];
					fromView.getLocationInWindow(locs);
					int[] toLocs=new int[2];
					lastView.getLocationInWindow(toLocs);
					lastView.startAnimation(slidingAnimation(locs[0]-toLocs[0],locs[1]-toLocs[1],listener));
				}
			}
			
		}
	}

	//����֮����
	private void onDrop(int x, int y) {
		fromView.setDrawingCacheBackgroundColor(Color.TRANSPARENT);
		Configure.isMove = false;
		if (Configure.isDelDark) {
			DelDone = getDelAnimation(x, y);
			// AnimationUtils.loadAnimation(getContext(), R.anim.del_done);
			DelDone.setAnimationListener(new Animation.AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					Configure.removeItem = dragPosition;
					if(onDragListener!=null){
						onDragListener.onPage(DRAG_UNDO, moveNum);
					}
				}
			});
			fromView.setVisibility(View.VISIBLE);
			fromView.startAnimation(DelDone);
			return;
		}
		if(onDragListener!=null){
			onDragListener.onPage(DEL_DISAPPEARE, moveNum);
		}
		if (moveNum != 0) {
			if(onDragListener!=null){
				onDragListener.onChange(dragPosition, dropPosition, moveNum);
			}
			moveNum = 0;
			return;
		}
		//�ƶ���ǰ�϶�����ͼ
		int[] tolocs=new int[2];
		//��ȡ��ǰ����λ������ͼ������
		getChildAt(dropPosition - getFirstVisiblePosition()).getLocationInWindow(tolocs);
		int[] fromlocs=new int[2];
		getChildAt(dragPosition - getFirstVisiblePosition()).getLocationInWindow(fromlocs);
		System.out.println(dropPosition+"   "+dragPosition);
		View toView = getChildAt(dragPosition - getFirstVisiblePosition());
		toView.startAnimation(slidingAnimation(windowParams.x-fromlocs[0],
				windowParams.x-fromlocs[0]-(windowParams.x-tolocs[0]),
				windowParams.y-fromlocs[1],
				windowParams.y-fromlocs[1]-(windowParams.y-tolocs[1]),new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						if(onGridViewItemListener!=null){
							onGridViewItemListener.onExchangeFinish();
						}
					}
				}));
	}

	//�Ƴ�������ͼ
	private void stopDrag() {
		if (iv_drag != null) {
			windowManager.removeView(iv_drag);
			iv_drag = null;
		}
	}
	
	public static final int SLIDING=0;//����ҳ��
	public static final int DEL_APPEARE=1;//ɾ����ť����
	public static final int DEL_PREPARE=2;//ɾ����ť����
	public static final int DEL_CONFIRM=3;//ɾ����ť�䵭
	public static final int DEL_DISAPPEARE=4;//ɾ����ť��ȥ
	public static final int DRAG_UNDO=5;//����
	/** �϶����� */
	public static interface OnDragGridViewListener {
		//�϶�ҳ�����
		public void onPage(int cases, int page);
		public void onChange(int from, int to, int count);
	}
	public static interface OnGridViewItemListener{
		//λ�øı����
		public void onExchange(int from, int to);
		//λ��ת�����
		public void onExchangeFinish();
	}

	public Animation slidingAnimation(float fromX, float destX,float fromY, float destY,AnimationListener listener){
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation tran = new TranslateAnimation(fromX, destX, fromY,
				destY);
		int duration=500;
		tran.setDuration(duration);
		tran.setFillAfter(true);

		AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
		alpha.setFillAfter(true);
		alpha.setDuration(duration);

		ScaleAnimation scale = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f);
		scale.setFillAfter(true);
		scale.setDuration(duration);

		set.addAnimation(tran);
		set.addAnimation(alpha);
		set.addAnimation(scale);
		set.setAnimationListener(listener);
		return set;
	}
	/** �ӵ�ǰλ�û�����ָ��������λ�� */
	public Animation slidingAnimation(float destX,float destY,AnimationListener listener){
		TranslateAnimation tran = new TranslateAnimation(0, destX, 0,
				destY);
		tran.setDuration(500);
		tran.setFillAfter(true);
		tran.setAnimationListener(listener);
		return tran;
	}
	/** �ƶ���ָ�������� ֻ�������ֶ��� ˮƽ�ʹ�ֱ */
	public Animation transAnimation(float x, float y,AnimationListener listener) {
		TranslateAnimation go = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, x,
				Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, y);
		go.setFillAfter(true);
		go.setDuration(550);
		go.setAnimationListener(listener);
		return go;
	}

	public Animation getDownAnimation(float x, float y) {
		AnimationSet set = new AnimationSet(true);
		TranslateAnimation go = new TranslateAnimation(
				Animation.RELATIVE_TO_SELF, x, Animation.RELATIVE_TO_SELF, x,
				Animation.RELATIVE_TO_SELF, y, Animation.RELATIVE_TO_SELF, y);
		go.setFillAfter(true);
		go.setDuration(550);

		AlphaAnimation alpha = new AlphaAnimation(0.1f, 1.0f);
		alpha.setFillAfter(true);
		alpha.setDuration(550);

		ScaleAnimation scale = new ScaleAnimation(1.2f, 1.0f, 1.2f, 1.0f);
		scale.setFillAfter(true);
		scale.setDuration(550);

		set.addAnimation(go);
		set.addAnimation(alpha);
		set.addAnimation(scale);
		return set;
	}

	public Animation getDelAnimation(int x, int y) {
		AnimationSet set = new AnimationSet(true);
		RotateAnimation rotate = new RotateAnimation(0, 360,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		rotate.setFillAfter(true);
		rotate.setDuration(550);
		AlphaAnimation alpha = new AlphaAnimation(1.0f, 0.0f);
		alpha.setFillAfter(true);
		alpha.setDuration(550);
		
		set.addAnimation(alpha);
		set.addAnimation(rotate);
		return set;
	}
}
