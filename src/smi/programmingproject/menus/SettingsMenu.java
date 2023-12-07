package smi.programmingproject.menus;

import smi.programmingproject.Main;
import smi.programmingproject.values.GameSpeed;
import smi.programmingproject.values.Gravity;
import smi.programmingproject.values.WordLength;

import javax.swing.*;
import java.awt.*;

public class SettingsMenu extends Menu {
	public SettingsMenu(Main main) {
		super(main);            //Inherits menu

		final Color textColour = new Color(173, 33, 33);
		final Color buttonColour = new Color(255, 255, 255);

		//Pane dimensions: 1536, 864  (768, 432)

		JLayeredPane mainPane = main.getWidgets().createPane(0, 0, frame.getWidth(), frame.getHeight(), new Color(100, 150, 200), false, frame);   //pane is brought in so that objects may be directly added
		JLayeredPane buttonPane = main.getWidgets().createPane(468, 175, 600, 650, new Color(9, 97, 194), true, mainPane);            //Panes are to be layered on the main Pane, and objects will be added to them.

		JLabel title = new JLabel("The settings are as following:", SwingConstants.CENTER);
		main.getWidgets().createComponent(title, 468, 0, 600, 200, 50, null, textColour, false, mainPane);

		JButton customArraysButton = new JButton("Custom arrays");          //Creates a button that directs the user to CustomArrayMenu
		main.getWidgets().createComponent(customArraysButton, 150, 50, 300, 70, 30, buttonColour, textColour, true, buttonPane);
		customArraysButton.addActionListener(e -> {
			frame.dispose();
			new CustomArrayMenu(main);
		});

		final JButton wordLength = new JButton("Word length: " + main.getWordLength());         //Creates a button that toggles the wordLength setting
		main.getWidgets().createComponent(wordLength, 150, 170, 300, 70, 30, buttonColour, textColour, true, buttonPane);

		wordLength.addActionListener(e -> {
			WordLength wordLengthValue = main.getWordLength();
			switch (wordLengthValue) {
				case SHORT:
					wordLengthValue = WordLength.MEDIUM;
					break;
				case MEDIUM:
					wordLengthValue = WordLength.LONG;
					break;
				case LONG:
					wordLengthValue = WordLength.ALL;
					break;
				case ALL:
					wordLengthValue = WordLength.SHORT;
					break;
			}
			main.setWordLength(wordLengthValue);
			wordLength.setText("Word length: " + wordLengthValue);

		});

		JButton speedButton = new JButton("Game speed : " + main.getGameSpeed());       //Creates a button that toggles the gameSpeed setting
		main.getWidgets().createComponent(speedButton, 150, 290, 300, 70, 30, buttonColour, textColour, true, buttonPane);
		//Change to switch operator
		speedButton.addActionListener(e -> {
			GameSpeed speedValue = main.getGameSpeed();
			switch (speedValue) {
				case SLOW:
					speedValue = GameSpeed.MEDIUM;
					break;
				case MEDIUM:
					speedValue = GameSpeed.QUICK;
					break;
				case QUICK:
					speedValue = GameSpeed.SLOW;
					break;
			}
			main.setGameSpeed(speedValue);
			speedButton.setText("Game speed : " + speedValue);
		});

		JButton gravityButton = new JButton("Gravity : " + main.getGravity());        //Creates a button that toggles the jumpHeight button
		main.getWidgets().createComponent(gravityButton, 150, 410, 300, 70, 30, buttonColour, textColour, true, buttonPane);
		gravityButton.addActionListener(e -> {
			Gravity gravityValue = main.getGravity();
			switch (gravityValue) {
				case LOW:
					gravityValue = Gravity.MEDIUM;
					System.out.println("medium");
					break;
				case MEDIUM:
					gravityValue = Gravity.HIGH;
					System.out.println("high");
					break;
				case HIGH:
					gravityValue = Gravity.LOW;
					System.out.println("low");
					break;
			}
			main.setGravity(gravityValue);
			gravityButton.setText("Gravity : " + gravityValue);
		});

		JButton returnButton = new JButton("Return");           //Creates a button that returns the user to MainMenu
		main.getWidgets().createComponent(returnButton, 150, 530, 300, 70, 30, buttonColour, textColour, true, buttonPane);
		returnButton.addActionListener(e -> {
			frame.dispose();
			new MainMenu(main);
		});
	}
}

