package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import database.DBStore;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import javafx.stage.Stage;
import model.HiringRecord;
import model.Room;
import model.Standard;
import model.Suite;
import util.DateTime;

public class HomeController {

	@FXML
	Button click;

	@FXML
	FlowPane homeFlowPane;

	@FXML
	ScrollPane homeScrollPane;

	@FXML
	BorderPane homeBorderPane;

	@FXML
	Button closeButton, importData, refresh;

	@FXML
	private TableView<Room> roomData = new TableView<>();

	@FXML
	private TableColumn<Room, String> roomID;

	@FXML
	private TableColumn<Room, String> roomType;

	@FXML
	private TableColumn<Room, String> totalBedrooms;

	@FXML
	private TableColumn<Room, String> rentFee;

	@FXML
	private TableColumn<Room, String> featureSummary;

	@FXML
	private TableColumn<Room, String> image_name;

	@FXML
	private TableColumn<Room, String> description;

	@FXML
	private Label typeRoom;

	@FXML

	private ArrayList<Room> roomList = new ArrayList<Room>();
	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	int i = 0;

	@FXML
	public void initialize() throws Exception {
		System.out.println("Inside Main Controller");
		selectRoomDetailsAction();
	}

	/**
	 * For Standard FXML Page
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void addStandardAction(ActionEvent event) throws Exception {

		Pane newStandardPane = FXMLLoader.load(getClass().getResource("/view/StandardRoom.fxml"));
		homeBorderPane.getChildren().setAll(newStandardPane);
	}

	/**
	 * For Suite FXML Page
	 * 
	 * @param event
	 * @throws Exception
	 */
	public void addSuiteAction(ActionEvent event) throws Exception {

		Pane newSuitePane = FXMLLoader.load(getClass().getResource("/view/SuiteRoom.fxml"));
		homeBorderPane.getChildren().setAll(newSuitePane);
	}

	/**
	 * Storing temporary data to array list
	 * 
	 * @param room
	 */
	public void addRoomDetails(Room room) {
		roomList.add(room);
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<Room> getRoomList() {
		return roomList;
	}

	@FXML
	public ObservableList<Room> getAllRoomDetails() throws Exception {

		DBStore dbstore = new DBStore();
		ObservableList<Room> roomList = FXCollections.observableArrayList(dbstore.selectAllRooms());
		roomID.setCellValueFactory(new PropertyValueFactory<Room, String>("roomID"));
		roomType.setCellValueFactory(new PropertyValueFactory<Room, String>("roomType"));
		totalBedrooms.setCellValueFactory(new PropertyValueFactory<Room, String>("totalBedrooms"));
		featureSummary.setCellValueFactory(new PropertyValueFactory<Room, String>("featureSummary"));
		image_name.setCellValueFactory(new PropertyValueFactory<Room, String>("image_name"));
		return roomList;
	}

	public void selectRoomDetailsAction() throws Exception {

		roomID.setCellValueFactory(new PropertyValueFactory<Room, String>("roomID"));
		roomType.setCellValueFactory(new PropertyValueFactory<Room, String>("roomType"));
		totalBedrooms.setCellValueFactory(new PropertyValueFactory<Room, String>("totalBedrooms"));
		featureSummary.setCellValueFactory(new PropertyValueFactory<Room, String>("featureSummary"));
		image_name.setCellValueFactory(new PropertyValueFactory<Room, String>("image_name"));
		roomData.setItems(getAllRoomDetails());
		if (i == 0) {
			addButtonToTable();
		}
		i++;
	}

//Adding Book Button at the end of each room list row
	private void addButtonToTable() {

		TableColumn<Room, Void> colBtn = new TableColumn("");
		Callback<TableColumn<Room, Void>, TableCell<Room, Void>> cellFactory = new Callback<TableColumn<Room, Void>, TableCell<Room, Void>>() {
			@Override
			public TableCell<Room, Void> call(TableColumn<Room, Void> param) {
				final TableCell<Room, Void> cell = new TableCell<Room, Void>() {
					private Button btn = new Button("BOOK NOW");

					{
						btn.setStyle("-fx-background-color: #3498db ; -fx-text-fill: White; ");
						btn.setOnAction((ActionEvent event) -> {
							Room data = getTableView().getItems().get(getIndex());
							System.out.println("selectedData: " + data.getRoomID());

							FXMLLoader loader = new FXMLLoader();
							loader.setLocation(getClass().getResource("/view/RoomDetails.fxml"));
							Parent tableViewParent = null;
							try {
								tableViewParent = loader.load();
							} catch (IOException e) {
								e.printStackTrace();
							}
							Pane roomPane = new Pane(tableViewParent);
							RoomDetailController controller = loader.getController();
							try {
								controller.setData(data);
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							} catch (Exception e) {
								e.printStackTrace();
							}
							homeBorderPane.getChildren().setAll(roomPane);
						});
					}

					@Override
					public void updateItem(Void item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							setGraphic(null);
						} else {
							setGraphic(btn);
						}
					}
				};
				return cell;
			}
		};
		colBtn.setCellFactory(cellFactory);
		roomData.getColumns().add(colBtn);
	}

