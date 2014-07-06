package opengl.lance.demo_15;

import java.util.ArrayList;

public class GoThread extends Thread {

	ArrayList<LogicControl> al;// �����б�
	boolean flag = true;// �߳̿��Ʊ�־λ

	public GoThread(ArrayList<LogicControl> al) {
		this.al = al;
	}

	public void run() {
		while (flag) {
			int size = al.size();
			for (int i = 0; i < size; i++) {
				LogicControl ct = al.get(i);
				ct.go(al);
			}
			try {
				sleep(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
