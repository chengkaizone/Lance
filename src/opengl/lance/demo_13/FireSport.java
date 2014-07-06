package opengl.lance.demo_13;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

public class FireSport {
	// 不同颜色种类的粒子
	public static Particle[] pArray = {
			new Particle(2, 0.9882f, 0.9882f, 0.8784f, 0),
			new Particle(2, 0.9216f, 0.2784f, 0.2392f, 0),
			new Particle(2, 1.0f, 0.3686f, 0.2824f, 0),
			new Particle(2, 0.8157f, 0.9882f, 0.6863f, 0),
			new Particle(2, 0.9922f, 0.7843f, 0.9882f, 0),
			new Particle(2, 0.1f, 1, 0.3f, 0), };
	public static List<HandleParticle> particles = new ArrayList<HandleParticle>();
	static boolean flag = true;
	FireSportThread fst;

	public FireSport() {
		fst = new FireSportThread();
		fst.start();
	}

	public void drawSelf(GL10 gl) {
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).drawSelf(gl);
		}
	}
}
