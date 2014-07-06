package opengl.cheng.demo_15map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class ResultFrame extends JFrame {
	JTextArea jta = new JTextArea();
	JScrollPane jsp = new JScrollPane(jta);

	public ResultFrame(String code, String title) {
		this.setTitle(title);
		this.add(jsp);
		jta.setText(code);
		this.setBounds(100, 100, 400, 300);
		this.setVisible(true);
	}

}
