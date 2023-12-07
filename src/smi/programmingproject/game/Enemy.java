package smi.programmingproject.game;

public class Enemy {

	int[] coords = new int[2];
	boolean left = true;

	public Enemy(int enemyX, int enemyY) {
		coords[0] = enemyX;
		coords[1] = enemyY;
	}

	public void nextMove(char[][] levelElements) {
		int offset = 1;

		if (left) {
			offset = -1;
		}

		if (levelElements[coords[0] + offset][coords[1]] != '~') {
			left = !left;
			offset = offset * -1;
		}
		coords[0] = coords[0] + offset;
	}

	public int[] getCoords() {
		return coords;
	}
}
