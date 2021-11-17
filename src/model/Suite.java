package model;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import database.DBStore;
import exception.MaintenanceException;
import exception.RentException;
import exception.ReturnException;

public class Suite extends Room {

	public Suite() {
		super();
	}

	/**
	 * 
	 * @param roomID
	 * @param totalBedrooms
	 * @param featureSummary
	 * @param roomType
	 * @param roomStatus
	 * @param lastMaintenanceDate
	 * @param rentFee
	 * @param lateFee
	 * @param image_name
	 * @param description
	 */
	public Suite(String roomID, int totalBedrooms, String featureSummary, String roomType, String roomStatus,
			Date lastMaintenanceDate, double rentFee, double lateFee, String image_name, String description) {
		super(roomID, totalBedrooms, featureSummary, roomType, roomStatus, lastMaintenanceDate, rentFee, lateFee,
				image_name, description);
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

		int maxDays = 10;

		Calendar c2 = Calendar.getInstance();
		c2.setTime(new Date(this.lastMaintenanceDate.getTime()));
		c2.add(Calendar.DATE, 10);
		Date nextMaintenance = c2.getTime();

		long difference1 = this.lastMaintenanceDate.getTime() - estimatedReturnDate.getTime();
		long difference2 = rentDate.getTime() - this.lastMaintenanceDate.getTime();
		if (difference1 <= 0 && difference2 <= 0) {
			throw new RentException("Room is unavailable due to maintenance work. ");
		}

		long nextMaintenanceDateCheck = nextMaintenance.getTime() - rentDate.getTime();

		if (nextMaintenance.equals(rentDate)) {
			throw new RentException("Room is unavailable due to maintenance work ");
		}

		long difference3 = nextMaintenance.getTime() - this.lastMaintenanceDate.getTime();

		long differ4 = nextMaintenance.getTime() - estimatedReturnDate.getTime();
		long differ5 = rentDate.getTime() - nextMaintenance.getTime();

		if (differ4 <= 0 && differ5 <= 0) {
			throw new RentException("Room is unavailable due to maintenance work ");
		}
		if (difference3 <= 0 && nextMaintenanceDateCheck <= 0) {
			throw new RentException("Room is unavailable due to maintenance work ");
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
	public void returnRoom(Date returnDate) throws ReturnException, ClassNotFoundException, SQLException {

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
		HiringRecord hrObj = dbstore.findLatestHiringRecord(roomID);
		long difference1 = (returnDate.getTime() - hrObj.getRentDate().getTime()) / 86400000;
		if (difference1 < 0) {
			throw new ReturnException("Invalid return date, Rent date is prior to the return date.");
		}

		Calendar c2 = Calendar.getInstance();
		c2.setTime(new Date(this.lastMaintenanceDate.getTime()));
		c2.add(Calendar.DATE, 10);
		Date nextMaintenance = c2.getTime();

		long difference2 = (returnDate.getTime() - nextMaintenance.getTime()) / 86400000;
		if (difference2 < 0) {
			throw new ReturnException("Invalid Return date.");
		}

		estimatedDate = hrObj.getEstimatedDate();
		long difference3 = (returnDate.getTime() - estimatedDate.getTime()) / 86400000;
		int extraDays = (int) difference3;
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

		System.out.println("\nReturn Details of suite");
		System.out.println("------------");
		System.out.println(hrObj.toString());
		System.out.println(hrObj.getDetails());

		updateReturnDetails(hrObj);
	}

	@Override
	public void performMaintenance() throws MaintenanceException, ClassNotFoundException, SQLException {
	}

}
