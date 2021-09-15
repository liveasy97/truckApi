package com.truckdriver.driver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverResponse {

	private String driverId;
	private String status;
	private String transporterId;
	private String phoneNum;
	
	private String driverName;
	private String truckId;
}
