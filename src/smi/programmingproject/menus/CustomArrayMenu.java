package smi.programmingproject.menus;

import smi.programmingproject.Main;

import javax.swing.*;
import java.awt.*;

public class CustomArrayMenu extends Menu {

	public CustomArrayMenu(Main main) {
		super(main);

		final Color textColour = new Color(173, 33, 33);
		final Color buttonColour = new Color(255, 255, 255);

		//Pane dimensions: 1536, 864  (768, 432)

		final JTextField arrayInput = new JTextField();            //Creates the text field the custom array can be entered into
		StringBuilder customArrayString = new StringBuilder();
		String[] customArrayContents = main.getCustomArrayContents();

		for (int i = 0; i < main.getCustomArrayLength(); i++) {

			if (i != 0) {
				customArrayString.append(", ");
			}
			customArrayString.append(customArrayContents[i]);    //Displays the current customArray value upon menu load
		}

		JLayeredPane mainPane = main.getWidgets().createPane(0, 0, frame.getWidth(), frame.getHeight(), new Color(100, 150, 200), false, frame);

		JLayeredPane buttonPane = main.getWidgets().createPane(468, 175, 600, 550, new Color(9, 97, 194), true, mainPane);            //Panes are to be layered on the main Pane, and objects will be added to them.
		JLabel title = new JLabel("Custom arrays", SwingConstants.CENTER);
		main.getWidgets().createComponent(title, 468, 0, 600, 200, 50, null, textColour, false, mainPane);

		final JTextArea formatTip = new JTextArea("Please format the custom array as\nword1, word2, word3, word4, word5\n Letter limit of 15, at least 7 items.");        //Add length limits
		main.getWidgets().createComponent(formatTip, 1100, 375, 400, 100, 25, null, textColour, true, mainPane);


		arrayInput.setText(String.valueOf(customArrayString));
		main.getWidgets().createComponent(arrayInput, 150, 170, 300, 70, 30, buttonColour, textColour, true, buttonPane);
		arrayInput.setVisible(main.getCustomArray());

		JTextArea submissionFailure = new JTextArea("Array format incorrect/nPlease ensure your list matches requirements. (Only alphabetical characters,/nword lengths > 16, no repeats");        //Add length limits
		main.getWidgets().createComponent(submissionFailure, 1100, 375, 400, 100, 25, null, textColour, true, mainPane);
		submissionFailure.setVisible(false);

		JButton submitButton = new JButton("Submit");
		main.getWidgets().createComponent(submitButton, 150, 290, 300, 70, 30, buttonColour, textColour, true, buttonPane);


		submitButton.addActionListener(e -> {
			boolean valid = true;
			String[] enteredArrayContents = arrayInput.getText().split(", ");        //Divides the submitted string into array elements

			for (String enteredElement : enteredArrayContents) {
				if (((enteredElement.length() > 15) || (enteredElement.length() < 1)) && (enteredArrayContents.length > 6)) {
					valid = false;                            //Checks that all length requirements are met
					break;
				}
			}

			for (String enteredElement : enteredArrayContents) {

				for (int i = 0; i < enteredElement.length(); i++) {
					if (!Character.isAlphabetic(enteredElement.charAt(i))) {    //Checks that all letters are alphabetical
						valid = false;
					}
				}
			}

			for (int i = 1; i < enteredArrayContents.length; i++) {
				for (int j = 1; j < enteredArrayContents.length; j++) {        //Checks that there are no repeated words
					if ((i != j) && (enteredArrayContents[i].equalsIgnoreCase(enteredArrayContents[j]))) {
						valid = false;
						break;
					}
				}
			}

			if (valid) {
				main.setCustomArrayContents(enteredArrayContents);
			} else {
				submissionFailure.setVisible(true);
				System.out.println("Submission failed");
			}
		});

		submitButton.setVisible(main.getCustomArray());

		final JButton customArrayToggle = new JButton("Custom arrays: " + main.getCustomArray());
		main.getWidgets().createComponent(customArrayToggle, 150, 50, 300, 70, 30, buttonColour, textColour, true, buttonPane);

		customArrayToggle.addActionListener(e -> {

			boolean customArray = main.getCustomArray();
			customArray = !customArray;      //toggles customArray setting between true and false
			main.setCustomArray(customArray);
			customArrayToggle.setText("Custom arrays: " + customArray);  //Edits button text
			main.setCustomArray(customArray);
			arrayInput.setVisible(customArray);
			formatTip.setVisible(customArray);
			submitButton.setVisible(customArray);        //Allows the user to interact with the customArray settings
		});


		JButton returnButton = new JButton("Return");
		main.getWidgets().createComponent(returnButton, 150, 410, 300, 70, 30, buttonColour, textColour, true, buttonPane);
		returnButton.addActionListener(e -> {
			frame.dispose();
			new SettingsMenu(main);
		});
	}
}
