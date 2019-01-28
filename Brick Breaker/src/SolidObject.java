import javafx.scene.shape.Rectangle;

public abstract class SolidObject {
	//types of SolidObject
	public enum Type {
		SAFEWALL,	//1
		BADWALL, 	//2
		PADDLE,		//3
		ANIMAL,		//4
		ERROR
	}
	private Type _state;
	private Rectangle _boundBox;
	
	public SolidObject(Rectangle boundBox, int state) {
		_boundBox = boundBox;
		setType(state);
	}
	
	public void setBoundBox(Rectangle boundBox) {
		_boundBox = boundBox;
	}
	public void setType(int state) {
		switch(state) {
		case 1: _state = Type.SAFEWALL;
			break;
		case 2:	_state = Type.BADWALL;
			break;
		case 3:	_state = Type.PADDLE;
			break;
		case 4:	_state = Type.ANIMAL;
			break;
		default:	
			_state = Type.ERROR;
			System.out.println("INVALID SolidObject Type");
			break;
		}
	}
	
	/**
	 * @return Type of SolidObject
	 */
	public Type getType() {
		return _state;
	}
	
	/**
	 * @returns the boundBox of a SolidObject
	 */
	public Rectangle getBoundBox() {
		return _boundBox;
	}
	
	/**
	 * Checks collision between the boundbox of a solid object and the ball
	 */
	public boolean checkCollide(Ball ball, Rectangle boundBox) {
		final double centerX = ball.getCenterX();
		final double centerY = ball.getCenterY();
		final double radius = ball.getCircle().getRadius();
		
		//Rectangle around circle for checking collisions
		double [][] points = {
				{centerX - radius , centerY - radius},	//Upper left
				{centerX - radius , centerY + radius},	//Bottom Left
				{centerX + radius , centerY - radius},	//Upper Right
				{centerX + radius , centerY + radius}	//Bottom Right
		};
		
		//Check ahead for collisions in the direction of the velocity
		for (int i = 0; i < 10; i++) {
			final double [][] newPoints = new double[4][2];
			
			newPoints[0][0] = points[0][0] + ball.getXVel() * (.01d * i);
			newPoints[0][1] = points[0][1] + ball.getYVel() * (.01d * i);
			
			newPoints[1][0] = points[1][0] + ball.getXVel() * (.01d * i);
			newPoints[1][1] = points[1][1] + ball.getYVel() * (.01d * i);
			
			newPoints[2][0] = points[2][0] + ball.getXVel() * (.01d * i);
			newPoints[2][1] = points[2][1] + ball.getYVel() * (.01d * i);
			
			newPoints[3][0] = points[3][0] + ball.getXVel() * (.01d * i);
			newPoints[3][1] = points[3][1] + ball.getYVel() * (.01d * i);
			
			//If any of the projected points are within a boundbox return true
			for (double coordinate[] : newPoints) {
				if (boundBox.contains(coordinate[0],coordinate[1])){
					return true;
				}
			}
		}
		
		return false;
	}
	
}
