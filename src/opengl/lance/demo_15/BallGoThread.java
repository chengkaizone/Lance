package opengl.lance.demo_15;

import java.util.ArrayList;

public class BallGoThread extends Thread {

	ArrayList<BallForControl> alBall;// �������˶��б�
	boolean flag = true;// �������˶���־λ

	public BallGoThread(ArrayList<BallForControl> alBall) {
		this.alBall = alBall;
	}

	public void run() {
		while (flag) {
			int size = alBall.size();
			for (int i = 0; i < size; i++) {
				alBall.get(i).go(alBall);
			}
			try {
				sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
