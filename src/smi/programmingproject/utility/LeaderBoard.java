package smi.programmingproject.utility;

public class LeaderBoard {

	protected String[][] leaderBoardContents;
	private final int leaderBoardLength;

	public LeaderBoard(String[][] leaderBoardContents) {    //Inputs : 2D Object array   Outputs : None (constructor)
		this.leaderBoardContents = leaderBoardContents;        // Preconditions : leaderBoardContents has been extrapolated from customArrayFile
		leaderBoardLength = leaderBoardContents.length;
	}

	public void submitValue(Object[] leaderBoardValues) {    //Inputs : Object array

		for (int i = 0; i < leaderBoardContents.length; i++) {
			System.out.println("Submit!");

			if (Integer.parseInt((String) leaderBoardValues[1]) < Integer.parseInt(leaderBoardContents[i][1])) {

				for (; i < (leaderBoardLength); i++) {        //For every item in the leaderboard, checks the score against the imputed score.
					String[] holdingArray = leaderBoardContents[i];        //Then, when an instance is found where
					leaderBoardContents[i] = (String[]) leaderBoardValues;
					leaderBoardValues = holdingArray;
				}
				break;
			}
		}
	}

	public String[][] getLeaderBoardContents() {
		return leaderBoardContents;
	}
}
