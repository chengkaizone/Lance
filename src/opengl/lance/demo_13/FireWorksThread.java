package opengl.lance.demo_13;

import java.util.ArrayList;
import java.util.Collections;

//��ʱ�˶�����������ӵ��߳�
public class FireWorksThread extends Thread {
	static final float SPEED_SPAN = 1.9f;// ���ӳ��ٶ�
	static final float SPEED = 0.08f;// �����ƶ�ÿһ����ģ��ʱ�ӣ�Ҳ����ʱ������
	FireWorks fw;
	boolean flag = true;

	public FireWorksThread(FireWorks fw) {
		this.fw = fw;
		this.setName("FWT");
	}

	public void run() {
		while (flag) {
			// ����������
			for (int i = 0; i < 12; i++) {
				// ���ѡ�����ӵĻ�����
				int index = (int) (FireWorks.pfdArray.length * Math.random());
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
				fw.al.add(new SingleParticle(index, vx, vy, vz));
			}

			// ��ʱ���˶����Ӳ���ʶ��������
			ArrayList<SingleParticle> alDel = new ArrayList<SingleParticle>();
			for (SingleParticle sp : fw.al) {// ɨ�������б����޸�����ʱ���
				sp.timeSpan = sp.timeSpan + SPEED;
				if (sp.timeSpan > 3f) {// ��ʱ�������ʱ����������ӽ�ɾ���б��ѱ�ɾ��
					alDel.add(sp);
				}
			}

			// ɾ����������
			for (SingleParticle sp : alDel) {
				fw.al.remove(sp);
			}

			// �����Ӹ������ӵ㣨��������ľ������򣬱�֤�Ȼ���Զ�����ӣ�����ƽ�������
			// �������ܱ�֤��Ϲ�������
			try {
				Collections.sort(fw.al);
			} catch (Exception e) {
			}

			try {
				Thread.sleep(150);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
