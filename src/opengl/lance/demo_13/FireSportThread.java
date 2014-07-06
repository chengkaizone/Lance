package opengl.lance.demo_13;

import java.util.ArrayList;
import java.util.List;

public class FireSportThread extends Thread {
	static final float SPEED_SPAN = 1.9f;// ���ӳ��ٶ�
	static final float SPEED = 0.08f;// �����ƶ�ÿһ����ģ��ʱ�ӣ�Ҳ����ʱ������
	boolean flag = true;

	public void run() {
		while (flag) {
			// ����������
			for (int i = 0; i < 12; i++) {
				// ���ѡ�����ӵĻ�����
				int index = (int) (FireSport.pArray.length * Math.random());
				// ����������ӵķ�λ�Ǽ�����
				double elevation = Math.random() * Math.PI / 12 + Math.PI * 5
						/ 12;// ����
				double direction = Math.random() * Math.PI * 2;// ��λ��
				// �����������XYZ�᷽����ٶȷ���
				float vy = (float) (SPEED_SPAN * Math.sin(elevation));
				float vx = (float) (SPEED_SPAN * Math.cos(elevation) * Math
						.cos(direction));
				float vz = (float) (SPEED_SPAN * Math.cos(elevation) * Math
						.sin(direction));
				// �������Ӷ�����ӽ������б�
				FireSport.particles.add(new HandleParticle(index, vx, vy, vz));
			}

			// ��ʱ���˶����Ӳ���ʶ��������
			ArrayList<HandleParticle> alDel = new ArrayList<HandleParticle>();
			for (HandleParticle sp : FireSport.particles) {// ɨ�������б����޸�����ʱ���
				sp.timeSpan = sp.timeSpan + SPEED;
				if (sp.timeSpan > 3f) {// ��ʱ�������ʱ����������ӽ�ɾ���б��ѱ�ɾ��
					alDel.add(sp);
				}
			}

			// ɾ����������
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
	// // �����ٶȵ�ʱ����
	// static final float SPEED_SPAN = 0.08f;
	// // ���ӳ��ٶ�
	// static final float SPEED = 1.9f;
	// boolean flag = true;
	//
	// public void run() {
	// while (flag) {
	// // ���ѡ��ĳ����ɫ���ӻ���
	// int index = (int) (FireSport.pArray.length * Math.random());
	// // ��Ҫ�������Ǻͷ�λ�ǵ�ԭ������Ϊ��λ�ѿ�������ϵĬ��ֻ��һ��~��һ������ϵΪ��׼
	// // �����������
	// double evel = Math.PI / 12 * Math.random() + Math.PI * 5 / 12;
	// // ���������λ��
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
