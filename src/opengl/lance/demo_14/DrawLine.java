package opengl.lance.demo_14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

import android.view.SurfaceHolder;

public class DrawLine {
	MapSurfaceView mSurfaceView;
	// 需要搜索的地图---该地图可以看成是一个立体的三维数组；但只有一层
	int[][] map = MapList.map[0];
	// 出发点
	int[] source = MapList.source;
	// 搜索目标点
	int[] target = MapList.targetA[0];
	// 使用何种算法
	int algorithmId = 0;

	// 保存搜索过程
	ArrayList<int[][]> searchProcess = new ArrayList<int[][]>();
	// 深度优先使用的历史栈
	Stack<int[][]> stack = new Stack<int[][]>();
	// 广度优先使用队列
	LinkedList<int[][]> queue = new LinkedList<int[][]>();
	// A*优先级队列
	PriorityQueue<int[][]> astartQueue = new PriorityQueue<int[][]>(100,
			new StarComparator(this));
	// 结果路径记录
	HashMap<String, int[][]> hm = new HashMap<String, int[][]>();
	// 0~未去过、1~已去过
	int[][] visited = new int[19][19];
	// 记录路径长度
	int[][] length = new int[19][19];
	// 记录每个点的最短距离
	HashMap<String, ArrayList<int[][]>> hmPath = new HashMap<String, ArrayList<int[][]>>();
	// 是否找到路径
	boolean pathFlag = false;
	// 每次搜索的时间间隔
	int timeSpan = 10;
	SurfaceHolder holder;

	int[][] sequence = { { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 0 }, { -1, 1 },
			{ -1, -1 }, { 1, -1 }, { 1, 1 }, };
	// 记录搜索使用的步骤
	int tempCount;
	final int DFS_COUNT = 1;
	final int BFS_COUNT = 2;
	final int BFSASTAR_COUNT = 3;
	final int DIJKSTAR_COUNT = 4;
	final int DIJKSTARSTAR_COUNT = 5;

	public DrawLine(MapSurfaceView mSurfaceView, SurfaceHolder holder) {
		this.mSurfaceView = mSurfaceView;
		this.holder = holder;
	}

	public void clearState() {
		searchProcess.clear();
		stack.clear();
		queue.clear();
		astartQueue.clear();
		hm.clear();
		visited = new int[19][19];
		pathFlag = false;
		hmPath.clear();
		// 设置画笔宽度
		mSurfaceView.paint.setStrokeWidth(0);
		for (int i = 0; i < length.length; i++) {
			for (int j = 0; j < length[0].length; j++) {
				length[i][j] = 9999;
			}
		}
	}

	// 运行搜索方法
	public void runAlgorithm() {
		clearState();
		switch (algorithmId) {
		case 0:
			DFS();
			break;
		case 1:
			BFS();
			break;
		case 2:
			BFSAStar();
			break;
		case 3:
			Dijkstra();
			break;
		case 4:
			DijkstarAStar();
			break;
		}
	}

