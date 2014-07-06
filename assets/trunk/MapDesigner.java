package opengl.cheng.demo_15map;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;

/**
 * ��ͼ��ƴ���---��Ҫ������ť�¼��͵��ô������ɽ���
 * 
 * @author Administrator
 * 
 */
public class MapDesigner extends JFrame implements ActionListener {
	int row;
	int col;
	MapPanel mdp;
	JScrollPane jsp;
	JButton btnMap = new JButton("���ɵ�ͼ");
	JButton btnD = new JButton("���ɾ���λ��");
	JButton btnC = new JButton("���������λ��");

	JRadioButton jBlack = new JRadioButton("��ɫ", null, true);
	JRadioButton jWhite = new JRadioButton("��ɫ", null, false);
	JRadioButton jCrystal = new JRadioButton("����", null, false);
	JRadioButton jCamera = new JRadioButton("�����", null, false);
	ButtonGroup btns = new ButtonGroup();
	// �õ�����������ͼƬ����
	Image crystal;
	Image camera;

	// �������
	JPanel panel = new JPanel();

	public MapDesigner(int row, int col) {
		this.row = row;
		this.col = col;
		this.setTitle("�򵥵ĵ�ͼ�����");
		crystal = new ImageIcon("img/Diamond.png").getImage();
		camera = new ImageIcon("img/camera.png").getImage();

		// ��ʼ����ͼ�����
		mdp = new MapPanel(row, col, this);
		jsp = new JScrollPane(mdp);
		this.add(jsp);
		// �¼�������ť
		panel.add(btnMap);
		panel.add(btnD);
		panel.add(btnC);
		// ����ѡ��ť��ӽ����
		panel.add(jBlack);
		panel.add(jWhite);
		panel.add(jCrystal);
		panel.add(jCamera);
		// ��ӵ�ѡ��ť��
		btns.add(jBlack);
		btns.add(jWhite);
		btns.add(jCrystal);
		btns.add(jCamera);
		// ���������ڲ��ֵĶ���
		this.add(panel, BorderLayout.NORTH);
		btnMap.addActionListener(this);
		btnD.addActionListener(this);
		btnC.addActionListener(this);
		// ���ñ߽�
		this.setBounds(10, 10, 800, 600);
		// ���ÿɼ�
		this.setVisible(true);
		// �����
		this.mdp.requestFocus(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// �жϼ�������
		if (e.getSource() == btnMap) {
			String s = "public static final int[][] Map=//0����ͨ��1��ͨ��{";
			for (int i = 0; i < mdp.row; i++) {
				s = s + "\n\t{";
				for (int j = 0; j < mdp.col; j++) {
					s = s + mdp.mapData[i][j] + ",";
				}
				s = s.substring(0, s.length() - 1) + "},";
			}
			s = s.substring(0, s.length() - 1) + "\n};";
			new ResultFrame(s, "�Թ���ͼ����");
		} else if (e.getSource() == this.btnD) {// ���ɾ������
			String s = "public static final int[][] MAP_OBJECT=//��ʾ���뾧��λ�õľ���{";
			int count = 0;
			for (int i = 0; i < mdp.row; i++) {
				s = s + "\n\t{";
				for (int j = 0; j < mdp.col; j++) {
					s = s + mdp.diamondMap[i][j] + ",";
					if (mdp.diamondMap[i][j] == 1) {
						count++;
					}
				}
				s = s.substring(0, s.length() - 1) + "},";
			}
			s = s.substring(0, s.length() - 1) + "\n};";
			s = s + "\npublic static final int OBJECT_COUNT=" + count
					+ ";//������������";
			new ResultFrame(s, "����ֲ�ͼ����");
		} else if (e.getSource() == this.btnC) {// ���������λ�ô���
			String s = "public static final int CAMERA_COL="
					+ this.mdp.cameraCol + ";//��ʼ��Cameraλ��\n"
					+ "public static final int CAMERA_ROW="
					+ this.mdp.cameraRow + ";";
			new ResultFrame(s, "�������ʼλ�ô���");
		}
	}

	public static void main(String[] args) {
		new MapNum();
	}
}
