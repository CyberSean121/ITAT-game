package smi.programmingproject.utility;

import smi.programmingproject.Main;
import smi.programmingproject.values.Level;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Widgets {

	private final String font = "IMPACT";
	private final int style = Font.PLAIN;        //Static variables
	private final Border border = BorderFactory.createLineBorder(Color.BLACK, 4);

	public JLayeredPane createPane(int xCoord, int yCoord, int width, int height, Color colour, boolean bordered, Container frame) {
		JLayeredPane pane = new JLayeredPane();             //Inputs: coordinates for placement and dimensions    Outputs: JPanel
		pane.setBounds(xCoord, yCoord, width, height);
		pane.setBackground(colour);         //Customisation
		pane.setOpaque(true);
		if (bordered) {
			pane.setBorder(border);
		}
		frame.add(pane);
		return pane;
	}

	//Value never used, will be used when the game is running
	public void createComponent(JComponent component, int xCoord, int yCoord, int width, int height, int size, Color bColour, Color fColour, boolean bordered, Container pane) {

		component.setBounds(xCoord, yCoord, width, height);                             //Inputs: coordinates, dimensions, customisation details and the frame   Outputs: component of the requireds type
		component.setForeground(fColour);       //Customisation
		component.setFont(new Font(font, style, size));

		if (bordered) {
			component.setBorder(border);
		}

		if (bColour != null) {                                                            //Preconditions: JLayeredPane and JComponent are already preinstantiated
			component.setBackground(bColour);
			component.setOpaque(true);
		}

		pane.add(component); //adds element to pane
	}

	public void createLeaderBoard(Main main, int xCoord, int yCoord, int width, int height, int size, Color bColour, Color fColour, boolean bordered, Container panel, Level level) {

		LeaderBoard[] leaderBoardValues = main.getLeaderBoards();
		int levelNumber = 0;

		switch (level) {    //Matches enums to integer equivalents
			case FIRST:
				levelNumber = 1;
				break;
			case SECOND:
				levelNumber = 2;
				break;
			case THIRD:
				levelNumber = 3;
				break;
		}

		Object[][] leaderBoardItems = leaderBoardValues[levelNumber].getLeaderBoardContents();
		//Gets the necessary leaderboard elements

		StringBuilder contents = new StringBuilder("Leaderboard\n");

		for (Object[] leaderBoardItem : leaderBoardItems) {
			contents.append(leaderBoardItem[0]).append(": ").append(leaderBoardItem[1]).append("\n");  //Writes the values from 2D array to string array for use in JTextArea
		}

		JTextArea leaderBoard = new JTextArea(String.valueOf(contents));  //Initialises the textArea
		leaderBoard.setBounds(xCoord, yCoord, width, height);
		leaderBoard.setBackground(bColour);
		leaderBoard.setForeground(fColour);              //Customisation
		leaderBoard.setOpaque(true);
		leaderBoard.setFont(new Font(font, style, size));
		leaderBoard.setEditable(false);

		if (bordered) {
			leaderBoard.setBorder(border);
		}
		panel.add(leaderBoard);        //Adds to the panel
	}
}