	// 深度优先搜索法
	public void DFS() {
		new Thread() {
			public void run() {
				// 执行线程的标志
				boolean flag = true;
				// 出发点坐标
				int[][] start = { { source[0], source[1] },
						{ source[0], source[1] }, };
				stack.push(start);
				// 计算使用了多少步到达目标点
				int count = 0;
				while (flag) {
					// 取出当前路径边
					int[][] currentEdge = stack.pop();
					// 取出去过的目标点
					int[] tempTarget = currentEdge[1];
					// 判断目的点是否去过；如果去过则跳过循环
					if (visited[tempTarget[1]][tempTarget[0]] == 1) {
						continue;
					}
					// 如果没有去过则访问t它；计数器加1
					count++;
					// 标记已访问
					visited[tempTarget[1]][tempTarget[0]] = 1;
					// 将临时边加入搜索过程
					searchProcess.add(currentEdge);
					hm.put(tempTarget[0] + ":" + tempTarget[1], new int[][] {
							currentEdge[1], currentEdge[0] });
					// 绘制画布
					mSurfaceView.repaint(holder);
					try {
						Thread.sleep(timeSpan);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 如果到达目的点---结束线程
					if (tempTarget[0] == target[0]
							&& tempTarget[1] == target[1]) {
						break;
					}
					// 将所有可能的边入栈---取出边的节点---列号~行号
					int currCol = tempTarget[0];
					int currRow = tempTarget[1];
					for (int[] rc : sequence) {
						int i = rc[1];
						int j = rc[0];
						if (i == 0 && j == 0) {
							continue;
						}
						if (currRow + i >= 0 && currRow + i < 19
								&& currCol + j >= 0 && currCol + j < 19
								&& map[currRow + i][currCol + j] != 1) {
							int[][] tempEdge = {
									{ tempTarget[0], tempTarget[1] },
									{ currCol + j, currRow + i }, };
							stack.push(tempEdge);
						}
					}
				}
				pathFlag = true;// 已找到目标点
				mSurfaceView.repaint(holder);
				tempCount = count;
				// 发送消息显示结果
				mSurfaceView.mActivity.hd.sendEmptyMessage(DFS_COUNT);
				mSurfaceView.mActivity.button.setClickable(true);
			}
		}.start();
	}

	// 广度优先算法
	public void BFS() {
		new Thread() {
			public void run() {
				int count = 0;
				boolean flag = true;
				int[][] start = { { source[0], source[1] },
						{ source[0], source[1] }, };
				// 将开始点加入队列的末尾
				queue.offer(start);
				while (flag) {
					// 去除头部中的数据---二维数组是两个点代表一条边
					int[][] currentEdge = queue.poll();
					// 取出当前边的目标点---代表一个点
					int[] tempTarget = currentEdge[1];
					// 判断当前点是否去过
					if (visited[tempTarget[1]][tempTarget[0]] == 1) {
						continue;
					}
					count++;
					// 标记已访问过
					visited[tempTarget[1]][tempTarget[0]] = 1;
					searchProcess.add(currentEdge);
					// 记录父节点
					hm.put(tempTarget[0] + ":" + tempTarget[1], new int[][] {
							currentEdge[1], currentEdge[0], });
					mSurfaceView.repaint(holder);
					try {
						Thread.sleep(timeSpan);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// 如果为目标点结束循环
					if (tempTarget[0] == target[0]
							&& tempTarget[1] == target[1]) {
						break;
					}
					int currCol = tempTarget[0];
					int currRow = tempTarget[1];

					for (int[] rc : sequence) {
						int i = rc[1];
						int j = rc[0];
						if (i == 0 && j == 0) {
							continue;
						}
						if (currRow + i >= 0 && currRow + i < 19
								&& currCol + j >= 0 && currCol + j < 19
								&& map[currRow + i][currCol + j] != 1) {
							int[][] tempEdge = {
									{ tempTarget[0], tempTarget[1] },
									{ currCol + j, currRow + i }, };
							queue.offer(tempEdge);
						}
					}
				}
				pathFlag = true;
				mSurfaceView.repaint(holder);
				tempCount = count;
				mSurfaceView.mActivity.hd.sendEmptyMessage(BFS_COUNT);
				mSurfaceView.mActivity.button.setClickable(true);
			}
		}.start();
	}

	// 广度优先A*搜索法
	public void BFSAStar() {
		new Thread() {
			public void run() {
				int count = 0;
				boolean flag = true;
				int[][] start = { { source[0], source[1] },
						{ source[0], source[1] }, };
				astartQueue.offer(start);
				while (flag) {
					int[][] currentEdge = astartQueue.poll();
					int[] tempTarget = currentEdge[1];
					if (visited[tempTarget[1]][tempTarget[0]] == 1) {
						continue;
					}
					count++;
					visited[tempTarget[1]][tempTarget[0]] = 1;
					searchProcess.add(currentEdge);
					hm.put(tempTarget[0] + ":" + tempTarget[1], new int[][] {
							currentEdge[1], currentEdge[0] });
					mSurfaceView.repaint(holder);
					try {
						Thread.sleep(timeSpan);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (tempTarget[0] == target[0]
							&& tempTarget[1] == target[1]) {
						break;
					}
					int currCol = tempTarget[0];
					int currRow = tempTarget[1];

					for (int[] rc : sequence) {
						int i = rc[1];
						int j = rc[0];
						if (i == 0 && j == 0) {
							continue;
						}
						if (currRow + i >= 0 && currRow + i < 19
								&& currCol + j >= 0 && currCol + j < 19
								&& map[currRow + i][currCol + j] != 1) {
							int[][] tempEdge = {
									{ tempTarget[0], tempTarget[1] },
									{ currCol + j, currRow + i }, };
							astartQueue.offer(tempEdge);
						}
					}
				}
				pathFlag = true;
				mSurfaceView.repaint(holder);
				tempCount = count;
				mSurfaceView.mActivity.hd.sendEmptyMessage(BFSASTAR_COUNT);
				mSurfaceView.mActivity.button.setClickable(true);
			}
		}.start();
	}

	public void Dijkstra() {
		new Thread() {
			public void run() {
				int count = 0;
				boolean flag = true;
				int[] start = { source[0], source[1], };
				visited[source[1]][source[0]] = 1;
				for (int[] rowcol : sequence) {
					int trow = start[1] + rowcol[1];
					int tcol = start[0] + rowcol[0];
					if (trow < 0 || trow > 18 || tcol < 0 || tcol > 18) {
						continue;
					}
					if (map[trow][tcol] != 0) {
						continue;
					}
					length[trow][tcol] = 1;
					String key = tcol + ":" + trow;
					ArrayList<int[][]> al = new ArrayList<int[][]>();
					al.add(new int[][] { { start[0], start[1] },
							{ tcol, trow }, });
					hmPath.put(key, al);
					searchProcess.add(new int[][] { { start[0], start[1] },
							{ tcol, trow } });
					count++;
				}
				mSurfaceView.repaint(holder);
				outer: while (flag) {
					int[] k = new int[2];
					int minLen = 9999;
					for (int i = 0; i < visited.length; i++) {
						for (int j = 0; j < visited[0].length; j++) {
							if (visited[i][j] == 0) {
								if (minLen > length[i][j]) {
									minLen = length[i][j];
									k[0] = j;
									k[1] = i;
								}
							}
						}
					}
					visited[k[1]][k[0]] = 1;
					mSurfaceView.repaint(holder);
					int dk = length[k[1]][k[0]];
					ArrayList<int[][]> al = hmPath.get(k[0] + ":" + k[1]);
					for (int[] rowcol : sequence) {
						int trow = k[1] + rowcol[1];
						int tcol = k[0] + rowcol[0];
						if (trow < 0 || trow > 18 || tcol < 0 || tcol > 18) {
							continue;
						}
						if (map[trow][tcol] != 0) {
							continue;
						}
						int dj = length[trow][tcol];
						int dkPluskj = dk + 1;
						if (dj > dkPluskj) {
							String key = tcol + ":" + trow;
							ArrayList<int[][]> tempal = (ArrayList<int[][]>) al
									.clone();
							tempal.add(new int[][] { { k[0], k[1] },
									{ tcol, trow }, });
							hmPath.put(key, tempal);
							length[trow][tcol] = dkPluskj;
							if (dj == 9999) {
								searchProcess.add(new int[][] { { k[0], k[1] },
										{ tcol, trow }, });
								count++;
							}
						}
						if (tcol == (target[0]) && trow == target[1]) {
							pathFlag = true;
							tempCount = count;
							mSurfaceView.mActivity.hd
									.sendEmptyMessage(DIJKSTAR_COUNT);
							mSurfaceView.mActivity.button.setClickable(true);
							mSurfaceView.repaint(holder);
							break outer;
						}
					}
					try {
						Thread.sleep(timeSpan);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void DijkstarAStar() {
		new Thread() {
			public void run() {
				int count = 0;
				boolean flag = true;
				int[] start = { source[0], source[1] };
				visited[source[1]][source[0]] = 1;
				for (int[] rowcol : sequence) {
					int trow = start[1] + rowcol[1];
					int tcol = start[0] + rowcol[0];
					if (trow < 0 || trow > 18 || tcol < 0 || tcol > 18) {
						continue;
					}
					if (map[trow][tcol] != 0) {
						continue;
					}
					length[trow][tcol] = 1;
					String key = tcol + ":" + trow;
					ArrayList<int[][]> al = new ArrayList<int[][]>();
					al.add(new int[][] { { start[0], start[1] },
							{ tcol, trow }, });
					hmPath.put(key, al);
					searchProcess.add(new int[][] { { start[0], start[1] },
							{ tcol, trow }, });
					count++;
				}
				mSurfaceView.repaint(holder);
				outer: while (flag) {
					int[] k = new int[2];
					int minLen = 9999;
					boolean iniFlag = true;
					for (int i = 0; i < visited.length; i++) {
						for (int j = 0; j < visited[0].length; j++) {
							if (visited[i][j] == 0) {
								if (length[i][j] != 9999) {
									if (iniFlag) {
										minLen = length[i][j]
												+ (int) Math
														.sqrt((j - target[0])
																* (j - target[0])
																+ (i - target[1])
																* (i - target[1]));
										k[0] = j;
										k[1] = i;
										iniFlag = !iniFlag;
									} else {
										int tempLen = length[i][j]
												+ (int) Math
														.sqrt((j - target[0])
																* (j - target[0])
																+ (i - target[1])
																* (i - target[1]));
										if (minLen > tempLen) {
											minLen = tempLen;
											k[0] = j;
											k[1] = i;
										}
									}
								}
							}
						}
					}
					visited[k[1]][k[0]] = 1;
					mSurfaceView.repaint(holder);
					int dk = length[k[1]][k[0]];
					ArrayList<int[][]> al = hmPath.get(k[0] + ":" + k[1]);
					for (int[] rowcol : sequence) {
						int trow = k[1] + rowcol[1];
						int tcol = k[0] + rowcol[0];
						if (trow < 0 || trow > 18 || tcol < 0 || tcol > 18) {
							continue;
						}
						if (map[trow][tcol] != 0) {
							continue;
						}
						int dj = length[trow][tcol];
						int dkPluskj = dk + 1;
						if (dj > dkPluskj) {
							String key = tcol + ":" + trow;
							ArrayList<int[][]> tempal = (ArrayList<int[][]>) al
									.clone();
							tempal.add(new int[][] { { k[0], k[1] },
									{ tcol, trow }, });
							hmPath.put(key, tempal);
							length[trow][tcol] = dkPluskj;
							if (dj == 9999) {
								searchProcess.add(new int[][] { { k[0], k[1] },
										{ tcol, trow }, });
								count++;
							}
						}
						if (tcol == target[0] && trow == target[1]) {
							pathFlag = true;
							tempCount = count;
							mSurfaceView.mActivity.hd
									.sendEmptyMessage(DIJKSTARSTAR_COUNT);
							mSurfaceView.mActivity.button.setClickable(true);
							mSurfaceView.repaint(holder);
							break outer;
						}
					}
					try {
						Thread.sleep(timeSpan);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
}