	/**
	 * This method is ued for importing data from a text file
	 * 
	 * @throws ClassNotFoundException
	 * @throws ParseException
	 */

	public void importDataFromFile() throws ClassNotFoundException, ParseException {

		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extensionFilter);
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			System.out.println(file.getName());
			importDataFromFile(file);
		}
	}

	/**
	 * Used for fetching standard room details from file
	 * 
	 * @param dataFromFile
	 * @return
	 */
	private Standard standardRoom(List<String> dataFromFile) {

		Standard standObj = new Standard();
		int count = 0;
		standObj.setRoomID(dataFromFile.get(count++));
		standObj.setTotalBedrooms(Integer.parseInt(dataFromFile.get(count++)));
		standObj.setRoomType("STANDARD");
		standObj.setRoomStatus(dataFromFile.get(count++));
		standObj.setFeatureSummary(dataFromFile.get(count++));
		standObj.setRentFee(Double.parseDouble(dataFromFile.get(count++)));
		standObj.setLateFee(Double.parseDouble(dataFromFile.get(count++)));
		standObj.setImage_name(dataFromFile.get(count++));
		standObj.setDescription(dataFromFile.get(count++));
		return standObj;

	}

	/**
	 * used for fetching suite room details from file
	 * 
	 * @param dataFromFile
	 * @return
	 * @throws ParseException
	 */
	private Suite suiteRoom(List<String> dataFromFile) throws ParseException {

		Suite suiteObj = new Suite();
		int count = 0;
		suiteObj.setRoomID(dataFromFile.get(count++));
		suiteObj.setTotalBedrooms(Integer.parseInt(dataFromFile.get(count++)));
		suiteObj.setRoomType(dataFromFile.get(count++));
		suiteObj.setRoomStatus(dataFromFile.get(count++));
		suiteObj.setFeatureSummary(dataFromFile.get(count++));
		suiteObj.setLastMaintenanceDate(df.parse(dataFromFile.get(count++)));
		suiteObj.setRentFee(Double.parseDouble(dataFromFile.get(count++)));
		suiteObj.setLateFee(Double.parseDouble(dataFromFile.get(count++)));
		suiteObj.setImage_name(dataFromFile.get(count++));
		suiteObj.setDescription(dataFromFile.get(count++));
		return suiteObj;
	}

	/**
	 * used for fetching Hiring records details from file
	 * 
	 * @param dataFromFile
	 * @param roomID
	 * @return
	 * @throws ParseException
	 */
	private HiringRecord hiringData(List<String> dataFromFile, String roomID) throws ParseException {
		
		HiringRecord hrObj = new HiringRecord();
		int count = 0;
		hrObj.setRoomID(dataFromFile.get(count++));
		hrObj.setRecordID(dataFromFile.get(count++));
		hrObj.setCustomerID(dataFromFile.get(count++));
		hrObj.setRentDate(df.parse(dataFromFile.get(count++)));
		hrObj.setEstimatedDate(df.parse(dataFromFile.get(count++)));
		String actualReturnDate = dataFromFile.get(count++);
		if (!actualReturnDate.equals("none")) {
			hrObj.setActualReturnDate(df.parse(actualReturnDate));
			hrObj.setRentFee(Double.parseDouble(dataFromFile.get(count++)));
			hrObj.setLateFee(Double.parseDouble(dataFromFile.get(count++)));
			hrObj.setTotalRentFee(Double.parseDouble(dataFromFile.get(count++)));
		} 
		return hrObj;
	}

	/**
	 * 
	 * @param file
	 * @throws ClassNotFoundException
	 * @throws ParseException
	 */
	private void importDataFromFile(File file) throws ClassNotFoundException, ParseException {

		BufferedReader bufferedReader = null;
		try {
			ArrayList<HiringRecord> hiringList = new ArrayList<HiringRecord>();
			Room roomObj;
			String roomId = null;
			HiringRecord hrObj = null;

			bufferedReader = new BufferedReader(new FileReader(file));
			List<String> importFileData = new ArrayList<>();
			String text;
			while ((text = bufferedReader.readLine()) != null) {
				importFileData = Arrays.asList(text.split(":"));
				if (importFileData.size() == 8) {
					roomObj = standardRoom(importFileData);
					roomId = roomObj.getRoomID();
					roomList.add(roomObj);
				} else if (importFileData.size() == 10) {
					roomObj = suiteRoom(importFileData);
					roomId = roomObj.getRoomID();
					roomList.add(roomObj);
				} else if (importFileData.size() == 9) {
					if (roomId != null) {
						hrObj = hiringData(importFileData, roomId);
						hiringList.add(hrObj);
					} else {
						break;
					}
				} else {
					System.out.println();
					break;
				}
			}
			// Insert room data to the database
			for (Room roomData : roomList) {
				try {
					DBStore dbstore = new DBStore();
					dbstore.addRoom(roomData);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// Inser hiring records to the database
			for (HiringRecord rentHistory : hiringList) {
				try {
					DBStore dbstore = new DBStore();
					dbstore.addHiringRecord(rentHistory);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bufferedReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * This method is used for exporting data from database and store it in a text
	 * file
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */

	public void exportDataFromDB() throws ClassNotFoundException, SQLException {
		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extensionFilter);

		File file = fileChooser.showSaveDialog(null);

		String getData = fetchDBData();

		if (file != null) {
			SaveDataToFile(getData, file);
		}
	}

	/**
	 * 
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public String fetchDBData() throws ClassNotFoundException, SQLException {

		DBStore dbstore = new DBStore();
		List<HiringRecord> hiringList = null;
		String data = "";

		List<Room> roomList = dbstore.selectAllRooms();
		for (Room roomData : roomList) {
			data = data + roomData.toString() + System.lineSeparator();
			try {
				hiringList = dbstore.findAllHiringRecords(roomData.getRoomID());
				for (HiringRecord rentHistory : hiringList) {
					data = data + rentHistory.toString() + System.lineSeparator();

				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return data;
	}

	/**
	 * Saving all data to a text file
	 * 
	 * @param data
	 * @param file
	 */
	private void SaveDataToFile(String data, File file) { // Writing the data to the files

		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);

			fileWriter.write(data);
			fileWriter.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param roomList
	 */
	public void setRoomList(ArrayList<Room> roomList) {
		this.roomList = roomList;
	}

	@FXML
	public void refreshTable() throws Exception {
		// Refreshing db data
		selectRoomDetailsAction();
	}

	@FXML
	private void closeButtonAction() {
		// Exits from the program
		Stage stage = (Stage) closeButton.getScene().getWindow();
		stage.close();
	}
}