import javafx.scene.shape.Rectangle;

public class Wall extends SolidObject{

	private boolean _bottom;
	private int hitCounter;
	
	public Wall(Rectangle wall, boolean bottom) {
		super(wall,1);
		_bottom = bottom;
		if (bottom) {
			this.setType(2);
		}
	}
	/**
	 * 
	 * @return boolean: whether or not the wall is the bottom
	 */
	public boolean isBottom() {
		return _bottom;
	}
}
