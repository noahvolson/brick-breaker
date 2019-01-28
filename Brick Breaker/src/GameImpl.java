import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.animation.AnimationTimer;
import javafx.scene.input.MouseEvent;
import javafx.event.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class GameImpl extends Pane implements Game {
	/**
	 * Defines different states of the game.
	 */
	public enum GameState {
		WON, LOST, ACTIVE, NEW
	}

	// Constants
	/**
	 * The width of the game board.
	 */
	public static final int WIDTH = 400;
	/**
	 * The height of the game board.
	 */
	public static final int HEIGHT = 600;

	// Instance variables
	private Ball _ball;
	private Paddle _paddle;
	private SolidObject[][] _allSolid;
	private int _animalCount;	//number of animals remaining
	private int _lossCount;		//number of times the _ball has hit the bottom
	
	/**
	 * Constructs a new GameImpl.
	 */
	public GameImpl () {
		setStyle("-fx-background-color: white;");

		restartGame(GameState.NEW);
	}

	/**
	 * Returns the name of the game.
	 */
	public String getName () {
		return "Zutopia";
	}
	
	/**
	 * Returns the pane containing the game.
	 */
	public Pane getPane () {
		return this;
	}

	/**
	 * resets the game: resets ball acceleration, animals, loss counter, 
	 * instantiates walls, paddle, and event handlers
	 * @param current game state
	 */
	private void restartGame (GameState state) {
		getChildren().clear();  // remove all components from the game
		_lossCount = 0;
		
		initBall();
		initPaddle();
		initAnimals();
		Label startLabel = initMessage(state);
		initEventHandlers(startLabel);
		
		//Create and add walls
		_allSolid [4][0] = new Wall(new Rectangle(-15,0,15,600), false);	//left
		_allSolid [4][1] = new Wall(new Rectangle(400,0,15,600), false);	//right
		_allSolid [4][2] = new Wall(new Rectangle(0,-15,400,15), false);	//top
		_allSolid [4][3] = new Wall(new Rectangle(0,600,400,15), true); 	//bottom

	}
	/**
	 * Initializes animals on the play space
	 */
	private void initAnimals() {
		//Create and add animals ...
				Random random = new Random();
				String[] animImagNames = {"duck.jpg", "horse.jpg", "goat.jpg"};
				_allSolid = new SolidObject[6][4];
				
				int x = 0;
				int	y = 50;
				int randNum;
				for(int rowNum = 0; rowNum < 4; rowNum++) {
					for(int colNum = 0; colNum < 4 ; colNum++) {
						x += 80;
						randNum = random.nextInt(3);
						try {
							Image image = new Image(new FileInputStream(new File(animImagNames[randNum])));
							_allSolid[rowNum][colNum] = new Animal(image, x,y);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						getChildren().add(((Animal) _allSolid[rowNum][colNum]).getImageLabel());
						
					}
					x = 0;
					y += 80;
				}
	}
	/**
	 * adds a message to the play space based on game state
	 * @param current game state
	 */
	private Label initMessage(GameState state) {
		// Add start message
		final String message;
		if (state == GameState.LOST) {
			message = "Game Over\n";
		} else if (state == GameState.WON) {
			message = "You won!\n";
		} else {
			message = "";
		}
		final Label startLabel = new Label(message + "Click mouse to start");
		startLabel.setLayoutX(WIDTH / 2 - 50);
		startLabel.setLayoutY(HEIGHT / 2 + 100);
		getChildren().add(startLabel);
		return startLabel;
	}
	
	/**
	 * adds the paddle to the play space.
	 */
	private void initPaddle() {
		// Create and add _paddle
		_paddle = new Paddle();
		getChildren().add(_paddle.getRectangle());  // Add the _paddle to the game board
	}
	
	/**
	 * adds the ball to the play space
	 */
	private void initBall() {
	// Create and add _ball
		_ball = new Ball();
		getChildren().add(_ball.getCircle());  // Add the _ball to the game board
	}
	
	/**
	 * Initializes event handlers for paddle movement and clicking.
	 * @param startLabel containing starting message
	 */
	private void initEventHandlers(Label startLabel) {
	// Add event handler to start the game
		setOnMouseClicked(new EventHandler<MouseEvent> () {
				@Override
				public void handle (MouseEvent event) {
					GameImpl.this.setOnMouseClicked(null);
					
					// As soon as the mouse is clicked, remove the startLabel from the game board
					getChildren().remove(startLabel);
					run();
					}
		});

				//another event handler to steer _paddle 
		setOnMouseMoved(new EventHandler<MouseEvent> (){
				@Override
				public void handle(MouseEvent event) {
				double centerX = event.getSceneX();
				double centerY = event.getSceneY();
				_paddle.moveTo(centerX, centerY);
		
				_allSolid[5][0] = _paddle;
				//System.out.println(event.getSceneX() +"\t"+ event.getSceneY());
				}
					
		});

	}
	
	/**
	 * Begins the game-play by creating and starting an AnimationTimer.
	 */
	public void run () {
		// Instantiate and start an AnimationTimer to update the component of the game.
		new AnimationTimer () {
			private long lastNanoTime = -1;
			public void handle (long currentNanoTime) {
				if (lastNanoTime >= 0) {  // Necessary for first clock-tick.
					GameState state;
					if ((state = runOneTimestep(currentNanoTime - lastNanoTime)) != GameState.ACTIVE) {
						// Once the game is no longer ACTIVE, stop the AnimationTimer.
						stop();
						// Restart the game, with a message that depends on whether
						// the user won or lost the game.
						restartGame(state);
					}
				}
				// Keep track of how much time actually transpired since the last clock-tick.
				lastNanoTime = currentNanoTime;
			}
		}.start();
	}
	/**
	 * Updates the state of the game at each timestep. In particular, this method should
	 * move the _ball, check if the _ball collided with any of the animals, walls, or the _paddle, etc.
	 * @param deltaNanoTime how much time (in nanoseconds) has transpired since the last update
	 * @return the current game state
	 */
	public GameState runOneTimestep (long deltaNanoTime) {
		for (int row = 0; row < 6; row++) {
			for (int col = 0; col < 4; col++) {
				if (_allSolid[row][col] != null) {
					if (_allSolid[row][col].checkCollide(_ball, _allSolid[row][col].getBoundBox())) {
						switch(_allSolid[row][col].getType()) {
						case SAFEWALL:
							if (_allSolid[row][col].getBoundBox().getHeight() == 15) {	//check whether the wall is vertical or horizontal
								_ball.hBounce();
							}else {
								_ball.vBounce();
							}
							break;
						case BADWALL:
							_lossCount++;
							if(_lossCount == 5) {
								return GameState.LOST;	//at 5 hits, you lose the game
							}
							_ball.hBounce();
							break;
						case PADDLE:
							_ball.hBounce();
							break;
						case ANIMAL:
							Animal forRemoval = (Animal)_allSolid[row][col];
							getChildren().remove(forRemoval.getImageLabel());
							_allSolid[row][col] = null;
							_ball.accelerate();
							_animalCount = 0;
							for(int rowAnim = 0; rowAnim < 4; rowAnim++) {	//when an animal "teleports" it is replaced with null
								for (int colAnim = 0; colAnim < 4; colAnim++) {
									if(_allSolid[rowAnim][colAnim] != null) {
										_animalCount++;
									}
								}
							}
							if (_animalCount == 0) {	//if there are no animals left you win
								return GameState.WON;
							}
							break;
						default:
							break;
						}
					}	
				}
			}
		}
		_ball.updatePosition(deltaNanoTime);
		
		return GameState.ACTIVE;
	}
}
