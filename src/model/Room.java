package model;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import database.DBStore;
import exception.MaintenanceException;
import exception.RentException;
import exception.ReturnException;

public abstract class Room {

	protected String roomID;
	protected int totalBedrooms;
	protected String roomType;
	protected String roomStatus;
	protected double rentFee;
	protected double lateFee;
	protected Date lastMaintenanceDate = null;
	protected ArrayList<HiringRecord> hiringList = new ArrayList<HiringRecord>();
	protected String featureSummary;
	Boolean overDueCheck = false;
	protected String image_name;
	protected String description;

	public Room(String roomID) {
		this.roomID = roomID;
	}

	/**
	 * 
	 * @param hiringrecord is an array list for storing rental history
	 */
	public void addHiringRcord(HiringRecord hiringrecord) {
		hiringList.add(hiringrecord);
	}

	/**
	 * 
	 * @param roomID         for room identification
	 * @param totalBedrooms  indicates number of standard and suite bedrooms
	 * @param featureSummary is used to summarize room features
	 * @param roomType       indicates two different types of rental rooms of city
	 *                       lodge
	 * @param roomStatus     represents three different room status
	 * @param rentFee        represents rent date per day of each rooms
	 * @param lateFee        indicates late rent fee after the estimated rent period
	 */
	public Room(String roomID, int totalBedrooms, String featureSummary, String roomType, String roomStatus,
			double rentFee, double lateFee, String image_name, String description) {

		this.roomID = roomID;
		this.totalBedrooms = totalBedrooms;
		this.featureSummary = featureSummary;
		this.roomType = roomType;
		this.roomStatus = roomStatus;
		this.rentFee = rentFee;
		this.lateFee = lateFee;
		this.image_name = image_name;
		this.description = description;
	}

	/**
	 * 
	 * @param roomID         for room identification
	 * @param totalBedrooms  indicates number of standard and suite bedrooms
	 * @param featureSummary is used to summarize room features
	 * @param roomType       indicates two different types of rental rooms of city
	 *                       lodge
	 * @param roomStatus     represents three different room status
	 * @param dd             represents day of maintenance date
	 * @param MM             represents month of maintenance date
	 * @param yyyy           represents year of maintenance date
	 * @param rentFee        represents rent date per day of each rooms
	 * @param lateFee        indicates late rent fee after the estimated rent period
	 */

	public Room(String roomID, int totalBedrooms, String featureSummary, String roomType, String roomStatus,
			Date lastMaintenanceDate, double rentFee, double lateFee, String image_name, String description) {

		this.roomID = roomID;
		this.totalBedrooms = totalBedrooms;
		this.featureSummary = featureSummary;
		this.roomType = roomType;
		this.roomStatus = roomStatus;
		this.rentFee = rentFee;
		this.lateFee = lateFee;
		this.lastMaintenanceDate = lastMaintenanceDate;
		this.image_name = image_name;
		this.description = description;
	}

	public Room() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @return rentFee
	 */
	public double getRentFee() {
		return rentFee;
	}

	/**
	 * 
	 * @param rentFee
	 */
	public void setRentFee(double rentFee) {
		this.rentFee = rentFee;
	}

	/**
	 * 
	 * @return lateFee
	 */
	public double getLateFee() {
		return lateFee;
	}

