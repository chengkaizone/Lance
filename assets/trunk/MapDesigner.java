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
 * 地图设计窗口---主要监听按钮事件和调用代码生成界面
 * 
 * @author Administrator
 * 
 */
public class MapDesigner extends JFrame implements ActionListener {
	int row;
	int col;
	MapPanel mdp;
	JScrollPane jsp;
	JButton btnMap = new JButton("生成地图");
	JButton btnD = new JButton("生成晶体位置");
	JButton btnC = new JButton("生成摄像机位置");

	JRadioButton jBlack = new JRadioButton("黑色", null, true);
	JRadioButton jWhite = new JRadioButton("白色", null, false);
	JRadioButton jCrystal = new JRadioButton("晶体", null, false);
	JRadioButton jCamera = new JRadioButton("摄像机", null, false);
	ButtonGroup btns = new ButtonGroup();
	// 得到晶体和摄像机图片对象
	Image crystal;
	Image camera;

	// 容器面板
	JPanel panel = new JPanel();

	public MapDesigner(int row, int col) {
		this.row = row;
		this.col = col;
		this.setTitle("简单的地图设计器");
		crystal = new ImageIcon("img/Diamond.png").getImage();
		camera = new ImageIcon("img/camera.png").getImage();

		// 初始化地图设计器
		mdp = new MapPanel(row, col, this);
		jsp = new JScrollPane(mdp);
		this.add(jsp);
		// 事件监听按钮
		panel.add(btnMap);
		panel.add(btnD);
		panel.add(btnC);
		// 将单选按钮添加进面板
		panel.add(jBlack);
		panel.add(jWhite);
		panel.add(jCrystal);
		panel.add(jCamera);
		// 添加单选按钮组
		btns.add(jBlack);
		btns.add(jWhite);
		btns.add(jCrystal);
		btns.add(jCamera);
		// 将面板放置在布局的顶部
		this.add(panel, BorderLayout.NORTH);
		btnMap.addActionListener(this);
		btnD.addActionListener(this);
		btnC.addActionListener(this);
		// 设置边界
		this.setBounds(10, 10, 800, 600);
		// 设置可见
		this.setVisible(true);
		// 请求获焦
		this.mdp.requestFocus(true);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 判断监听对象
		if (e.getSource() == btnMap) {
			String s = "public static final int[][] Map=//0不可通过1可通过{";
			for (int i = 0; i < mdp.row; i++) {
				s = s + "\n\t{";
				for (int j = 0; j < mdp.col; j++) {
					s = s + mdp.mapData[i][j] + ",";
				}
				s = s.substring(0, s.length() - 1) + "},";
			}
			s = s.substring(0, s.length() - 1) + "\n};";
			new ResultFrame(s, "迷宫地图代码");
		} else if (e.getSource() == this.btnD) {// 生成晶体代码
			String s = "public static final int[][] MAP_OBJECT=//表示可与晶体位置的矩阵{";
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
					+ ";//可遇晶体数量";
			new ResultFrame(s, "晶体分布图代码");
		} else if (e.getSource() == this.btnC) {// 生成摄像机位置代码
			String s = "public static final int CAMERA_COL="
					+ this.mdp.cameraCol + ";//初始化Camera位置\n"
					+ "public static final int CAMERA_ROW="
					+ this.mdp.cameraRow + ";";
			new ResultFrame(s, "摄像机初始位置代码");
		}
	}

	public static void main(String[] args) {
		new MapNum();
	}
}
