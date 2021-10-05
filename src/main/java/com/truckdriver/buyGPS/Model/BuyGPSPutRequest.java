package com.truckdriver.buyGPS.Model;

import lombok.Data;

@Data
public class BuyGPSPutRequest 
{
	private long rate;
	private String duration;
	private String address;
	private Boolean installedStatus;
	private String imei;

}
