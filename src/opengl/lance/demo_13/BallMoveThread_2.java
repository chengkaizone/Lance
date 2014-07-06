package opengl.lance.demo_13;

import java.util.List;

import static opengl.lance.demo_13.Constant_2.*;

public class BallMoveThread_2 extends Thread {
	private List<BallController_2> bcs;

	public BallMoveThread_2(List<BallController_2> bcs) {
		this.bcs = bcs;
	}

	public void run() {
		while (THREAD_FLAG) {
			for (int i = 0; i < bcs.size(); i++) {
				BallController_2 bc = bcs.get(i);
				bc.move(bcs);
			}
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
