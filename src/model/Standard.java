package model;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import database.DBStore;
import exception.MaintenanceException;
import exception.RentException;
import exception.ReturnException;

public class Standard extends Room {

	public Standard() {
		super();
	}

	/**
	 * 
	 * @param roomID
	 * @param totalBedrooms
	 * @param featureSummary
	 * @param roomType
	 * @param roomStatus
	 * @param rentFee
	 * @param lateFee
	 * @param image_name
	 * @param description
	 */
	public Standard(String roomID, int totalBedrooms, String featureSummary, String roomType, String roomStatus,
			double rentFee, double lateFee, String image_name, String description) {
		super(roomID, totalBedrooms, featureSummary, roomType, roomStatus, rentFee, lateFee, image_name, description);
	}

	@Override
	public void rent(String customerId, Date rentDate, int numOfRentDay) throws RentException {

		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date(rentDate.getTime()));
		c1.add(Calendar.DATE, numOfRentDay);
		Date estimatedReturnDate = c1.getTime();

		try {
			rentDateValidation(rentDate, estimatedReturnDate);
		} catch (ClassNotFoundException e1) {

			e1.printStackTrace();
		} catch (SQLException e1) {

			e1.printStackTrace();
		}

		int minDays = 0;
		int maxDays = 10;
		if ((c1.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (Calendar.DAY_OF_WEEK == Calendar.SUNDAY)) {
			// Minimum three days if the rental day is in weekends.
			minDays = 3;
		} else {
			// Minimum two days if the rental day is one of the weekdays
			minDays = 2;
		}

		if (numOfRentDay < minDays) {
			throw new RentException("You have to stay atleast " + minDays + " days here");
		}
		if (numOfRentDay > maxDays) {
			throw new RentException("The room is available for " + maxDays + " days only");
		}

		try {
			insertHiringRecord(customerId, rentDate, numOfRentDay);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void returnRoom(Date returnDate) throws ReturnException {

		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date(returnDate.getTime()));
		Date estimatedDate = null;
		double totalRent = 0.00;
		double lateRentTotal = 0.00;
		double netFee = 0.00;
		DBStore dbstore = null;
		try {
			dbstore = new DBStore();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			HiringRecord hrObj = dbstore.findLatestHiringRecord(roomID);

			long difference1 = (returnDate.getTime() - hrObj.getRentDate().getTime()) / 86400000;
			if (difference1 < 0) {
				throw new ReturnException("Invalid.Return date is prior to the rent date.");
			}
			estimatedDate = hrObj.getEstimatedDate();
			long difference2 = (returnDate.getTime() - estimatedDate.getTime()) / 86400000;
			int extraDays = (int) difference2;
			if (extraDays < 0) {
				totalRent = rentFeeCalculation(returnDate, hrObj.getRentDate());
				netFee = totalRent + lateRentTotal;
				hrObj.rentFee(returnDate, totalRent, totalRent);
			} else if (extraDays == 0) {
				totalRent = this.rentFeeCalculation(estimatedDate, hrObj.getRentDate());
				netFee = totalRent + lateRentTotal;
				hrObj.rentFee(returnDate, totalRent, totalRent);
			} else {
				totalRent = this.rentFeeCalculation(estimatedDate, hrObj.getRentDate());
				lateRentTotal = this.lateRentCalculation(returnDate, estimatedDate);
				netFee = totalRent + lateRentTotal;
				hrObj.lateFee(returnDate, totalRent, lateRentTotal, netFee);
			}

			hrObj.setActualReturnDate(returnDate);
			hrObj.setRentFee(totalRent);
			hrObj.setLateFee(lateRentTotal);
			hrObj.setTotalRentFee(netFee);
			System.out.println("\nReturn Details of standard");
			System.out.println("------------");
			System.out.println(hrObj.toString());
			System.out.println(hrObj.getDetails());
			updateReturnDetails(hrObj);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void performMaintenance() throws MaintenanceException, ClassNotFoundException, SQLException {
		DBStore dbstore = new DBStore();
		dbstore.updateRoomStatus(roomID, "MAINTENANCE");

	}

}