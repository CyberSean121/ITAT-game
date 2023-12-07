package smi.programmingproject.utility;

import javax.swing.*;
import java.io.*;

public class FileInfo {

	public ImageIcon getImage(String fileName) {        //Inputs: name of file     Outputs: ImageIcon containing image   Preconditions: file location is valid
		return new ImageIcon(fileName);
		//grabs image from file
	}

	public String[][] getScores(String fileName, int leaderBoardLength) {    //Allows IOException to throw in function if necessary
		String[][] leaderBoardValues = new String[leaderBoardLength][2];
		File scoresTextFile = new File(fileName);

		try {
			FileReader fileReader = new FileReader(scoresTextFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader); //Opens BufferedReader in the file

			for (int i = 0; i < leaderBoardLength; i++) {   //avoids reading file twice per instance (checking if null) while looping.
				String thisLine = bufferedReader.readLine();

				if (thisLine != null) {
					String[] line = thisLine.split(":");  //Breaks up the line into elements at ":"
					leaderBoardValues[i][0] = line[0];
					leaderBoardValues[i][1] = line[1];  //Assigns the values of the document to a 2D array
				} else {
					leaderBoardValues[i][0] = "No value";
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Scores could not be found");
		}
		return leaderBoardValues;
	}

	public void setScores(String fileName, Object[][] leaderBoardValues) {
		File scoresTextFile = new File(fileName);    //Where settings are to be written to

		try {
			FileWriter fileWriter = new FileWriter(scoresTextFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			for (int i = 0; i < leaderBoardValues.length; i++) {

				if (i != 0) {
					bufferedWriter.write("\n");
				}

				bufferedWriter.write(leaderBoardValues[i][0] + ":" + leaderBoardValues[i][1]);
			}
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.println("Scores could not be copied to the file");

		}
	}


	public void setSettings(String settingsFileName, String[] settingsData) {
		File settingsTextFile = new File(settingsFileName);  //sources to the file written to

		try {
			FileWriter fileWriter = new FileWriter(settingsTextFile);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			for (int i = 0; i < settingsData.length; i++) {

				if (i != 0) {
					bufferedWriter.write("\n"); //next line
				}
				bufferedWriter.write(settingsData[i]); //writes next settings value
			}
			bufferedWriter.close();
		} catch (IOException e) {
			System.out.println("Could not copy settingsData to the file");
		}
	}

	public String[] getArrayContents(String arrayFileName) {  //Inputs
		File arrayTextFile = new File(arrayFileName);
		int fileLength = getFileLength(arrayTextFile);
		String[] arrayItems = new String[fileLength];        //attains length of file and assigns length to an array
		try {
			FileReader fileReader = new FileReader(arrayTextFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			//avoids reading file twice per instance (checking if null) while looping.

			for (int i = 0; i < fileLength; i++) {
				arrayItems[i] = bufferedReader.readLine();        //Copies contents
			}

		} catch (IOException e) {
			System.out.println("Custom array contents could not be acquired");
			e.printStackTrace();     //notifies of failure
		}
		return arrayItems;
	}

	public void setCustomArrayContents(String customArrayFileName, String[] customArrayContents) {
		File arrayTextFile = new File(customArrayFileName);
		try {
			FileWriter fileWriter = new FileWriter(arrayTextFile);
			BufferedWriter writer = new BufferedWriter(fileWriter);

			for (int i = 0; i < customArrayContents.length; i++) {       //loops through the lines
				if (i != 0) {
					writer.write("\n");                 //writing each item of the customArray
				}
				writer.write(customArrayContents[i]);
			}
			writer.close();
		} catch (IOException e) {
			System.out.println("Could not write customArray to the file");
			e.printStackTrace();
		}
	}

	public char[][] getLevelLayout(String levelFileName, int levelWidth, int levelHeight) {
		File levelTextFile = new File(levelFileName);
		char[][] fileContents = new char[levelWidth][levelHeight];

		try {
			FileReader fileReader = new FileReader(levelTextFile);
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			for (int j = 0; j < levelHeight; j++) {

				for (int i = 0; i < levelWidth; i++) {      //loops through the 84 elements, recording the char from each
					int c;
					if ((c = bufferedReader.read()) != -1) {     //Testing that the item isn't null

						fileContents[i][j] = (char) c;

					} else {

						fileContents[i][j] = '-';        //If it is null, defaults the item to air
					}
				}
			}

		} catch (IOException e) {
			System.out.println("Level layout could not be acquired");
			e.printStackTrace();
		}
		return fileContents;
	}

	public int getFileLength(File arrayTextFile) {
		int i = 0;
		try {
			FileReader fileReader = new FileReader(arrayTextFile);        //Creates new file reader and BF reader
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while (bufferedReader.readLine() != null) {
				i++;        //Increments i for however many lines the file has.
			}
		} catch (IOException e) {
			System.out.println("File length not known");
			e.printStackTrace();
		}
		return i;
	}
}



