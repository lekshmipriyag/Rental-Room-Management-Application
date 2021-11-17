package exception;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class InvalidException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param input
	 */
	public InvalidException(String input) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Invalid Exception");
		alert.setHeaderText("Exception:");
		alert.setContentText("Invalid Input - " + input);
		alert.showAndWait();
	}
}
