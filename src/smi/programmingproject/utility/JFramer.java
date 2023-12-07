package smi.programmingproject.utility;

import javax.swing.*;
import java.awt.*;

public class JFramer {          //Inputs: none  Outputs: none   Preconditions: none
	JFrame frame = new JFrame();

	public JFramer() {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize(screenSize.width, screenSize.height);
		frame.setResizable(false);
		frame.setUndecorated(true);      //customises window and renders it visible
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}

	public JFrame getFrame() {
		return frame;
	}
}

