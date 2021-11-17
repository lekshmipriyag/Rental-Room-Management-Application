package controller;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import database.DBStore;
import exception.InvalidException;
import exception.MaintenanceException;
import exception.RentException;
import exception.ReturnException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.HiringRecord;
import model.Room;

public class RoomDetailController {

	@FXML
	BorderPane homeBorderPane;

	@FXML
	GridPane rentGrid, returnGrid;

	@FXML
	Label typeRoom, bedRoom, rentTitle, maintenanceStatus, completionDatelbl;

	@FXML
	ImageView roomImage;

	@FXML
	TextField hiddenRoomID, hiddenRentFee, custID, hiddenRoomStatus, mainCompleteDate;

	@FXML
	TextField hiddenRoomType, returnCustomerID;

	@FXML
	DatePicker fromDate, toDate, returnDategrid;

	@FXML
	DatePicker completeMaintDate;

	@FXML
	private TableView<HiringRecord> rentHistory = new TableView<>();

	@FXML
	private TableColumn<HiringRecord, String> recordID;

	@FXML
	private TableColumn<HiringRecord, Date> rentDate;

	@FXML
	private TableColumn<HiringRecord, Date> estimatedDate;

	@FXML
	private TableColumn<HiringRecord, Date> actualReturnDate;

	@FXML
	private TableColumn<HiringRecord, String> customerID;

	@FXML
	private TableColumn<HiringRecord, Double> rentFee;

	@FXML
	private TableColumn<HiringRecord, Double> lateFee;

	@FXML
	private TableColumn<HiringRecord, Double> totalRentFee;

	@FXML
	private Button Maintenance, goBtn, completeMaint;

	@FXML
	ArrayList<HiringRecord> hiringList = new ArrayList<HiringRecord>();

	@FXML
	String roomCategory;
	String cityroomID;
	int bedRoomCount;
	String roomFeatureSummary;
	String roomDescription;
	String cityroomStatus;
	LocalDate lastMaintenanceDate = null;
	Room room = null;

	/**
	 * Setting selected room data to room details page
	 * 
	 * @param roomObj
	 * @throws Exception
	 */
	public void setData(Room roomObj) throws Exception {
		String roomDbImage = roomObj.getImage_name();
		hiddenRoomID.setText(roomObj.getRoomID());
		hiddenRoomStatus.setText(roomObj.getRoomStatus());
		hiddenRoomType.setText(roomObj.getRoomType());
		String lateFee = String.valueOf(roomObj.getLateFee());
		hiddenRentFee.setText(lateFee);
		if (roomDbImage != "") {
			final String dir = System.getProperty("user.dir");
			String currenDir = dir + "\\src\\images\\" + roomDbImage;
			Image image = new Image(new FileInputStream(currenDir));
			roomImage.setFitHeight(350);
			roomImage.setFitWidth(350);
			roomImage.setImage(image);
		}
		typeRoom.setText(roomObj.getRoomType() + " Room");
		bedRoom.setText("No.Of Bedroom(s) : " + roomObj.getTotalBedrooms() + "\n" + "Currently "
				+ roomObj.getRoomStatus() + "\n" + "$ " + roomObj.getRentFee() + " /- Per Night" + "\nFeatures : "
				+ roomObj.getFeatureSummary() + ".\n" + "About this room : " + roomObj.getDescription() + ".");

		selectRoomHistory(roomObj.getRoomID());
		if (roomObj.getRoomStatus().toUpperCase().equalsIgnoreCase("AVAILABLE")) {
			rentTitle.setText("Rent this room here ...");
			rentGrid.setVisible(true);
			returnGrid.setVisible(false);
			Maintenance.setVisible(true);

		}
		if (roomObj.getRoomStatus().toUpperCase().equalsIgnoreCase("RENTED")) {
			rentTitle.setText("Return this room here ...");
			rentGrid.setVisible(false);
			returnGrid.setVisible(true);
			returnCustomerID.setDisable(true);
			Maintenance.setVisible(false);
			selectRoomHistory(roomObj.getRoomID());

		}
		if (roomObj.getRoomStatus().toUpperCase().equalsIgnoreCase("MAINTENANCE")) {
			rentTitle.setText("");
			rentGrid.setVisible(false);
			returnGrid.setVisible(false);
			maintenanceStatus.setText("Under Maintenance");
			Maintenance.setVisible(false);
			completeMaint.setVisible(true);
			selectRoomHistory(roomObj.getRoomID());
		}
	}

