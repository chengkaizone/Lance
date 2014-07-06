package opengl.lance.demo_13;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

//�����������ϵͳ����
public class FireWorks {
	// ���ڻ��Ƶĸ�������������ɵ�����
	static ParticleForDraw[] pfdArray;

	// ����������ӵ��б�
	ArrayList<SingleParticle> al = new ArrayList<SingleParticle>();
	// ��ʱ�˶�����������ӵ��߳�
	FireWorksThread fwt;

	public FireWorks(int[] texId) {
		// �����ڻ��Ƶĸ����������ӽ��г�ʼ��
		pfdArray = new ParticleForDraw[] {
				new ParticleForDraw(texId[0], 0.05f),
				new ParticleForDraw(texId[1], 0.05f),
				new ParticleForDraw(texId[2], 0.05f),
				new ParticleForDraw(texId[3], 0.05f),
				new ParticleForDraw(texId[4], 0.05f),
				new ParticleForDraw(texId[5], 0.05f) };

		// ��ʼ����ʱ�˶�����������ӵ��̲߳�����
		fwt = new FireWorksThread(this);
		fwt.start();
	}

	public void drawSelf(GL10 gl) {
		// �������
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		int size = al.size();
		// ѭ��ɨ������������ӵ��б����Ƹ�������
		for (int i = 0; i < size; i++) {
			try {
				al.get(i).drawSelf(gl);
			} catch (Exception e) {
			}
		}

		// �رջ��
		gl.glDisable(GL10.GL_BLEND);
	}
}
