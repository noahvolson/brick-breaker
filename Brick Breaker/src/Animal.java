import java.io.IOException;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Animal extends SolidObject{
	private final Label _imageLabel;
	
	public Animal(Image image, int x, int y) throws IOException {
		
		//boundbox
		super (new Rectangle(x - image.getWidth()/2 ,y - image.getHeight()/2, image.getWidth(), image.getHeight()), 4);
		
		//imageLabel for display
		_imageLabel = new Label("", new ImageView(image));
		_imageLabel.setLayoutX(x - image.getWidth()/2);
		_imageLabel.setLayoutY(y - image.getHeight()/2);
	}
	
	/**
	 * @return the image label of the object
	 * This is used to display images of animals
	 */
	public Label getImageLabel() {
		return _imageLabel;
	}
}
