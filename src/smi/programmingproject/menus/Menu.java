package smi.programmingproject.menus;

import smi.programmingproject.Main;
import smi.programmingproject.utility.JFramer;

import javax.swing.*;
import java.awt.*;

public abstract class Menu {     //Inputs: none  Outputs: none   Preconditions: This superclass has inheriting children menus
	protected JFrame frame;      //superclass for all future menus
	protected Main main;

	public Menu(Main main) {
		JFramer jFramer = new JFramer();
		JFrame frame = jFramer.getFrame();
		frame.setBackground(new Color(100, 150, 200));

		this.main = main;
		this.frame = frame;
	}
}

