package smi.programmingproject.menus;

import smi.programmingproject.Main;
import smi.programmingproject.game.Game;
import smi.programmingproject.values.Level;

import javax.swing.*;
import java.awt.*;

public class LevelSelectMenu extends Menu {
	public LevelSelectMenu(Main main) {
		super(main);

		final Color textColour = new Color(175, 35, 35);
		final Color buttonColour = new Color(255, 255, 255);

		JLayeredPane mainPane = main.getWidgets().createPane(0, 0, frame.getWidth(), frame.getHeight(), new Color(100, 150, 200), false, frame);            //Panes are created first so that objects may be directly added
		JLayeredPane levelSelectPane = main.getWidgets().createPane(368, 175, 800, 550, new Color(9, 97, 194), true, mainPane);

		JLabel title = new JLabel("Level select", SwingConstants.CENTER);
		main.getWidgets().createComponent(title, 518, 0, 500, 200, 50, null, textColour, false, mainPane);

		//Add images of the levels once created

		JButton level1Button = new JButton("Level 1");
		main.getWidgets().createComponent(level1Button, 75, 50, 300, 70, 30, buttonColour, textColour, true, levelSelectPane);
		level1Button.addActionListener(e -> {
			frame.dispose();
			Game game = new Game(main, Level.FIRST);
			new Thread(game::run).start();
			System.out.println("Game started");
		});

		JButton level2Button = new JButton("Level 2");
		main.getWidgets().createComponent(level2Button, 75, 170, 300, 70, 30, buttonColour, textColour, true, levelSelectPane);
		level2Button.addActionListener(e -> {
			frame.dispose();
			Game game = new Game(main, Level.SECOND);
			new Thread(game::run).start();
			System.out.println("Game started");
		});

		JButton level3Button = new JButton("Level 3");
		main.getWidgets().createComponent(level3Button, 75, 290, 300, 70, 30, buttonColour, textColour, true, levelSelectPane);
		level3Button.addActionListener(e -> {
			frame.dispose();
			Game game = new Game(main, Level.THIRD);
			new Thread(game::run).start();
			System.out.println("Game started");
		});

		JButton returnButton = new JButton("Return");           //Creates a button that returns the user to MainMenu
		main.getWidgets().createComponent(returnButton, 75, 410, 300, 70, 30, buttonColour, textColour, true, levelSelectPane);
		returnButton.addActionListener(e -> {
			frame.dispose();
			new MainMenu(main);
		});
	}
}