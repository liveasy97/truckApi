
package com.TruckApi.TruckApi;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;


import java.sql.Timestamp;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Optional;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.TruckApi.TruckApi.Constants.TruckConstants;
import com.TruckApi.TruckApi.Dao.SecondTruckDao;
import com.TruckApi.TruckApi.Dao.TruckDao;
import com.TruckApi.TruckApi.Exception.BusinessException;
import com.TruckApi.TruckApi.Exception.EntityNotFoundException;
import com.TruckApi.TruckApi.Model.TruckCreateResponse;
import com.TruckApi.TruckApi.Model.TruckDeleteResponse;
import com.TruckApi.TruckApi.Model.TruckRequest;
import com.TruckApi.TruckApi.Model.TruckUpdateRequest;
import com.TruckApi.TruckApi.Model.TruckUpdateResponse;
import com.TruckApi.TruckApi.Service.TruckServiceImpl;
import com.TruckApi.TruckApi.entities.TruckData;
import com.TruckApi.TruckApi.entities.TruckTransporterData;

@SpringBootTest
public class TestTruckService {
	
private static Validator validator;
	
    @BeforeAll
    public static void setUp() {
       ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
       validator = factory.getValidator();
    }

	@Autowired
	private TruckServiceImpl truckService;

	@MockBean
	private TruckDao truckDao;

	@MockBean
	private SecondTruckDao sTruckDao;

	@Test
	public void addDataSuccess() {

		TruckRequest truckRequest = new TruckRequest("transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69",
				"AP 32 AD 2220", "alpha", (long) 50, "driver:0de885e0-5f43-4c68-8dde-b25464747865", 16, (long) 60,
				TruckData.TruckType.OPEN_HALF_BODY);

		List<TruckData> listTruckData = createTruckData();

		List<TruckTransporterData> listTruckTransporterData = createTrucTransporterkData();

		when(truckDao.save(listTruckData.get(0))).thenReturn(listTruckData.get(0));

		when(sTruckDao.save(listTruckTransporterData.get(0))).thenReturn(listTruckTransporterData.get(0));

		TruckCreateResponse truckCreateResponse = new TruckCreateResponse(TruckConstants.ADD_SUCCESS,
				"transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69", null, "AP 32 AD 2220", false, "alpha", (long) 50,
				"driver:0de885e0-5f43-4c68-8dde-b25464747865", 16, (long) 60, TruckData.TruckType.OPEN_HALF_BODY);

		assertEquals(truckCreateResponse.getStatus(), truckService.addData(truckRequest).getStatus());

	}

	@Test
	public void addDataFailed_nullTransporterId() {

		TruckRequest truckRequest = new TruckRequest(null, "AP 32 AD 2220", null, (long) 0, null, null, null, null);
		
		
		Set<ConstraintViolation<TruckRequest>> constraintViolations = validator.validate(truckRequest);
		Iterator<ConstraintViolation<TruckRequest>> itr= constraintViolations.iterator();
		Set<String> set =new HashSet<String>();
		while(itr.hasNext()) set.add(itr.next().getMessage());
		
		assertEquals(1, constraintViolations.size());
		
		assertTrue(set.contains("Transporter Id can not be null"));
	

	}

	@Test
	public void addDataFailed_invalidTruckNo_null() {

		TruckRequest truckRequest = new TruckRequest("transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69", null, null,
				0, null, null, null, null);

		Set<ConstraintViolation<TruckRequest>> constraintViolations = validator.validate( truckRequest );
		Iterator<ConstraintViolation<TruckRequest>> itr= constraintViolations.iterator();
		Set<String> set =new HashSet<String>();
		while(itr.hasNext()) set.add(itr.next().getMessage());
		
		assertEquals(1, constraintViolations.size());
		
		assertTrue(set.contains("Truck Number can not be null"));

	}

	@Test
	public void addDataFailed_invalidTyre() {

		TruckRequest truckRequest = new TruckRequest("transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb68", "UP32AD2219",
				null, 0, null, 3 , null,TruckData.TruckType.OPEN_HALF_BODY);

		Throwable exception = assertThrows(
				BusinessException.class, () -> {
					 truckService.addData(truckRequest);
					
    }
);
		assertEquals("Error: Failed: invalid number of tyres", exception.getMessage());

	}
	
	
	@Test
	public void addDataFailed_invalidTruckNo_notNull1() {

		TruckRequest truckRequest = new TruckRequest("transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb68", "A32ad2219",
				null, 0, null, null, null, null);

		Throwable exception = assertThrows(
				BusinessException.class, () -> {
					 truckService.addData(truckRequest);
					
    }
);
		assertEquals("Error: Failed: Enter Correct Truck Number", exception.getMessage());

	}

	


