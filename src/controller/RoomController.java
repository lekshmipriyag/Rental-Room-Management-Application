package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import database.DBStore;
import exception.InvalidException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Room;
import model.Standard;
import model.Suite;

public class RoomController {

	
	@FXML
	TextField txtRoomType, txtRoomID;

	@FXML
	RadioButton radioBedroom;

	@FXML
	ToggleGroup bedroom;

	@FXML
	TextArea txtFeatureSummary, txtDescription;

	@FXML
	DatePicker lastMaintenaceDateID;

	@FXML
	ImageView ImageViewID, roomImage;

	@FXML
	Label imageNameRoom;

	
	@FXML
	String roomCategory;
	String cityroomID;
	int bedRoomCount;
	String roomFeatureSummary = "";
	String roomDescription;
	String cityroomStatus;
	LocalDate lastMaintenanceDate = null;
	String upladedRoomImage = "no-Image-Available.png";
	double roomrentFee = 0.00;
	double lateFee =  0.00;
	private ArrayList<Room> roomList = new ArrayList<Room>();
	Date maintenanceDate = null;
	String date = "";
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	@FXML
	public void initialize() throws Exception {
			System.out.println("Inside Room Controller");
		
		}
	
	@FXML
	public void addStandardRoom(ActionEvent event) throws InvalidException, ClassNotFoundException, SQLException {

		roomCategory = txtRoomType.getText().toUpperCase();
		Random rand = new Random();
		// Generate random three integers for room id.
		int rand_int1 = rand.nextInt(900) + 100;
		cityroomID = roomCategory.equals("STANDARD") ? "R_" + rand_int1 : "S_" + rand_int1;
		txtRoomID.setText(cityroomID);
		
		// User input Validations
		RadioButton radioBedroom = (RadioButton) bedroom.getSelectedToggle();
		
		if(radioBedroom == null) {
				throw new InvalidException("select bedroomCount");
		}else {
			bedRoomCount = Integer.parseInt(radioBedroom.getText());
		}
		
		roomFeatureSummary = txtFeatureSummary.getText();
		if(roomFeatureSummary.equalsIgnoreCase("")) {
			throw new InvalidException("Please add some room features");
		}
		
		roomDescription = txtDescription.getText();
		if(roomDescription.equalsIgnoreCase("")) {
			throw new InvalidException("Please add room description");
		}

		cityroomStatus = "AVAILABLE";

		if (bedRoomCount == 1) {
			roomrentFee = 59.00;
		} else if (bedRoomCount == 2) {
			roomrentFee =  99.00;
		} else {
			roomrentFee =  199.00;
		}
		lateFee = (roomrentFee * 1.35);

		Room roomModelObj = new Standard(cityroomID, bedRoomCount, roomFeatureSummary, roomCategory, cityroomStatus,
				roomrentFee, lateFee, upladedRoomImage, roomDescription);
		DBStore dbstore = new DBStore();
		dbstore.addRoom(roomModelObj);
	    this.addRoomDetails(roomModelObj);
	    successAlert();
	    clearStandardInputs();
	}

	@FXML
	public void addSuiteRoom(ActionEvent event)  throws Exception {

		roomCategory = txtRoomType.getText().toUpperCase();
		Random rand = new Random();
		// Generate random three integers for room id.
		int rand_int1 = rand.nextInt(900) + 100;
		cityroomID = roomCategory.equals("STANDARD") ? "R_" + rand_int1 : "S_" + rand_int1;
		txtRoomID.setText(cityroomID);
		bedRoomCount = 6;
	System.out.println(upladedRoomImage);
  // User Input validations
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (lastMaintenaceDateID.getValue() == null) {
			throw new InvalidException("Please select Maintenance Date");
		}
		date = lastMaintenaceDateID.getValue().toString();
		maintenanceDate = df.parse(date);
		Date todayDate = new Date();
		long difference = (todayDate.getTime() - maintenanceDate.getTime()) / 86400000;
		
		if (difference < 0) {
			throw new InvalidException("Incorrect Maintenance Date. It should be today or previous date");
			
		}
		roomFeatureSummary = txtFeatureSummary.getText();
		if(roomFeatureSummary.equalsIgnoreCase("")) {
			throw new InvalidException("Please add some room features");
		}
	
		roomDescription = txtDescription.getText();
		if(roomDescription.equalsIgnoreCase("")) {
			throw new InvalidException("Please add room description");
		}
		cityroomStatus = "AVAILABLE";
		roomrentFee =  999.00;
		lateFee =  1099.00;
		Room roomModelObj = new Suite(cityroomID, bedRoomCount, roomFeatureSummary, roomCategory, cityroomStatus,
				maintenanceDate, roomrentFee, lateFee, upladedRoomImage, roomDescription);
		DBStore dbstore = new DBStore();
		dbstore.addRoom(roomModelObj);
		this.addRoomDetails(roomModelObj); 
		successAlert();
	 clearSuiteInputs();
	  
	}

//After submitting clear all existing data of standard room
	public void clearStandardInputs() {
		txtRoomID.setText("");
		radioBedroom.setText("");
		txtFeatureSummary.setText("");
		txtDescription.setText("");
		upladedRoomImage = "";
	}
	//After submitting clear all existing data of suite room
	public void clearSuiteInputs() {
		 txtRoomID.setText("");
		 lastMaintenaceDateID.setValue(null);
		 txtFeatureSummary.setText("");
		 upladedRoomImage = "";
		 txtDescription.setText("");
	}
	
	
	@FXML
	public String imageUploadAction() throws IOException, InvalidException {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select Image");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Web pages", "*.png", "*.jpeg", "*.jpg"));
		File file = fileChooser.showOpenDialog(null);

		if (file != null) {

			upladedRoomImage = file.getName();
			System.out.println(upladedRoomImage);
			final String dir = System.getProperty("user.dir");
			
			System.out.println("current dir = " + dir + "\\src\\images\\");
			Path from = Paths.get(file.toURI());
			Path to = Paths.get(dir + "\\src\\images\\" + upladedRoomImage);
			CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING,
					StandardCopyOption.COPY_ATTRIBUTES };
			Files.copy(from, to, options);
		}else {
			throw new InvalidException("No File chosen");
		}

		return upladedRoomImage;

	}
	
	public void addRoomDetails(Room room) {
		// add all rooms temporary arraylist
		roomList.add(room);
	}

	public ArrayList<Room> getRoomList() {
		return roomList;
	}
	
	/**
	 * For showing success message
	 */
	public void successAlert ()
	{
		Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Room Successfully added");
        alert.setHeaderText("SUCCESS");
        alert.showAndWait();
	}
}
