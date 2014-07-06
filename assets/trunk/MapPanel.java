package opengl.cheng.demo_15map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MapPanel extends JPanel implements MouseListener,
		MouseMotionListener {
	int row;
	int col;
	int span = 32;
	MapDesigner md;
	int[][] mapData;
	int[][] diamondMap;
	int cameraRow;
	int cameraCol;
	boolean cameraFlag = false;

	public MapPanel(int row, int col, MapDesigner md) {
		this.row = row;
		this.col = col;
		this.md = md;
		// 设置面板边界大小
		this.setPreferredSize(new Dimension(span * col, span * row));
		mapData = new int[row][col];
		// 初始化地图数据
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				mapData[i][j] = 0;
			}
		}
		diamondMap = new int[row][col];
		// 初始化方块数据
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				diamondMap[i][j] = 0;
			}
		}
		// 设置事件监听
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	public void paint(Graphics g) {
		// 设置颜色
		g.setColor(Color.BLACK);
		// 设置矩形区域
		g.fillRect(0, 0, span * col, span * row);
		// 绘制白色格子
		for (int i = 0; i < mapData.length; i++) {
			for (int j = 0; j < mapData[0].length; j++) {
				if (mapData[i][j] == 0) {
					g.setColor(Color.WHITE);
					g.fillRect(j * span, i * span, span, span);
				}
			}
		}
		// 将晶体绘制到地图上
		for (int i = 0; i < diamondMap.length; i++) {
			for (int j = 0; j < diamondMap[0].length; j++) {
				if (diamondMap[i][j] == 1) {
					g.drawImage(md.crystal, j * span + 1, i * span + 3, this);
				}
			}
		}
		// 绘制摄像机
		if (this.cameraFlag) {
			g.drawImage(md.camera, cameraCol * span + 1, cameraRow * span + 3,
					this);
		}
		// 绘制横线
		g.setColor(Color.GREEN);
		for (int i = 0; i < row + 1; i++) {
			g.drawLine(0, span * i, span * col, span * i);
		}
		// 绘制分割线--竖线
		for (int i = 0; i < col + 1; i++) {
			g.drawLine(span * i, 0, span * i, span * row);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (md.jBlack.isSelected() || md.jWhite.isSelected()) {
			int x = e.getX();
			int y = e.getY();

			int rowC = y / span;
			int colC = x / span;
			if (rowC >= row || colC >= col) {
				return;
			}
			mapData[rowC][colC] = (mapData[rowC][colC] + 1) % 2;
			if (mapData[rowC][colC] == 0) {
				diamondMap[rowC][colC] = 0;
				this.cameraFlag = false;
			}
		} else if (md.jCrystal.isSelected()) {
			int x = e.getX();
			int y = e.getY();
			int rowC = y / span;
			int colC = x / span;
			if (rowC >= row || colC >= col) {
				return;
			}
			if (mapData[rowC][colC] == 0) {
				JOptionPane.showMessageDialog(this, "不可通过不能摆放晶体！", "摆放错误",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			diamondMap[rowC][colC] = (diamondMap[rowC][colC] + 1) % 2;
		} else if (md.jCamera.isSelected()) {
			int x = e.getX();
			int y = e.getY();
			int rowC = y / span;
			int colC = x / span;
			if (mapData[rowC][colC] == 0) {
				JOptionPane.showMessageDialog(this, "不能摆放摄像机！", "摆放错误",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			this.cameraCol = colC;
			this.cameraRow = rowC;
			this.cameraFlag = true;
		}
		this.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	// 拖动鼠标绘制地图的事件监听
	public void mouseDragged(MouseEvent e) {
		if (md.jBlack.isSelected() || md.jWhite.isSelected()) {
			int x = e.getX();
			int y = e.getY();
			int rowC = y / span;
			int colC = x / span;
			if (rowC >= row || colC >= col) {
				return;
			}

			mapData[rowC][colC] = md.jBlack.isSelected() ? 1 : 0;
			if (mapData[rowC][colC] == 0) {
				diamondMap[rowC][colC] = 0;
				this.cameraFlag = false;
			}
		}
		// 重绘
		this.repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	public static void main(String[] args) {
		new MapNum();
	}
}
