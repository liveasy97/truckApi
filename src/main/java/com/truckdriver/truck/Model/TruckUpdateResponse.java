package com.truckdriver.truck.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sharedEntity.TruckData.RcStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckUpdateResponse {

	private String status;
	private String transporterId;
	private String truckId;

	private String truckNo;

	private Boolean truckApproved;
	private String imei;
	private String deviceId;
	private long passingWeight;
	private String driverId;
	private Integer tyres;

	private Long truckLength;

	private String truckType;
	private RcStatus rcStatus;
}
