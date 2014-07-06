package opengl.lance.demo_11;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import static opengl.lance.demo_11.Constant_1.*;

public class MyCactusGroup {
	public DrawCactus cfd;
	public List<Cactus> cactuses = new ArrayList<Cactus>();

	public MyCactusGroup(int texId) {
		cfd = new DrawCactus(texId);
		// 根据地图设计器计算需要绘制多少植被
		for (int i = 0; i < Constant_1.DESERT_ROWS; i++) {
			for (int j = 0; j < Constant_1.DESERT_COLS; j++) {
				if (Constant_1.MAP_CACTUS[i][j] == 0) {
					continue;
				}
				Cactus cac = new Cactus((j + 0.5f) * UNIT_SIZE, (i + 0.5f)
						* UNIT_SIZE, 0, this);
				cactuses.add(cac);
			}
		}
	}

	public void calDirection() {
		for (int i = 0; i < cactuses.size(); i++) {
			cactuses.get(i).calculateBillboardDirection();
		}
	}

	public void drawSelf(GL10 gl) {
		for (int i = 0; i < cactuses.size(); i++) {
			cactuses.get(i).drawSelf(gl);
		}
	}
}
