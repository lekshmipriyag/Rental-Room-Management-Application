package model;

/**
 * @author lekshmipriya
 * import the needed packages
 */

import java.text.DecimalFormat;
import java.util.Date;

public class HiringRecord {

	private String recordID;
	private Date rentDate;
	private Date estimatedDate;
	private Date actualReturnDate;
	private double rentFee;
	private double lateFee;
	private int numOfRentDays;
	private double totalRentFee;
	private String customerID;
	private String roomID;

	// for printing fee amounts with 2 decimal places
	private DecimalFormat df2 = new DecimalFormat("0.00");

	public HiringRecord() {

		this.recordID = "";
		this.rentFee = 0.00;
		this.lateFee = 0.00;
		this.totalRentFee = 0.00;
		this.numOfRentDays = 0;
		this.customerID = "";
		this.roomID = "";

	}

	/**
	 * 
	 * @param recordID is used for identifying each hiring record.
	 * @param dd       indicates day of rent date
	 * @param mm       indicates month of rent date
	 * @param yyyy     indicate year of rent date
	 * @param days     indicates number of rent days
	 */
	public HiringRecord(String recordID, Date rentDate, int days) {
		this.recordID = recordID;
		this.rentDate = rentDate;
		this.estimatedDate = rentDate;
		this.numOfRentDays = days;
	}

	/**
	 * 
	 * @param actualReturnDate is the date when the customer actually returns the
	 *                         room
	 * @param rentFee          rent based on room type and number of days
	 * @param totalRentFee     total of rent and late fee
	 */

	public void rentFee(Date actualReturnDate, double rentFee, double totalRentFee) {

		this.actualReturnDate = actualReturnDate;
		this.rentFee = rentFee;
		this.totalRentFee = totalRentFee;
	}

	/**
	 * 
	 * @param actualReturnDate is the date when the customer actually returns the
	 *                         room
	 * @param rentFee          rent based on room type and number of days
	 * @param lateFee          indicates the additional fee of actual return date
	 *                         from estimated return date
	 * @param totalRentFee     total of rent and late fee
	 */
	public void lateFee(Date actualReturnDate, double rentFee, double lateFee, double totalRentFee) {

		this.actualReturnDate = actualReturnDate;
		this.rentFee = rentFee;
		this.lateFee = lateFee;
		this.totalRentFee = totalRentFee;
	}

	/**
	 * 
	 * @return customerID
	 */
	public String getCustomerID() {
		return customerID;
	}

	/**
	 * 
	 * @param customerID
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	/**
	 * 
	 * @return roomID
	 */
	public String getRoomID() {
		return roomID;
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
	 * @return record id
	 */
	public String getRecordID() {
		return recordID;
	}

	/**
	 * 
	 * @return rent date
	 */
	public Date getRentDate() {
		return rentDate;
	}

	/**
	 * 
	 * @param date for setting rent date
	 */
	public void setRentDate(Date date) {
		this.rentDate = date;
	}

	/**
	 * 
	 * @return estimated rent date
	 */
	public Date getEstimatedDate() {
		return estimatedDate;
	}

	/**
	 * 
	 * @param estimatedDate for setting estimated rtent date
	 */
	public void setEstimatedDate(Date estimatedDate) {
		this.estimatedDate = estimatedDate;
	}

	/**
	 * 
	 * @return actual return date
	 */
	public Date getActualReturnDate() {
		return actualReturnDate;
	}

	/**
	 * 
	 * @param set actualReturnDate
	 */
	public void setActualReturnDate(Date actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	/**
	 * 
	 * @return rent fee
	 */
	public double getRentFee() {
		return rentFee;
	}

	/**
	 * 
	 * @return total rent fee
	 */
	public double getTotalRentFee() {
		return totalRentFee;
	}

	/**
	 * 
	 * @param set total rent fee
	 */
	public void setTotalRentFee(double totalRentFee) {
		this.totalRentFee = totalRentFee;
	}

	/**
	 * 
	 * @param set rent fee
	 */
	public void setRentFee(double rentFee) {
		this.rentFee = rentFee;
	}

	/**
	 * 
	 * @return late fee
	 */
	public double getLateFee() {
		return lateFee;
	}

	/**
	 * 
	 * @param set late fee
	 */
	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}

	/**
	 * 
	 * @return number of rent days
	 */
	public int getNumOfRentDays() {
		return numOfRentDays;
	}

	/**
	 * 
	 * @param set number of rent days
	 */
	public void setNumOfRentDays(int numOfRentDays) {
		this.numOfRentDays = numOfRentDays;
	}

	/**
	 * 
	 * @param recordID for setting record ID
	 */
	public void setRecordID(String recordID) {
		this.recordID = recordID;
	}

	/**
	 * toString method is used to return a string containing the details of a hiring
	 * record
	 */
	public String toString() {
		String strData = "";

		if (this.rentFee == 0.00) {
			strData =  this.roomID + ":"+this.recordID + ":" +this.customerID + ":"+ this.rentDate + ":" + this.estimatedDate + ":" + "none:0.00:0.00:0.00";
		} else {
			strData =  this.roomID + ":"+this.recordID + ":" +this.customerID +":" + this.rentDate + ":" + this.estimatedDate + ":" + this.actualReturnDate
					+ ":" + df2.format(this.rentFee) + ":" + df2.format(this.lateFee)+ ":" + df2.format(this.totalRentFee);
		}
		return strData;
	}

	/**
	 * 
	 * getDetails method is used to build a string and return that string
	 */
	public String getDetails() {
		String rentDetails = "";
		try {
			if (this.recordID != null) {
				if (this.rentFee == 0.00) {
					rentDetails = "\n Record ID :\t" + this.recordID + "\n";
					rentDetails = rentDetails + " Rent Date :\t" + this.rentDate + "\n";
					rentDetails = rentDetails + " Estimated Return Date : " + this.estimatedDate + "\n";
				} else {
					rentDetails = "\n Record ID :\t" + this.recordID + "\n";
					rentDetails = rentDetails + " Rent Date :\t" + this.rentDate + "\n";
					rentDetails = rentDetails + " Estimated Return Date : " + this.estimatedDate + "\n";
					rentDetails = rentDetails + " Actual Return Date : " + this.actualReturnDate + "\n";
					rentDetails = rentDetails + " Rental Fee : \t" + df2.format(this.rentFee) + "\n";
					rentDetails = rentDetails + " Late Fee : \t" + df2.format(this.lateFee) + "\n";
				}
			}
		} catch (NullPointerException np) {
			return rentDetails;
		}
		return rentDetails;
	}

}