	/**
	 * 
	 * @param lateFee
	 */
	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}

	/**
	 * 
	 * @return lastMaintenanceDate
	 */
	public Date getLastMaintenanceDate() {
		return lastMaintenanceDate;
	}

	/**
	 * 
	 * @param lastMaintenanceDate
	 */
	public void setLastMaintenanceDate(Date lastMaintenanceDate) {
		this.lastMaintenanceDate = lastMaintenanceDate;
	}

	/**
	 * 
	 * @return image_name
	 */

	public String getImage_name() {
		return image_name;
	}

	/**
	 * 
	 * @param image_name
	 */
	public void setImage_name(String image_name) {
		this.image_name = image_name;
	}

	/**
	 * 
	 * @return description
	 */

	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @param roomID
	 */
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}

	/**
	 * 
	 * @param totalBedrooms
	 */

	public void setTotalBedrooms(int totalBedrooms) {
		this.totalBedrooms = totalBedrooms;
	}

	/**
	 * 
	 * @param roomType
	 */
	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	/**
	 * 
	 * @return lastMaintenanceDate
	 */

	/**
	 * 
	 * @return room id
	 */
	public String getRoomID() {
		return this.roomID;
	}

	/**
	 * 
	 * @return number of bed rooms
	 */
	public int getTotalBedrooms() {
		return this.totalBedrooms;
	}

	/**
	 * 
	 * @return type of room
	 */
	public String getRoomType() {
		return this.roomType;
	}

	/**
	 * 
	 * @return room status
	 */
	public String getRoomStatus() {
		return this.roomStatus;
	}

	/**
	 * 
	 * @param roomStatus
	 */
	public void setRoomStatus(String roomStatus) {
		this.roomStatus = roomStatus;
	}

	/**
	 * 
	 * @return features of each room
	 */
	public String getFeatureSummary() {
		return this.featureSummary;
	}

	/**
	 * 
	 * @param featureSummary
	 */
	public void setFeatureSummary(String featureSummary) {
		this.featureSummary = featureSummary;
	}

	public abstract void rent(String customerId, Date rentDate, int numOfRentDay) throws RentException;

	public abstract void returnRoom(Date returnDate) throws ReturnException, ClassNotFoundException, SQLException;

	public abstract void performMaintenance() throws MaintenanceException, ClassNotFoundException, SQLException;

	/**
	 * 
	 * @throws MaintenanceException
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void completeMaintenance() throws MaintenanceException, ClassNotFoundException, SQLException {

		DBStore dbstore = new DBStore();
		dbstore.updateRoomStatus(roomID, "AVAILABLE");
	}

	/**
	 * 
	 * @param CompletionDate
	 * @throws MaintenanceException
	 * @throws ClassNotFoundException
	 * @throws SQLException           This method is used for suite maintenance
	 *                                completion only
	 */
	public void completeMaintenance(Date CompletionDate)
			throws MaintenanceException, ClassNotFoundException, SQLException {
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date(CompletionDate.getTime()));
		long difference1 = (CompletionDate.getTime() - this.lastMaintenanceDate.getTime()) / 86400000;
		int extraDays = (int) difference1;
		Date todayDate = new Date();
		if (extraDays < 0) {
			throw new MaintenanceException("Completion date should be greater than or equal to last maintenance date");
		} else {
			this.lastMaintenanceDate = CompletionDate;
			long difference2 = (todayDate.getTime() - this.lastMaintenanceDate.getTime()) / 86400000;
			int days = (int) difference2;
			System.out.println("suite completion date" + days);
			if (days > 10) {
				throw new MaintenanceException("Maintenance date is still over-dued");
			}
			this.roomStatus = "AVAILABLE";

			DBStore dbstore = new DBStore();
			dbstore.updateSuiteMaintenanceDate(this.roomID, this.roomStatus, this.lastMaintenanceDate);

		}
	}

	/**
	 * @param rentDate
	 * @param estimatedReturnDate
	 * @throws RentException
	 * @throws ClassNotFoundException
	 * @throws SQLException           This method is used to display exception for
	 *                                already existing details
	 */

	public void rentDateValidation(Date rentDate, Date estimatedReturnDate)
			throws RentException, ClassNotFoundException, SQLException {
		DBStore dbstore = new DBStore();

		int recordsCount = 0;
		recordsCount = dbstore.getHiringRecordsCount(rentDate, roomID);
		System.out.println("Total Count in the table is" + recordsCount);
		if (recordsCount > 0) {
			throw new RentException("Sorry!,Record already exist for this Period");
		}

		int recordsCountEstimated = dbstore.getHiringRecordsCount(estimatedReturnDate, roomID);
		if (recordsCountEstimated > 0) {
			throw new RentException("Sorry!,Record already exist for this Period");
		}
	}

	/**
	 * 
	 * @param customerId
	 * @param rentDate
	 * @param numOfRentDay
	 * @throws ClassNotFoundException
	 * @throws SQLException           This method is used for adding hiring record
	 *                                details to database
	 */
	public void insertHiringRecord(String customerId, Date rentDate, int numOfRentDay)
			throws ClassNotFoundException, SQLException {

		System.out.println("Insert Hiring Record");
		this.roomStatus = "RENTED";
		String recordID = "";
		SimpleDateFormat formatter = new SimpleDateFormat("ddMMyyyy");
		String strRentStartDate = formatter.format(rentDate);
		recordID = this.roomID + "_" + customerId + "_" + strRentStartDate;
		HiringRecord hrObj = new HiringRecord();
		hrObj = new HiringRecord(recordID, rentDate, numOfRentDay);

		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date(rentDate.getTime()));
		c1.add(Calendar.DATE, numOfRentDay);

		Date estimatedReturnDate = c1.getTime();
		this.addHiringRcord(hrObj);
		hrObj.setRentDate(rentDate);
		hrObj.setCustomerID(customerId);
		hrObj.setRoomID(this.roomID);
		hrObj.setEstimatedDate(estimatedReturnDate);

		System.out.println("\nRent Details");
		System.out.println("------------");
		System.out.println(hrObj.getRoomID() + " " + hrObj.getCustomerID());
		System.out.println(hrObj.getRentDate() + " " + hrObj.getEstimatedDate());
		System.out.println("Rent Fee" + hrObj.getRentFee() + " Late Fee" + hrObj.getLateFee() + " Total rent"
				+ hrObj.getTotalRentFee());
		System.out.println(hrObj.toString());
		try {
			System.out.println(hrObj.getDetails());
		} catch (Exception e) {
			e.printStackTrace();
		}
		DBStore dbstore = new DBStore();
		dbstore.addHiringRecord(hrObj);
		dbstore.updateRoomStatus(this.roomID, this.roomStatus);
	}

	/**
	 * 
	 * @param estimatedDate indicates estimated return date
	 * @param rentDate      represents rent date by the customer This method is used
	 *                      to calculate total rent fee from estimated date and
	 *                      actual rent date
	 */
	public double rentFeeCalculation(Date estimatedDate, Date rentDate) {
		long renDays = (estimatedDate.getTime() - rentDate.getTime()) / 86400000;
		int rentDays = (int) renDays;
		double rent = rentDays * rentFee;
		return rent;
	}

	/**
	 * 
	 * @param estimatedDate indicates estimated return date
	 * @param rentDate      represents rent date by the customer This method is used
	 *                      to calculate total late fee from estimated date and
	 *                      actual rent date
	 */

	public double lateRentCalculation(Date estimatedDate, Date rentDate) {
		long renDays = (estimatedDate.getTime() - rentDate.getTime()) / 86400000;
		int rentDays = (int) renDays;
		double lateRent = rentDays * lateFee;
		return lateRent;
	}

	/**
	 * 
	 * @param hrObj used for updating room status when room is available for rent
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void updateReturnDetails(HiringRecord hrObj) throws ClassNotFoundException, SQLException {

		this.setRoomStatus("AVAILABLE");
		DBStore dbstore = new DBStore();
		dbstore.updateHiringRecord(hrObj);
		dbstore.updateRoomStatus(roomID, roomStatus);
	}

	/**
	 * This method is used to build a string and return the string in a pre-defined
	 * standard format
	 */
	public String toString() {

		String strData = "";
		if (this.roomType.equalsIgnoreCase("STANDARD")) {
			strData = this.getRoomID() + ":" + this.getTotalBedrooms()+":"+ this.getRoomStatus() + ":" + this.featureSummary + ":"
					+ this.getRentFee()+":"+ this.getLateFee()+":"+this.getImage_name()+":"+this.getDescription();

		} else {
			strData = this.getRoomID() + ":" + this.getTotalBedrooms()+":"+ this.getRoomType()+":"+ this.getRoomStatus() + ":" + this.featureSummary + ":" 
					+ this.getLastMaintenanceDate()+":"+ this.getRentFee()+":"+ this.getLateFee()+":"+this.getImage_name()+":"+this.getDescription();

		}
		return strData;
	}
	/**
	 * 
	 * getDetails method is used to build a string and return that string
	 */
	public String getDetails() {
		String getData = "";

		getData = "\n Room ID : " + this.roomID + "\n";
		getData = getData + " Number Of Bedrooms : " + this.totalBedrooms + "\n";
		getData = getData + " Type : " + this.roomType + "\n";
		getData = getData + " Status : " + this.roomStatus + "\n";
		if (this.roomType.equalsIgnoreCase("SUITE")) {
			getData = getData + " Last maintenance date : " + this.lastMaintenanceDate + "\n";
		}
		getData = getData + " Feature summary : " + this.featureSummary + "\n";
		getData = getData + "\n RENTAL RECORD(S)";
		return getData;
	}
}