	@FXML
	public ObservableList<HiringRecord> getRoomHistoryDetails(String roomID) throws Exception {
		DBStore dbstore = new DBStore();

		ObservableList<HiringRecord> hiringList = FXCollections
				.observableArrayList(dbstore.findAllHiringRecords(roomID));
		recordID.setCellValueFactory(new PropertyValueFactory<HiringRecord, String>("recordID"));
		customerID.setCellValueFactory(new PropertyValueFactory<HiringRecord, String>("customerID"));
		rentDate.setCellValueFactory(new PropertyValueFactory<HiringRecord, Date>("rentDate"));
		estimatedDate.setCellValueFactory(new PropertyValueFactory<HiringRecord, Date>("estimatedDate"));
		actualReturnDate.setCellValueFactory(new PropertyValueFactory<HiringRecord, Date>("actualReturnDate"));
		NumberFormat currencyFormat = NumberFormat.getInstance();

		// for howing Two decimal digits of rent FEE
		rentFee.setCellFactory(tc -> new TableCell<HiringRecord, Double>() {

			@Override
			protected void updateItem(Double rentFee, boolean empty) {
				super.updateItem(rentFee, empty);
				if (empty) {
					setText(null);
				} else {
					setText(currencyFormat.format(rentFee));
				}
			}
		});
		// for howing Two decimal digits of late FEE
		lateFee.setCellFactory(tc -> new TableCell<HiringRecord, Double>() {

			@Override
			protected void updateItem(Double lateFee, boolean empty) {
				super.updateItem(lateFee, empty);
				if (empty) {
					setText(null);
				} else {
					setText(currencyFormat.format(lateFee));
				}
			}
		});
		// for showing Two decimal digits of total FEE

		totalRentFee.setCellFactory(tc -> new TableCell<HiringRecord, Double>() {

			@Override
			protected void updateItem(Double totalRentFee, boolean empty) {
				super.updateItem(totalRentFee, empty);
				if (empty) {
					setText(null);
				} else {
					setText(currencyFormat.format(totalRentFee));
				}
			}
		});
		return hiringList;
	}

	/**
	 * 
	 * @param roomID
	 * @throws Exception
	 */
	public void selectRoomHistory(String roomID) throws Exception {
		String histOryOfRoomID = roomID;
		recordID.setCellValueFactory(new PropertyValueFactory<HiringRecord, String>("recordID"));
		customerID.setCellValueFactory(new PropertyValueFactory<HiringRecord, String>("customerID"));
		rentDate.setCellValueFactory(new PropertyValueFactory<HiringRecord, Date>("rentDate"));
		estimatedDate.setCellValueFactory(new PropertyValueFactory<HiringRecord, Date>("estimatedDate"));
		actualReturnDate.setCellValueFactory(new PropertyValueFactory<HiringRecord, Date>("actualReturnDate"));
		rentFee.setCellValueFactory(new PropertyValueFactory<HiringRecord, Double>("rentFee"));
		lateFee.setCellValueFactory(new PropertyValueFactory<HiringRecord, Double>("lateFee"));
		totalRentFee.setCellValueFactory(new PropertyValueFactory<HiringRecord, Double>("totalRentFee"));
		rentHistory.setItems(getRoomHistoryDetails(histOryOfRoomID));
	}

	// Rent Room

