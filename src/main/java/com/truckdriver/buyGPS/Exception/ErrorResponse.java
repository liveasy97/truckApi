package com.truckdriver.buyGPS.Exception;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ErrorResponse 
{

	private HttpStatus status;
	private String message;
	private String debugMessage;
	
	public ErrorResponse(HttpStatus status) 
	{
		super();
		this.status = status;
	}

	public ErrorResponse(HttpStatus status, String message, Throwable ex) 
	{
		super();
		this.status = status;
		this.message = message;
		this.debugMessage=ex.getLocalizedMessage();
	}
	
	public ErrorResponse(HttpStatus status, Throwable ex) 
	{
		super();
		this.status = status;
	    this.message="unexpected error";
	    this.debugMessage=ex.getLocalizedMessage();
	}

	
	
	
	
	
}
