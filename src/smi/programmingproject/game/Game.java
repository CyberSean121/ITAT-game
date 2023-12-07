package smi.programmingproject.game;
import smi.programmingproject.Main;
import smi.programmingproject.menus.DeathScreen;
import smi.programmingproject.menus.LevelSelectMenu;
import smi.programmingproject.menus.MainMenu;
import smi.programmingproject.menus.WinScreen;
import smi.programmingproject.utility.JFramer;
import smi.programmingproject.values.Level;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class Game implements KeyListener {
	private final JFrame frame;
	private final Main main;
	private final Color textColour = new Color(173, 33, 33);
	private final Color buttonColour = new Color(230, 230, 230);
	private final Color frameColour = new Color(100, 150, 200);
	private final int[][][] coords = new int[12][7][2];
	private final char[][] levelElements;
	private final String[] pauseWords = new String[]{"resume", "levelselect", "mainmenu", "quit"};
	private final String nonPermeable = "#_*";
	private final int levelWidth = 12;
	private final int levelHeight = 7;
	private final Random random = new Random();
	private final String[] imageNames = new String[]{"Air", "Attack", "Death", "Enemy", "Hero", "Key", "Ladder", "Patrol", "Rock", "Use", "Door"};
	private final Level level;
	private final DecimalFormat df = new DecimalFormat("0.00");
	private ArrayList<Enemy> enemyList = new ArrayList<>();
	private StringBuilder keysInput = new StringBuilder(16);
	private String[] actionWords = new String[6];
	private String[] queuedWords;
	private Image[] images;
	private String[] arrayContents;
	private boolean gotKey = false;
	private boolean grounded;
	private boolean quit = false;
	private boolean paused = false;
	private boolean ended = false;
	private float gravityMod = 1;
	private float timer;
	private int heroX;
	private int heroY;
	private int gravityCounter = 0;
	private int enemyPatrolCounter = 0;
	private int ticksUntilAction = 180;

	public Game(Main main, Level level) {

		this.main = main;
		this.level = level;

		frame = new JFramer().getFrame();        //creates a new frame
		frame.addKeyListener(this);
		frame.createBufferStrategy(3);    //adds keyListener and bufferStrategy for later use

		main.getWidgets().createPane(25, 25, 1200, 700, frameColour, false, frame);

		levelElements = main.getFileInfo().getLevelLayout("textFiles/level" + level + ".txt", levelWidth, levelHeight);

		switch (main.getGameSpeed()) {
			case QUICK:
				ticksUntilAction = (ticksUntilAction / 2);
				break;
			case MEDIUM:
				ticksUntilAction = ((ticksUntilAction * 2) / 3);
				break;
		}

		switch (main.getGravity()) {
			case HIGH:
				gravityMod = ((gravityMod * 2) / 3);
				break;
			case LOW:
				gravityMod = ((gravityMod * 3) / 2);
		}

		for (int i = 0; i < levelWidth; i++) {
			for (int j = 0; j < levelHeight; j++) {

				coords[i][j][0] = (i * 100) + 50;
				coords[i][j][1] = (j * 100) + 50;

				char element = levelElements[i][j];
				if (element == '@') { //hero found in level
					heroX = i;
					heroY = j;
					grounded = nonPermeable.indexOf(levelElements[heroX][heroY + 1]) != -1;
					levelElements[i][j] = '-';

				} else if (element == '*') {
					enemyList.add(new Enemy(i, j));
					levelElements[i][j] = '~';
				}

				//Extrapolates the coordinates of the squares for each tile
				//+50 so the coords are the centre of each square so elements can be centred by default
				//Objects can be adjusted from this default on an individual basis.
			}
		}
		ArrayList<String> filteredContents = new ArrayList<>();        //Temporary list that can easily be added to (dynamic length)

		if (main.getCustomArray()) {
			String[] unfilteredContents = main.getCustomArrayContents();        //Brings in custom array

			int[] lengthBoundaries = new int[]{0, 16};

			switch (main.getWordLength()) {        //Assigns the appropriate boundaries
				case SHORT:
					lengthBoundaries[1] = 6;
					break;
				case MEDIUM:
					lengthBoundaries[0] = 4;
					lengthBoundaries[1] = 12;
					break;
				case LONG:
					lengthBoundaries[0] = 8;
			}

			for (String unfilteredContent : unfilteredContents) {    //Adds all the words of correct length to the list
				int wordLength = unfilteredContent.length();
				if ((wordLength > lengthBoundaries[0]) && wordLength < lengthBoundaries[1]) {
					filteredContents.add(unfilteredContent);
				}
			}
			if (filteredContents.toArray().length > 6) {
				arrayContents = filteredContents.toArray(new String[0]);        //If there are enough words of the right length
			}                                                                    //Those words are used
		}

		if (arrayContents == null) {
			arrayContents = main.getDefaultArrayContents();
		}
		shuffle(arrayContents);        //Initial shuffle of the array

		queuedWords = new String[arrayContents.length - actionWords.length];

		System.arraycopy(arrayContents, 0, actionWords, 0, actionWords.length);        //First 6 words are actionWords
		System.arraycopy(arrayContents, actionWords.length, queuedWords, 0, arrayContents.length - actionWords.length);
		//Remainder go into queuedWords


		int imageNumber = Objects.requireNonNull(new File("images").listFiles()).length;
		images = new Image[imageNumber];        //Gets number of image files, more adaptable

		for (int i = 0; i < (images.length); i++) {

			StringBuilder name = new StringBuilder("images/").append(imageNames[i]).append(".png");

			try {
				images[i] = ImageIO.read(new File(name.toString()));        //Reads the file with imageNames[i] path

			} catch (IOException e) {
				System.out.println("Image not found");

				try {
					images[i] = ImageIO.read(new File("images/Death"));

				} catch (IOException f) {
					System.out.println("No image could be loaded");        //If it can, replaces image with that of Death.jpg
					f.printStackTrace();                                // If it can't, prints both stacktrace instances and prints message
				}
				e.printStackTrace();
			}
		}
	}


//	public void addObject(char item, xCoord, yCoord) {
//		if (levelElements[xCoord][yCoord] == '-') {
// 			levelElements[xCoord][yCoord] = item;
//	}

	public void run() {

		double tickRate = 1_000_000_000 / 60d;    //Setting the rate of ticks to 1 every 2.5 seconds
		double delta = 0;
		long last = System.nanoTime();

		while (!quit) {//Game loop, goes forever

			long now = System.nanoTime();
			delta += (now - last) / tickRate;    //delta = number of ticks to do
			last = now;        //carries forward remainder

			while (delta > 1) {
				tick();        //ticks
				delta--;
			}
			render();        //Renders every tick and every non-tick loop
		}
	}

	public void tick() {

		if (!paused) {

			timer = timer + (float) 1 / 60;
			enemyPatrolCounter++;

			if (!grounded) {    //checks if hero is airborne

				gravityCounter++;

				if (gravityCounter == ticksUntilAction * gravityMod) {        //Checks if hero has been falling 90 ticks
					heroY++;
					gravityCounter = 0;            //drops hero 1 tile
					for (int i = 0; i < enemyList.toArray().length; i++) {
						int[] enemyCoords = enemyList.get(i).getCoords();
						if ((enemyCoords[0] == heroX) && (enemyCoords[1] == heroY)) {
							quit = true;
							frame.dispose();        //Close current frame, end game loop
							new DeathScreen(main, level);
						}
					}

					if ((nonPermeable.indexOf(levelElements[heroX][heroY + 1]) != -1)) {
						grounded = true;    //re-evaluates if hero is airborne
					}
				}
			}

			if (enemyPatrolCounter == ticksUntilAction) {

				for (Enemy enemy : enemyList) {
					enemy.nextMove(levelElements);       //Changes enemy direction if necessary, moves enemy one square in its faced direction.
					enemyPatrolCounter = 0;
					int[] enemyPos = enemy.getCoords();
					if ((enemyPos[0] == heroX) && (enemyPos[1] == heroY)) {
						quit = true;
						frame.dispose();        //Close current frame, end game loop
						new DeathScreen(main, level);
					}
				}
			}
		}
	}

	public void render() {

		BufferStrategy bufferStrategy = frame.getBufferStrategy();
		Graphics g = bufferStrategy.getDrawGraphics();

		g.setColor(frameColour);
		g.fillRect(0, 0, frame.getWidth(), frame.getHeight());    //paints background

		//Static variables
		int style = Font.PLAIN;
		String font = "IMPACT";
		g.setFont(new Font(font, style, 20));
		g.setColor(Color.BLACK);

		int alignmentValue = 50;
		for (int i = 0; i < levelWidth; i++) {
			for (int j = 0; j < levelHeight; j++) {

				g.drawRect(coords[i][j][0] - 50, coords[i][j][1] - 50, 100, 100);
				//draws the grid square by square

				String tileItem = "Death";

				switch (levelElements[i][j]) {        //cycles through the array items
					case '-':
						tileItem = "Air";
						break;
					case '#':
						tileItem = "Rock";
						break;
					case '?':
						tileItem = "Key";
						break;
					case '!':
						tileItem = "Door";        //Checks the tile, and assigns its position the appropriate element
						break;
					case '~':
						tileItem = "Patrol";
						break;
					case '_':
						tileItem = "Ladder";
						break;
				}

				drawImage(g, tileItem, coords[i][j][0], coords[i][j][1], 100, 100, alignmentValue);    //displays the contents of that tile on the tile
			}
		}

		drawImage(g, "Hero", coords[heroX][heroY][0], coords[heroX][heroY][1], 75, 75, alignmentValue * 3 / 4);    //Draws hero over tiles

		for (Enemy enemy : enemyList) {
			int[] enemyCoords = enemy.getCoords();
			drawImage(g, "Enemy", coords[enemyCoords[0]][enemyCoords[1]][0], coords[enemyCoords[0]][enemyCoords[1]][1], 75, 75, alignmentValue * 3 / 4);
		}

		g.setFont(new Font(font, style, 30));

		int textOffset = 60;

		drawButtonIcon(g, 1336, 25, 175, 75, textOffset * 3 / 4, "Pause [esc]");        //Pause icon

		drawButtonIcon(g, 1336, 125, 175, 75, textOffset * 3 / 4, String.valueOf(df.format(timer)));            //Timer

		g.setFont(new Font(font, style, 25));

		drawButtonIcon(g, 1055, 735, 150, 100, textOffset, actionWords[0]);        //Left icon

		drawButtonIcon(g, 1365, 735, 150, 100, textOffset, actionWords[1]);        //Right icon

		drawButtonIcon(g, 1210, 630, 150, 100, textOffset, actionWords[2]);        //Jump icon

		drawButtonIcon(g, 1210, 735, 150, 100, textOffset, actionWords[3]);        //Drop icon

		drawButtonIcon(g, 50, 735, 150, 100, textOffset, actionWords[4]);        //Attack icon

		drawButtonIcon(g, 310, 735, 150, 100, textOffset, actionWords[5]);        //Use icon

		drawImage(g, "Attack", 200, 735, 100, 100, 0);        //Attack image

		drawImage(g, "Use", 460, 735, 100, 100, 0);        //Use image

		if (!ended) {
			drawButtonIcon(g, 600, 735, 230, 100, textOffset, keysInput.toString());        //Type display
		}

		if (paused && ended) {
			g.setColor(Color.BLACK);
			g.drawRect(400, 160, 700, 450);        //Endgame block
			g.setColor(new Color(20, 120, 220));
			g.fillRect(500, 210, 500, 350);

			drawButtonIcon(g, 560, 260, 375, 100, textOffset, "Level finished, please enter initials.");        //Resume icon

			drawButtonIcon(g, 560, 410, 175, 100, textOffset, "Time : " + df.format(timer));        //Level select icon

			drawButtonIcon(g, 765, 410, 175, 100, textOffset, keysInput.toString());        //Quit icon

		} else if (paused) {
			g.setColor(Color.BLACK);
			g.drawRect(500, 210, 500, 350);        //Pause block
			g.setColor(new Color(20, 120, 220));
			g.fillRect(500, 210, 500, 350);

			drawButtonIcon(g, 560, 260, 175, 100, textOffset, "resume");        //Resume icon

			drawButtonIcon(g, 560, 410, 175, 100, textOffset, "levelselect");        //Level select icon

			drawButtonIcon(g, 765, 260, 175, 100, textOffset, "mainmenu");        //Main menu icon

			drawButtonIcon(g, 765, 410, 175, 100, textOffset, "quit");        //Quit icon
		}

		bufferStrategy.show();
		g.dispose();
	}

	public void shuffle(String[] array) {
		for (int i = 0; i < array.length - 1; i++) {

			int randomNumber = random.nextInt(array.length - 1) + 1;
			String spareWord = array[randomNumber];
			array[randomNumber] = array[i];
			array[i] = spareWord;
		}
		//Shuffle algorithm
		//queuedList will be shuffled every action, after a word is added.
		//Each element, in turn, will be swapped with another element that is not lower in array position.
	}

	public void drawImage(Graphics g, String name, int xCoord, int yCoord, int width, int height, int alignment) {
		Image image = null;
		for (int i = 0; i < images.length; i++) {
			if (Objects.equals(name, imageNames[i])) {
				image = images[i];
				break;
			}
		}
		g.drawImage(image, xCoord - alignment, yCoord - alignment, width, height, null);
	}

	public void drawButtonIcon(Graphics g, int xCoord, int yCoord, int width, int height, int textOffset, String text) {
		g.setColor(buttonColour);
		g.fillRect(xCoord, yCoord, width, height);
		g.setColor(Color.BLACK);                        //Creates a box, outlines it, adds text.
		g.drawRect(xCoord, yCoord, width, height);
		g.setColor(textColour);
		g.drawString(text, xCoord, yCoord + textOffset);
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {

		if (ended) {
			System.out.println("ENDED");

			if (Character.isAlphabetic(e.getKeyChar()) && (keysInput.length() > 3)) {
				keysInput.append(e.getKeyChar());
				System.out.println(keysInput);
				if (keysInput.length() == 3) {
					System.out.println("Submit?");
					String initials = keysInput.toString().toUpperCase(Locale.ROOT);
					int levelNumber = 0;
					switch (level) {
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

					Object[] leaderboardValues = new Object[2];
					leaderboardValues[0] = initials;
					leaderboardValues[1] = df.format(timer);

					main.getLeaderBoards()[levelNumber].submitValue(leaderboardValues);
					quit = true;
					frame.dispose();		//Ends game process, win screen
					new WinScreen(main, level);
				}
			}
		} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			String keysInputString = keysInput.toString();

			keysInput.delete(0, 15);

			if (!paused) {
				int wordUsed = -1;

				String permeable = "-_~!?";
				if (keysInputString.equalsIgnoreCase(actionWords[0]) && (permeable.indexOf(levelElements[heroX - 1][heroY]) != -1)) {
					heroX--;		//Moves character left
					wordUsed = 0;

				} else if (keysInputString.equalsIgnoreCase(actionWords[1]) && (permeable.indexOf(levelElements[heroX + 1][heroY]) != -1)) {
					heroX++;		//Moves character right
					wordUsed = 1;

				} else if (keysInputString.equalsIgnoreCase(actionWords[2]) && (nonPermeable.indexOf(levelElements[heroX][heroY + 1]) != -1)) {
					heroY--;		//Jumps character
					wordUsed = 2;

				} else if (keysInputString.equalsIgnoreCase(actionWords[3]) && (permeable.indexOf(levelElements[heroX][heroY + 1]) != -1)) {
					heroY++;		//Drops character
					wordUsed = 3;

				} else if (keysInputString.equalsIgnoreCase(actionWords[4])) {

					for (int i = 0; i < enemyList.toArray().length; i++) {
						int[] enemyCoords = enemyList.get(i).getCoords();

						if ((enemyCoords[0] < heroX + 2) && (enemyCoords[0] > heroX - 2) && (enemyCoords[1] < heroY + 2) && (enemyCoords[1] > heroY - 2)) {
							enemyList.remove(i);		//Kills surrounding enemies
							wordUsed = 4;
						}
					}

				} else if (keysInputString.equalsIgnoreCase(actionWords[5])) {    //Use

					for (int i = heroX - 1; i < heroX + 2; i++) {

						for (int j = heroY - 1; j < heroY + 2; j++) {

							if ((levelElements[i][j] == '!') && (gotKey)) {
								paused = true;        //Opens door, completes level
								ended = true;
								break;
							}
						}
					}

					for (int i = heroX - 1; i < heroX + 2; i++) {

						for (int j = heroY - 1; j < heroY + 2; j++) {

							if (levelElements[i][j] == '?') {
								levelElements[i][j] = '-';
								gotKey = true;        //Picks up key
								wordUsed = 5;
								break;
							}
						}
					}	//Ordered door THEN key because that way the player can't pick up the key and use it in one turn
				}

				for (int i = 0; i < enemyList.toArray().length; i++) {
					int[] enemyCoords = enemyList.get(i).getCoords();    //Check against all enemies
					if ((enemyCoords[0] == heroX) && (enemyCoords[1] == heroY)) {
						quit = true;
						frame.dispose();    //Clear current frame and halt loop
						new DeathScreen(main, level);
						wordUsed = 5;
						break;
					}
				}

				if (wordUsed != -1) {
					String spareWord = actionWords[wordUsed];
					actionWords[wordUsed] = queuedWords[0];
					queuedWords[0] = spareWord;
					shuffle(queuedWords);
				}

				if (nonPermeable.indexOf(levelElements[heroX][heroY + 1]) != -1) {
					grounded = true;

				} else if (permeable.indexOf(levelElements[heroX][heroY + 1]) != -1) {
					grounded = false;
				}

			}
			if (!ended) {
				if (keysInputString.equalsIgnoreCase(pauseWords[0])) {
					paused = false;

				} else if (keysInputString.equalsIgnoreCase(pauseWords[1])) {
					frame.dispose();
					new LevelSelectMenu(main);        //Directs to level select menu

				} else if (keysInputString.equalsIgnoreCase(pauseWords[2])) {
					frame.dispose();
					new MainMenu(main);        //Directs to main menu

				} else if (keysInputString.equalsIgnoreCase(pauseWords[3])) {
					quit = true;
					frame.dispose();        //Ends program
				}
			}

		} else if ((e.getExtendedKeyCode() == KeyEvent.VK_BACK_SPACE) && (!keysInput.toString().equals(""))) {
			keysInput.deleteCharAt(keysInput.length() - 1);        //Backspace deletes last character in the StringBuilder

		} else if (Character.isAlphabetic(e.getKeyChar())) {
			keysInput.append(e.getKeyChar());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			paused = !paused;
			System.out.println("Pause toggled");
			keysInput.delete(0, 15);
		}
	}
}


