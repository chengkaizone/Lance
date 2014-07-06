package opengl.lance.demo_13;

import java.util.ArrayList;
import java.util.List;

public class FireSportThread extends Thread {
	static final float SPEED_SPAN = 1.9f;// 粒子初速度
	static final float SPEED = 0.08f;// 粒子移动每一步的模拟时延，也就是时间戳间隔
	boolean flag = true;

	public void run() {
		while (flag) {
			// 随机添加粒子
			for (int i = 0; i < 12; i++) {
				// 随机选择粒子的绘制者
				int index = (int) (FireSport.pArray.length * Math.random());
				// 随机产生粒子的方位角及仰角
				double elevation = Math.random() * Math.PI / 12 + Math.PI * 5
						/ 12;// 仰角
				double direction = Math.random() * Math.PI * 2;// 方位角
				// 计算出粒子在XYZ轴方向的速度分量
				float vy = (float) (SPEED_SPAN * Math.sin(elevation));
				float vx = (float) (SPEED_SPAN * Math.cos(elevation) * Math
						.cos(direction));
				float vz = (float) (SPEED_SPAN * Math.cos(elevation) * Math
						.sin(direction));
				// 创建粒子对像并添加进粒子列表
				FireSport.particles.add(new HandleParticle(index, vx, vy, vz));
			}

			// 按时间运动粒子并标识过期粒子
			ArrayList<HandleParticle> alDel = new ArrayList<HandleParticle>();
			for (HandleParticle sp : FireSport.particles) {// 扫描粒子列表，并修改粒子时间戳
				sp.timeSpan = sp.timeSpan + SPEED;
				if (sp.timeSpan > 3f) {// 若时间戳超过时限则将粒子添加进删除列表已备删除
					alDel.add(sp);
				}
			}

			// 删除过期粒子
			for (HandleParticle sp : alDel) {
				FireSport.particles.remove(sp);
			}

			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	// // 计算速度的时间间隔
	// static final float SPEED_SPAN = 0.08f;
	// // 离子初速度
	// static final float SPEED = 1.9f;
	// boolean flag = true;
	//
	// public void run() {
	// while (flag) {
	// // 随机选择某种颜色粒子绘制
	// int index = (int) (FireSport.pArray.length * Math.random());
	// // 需要计算仰角和方位角的原因是因为三位笛卡尔坐标系默认只有一个~以一个坐标系为标准
	// // 随机产生仰角
	// double evel = Math.PI / 12 * Math.random() + Math.PI * 5 / 12;
	// // 随机产生方位角
	// double direct = Math.random() * Math.PI;
	// float xSpeed = (float) (Math.cos(evel) * Math.cos(direct)) * SPEED;
	// float ySpeed = (float) Math.sin(evel) * SPEED;
	// float zSpeed = (float) (Math.cos(evel) * Math.sin(direct)) * SPEED;
	// FireSport.particles.add(new HandleParticle(index, xSpeed, ySpeed,
	// zSpeed));
	// List<HandleParticle> dels = new ArrayList<HandleParticle>();
	// for (int i = 0; i < FireSport.particles.size(); i++) {
	// HandleParticle hp = FireSport.particles.get(i);
	// hp.timeSpan += SPEED_SPAN;
	// if (hp.timeSpan > 3f) {
	// dels.add(hp);
	// }
	// }
	// for (int i = 0; i < dels.size(); i++) {
	// FireSport.particles.remove(dels.get(i));
	// }
	// try {
	// Thread.sleep(150);
	// } catch (InterruptedException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// }
}
