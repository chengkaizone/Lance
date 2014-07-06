package opengl.lance.demo_13;

import java.util.List;

import static opengl.lance.demo_13.Constant_1.*;

public class BallMoveThread_1 extends Thread {
	List<BallController_1> bcs;

	public BallMoveThread_1(List<BallController_1> bcs) {
		this.bcs = bcs;
	}

	public void run() {
		while (Constant_1.THREAD_FLAG) {
			for (BallController_1 bc : bcs) {
				if (bc.ySpeed > MIN_SPEED || bc.state == 0 || bc.state == 1) {
					bc.move();
				}
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
