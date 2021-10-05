package com.truckdriver.driver.model;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverRequest {

	@NotBlank(message = "Transporter Id can not be null")
	private String transporterId;
	@NotBlank(message = "Phone Number can not be null")
	@Pattern(regexp = "^[6-9]\\d{9}$", message = "Must be a valid 10 digit number")
	private String phoneNum;
	@NotBlank(message = "Driver Name can not be null")
	private String driverName;
	private String truckId;

}
