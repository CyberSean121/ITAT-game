package smi.programmingproject;

import smi.programmingproject.menus.MainMenu;
import smi.programmingproject.utility.FileInfo;
import smi.programmingproject.utility.GlobalLeaderBoard;
import smi.programmingproject.utility.LeaderBoard;
import smi.programmingproject.utility.Widgets;
import smi.programmingproject.values.GameSpeed;
import smi.programmingproject.values.Gravity;
import smi.programmingproject.values.WordLength;

import java.io.IOException;


public class Main { //much of the error is due to non-completion

	private boolean customArray;
	private WordLength wordLength;
	private GameSpeed gameSpeed;          //creates variables that will have settings assigned to them
	private Gravity gravity;
	private String[] customArrayContents;       //Stores the current custom array
	private String[] defaultArrayContents;
	private int settingsNumber;
	private final LeaderBoard[] leaderBoards = new LeaderBoard[4];
	private final FileInfo fileInfo = new FileInfo();
	private final Widgets widgets = new Widgets();

	public static void main(String[] args) { //Immediately creates an instance of Main, to have something to give to MainMenu
		new Main();
	}

	public Main() {
		entrySequence();
		//creates first instance of MainMenu to begin the chain of menus
		new MainMenu(this);
	}

	public void entrySequence() {

		String[] settingsData = fileInfo.getArrayContents("textFiles/settings.txt");
		settingsNumber = settingsData.length;
		//fetch the saved settings from the settings file into a String arrayS

		setCustomArray(Boolean.parseBoolean(settingsData[0]));
		setWordLength(WordLength.valueOf(settingsData[1]));
		setGameSpeed(GameSpeed.valueOf(settingsData[2]));       //load the String array contents into the settings variables
		setGravity(Gravity.valueOf(settingsData[3]));


		int leaderBoardLength = 5;
		leaderBoards[1] = new LeaderBoard(fileInfo.getScores("textFiles/leaderboard1.txt", leaderBoardLength));
		leaderBoards[2] = new LeaderBoard(fileInfo.getScores("textFiles/leaderboard2.txt", leaderBoardLength));
		leaderBoards[3] = new LeaderBoard(fileInfo.getScores("textFiles/leaderboard3.txt", leaderBoardLength));
		//Assigns the leaderBoards to an array (Positions relating to level number)

		String[][] topScores = new String[leaderBoards.length - 1][2];
		for (int i = 1; i < (leaderBoards.length); i++) {
			topScores[i - 1] = leaderBoards[i].getLeaderBoardContents()[0];       //gathers the lowest time from each LB
		}

		leaderBoards[0] = new GlobalLeaderBoard(topScores);     //Compiles a leaderboard that shows the top score of each level

		customArrayContents = fileInfo.getArrayContents("textFiles/customArray.txt");
		defaultArrayContents = fileInfo.getArrayContents("textFiles/defaultArray.txt");

	}

	public void exitSequence() throws IOException {
		String[] settingsData = new String[settingsNumber];
		settingsData[0] = String.valueOf(customArray);
		settingsData[1] = String.valueOf(wordLength);
		settingsData[2] = String.valueOf(gameSpeed);   //Saves the settings to a string array
		settingsData[3] = String.valueOf(gravity);

		fileInfo.setSettings("textFiles/settings.txt", settingsData);  //Sends the string array to be written to file
		fileInfo.setCustomArrayContents("textFiles/customArray.txt", customArrayContents);

		fileInfo.setScores("textFiles/leaderboard1.txt", leaderBoards[1].getLeaderBoardContents());
		fileInfo.setScores("textFiles/leaderboard2.txt", leaderBoards[2].getLeaderBoardContents());   //Sets leaderboard text files to the correct values
		fileInfo.setScores("textFiles/leaderboard3.txt", leaderBoards[3].getLeaderBoardContents());
	}


	//getter/setter methods
	public boolean getCustomArray() {     //getter and setter functions
		return customArray;
	}

	public void setCustomArray(boolean customArray) {
		this.customArray = customArray;
	}

	public WordLength getWordLength() {
		return wordLength;
	}

	public void setWordLength(WordLength wordLength) {
		this.wordLength = wordLength;
	}

	public GameSpeed getGameSpeed() {
		return gameSpeed;
	}

	public void setGameSpeed(GameSpeed gameSpeed) {
		this.gameSpeed = gameSpeed;
	}

	public Gravity getGravity() {
		return gravity;
	}

	public void setGravity(Gravity gravity) {
		this.gravity = gravity;
	}

	public String[] getCustomArrayContents() {
		return customArrayContents;
	}

	public int getCustomArrayLength() {
		return customArrayContents.length;
	}

	public void setCustomArrayContents(String[] customArrayContents) {
		this.customArrayContents = customArrayContents;
	}

	public LeaderBoard[] getLeaderBoards() {
		return leaderBoards;
	}

	public Widgets getWidgets() {
		return widgets;
	}

	public FileInfo getFileInfo() {
		return fileInfo;
	}

	public String[] getDefaultArrayContents() {
		return defaultArrayContents;
	}
}
