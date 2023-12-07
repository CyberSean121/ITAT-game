package smi.programmingproject.menus;

import smi.programmingproject.Main;
import smi.programmingproject.game.Game;
import smi.programmingproject.values.Level;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class WinScreen extends Menu {

	public WinScreen(Main main, Level currentLevel) {
		super(main);

		final Color textColour = new Color(173, 33, 33);
		final Color buttonColour = new Color(255, 255, 255);

		//Pane dimensions: 1536, 864  (768, 432)

		JLayeredPane mainPane = main.getWidgets().createPane(0, 0, frame.getWidth(), frame.getHeight(), new Color(100, 150, 200), false, frame);
		JLayeredPane buttonPane = main.getWidgets().createPane(468, 175, 600, 640, new Color(9, 97, 194), true, mainPane);

		JLabel title = new JLabel("You win!", SwingConstants.CENTER);
		main.getWidgets().createComponent(title, 518, 0, 500, 200, 80, null, textColour, false, mainPane);

		if (currentLevel != Level.THIRD) {
			JButton nextButton = new JButton("Next level");
			main.getWidgets().createComponent(nextButton, 150, 50, 300, 70, 30, buttonColour, textColour, true, buttonPane);

			nextButton.addActionListener(e -> {
				frame.dispose();
				Game game = new Game(main, currentLevel);    //Starts new game instance, passes level enum
				new Thread(game::run).start();
				System.out.println("Game started");
				//Start game from the beginning here
			});
		} else {
			JButton creditsButton = new JButton("Credits");
			main.getWidgets().createComponent(creditsButton, 150, 50, 300, 70, 30, buttonColour, textColour, true, buttonPane);

			creditsButton.addActionListener(e -> {
				frame.dispose();
				//CreditsScreen
			});
		}

		JButton mainMenuButton = new JButton("Main menu");           //Creates a button that returns the user to MainMenu
		main.getWidgets().createComponent(mainMenuButton, 150, 170, 300, 70, 30, buttonColour, textColour, true, buttonPane);
		mainMenuButton.addActionListener(e -> {
			frame.dispose();
			new MainMenu(main);
		});

		JButton quitButton = new JButton("Quit");
		main.getWidgets().createComponent(quitButton, 150, 290, 300, 70, 30, buttonColour, textColour, true, buttonPane);

		quitButton.addActionListener(e -> {
			frame.dispose();
			try {
				main.exitSequence();
			} catch (IOException f) {
				System.out.println("EXIT SEQUENCE FAILED");
				f.printStackTrace();
			}
			//Start game from the beginning here
		});

		main.getWidgets().createLeaderBoard(main, 150, 410, 300, 200, 25, new Color(150, 200, 255), textColour, true, buttonPane, currentLevel);

	}
}
