package opengl.lance.demo_15;

import java.util.ArrayList;

public class BulletGoThread extends Thread {
	ArrayList<BulletForControl> alBFC;// �ӵ������б�
	boolean flag = true;// ѭ����־λ

	public BulletGoThread(ArrayList<BulletForControl> alBFC) {
		this.alBFC = alBFC;
	}

	public void run() {
		while (flag) {
			int size = alBFC.size();
			for (int i = 0; i < size; i++)// ɨ���б�
			{
				alBFC.get(i).go(alBFC);
			}
			try {
				sleep(100);// ���Ա�ը��˯��ʱ��
				// sleep(10000);//���Դ�͸��˯��ʱ��
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
