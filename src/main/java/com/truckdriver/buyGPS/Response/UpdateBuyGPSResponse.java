package com.truckdriver.buyGPS.Response;

import java.sql.Timestamp;

import lombok.Data;
@Data
public class UpdateBuyGPSResponse 
{
	private String message;
	
	private String gpsId;
	private String transporterId;
	private String truckId;
	private long rate;
	private String duration;
	private String address;
	private String purchaseDate;
	private boolean installedStatus;
	private String imei;
	public Timestamp timestamp;

}
