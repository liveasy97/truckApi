package com.truckdriver.truck.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckUpdateRequest {

	private Boolean truckApproved;

	private String imei;
	private String deviceId;
	private long passingWeight;
	private String driverId;
	private Integer tyres;
	private Long truckLength;

	private String truckType;
}