	@FXML
	public void rentRoomAction(ActionEvent event) throws Exception {

		String fetchRoomID = hiddenRoomID.getText().trim();
		String cust_ID = "";
		DBStore dbstore = new DBStore();
		room = dbstore.selectRoomByID(fetchRoomID);

		maintenanceStatus.setText("Room is rented now.");
		Maintenance.setVisible(false);
		if (room.getRoomStatus().equalsIgnoreCase("RENTED") || room.getRoomStatus().equalsIgnoreCase("MAINTENANCE")) {
			throw new RentException("SORRY!,This room is not available right now for rent");
		}

		String startDate = "";
		String endDate = "";
		Date fromRentDate = null;
		Date toRentDate = null;

		cust_ID = custID.getText();
		if (cust_ID.equalsIgnoreCase("")) {
			throw new InvalidException("Please enter customer id");
		}
		if (!cust_ID.matches("[0-9 -]{3}")) {

			throw new InvalidException("Only three digits are permitted to customerID");
		}

		cust_ID = "CUS" + cust_ID;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (fromDate.getValue() == null) {
			throw new InvalidException("Please select From Date");
		}

		startDate = fromDate.getValue().toString();
		fromRentDate = df.parse(startDate);
		if (toDate.getValue() == null) {
			throw new InvalidException("Please select To Date");
		}

		endDate = toDate.getValue().toString();
		toRentDate = df.parse(endDate);

		Date todayDate = new Date();
		long checkDate = (fromRentDate.getTime() - todayDate.getTime()) / 86400000;
		System.out.println("Date difference " + checkDate);

		if (checkDate < 0) {
			throw new RentException("Incorrect date! , From Date should be a today/future date");
		}

		long difference = (toRentDate.getTime() - fromRentDate.getTime()) / 86400000;
		int stayDays = (int) difference;
		if(stayDays < 0) {
			throw new RentException("Incorrect date! , From Date should be a prior to the future date");
		}
		System.out.println(stayDays);
		room.rent(cust_ID, fromRentDate, stayDays);
		hiddenRoomStatus.setText("RENTED");

		try {
			selectRoomHistory(fetchRoomID);
			if (hiddenRoomStatus.getText().equalsIgnoreCase("RENTED")) {
				rentTitle.setText("Return this room here ...");

				returnCustomerID.setText(cust_ID);
				returnCustomerID.setDisable(true);
				rentGrid.setVisible(false);
				returnGrid.setVisible(true);
			}
			clearRentInputs();
			String title = "Room Rented";
			updatedAlert(title);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Clear all inputs after rent
	public void clearRentInputs() {
		custID.setText("");
		fromDate.setValue(null);
		toDate.setValue(null);
	}

	// Return room after rent

	@FXML
	public void returnRoomAction(ActionEvent event) throws Exception {

		Maintenance.setVisible(false);

		String fetchRoomID = hiddenRoomID.getText().trim();
		DBStore dbstore = new DBStore();
		room = dbstore.selectRoomByID(fetchRoomID);
		if (room.getRoomStatus().equalsIgnoreCase("AVAILABLE")
				|| room.getRoomStatus().equalsIgnoreCase("MAINTENANCE")) {
			throw new ReturnException("SORRY!,This room is not rented now.");
		}

		Date roomReturnDate = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (returnDategrid.getValue() == null) {
			throw new InvalidException("Please select return Date");
		}

		String retDate = returnDategrid.getValue().toString();
		roomReturnDate = df.parse(retDate);
		System.out.println(roomReturnDate);

		room.returnRoom(roomReturnDate);
		selectRoomHistory(fetchRoomID);
		hiddenRoomStatus.setText("AVAILABLE");
		if (hiddenRoomStatus.getText().equalsIgnoreCase("AVAILABLE")) {
			rentTitle.setText("Rent this room here ...");
			rentGrid.setVisible(true);
			returnGrid.setVisible(false);
			Maintenance.setVisible(true);
			maintenanceStatus.setText("");
		}
		returnDategrid.setValue(null);
		String title = "Room Returned";
		updatedAlert(title);
	}

	// For Room maintenance of both standard and suite

	@FXML
	public void roomMaintenanceAction(ActionEvent event)
			throws ClassNotFoundException, SQLException, MaintenanceException {
		String fetchRoomID = hiddenRoomID.getText().trim();
		DBStore dbstore = new DBStore();
		room = dbstore.selectRoomByID(fetchRoomID);
		if (room.getRoomStatus().equalsIgnoreCase("RENTED")) {
			throw new MaintenanceException("This room is not rented now.");
		}
		room.performMaintenance();
		maintenanceStatus.setVisible(true);
		maintenanceStatus.setText("Under Maintenance");
		rentTitle.setText("");
		Maintenance.setVisible(false);
		completeMaint.setVisible(true);
		rentGrid.setVisible(false);
		returnGrid.setVisible(false);
		String title = "Maintenance Starts";
		updatedAlert(title);
	}

	@FXML
	public void completeMaintenanceAction(ActionEvent event)
			throws ClassNotFoundException, SQLException, MaintenanceException, ParseException {

		String fetchRoomID = hiddenRoomID.getText().trim();
		DBStore dbstore = new DBStore();
		room = dbstore.selectRoomByID(fetchRoomID);
		if (room.getRoomStatus().equalsIgnoreCase("RENTED")) {
			throw new MaintenanceException("SORRY!,This room is not rented now.");
		}
		if (room.getRoomType().toUpperCase().equalsIgnoreCase("STANDARD")) {
			room.completeMaintenance();
			maintenanceStatus.setVisible(true);
			maintenanceStatus.setText("Maintenance completed.");
			Maintenance.setVisible(true);
			hiddenRoomStatus.setText("AVAILABLE");
			rentTitle.setText("Rent this room here ...");
			rentGrid.setVisible(true);
			completeMaint.setVisible(false);
			String title = "Maintenance Completed";
			updatedAlert(title);
		} else {
			completionDatelbl.setVisible(true);
			completeMaintDate.setVisible(true);
			goBtn.setVisible(true);

		}

	}
	// for getting complete maintenance date info and updating to the db

	@FXML
	public void getCompletionDate() throws Exception {
		Date maintenanceDate = null;
		String date = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (completeMaintDate.getValue() == null) {
			throw new InvalidException("Please select Completion Date");
		}
		date = completeMaintDate.getValue().toString();
		maintenanceDate = df.parse(date);
		String fetchRoomID = hiddenRoomID.getText().trim();
		DBStore dbstore = new DBStore();
		room = dbstore.selectRoomByID(fetchRoomID);
		room.completeMaintenance(maintenanceDate);
		completeMaint.setVisible(false);
		completionDatelbl.setVisible(false);
		completeMaintDate.setVisible(false);
		goBtn.setVisible(false);
		maintenanceStatus.setText("");
		Maintenance.setVisible(true);
		rentTitle.setText("Rent this room here ...");
		rentGrid.setVisible(true);
		String title = "Maintenance Completed";
		updatedAlert(title);
	}

	// Showing confirmation alert after updation
	public void updatedAlert(String title) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Inputs Updated");
		alert.setHeaderText(title);
		alert.showAndWait();

	}
}