	@Test
	public void getTruckDataWithIdSuccess() {

		List<TruckData> listTruckData = createTruckData();

		when(truckDao.findByTruckId(TruckConstants.TRUCK_ID)).thenReturn(listTruckData.get(0));
		assertEquals(listTruckData.get(0), truckService.getDataById(TruckConstants.TRUCK_ID));

	}

	@Test
	public void updateSuccess() {

		List<TruckData> listTruckData = createTruckData();

		when(truckDao.findByTruckId(TruckConstants.TRUCK_ID)).thenReturn(listTruckData.get(0));

		TruckUpdateRequest truckUpdateRequest = new TruckUpdateRequest(false, "beta", (long) 1000, "driver:abccde",
				null, null, null);

		TruckUpdateResponse response = new TruckUpdateResponse(TruckConstants.UPDATE_SUCCESS,
				listTruckData.get(0).getTransporterId(), TruckConstants.TRUCK_ID, "AP 32 AD 2220", false, "beta",
				(long) 1000, "driver:abccde", 16, (long) 40, TruckData.TruckType.OPEN_HALF_BODY);
		
		TruckUpdateResponse result= truckService.updateData(TruckConstants.TRUCK_ID, truckUpdateRequest);
		
		System.err.println(response);
		System.err.println(result);
		
		

		assertEquals(response, result);

	}

	@Test
	public void updateDataFailed_AccountNotFound() {

		String wrongTruckId = "truckId:62cc8557-52cd-4742-a11e-276cc7abcde";

		List<TruckData> listTruckData = createTruckData();

		when(truckDao.findByTruckId("truckId:62cc8557-52cd-4742-a11e-276cc7abcde")).thenReturn(null);

		TruckUpdateRequest truckUpdateRequest = new TruckUpdateRequest(false, "beta", (long) 1000, "driver:abccde",
				null, null, null);

		Throwable exception = assertThrows(
				EntityNotFoundException.class, () -> {
				truckService.updateData("truckId:62cc8557-52cd-4742-a11e-276cc7abcde",truckUpdateRequest);
			
	            }
	    );
		System.err.println(exception.getMessage());
	    assertEquals("TruckData was not found for parameters {truckId=truckId:62cc8557-52cd-4742-a11e-276cc7abcde}", exception.getMessage());
	}
	
	@Test
	public void updateDataFailed_invalid_tyre() {


		List<TruckData> listTruckData = createTruckData();

		when(truckDao.findByTruckId(TruckConstants.TRUCK_ID)).thenReturn(listTruckData.get(0));

		TruckUpdateRequest truckUpdateRequest = new TruckUpdateRequest(false, "beta", (long) 1000, "driver:abccde",
				3, null, null);

		Throwable exception = assertThrows(
				BusinessException.class, () -> {
				truckService.updateData(TruckConstants.TRUCK_ID,truckUpdateRequest);
			
	            }
	    );
		System.err.println(exception.getMessage());
	    assertEquals("Error: Failed: invalid number of tyres", exception.getMessage());
	}
	
	

	@Test
	public void getTruckDataPagableSuccess_TransporterId() {

		String transporterId = TruckConstants.TRANSPORTER_ID;
		Boolean truckApproved = null;
		String truckId = null;

		Pageable currentPage;
		Integer pageNo;

		List<TruckData> listTruckData = createTruckData();

		pageNo = 0;
		currentPage = PageRequest.of(0, (int) TruckConstants.pageSize,Sort.Direction.DESC, "timestamp");

		when(truckDao.findByTransporterId(transporterId, currentPage)).thenReturn(listTruckData);
		assertEquals(listTruckData,
				truckService.getTruckDataPagableService(pageNo, transporterId, truckApproved, truckId));

	}

	@Test
	public void getTruckDataPagableSuccess_TruckApproved() {

		String transporterId = null;
		Boolean truckApproved = true;
		String truckId = null;

		Pageable currentPage;
		Integer pageNo;

		List<TruckData> listTruckData = createTruckData();

		pageNo = 0;
		currentPage = PageRequest.of(0, (int) TruckConstants.pageSize,Sort.Direction.DESC, "timestamp");

		when(truckDao.findByTruckApproved(truckApproved, currentPage)).thenReturn(listTruckData);
		assertEquals(listTruckData,
				truckService.getTruckDataPagableService(pageNo, transporterId, truckApproved, truckId));

	}

	@Test
	public void getTruckDataPagableSuccess_TransporterIdAndTruckApproved() {

		String transporterId = TruckConstants.TRANSPORTER_ID;
		Boolean truckApproved = true;
		String truckId = null;

		Pageable currentPage;
		Integer pageNo;

		List<TruckData> listTruckData = createTruckData();

		pageNo = 0;
		currentPage = PageRequest.of(0, (int) TruckConstants.pageSize,Sort.Direction.DESC, "timestamp");

		when(truckDao.findByTransporterIdAndTruckApproved(transporterId, truckApproved, currentPage))
				.thenReturn(listTruckData);
		assertEquals(listTruckData,
				truckService.getTruckDataPagableService(pageNo, transporterId, truckApproved, truckId));

	}

