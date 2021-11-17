package exception;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class MaintenanceException extends Exception {

	private static final long serialVersionUID = 1L;

	public MaintenanceException(String input) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Maintenance Exception");
		alert.setHeaderText("Exception:");
		alert.setContentText("Invalid Input - " + input);
		alert.showAndWait();
	}

}
