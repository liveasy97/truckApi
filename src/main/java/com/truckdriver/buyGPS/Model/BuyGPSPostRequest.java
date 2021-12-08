package com.truckdriver.buyGPS.Model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;

import lombok.Data;

@Data
public class BuyGPSPostRequest 
{

	@NotBlank(message="transporterId cannot be empty")
    private String transporterId;
	
	@NotBlank(message="truckId cannot be left empty")
    private String truckId;
	
	@NotNull(message="rate cannot be left empty")
    private long rate;
	
	@NotBlank(message="duration has to be specified")
    private String duration;
	
	@NotBlank(message="address cannot be empty")
    private String address;
	
	@CreatedDate
    private String purchaseDate;
	
   
    private String imei;
    
   
}
