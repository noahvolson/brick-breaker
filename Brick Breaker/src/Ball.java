import java.awt.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * Class that implements a ball with a position and velocity.
 */
public class Ball {
	// Constants
	/**
	 * Starting y position.
	 */
	public static final int Y_DISTANCE = (GameImpl.HEIGHT/2) + 10;
	/**
	 * The radius of the ball.
	 */
	public static final int BALL_RADIUS = 8;
	/**
	 * The initial velocity of the ball in the x direction.
	 */
	public static final double INITIAL_VX = 1e-7;
	/**
	 * The initial velocity of the ball in the y direction.
	 */
	public static final double INITIAL_VY = 1e-7;

	// Instance variables
	// (x,y) is the position of the center of the ball.
	private double _x, _y;
	private double _vx, _vy;
	private Circle circle;
	
	/**
	 * @return the Circle object that represents the ball on the game board.
	 */
	public Circle getCircle () {
		return circle;
	}

	/**
	 * Constructs a new Ball object at the centroid of the game board
	 * with a default velocity that points down and right.
	 */
	public Ball () {
		_x = GameImpl.WIDTH/2;
		_y = (GameImpl.HEIGHT/2) + 50;
		_vx = INITIAL_VX;
		_vy = INITIAL_VY;

		circle = new Circle(BALL_RADIUS, BALL_RADIUS, BALL_RADIUS);
		circle.setLayoutX(_x - BALL_RADIUS);
		circle.setLayoutY(_y - BALL_RADIUS);
		circle.setFill(Color.BLACK);
		
	}
	
	/**
	 * @return the x position of the center of the ball.
	 */
	public double getCenterX() {
		return _x;
	}
	
	/**
	 * @return the y position of the center of the ball.
	 */
	public double getCenterY() {
		return _y;
	}
	
	/**
	 * @return the velocity of the ball in the x direction.
	 */
	public double getXVel() {
		return _vx;
	}
	
	/**
	 * @return the velocity of the ball in the y direction.
	 */
	public double getYVel() {
		return _vy;
	}
	
	/**
	 * Called when the ball bounces on a vertical wall, 
	 * reverses movement in the x direction.
	 */
	public void vBounce() {
		_vx *= -1;
	}
	
	/**
	 * Called when the ball bounces on a horizontal wall, 
	 * reverses movement in the y direction.
	 */
	public void hBounce() {
		_vy *= -1;
		
		
	}
	
	/**
	 * Accelerates the ball
	 */
	public void accelerate() {
		_vy *= 1.1;
		_vx *= 1.1;
	}
	
	
	/**
	 * Updates the position of the ball, given its current position and velocity,
	 * based on the specified elapsed time since the last update.
	 * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
	 */
	public void updatePosition (long deltaNanoTime) {
		double dx = _vx * deltaNanoTime;
		double dy = _vy * deltaNanoTime;
		
		if((_x + dx) > 400 || (_x + dx) < 0) {	//Prevents the ball from going through walls
			vBounce();
		} else {
			_x += dx;
			circle.setTranslateX(_x - (circle.getLayoutX() + BALL_RADIUS));
		}
		if ((_y + dy) < 0) {
			hBounce();
		} else {
			_y += dy;
			circle.setTranslateY(_y - (circle.getLayoutY() + BALL_RADIUS));
		}
	}
}
