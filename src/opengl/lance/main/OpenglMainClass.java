package opengl.lance.main;

import java.util.ArrayList;
import java.util.List;

import opengl.lance.demo_10.Sample10_1;
import opengl.lance.demo_10.Sample10_2;
import opengl.lance.demo_11.Sample11_1;
import opengl.lance.demo_11.Sample11_2;
import opengl.lance.demo_11.Sample11_3;
import opengl.lance.demo_11.Sample11_4;
import opengl.lance.demo_13.Sample13_1;
import opengl.lance.demo_13.Sample13_2;
import opengl.lance.demo_13.Sample13_3;
import opengl.lance.demo_13.Sample13_4;
import opengl.lance.demo_13.Sample13_5;
import opengl.lance.demo_14.Sample14_1;
import opengl.lance.demo_15.Sample15_2;
import opengl.lance.demo_15.Sample15_3;
import opengl.lance.demo_15.Sample15_4;
import opengl.lance.demo_15.Sample15_5;
import opengl.lance.demo_15.Sample15_6;
import opengl.lance.demo_15.Sample15_7;
import opengl.lance.demo_15.Sample15_8;
import opengl.lance.demo_4.HexDemo;
import opengl.lance.demo_4.OtherDemo;
import opengl.lance.demo_4.PairDemo;
import opengl.lance.demo_5.MyActivity_3;
import opengl.lance.demo_5.MyActivity_4;
import opengl.lance.demo_5.MyActivity_5;
import opengl.lance.demo_5.MyActivity_6;
import opengl.lance.demo_5.OtherDemo_1;
import opengl.lance.demo_5.OtherDemo_2;
import opengl.lance.demo_6.Sample6_1;
import opengl.lance.demo_6.Sample6_2;
import opengl.lance.demo_6.Sample6_3;
import opengl.lance.demo_6.Sample6_4;
import opengl.lance.demo_6.Sample6_5;
import opengl.lance.demo_7.Activity_GL_Cirque_1;
import opengl.lance.demo_7.Activity_GL_Cirque_2;
import opengl.lance.demo_7.Activity_GL_Cylinder_1;
import opengl.lance.demo_7.Activity_GL_Cylinder_2;
import opengl.lance.demo_7.Activity_GL_HelicoidSurface_1;
import opengl.lance.demo_7.Activity_GL_HelicoidSurface_2;
import opengl.lance.demo_7.Activity_GL_Hyperboloid_1;
import opengl.lance.demo_7.Activity_GL_Hyperboloid_2;
import opengl.lance.demo_7.Activity_GL_Paraboloid_1;
import opengl.lance.demo_7.Activity_GL_Paraboloid_2;
import opengl.lance.demo_7.Activity_GL_Taper_1;
import opengl.lance.demo_7.Activity_GL_Taper_2;
import opengl.lance.demo_8.Sample8_1;
import opengl.lance.demo_8.Sample8_2;
import opengl.lance.demo_8.Sample8_3;
import opengl.lance.demo_8.Sample8_4;
import opengl.lance.demo_9.GL_Demo;
import org.lance.adapters.RootAdapter;
import org.lance.main.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * opengl�б����~��ͼ�����������ڸ߷ֱ����²���Ч���в���
 * 
 * @author Administrator
 * 
 */
