package opengl.cheng.demo_15map;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * һ����Ƶ�ͼ�����������Ĵ���
 * 
 * @author Administrator
 * 
 */
public class MapNum extends JFrame implements ActionListener {
	// ������ʾ��ǩ
	JLabel jlRow = new JLabel("��ͼ����");
	JLabel jlCol = new JLabel("��ͼ����");
	// ���������
	JTextField jtfRow = new JTextField("20");
	JTextField jtfCol = new JTextField("20");
	// ����һ����ť
	JButton btn = new JButton("ȷ��");

	public MapNum() {
		this.setTitle("�򵥵ĵ�ͼ�����");
		// �����ò��ֹ�����---ֱ����Ϊ����
		this.setLayout(null);
		// ���ñ�ǩ�������
		jlRow.setBounds(10, 5, 60, 20);
		jtfRow.setBounds(70, 5, 100, 20);

		jlCol.setBounds(10, 30, 60, 20);
		jtfCol.setBounds(70, 30, 100, 20);
		this.add(jlRow);
		this.add(jlCol);
		this.add(jtfRow);
		this.add(jtfCol);
		btn.setBounds(180, 5, 60, 20);
		this.add(btn);
		btn.addActionListener(this);
		this.setBounds(200, 150, 300, 200);
		// �˴���������Ϊ�ɼ�---�����޷�����
		this.setVisible(true);
	}

	// ��ť�¼�����
	public void actionPerformed(ActionEvent e) {
		int row = Integer.parseInt(jtfRow.getText());
		int col = Integer.parseInt(jtfCol.getText());
		// ������ͼ���������
		new MapDesigner(row, col);
		// �˴����ٵ�ǰ����
		this.dispose();
	}

	public static void main(String[] args) {
		new MapNum();
	}
}
