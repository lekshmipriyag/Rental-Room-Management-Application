package exception;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ReturnException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param input
	 */
	public ReturnException(String input) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Return Exception");
		alert.setHeaderText("Exception:");
		alert.setContentText("This Room cannot be return now - " + input);
		alert.showAndWait();
	}
}
