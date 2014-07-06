package asqare.lance.main;

import java.util.Timer;
import java.util.TimerTask;

import org.lance.main.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import asqare.lance.model.Piece;
import asqare.lance.service.GameConf;
import asqare.lance.service.GameService;
import asqare.lance.service.GameServiceImpl;
import asqare.lance.service.LinkInfo;
import asqare.lance.widget.GameView;

/**
 * 游戏UI
 * 
 * @author ganchengkai
 * 
 */
public class AsqareActivity extends Activity implements View.OnTouchListener,
		View.OnClickListener {
	private GameConf config;
	private GameService gameService;
	private GameView gameView;
	private Button startButton;
	private TextView timeTextView;
	private AlertDialog.Builder fail;
	private AlertDialog.Builder win;
	private Timer timer = new Timer();
	// 记录游戏的剩余时间
	private int gameTime;
	// 记录是否处于游戏状态
	private boolean isPlaying;
	private Vibrator vibrator;
	// 记录已经选中的方块
	private Piece selected = null;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0x123:
				timeTextView.setText("剩余时间： " + gameTime);
				gameTime--;
				// 时间小于0, 游戏失败
				if (gameTime < 0) {
					stopTimer();
					isPlaying = false;
					fail.show();
					return;
				}
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.candy_main);
		init();
	}

	private void init() {
		config = new GameConf(8, 9, 0, 0, 100000, this);
		gameView = (GameView) findViewById(R.id.gameView);
		timeTextView = (TextView) findViewById(R.id.timeText);
		startButton = (Button) this.findViewById(R.id.startButton);
		vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		gameService = new GameServiceImpl(this.config);
		gameView.setGameService(gameService);
		startButton.setOnClickListener(this);
		gameView.setOnTouchListener(this);
		fail = new AlertDialog.Builder(this).setTitle("fail!")
				.setMessage("游戏失败!重新开始!").setIcon(R.drawable.fail)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startGame(GameConf.DEFAULT_TIME);
					}
				}).setNegativeButton("取消", null);
		win = new AlertDialog.Builder(this).setTitle("win!")
				.setMessage("游戏胜利!重新开始!").setIcon(R.drawable.win)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						startGame(GameConf.DEFAULT_TIME);
					}
				}).setNegativeButton("取消", null);
	}

	@Override
	protected void onPause() {
		stopTimer();
		super.onPause();
	}

	@Override
	protected void onResume() {
		if (isPlaying) {
			startGame(gameTime);
		}
		super.onResume();
	}

	private void startGame(int gameTime) {
		if (this.timer != null) {
			stopTimer();
		}
		this.gameTime = gameTime;
		if (gameTime == GameConf.DEFAULT_TIME) {
			gameView.startGame();
		}
		isPlaying = true;
		this.timer = new Timer();
		this.timer.schedule(new TimerTask() {
			public void run() {
				handler.sendEmptyMessage(0x123);
			}
		}, 0, 1000);
		// 将选中方块设为null。
		this.selected = null;
	}

	/**
	 * 处理能连接的情况
	 * 
	 * @param linkInfo
	 * @param prePiece
	 *            前一个选中方块
	 * @param currentPiece
	 *            当前选择方块
	 * @param pieces
	 *            系统中还剩的全部方块
	 */
	private void handleSuccessLink(LinkInfo linkInfo, Piece prePiece,
			Piece currentPiece, Piece[][] pieces) {
		this.gameView.setLinkInfo(linkInfo);
		this.gameView.setSelectedPiece(null);
		this.gameView.postInvalidate();
		// 将两个Piece对象从数组中删除
		pieces[prePiece.getIndexX()][prePiece.getIndexY()] = null;
		pieces[currentPiece.getIndexX()][currentPiece.getIndexY()] = null;
		// 将选中的方块设置null。
		this.selected = null;
		this.vibrator.vibrate(50);
		// 判断是否还有剩下的方块, 如果没有, 游戏胜利
		if (!this.gameService.hasPieces()) {
			this.win.show();
			stopTimer();
			isPlaying = false;
		}
	}

	private void stopTimer() {
		this.timer.cancel();
		this.timer = null;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (!isPlaying) {
			return false;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 创建初始化方块
			Piece[][] pieces = gameService.getPieces();
			// 获取用户点击的x座标
			float touchX = event.getX();
			// 获取用户点击的y座标
			float touchY = event.getY();
			// 根据用户触碰的座标得到对应的Piece对象
			Piece currentPiece = gameService.findPiece(touchX, touchY);
			// 如果没有选中任何Piece对象(即鼠标点击的地方没有图片), 不再往下执行
			if (currentPiece == null)
				break;
			// 将gameView中的选中方块设为当前方块
			this.gameView.setSelectedPiece(currentPiece);
			// 表示之前没有选中任何一个Piece
			if (this.selected == null) {
				// 将当前方块设为已选中的方块, 重新将GamePanel绘制, 并不再往下执行
				this.selected = currentPiece;
				this.gameView.postInvalidate();
				break;
			}
			// 表示之前已经选择了一个
			if (this.selected != null) {
				// 在这里就要对currentPiece和prePiece进行判断并进行连接
				LinkInfo linkInfo = this.gameService.link(this.selected,
						currentPiece);
				// 两个Piece不可连, linkInfo为null
				if (linkInfo == null) {
					// 如果连接不成功, 将当前方块设为选中方块
					this.selected = currentPiece;
					this.gameView.postInvalidate();
				} else {
					// 处理成功连接
					handleSuccessLink(linkInfo, this.selected, currentPiece,
							pieces);
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			gameView.postInvalidate();
			break;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		startGame(GameConf.DEFAULT_TIME);
		;
	}
}