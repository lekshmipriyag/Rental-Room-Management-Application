package exception;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RentException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param input
	 */
	public RentException(String input) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Rent Exception");
		alert.setHeaderText("Exception:");
		alert.setContentText("This Room cannot be rent now - " + input);
		alert.showAndWait();
	}
}