public class OpenglMainClass extends Activity {
	private ListView list;
	private RootAdapter adapter;
	private String[] srr = { "������", "�����ζ�", "������͸��", "����Ч��", "��յ��Ч��", "��Դǿ�ȱ任",
			"�˶���Դ", "�淨����",
			"ƽ��������",
			// ������
			"����ӳ��������", "����ӳ������", "����ϵ", "��������͹���",
			"�ӽ�����",
			// ����ͼ�λ���
			"����Բ����", "����Բ����", "����׶��", "����׶��", "����Բ��", "����Բ��", "����������", "����������",
			"����˫����", "����˫����", "����������", "����������",
			// ����ת��
			"��������ת��", "����ƽ��ת��", "������תת��", "���긴��ת��",
			// �����������Ч
			"����Ч",
			// ��ϼ���
			"Դ���Ӻ�Ŀ������", "����ϵ��ϰ���",
			// 3D�߼�����
			"��־��(��Ҫʵ���)", "Ʈ�������", "ɽ�����ɼ���", "������",
			// ��ѧ������֪ʶ
			"����ϵͳ", "��ײ���(����)", "��ײ���(����)", "����ϵͳ(������ɫ)", "����ϵͳ(��־��Ӧ��)",
			// AIԭ��
			"��ͼ����(AI����)",
			// ��������
			"�������(��Ҫʵ���)", "�������", "AABB�߽�(��ײ���)", "����AABB(��ײ���)", "��͸ЧӦ",
			"ʰȡ����", "����" };
	private Class[] crr = { OtherDemo.class,// ������
			PairDemo.class,// �����ζ�
			HexDemo.class,// ������͸��
			OtherDemo_1.class,// ����Ч��
			OtherDemo_2.class,// ��յ��Ч��
			MyActivity_3.class,// ��Դǿ�ȱ任
			MyActivity_4.class,// �˶���Դ
			MyActivity_5.class,// �淨����
			MyActivity_6.class,// ƽ��������

			Sample6_1.class,// ����ӳ��������
			Sample6_2.class,// ����ӳ������
			Sample6_3.class,// ����ϵ
			Sample6_4.class,// ��������͹���
			Sample6_5.class,// �ӽ�����
			// ����ͼ�λ���
			Activity_GL_Cylinder_1.class,// ����Բ����
			Activity_GL_Cylinder_2.class,// ����Բ����
			Activity_GL_Taper_1.class,// ����׶��
			Activity_GL_Taper_2.class,// ����׶��
			Activity_GL_Cirque_1.class,// ����Բ��
			Activity_GL_Cirque_2.class,// ����Բ��
			Activity_GL_Paraboloid_1.class,// ����������
			Activity_GL_Paraboloid_2.class,// ����������
			Activity_GL_Hyperboloid_1.class,// ����˫����
			Activity_GL_Hyperboloid_2.class,// ����˫����
			Activity_GL_HelicoidSurface_1.class,// ����������
			Activity_GL_HelicoidSurface_2.class,// ����������
			// ����任
			Sample8_1.class,// ��������ת��
			Sample8_2.class,// ����ƽ��ת��
			Sample8_3.class,// ������תת��
			Sample8_4.class,// ���긴��ת��
			// �����������Ч
			GL_Demo.class,// ����Ч
			// ��ϼ���
			Sample10_1.class,// Դ���Ӻ�Ŀ������
			Sample10_2.class,// ����ϵ��ϰ���
			// ��־��~֡��������~ɽ������~������
			Sample11_1.class,// ��־��(��Ҫʵ���)
			Sample11_2.class,// Ʈ�������
			Sample11_3.class,// ɽ������
			Sample11_4.class,// ������
			// ��Ϸ�е���ѧ������
			Sample13_1.class,// ����ϵͳ
			Sample13_2.class,// ��ײ���(��������)
			Sample13_3.class,// ��ײ���(��������)
			Sample13_4.class,// ����ϵͳ(������ɫ����)
			Sample13_5.class,// ����ϵͳ(��־�弼��)
			// AI����~��ͼ��������
			Sample14_1.class,// ��ͼ����~�߷ֱ����ֻ��ϲ���Ч��������ͬ
			// ��ͼ�����~�������~�������~������~AABB�߽�~��͸ЧӦ~ʰȡ����~����
			Sample15_2.class,// �������(��Ҫʵ���)
			Sample15_3.class,// �������
			Sample15_4.class,// AABB�߽�
			Sample15_5.class,// ����AABB��ײ
			Sample15_6.class,// ��͸ЧӦ
			Sample15_7.class,// ʰȡ����
			Sample15_8.class,// ����~�߷ֱ����ֻ��ϲ���Ч��������ͬ
	};

	public void onCreate(Bundle save) {
		super.onCreate(save);
		setContentView(R.layout.ck_main);
		list = (ListView) findViewById(R.id.main_listview);
		setAdapter();
		list.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				startActivity(new Intent(OpenglMainClass.this, crr[position]));
			}
		});
	}

	private void setAdapter() {
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < srr.length; i++) {
			data.add(srr[i]);
		}
		adapter = new RootAdapter(this, data);
		list.setAdapter(adapter);
	}
}
