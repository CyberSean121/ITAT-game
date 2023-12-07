package smi.programmingproject.utility;

public class GlobalLeaderBoard extends LeaderBoard {
	protected Object[][] leaderBoardContents;

	public GlobalLeaderBoard(String[][] leaderBoardContents) {
		super(leaderBoardContents);

	}


	public void submitValue(Object[][] leaderBoardValues) {

		for (int i = 0; i < leaderBoardContents.length; i++) {

			if ((int) leaderBoardValues[i][1] > (int) leaderBoardContents[i][1]) {
				leaderBoardContents[i] = leaderBoardValues[i];
			}
		}
	}
}
