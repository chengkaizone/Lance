package opengl.lance.demo_14;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Stack;

import android.view.SurfaceHolder;

public class DrawLine {
	MapSurfaceView mSurfaceView;
	// ��Ҫ�����ĵ�ͼ---�õ�ͼ���Կ�����һ���������ά���飻��ֻ��һ��
	int[][] map = MapList.map[0];
	// ������
	int[] source = MapList.source;
	// ����Ŀ���
	int[] target = MapList.targetA[0];
	// ʹ�ú����㷨
	int algorithmId = 0;

	// ������������
	ArrayList<int[][]> searchProcess = new ArrayList<int[][]>();
	// �������ʹ�õ���ʷջ
	Stack<int[][]> stack = new Stack<int[][]>();
	// �������ʹ�ö���
	LinkedList<int[][]> queue = new LinkedList<int[][]>();
	// A*���ȼ�����
	PriorityQueue<int[][]> astartQueue = new PriorityQueue<int[][]>(100,
			new StarComparator(this));
	// ���·����¼
	HashMap<String, int[][]> hm = new HashMap<String, int[][]>();
	// 0~δȥ����1~��ȥ��
	int[][] visited = new int[19][19];
	// ��¼·������
	int[][] length = new int[19][19];
	// ��¼ÿ�������̾���
	HashMap<String, ArrayList<int[][]>> hmPath = new HashMap<String, ArrayList<int[][]>>();
	// �Ƿ��ҵ�·��
	boolean pathFlag = false;
	// ÿ��������ʱ����
	int timeSpan = 10;
	SurfaceHolder holder;

	int[][] sequence = { { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 0 }, { -1, 1 },
			{ -1, -1 }, { 1, -1 }, { 1, 1 }, };
	// ��¼����ʹ�õĲ���
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
		// ���û��ʿ��
		mSurfaceView.paint.setStrokeWidth(0);
		for (int i = 0; i < length.length; i++) {
			for (int j = 0; j < length[0].length; j++) {
				length[i][j] = 9999;
			}
		}
	}

	// ������������
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

	// �������������
	public void DFS() {
		new Thread() {
			public void run() {
				// ִ���̵߳ı�־
				boolean flag = true;
				// ����������
				int[][] start = { { source[0], source[1] },
						{ source[0], source[1] }, };
				stack.push(start);
				// ����ʹ���˶��ٲ�����Ŀ���
				int count = 0;
				while (flag) {
					// ȡ����ǰ·����
					int[][] currentEdge = stack.pop();
					// ȡ��ȥ����Ŀ���
					int[] tempTarget = currentEdge[1];
					// �ж�Ŀ�ĵ��Ƿ�ȥ�������ȥ��������ѭ��
					if (visited[tempTarget[1]][tempTarget[0]] == 1) {
						continue;
					}
					// ���û��ȥ�������t������������1
					count++;
					// ����ѷ���
					visited[tempTarget[1]][tempTarget[0]] = 1;
					// ����ʱ�߼�����������
					searchProcess.add(currentEdge);
					hm.put(tempTarget[0] + ":" + tempTarget[1], new int[][] {
							currentEdge[1], currentEdge[0] });
					// ���ƻ���
					mSurfaceView.repaint(holder);
					try {
						Thread.sleep(timeSpan);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// �������Ŀ�ĵ�---�����߳�
					if (tempTarget[0] == target[0]
							&& tempTarget[1] == target[1]) {
						break;
					}
					// �����п��ܵı���ջ---ȡ���ߵĽڵ�---�к�~�к�
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
				pathFlag = true;// ���ҵ�Ŀ���
				mSurfaceView.repaint(holder);
				tempCount = count;
				// ������Ϣ��ʾ���
				mSurfaceView.mActivity.hd.sendEmptyMessage(DFS_COUNT);
				mSurfaceView.mActivity.button.setClickable(true);
			}
		}.start();
	}

	// ��������㷨
	public void BFS() {
		new Thread() {
			public void run() {
				int count = 0;
				boolean flag = true;
				int[][] start = { { source[0], source[1] },
						{ source[0], source[1] }, };
				// ����ʼ�������е�ĩβ
				queue.offer(start);
				while (flag) {
					// ȥ��ͷ���е�����---��ά���������������һ����
					int[][] currentEdge = queue.poll();
					// ȡ����ǰ�ߵ�Ŀ���---����һ����
					int[] tempTarget = currentEdge[1];
					// �жϵ�ǰ���Ƿ�ȥ��
					if (visited[tempTarget[1]][tempTarget[0]] == 1) {
						continue;
					}
					count++;
					// ����ѷ��ʹ�
					visited[tempTarget[1]][tempTarget[0]] = 1;
					searchProcess.add(currentEdge);
					// ��¼���ڵ�
					hm.put(tempTarget[0] + ":" + tempTarget[1], new int[][] {
							currentEdge[1], currentEdge[0], });
					mSurfaceView.repaint(holder);
					try {
						Thread.sleep(timeSpan);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// ���ΪĿ������ѭ��
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

	// �������A*������
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
