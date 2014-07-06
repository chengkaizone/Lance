package opengl.cheng.demo_15map;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * 一个设计地图行数和列数的窗口
 * 
 * @author Administrator
 * 
 */
public class MapNum extends JFrame implements ActionListener {
	// 创建提示标签
	JLabel jlRow = new JLabel("地图行数");
	JLabel jlCol = new JLabel("地图列数");
	// 创建输入框
	JTextField jtfRow = new JTextField("20");
	JTextField jtfCol = new JTextField("20");
	// 创建一个按钮
	JButton btn = new JButton("确定");

	public MapNum() {
		this.setTitle("简单的地图设计器");
		// 不设置布局管理器---直接作为容器
		this.setLayout(null);
		// 设置标签和输入框
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
		// 此处必须设置为可见---否则无法运行
		this.setVisible(true);
	}

	// 按钮事件监听
	public void actionPerformed(ActionEvent e) {
		int row = Integer.parseInt(jtfRow.getText());
		int col = Integer.parseInt(jtfCol.getText());
		// 触发地图设计器窗口
		new MapDesigner(row, col);
		// 此处销毁当前窗口
		this.dispose();
	}

	public static void main(String[] args) {
		new MapNum();
	}
}
