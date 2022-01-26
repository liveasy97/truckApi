package com.truckdriver.truck.Model;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sharedEntity.TruckData.RcStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TruckRequest {

	@NotBlank(message = "Transporter Id can not be null")
	private String transporterId;
	@NotBlank(message = "Truck Number can not be null")
	private String truckNo;

	private String imei;
	private String deviceId;
	private long passingWeight;
	private String driverId;
	private Integer tyres;
	private Long truckLength;

	private String truckType;
	public RcStatus rcStatus;

}