	@Test
	public void deleteDataSuccess() {

		List<TruckData> listTruckData = createTruckData();

		List<TruckTransporterData> listTruckTransporterData = createTrucTransporterkData();

		when(truckDao.findByTruckId(TruckConstants.TRUCK_ID)).thenReturn(listTruckData.get(0));

		when(sTruckDao.findByTruckId(TruckConstants.TRUCK_ID)).thenReturn(listTruckTransporterData.get(0));

		TruckDeleteResponse response = new TruckDeleteResponse(TruckConstants.DELETE_SUCCESS);

		assertEquals(response, truckService.deleteData(TruckConstants.TRUCK_ID));

	}

	@Test
	public void deleteDataFailed_AccountNotFound() {

		String wrongTruckId = "truckId:62cc8557-52cd-4742-a11e-276cc7abcde";

		List<TruckData> listTruckData = createTruckData();

		when(truckDao.findByTruckId("truckId:62cc8557-52cd-4742-a11e-276cc7abcde")).thenReturn(listTruckData.get(0));

		Throwable exception = assertThrows(
				EntityNotFoundException.class, () -> {
				truckService.deleteData("truckId:62cc8557-52cd-4742-a11e-276cc7abcde");
			
	            }
	    );
		System.err.println(exception.getMessage());
	    assertEquals("TruckTransporterData was not found for parameters {truckId=truckId:62cc8557-52cd-4742-a11e-276cc7abcde}", exception.getMessage());
	}

	

	public List<TruckData> createTruckData() {
		List<TruckData> truckList = Arrays.asList(
				new TruckData(TruckConstants.TRUCK_ID, TruckConstants.TRANSPORTER_ID,
						"AP 32 AD 2220", true, "alpha", (long) 50, "driver:0de885e0-5f43-4c68-8dde-b25464747865", 16,
						(long) 40, TruckData.TruckType.OPEN_HALF_BODY, Timestamp.valueOf("2021-07-28 23:28:50.134")),
				new TruckData(TruckConstants.TRUCK_ID, TruckConstants.TRANSPORTER_ID,
						"AP 32 AD 2220", true, "alpha", (long) 50, "driver:0de885e0-5f43-4c68-8dde-b25464747865", 16,
						(long) 40, TruckData.TruckType.OPEN_HALF_BODY, Timestamp.valueOf("2021-07-28 23:28:50.134")),
				new TruckData(TruckConstants.TRUCK_ID, TruckConstants.TRANSPORTER_ID,
						"AP 32 AD 2220", true, "alpha", (long) 50, "driver:0de885e0-5f43-4c68-8dde-b25464747865", 16,
						(long) 40, TruckData.TruckType.OPEN_HALF_BODY, Timestamp.valueOf("2021-07-28 23:28:50.134")),
				new TruckData(TruckConstants.TRUCK_ID, TruckConstants.TRANSPORTER_ID,
						"AP 32 AD 2220", true, "alpha", (long) 50, "driver:0de885e0-5f43-4c68-8dde-b25464747865", 16,
						(long) 40, TruckData.TruckType.OPEN_HALF_BODY, Timestamp.valueOf("2021-07-28 23:28:50.134")),
				new TruckData(TruckConstants.TRUCK_ID, TruckConstants.TRANSPORTER_ID,
						"AP 32 AD 2220", true, "alpha", (long) 50, "driver:0de885e0-5f43-4c68-8dde-b25464747865", 16,
						(long) 40, TruckData.TruckType.OPEN_HALF_BODY, Timestamp.valueOf("2021-07-28 23:28:50.134")),
				new TruckData(TruckConstants.TRUCK_ID, TruckConstants.TRANSPORTER_ID,
						"AP 32 AD 2220", true, "alpha", (long) 50, "driver:0de885e0-5f43-4c68-8dde-b25464747865", 16,
						(long) 40, TruckData.TruckType.OPEN_HALF_BODY, Timestamp.valueOf("2021-07-28 23:28:50.134")),
				new TruckData(TruckConstants.TRUCK_ID, TruckConstants.TRANSPORTER_ID,
						"AP 32 AD 2220", true, "alpha", (long) 50, "driver:0de885e0-5f43-4c68-8dde-b25464747865", 16,
						(long) 40, TruckData.TruckType.OPEN_HALF_BODY, Timestamp.valueOf("2021-07-28 23:28:50.134")));

		return truckList;
	}

	public List<TruckTransporterData> createTrucTransporterkData() {
		List<TruckTransporterData> truckList = Arrays.asList(
				new TruckTransporterData(TruckConstants.TRUCK_ID, "transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69"),
				new TruckTransporterData("id3", TruckConstants.TRANSPORTER_ID));

		return truckList;
	}

}