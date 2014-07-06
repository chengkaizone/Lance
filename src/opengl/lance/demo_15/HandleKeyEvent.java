package opengl.lance.demo_15;

import static opengl.lance.demo_15.Constant_2.*;

public class HandleKeyEvent extends Thread {
	BallSurfaceView bsv;

	public HandleKeyEvent(BallSurfaceView bsv) {
		this.bsv = bsv;
	}

	public void run() {
		while (bsv.flag) {
			if (bsv.keyState == 0x1) {//
				bsv.ballX += (float) Math.sin(direction) * MOVE_SPAN;
				bsv.ballZ += -(float) Math.cos(direction) * MOVE_SPAN;
			} else if (bsv.keyState == 0x2) {//
				bsv.ballX += (float) Math.sin(direction) * MOVE_SPAN;
				bsv.ballZ += (float) Math.cos(direction) * MOVE_SPAN;
			} else if (bsv.keyState == 0x4) {
				direction -= DEGREE_SPAN;
			} else if (bsv.keyState == 0x8) {
				direction += DEGREE_SPAN;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
