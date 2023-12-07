package smi.programmingproject.menus;

import smi.programmingproject.Main;
import smi.programmingproject.game.Game;
import smi.programmingproject.values.Level;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainMenu extends Menu {        //Inputs: none  Outputs: Creates main menu   Preconditions: Menu parent class

	public MainMenu(Main main) {
		super(main);        //inherits Menu superclass

		final Color textColour = new Color(173, 33, 33);
		final Color buttonColour = new Color(255, 255, 255);

		//Pane dimensions: 1536, 864  (768, 432)

		JLayeredPane mainPane = main.getWidgets().createPane(0, 0, frame.getWidth(), frame.getHeight(), new Color(100, 150, 200), false, frame);            //Panes are created first so that objects may be directly added
		JLayeredPane buttonPane = main.getWidgets().createPane(468, 175, 600, 550, new Color(9, 97, 194), true, mainPane);           //Panes are to be layered on the main Pane, and objects will be added to them


		//Creating components in appropriate panes
		JLabel title = new JLabel("It takes a type", SwingConstants.CENTER);
		main.getWidgets().createComponent(title, 518, 0, 500, 200, 80, null, textColour, false, mainPane);

		JLabel hint = new JLabel("Type to navigate!!!", SwingConstants.CENTER);
		main.getWidgets().createComponent(hint, 1100, 550, 400, 100, 50, textColour, new Color(0, 0, 0), true, mainPane);

		main.getWidgets().createLeaderBoard(main, 1376, 10, 150, 150, 25, new Color(255, 204, 67), new Color(220, 90, 16), true, mainPane, Level.NONE);

		JButton startButton = new JButton("Start");
		main.getWidgets().createComponent(startButton, 150, 50, 300, 70, 30, buttonColour, textColour, true, buttonPane);

		startButton.addActionListener(e -> {        //Creates a button instance action
			frame.dispose();
			Game game = new Game(main, Level.FIRST);
			new Thread(game::run).start();
			System.out.println("Game started");
			//Start game from the beginning here
		});

		JButton levelsButton = new JButton("Level select");         //Creates a button that directs the user to LevelSelectMenu
		main.getWidgets().createComponent(levelsButton, 150, 170, 300, 70, 30, buttonColour, textColour, true, buttonPane);

		levelsButton.addActionListener(e -> {
			frame.dispose();
			new LevelSelectMenu(main);
		});

		JButton settingsButton = new JButton("Settings");           //Creates a button that directs the user to SettingsMenu
		main.getWidgets().createComponent(settingsButton, 150, 290, 300, 70, 30, buttonColour, textColour, true, buttonPane);

		settingsButton.addActionListener(e -> {
			frame.dispose();
			new SettingsMenu(main);
		});

		JButton exitButton = new JButton("Exit");           //Creates a button that exits the program, saving settings and any custom arrays and scores
		main.getWidgets().createComponent(exitButton, 150, 410, 300, 70, 30, buttonColour, textColour, true, buttonPane);

		exitButton.addActionListener(e -> {
			frame.dispose();
			try {
				main.exitSequence();
			} catch (IOException f) {
				System.out.println("EXIT SEQUENCE FAILED");
				f.printStackTrace();
			}
		});
	}
}
