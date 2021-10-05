package com.truckdriver.buyGPS.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BuyGPSExceptionAdvice extends ResponseEntityExceptionHandler
{


     @ExceptionHandler (EntityNotFoundException.class)
     public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex)
     {
    	 ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND);
    	 errorResponse.setMessage(ex.getMessage());
    	 return  buildResponseEntity(errorResponse);
     }

	private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
		// TODO Auto-generated method stub
		return new ResponseEntity<> (errorResponse, errorResponse.getStatus());
	}

	
	
	

	
}
