package com.TruckApi.TruckApi;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TimeZone;
import java.util.ArrayList;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.TruckApi.TruckApi.Constants.TruckConstants;
import com.TruckApi.TruckApi.Dao.TruckDao;
import com.TruckApi.TruckApi.entities.TruckData;

@DataJpaTest
public class TestTruckDao {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private TruckDao truckDao;
	
	public static void wait(int ms)
	{
	    try
	    {
	        Thread.sleep(ms);
	    }
	    catch(InterruptedException ex)
	    {
	        Thread.currentThread().interrupt();
	    }
	}

	@Test
	public void testFindByTruckId() {

		List<TruckData> listTruckData = createTruckData();

		TruckData savedInDb = entityManager.persist(listTruckData.get(0));
		TruckData getFromDb = truckDao.findByTruckId(TruckConstants.TRUCK_ID);
		
		System.err.println(savedInDb);
		System.err.println(getFromDb);
		

		assertThat(getFromDb).isEqualTo(savedInDb);

	}

	@Test
	public void testFindByTransporterId() {

		List<TruckData> listTruckData = new ArrayList<TruckData>();

		for(int i=1; i<=9; i++) {
			
			TruckData savedindb = entityManager.persist(new TruckData("truckId:1"+i, "transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69",
					"AP 32 AD 2220"+i , true, "alpha", (long) 50, "driver:0de885e0-5f43-4c68-8dde-b25464747865", 16,
					(long) 40, TruckData.TruckType.OPEN_HALF_BODY, Timestamp.valueOf("2021-07-28 23:28:50.134")));
			entityManager.flush();
			listTruckData.add(savedindb);
			wait(1);			
			 
		}
		
		System.err.println(listTruckData.toString());
 
		Collections.reverse(listTruckData);

		
		PageRequest firstPage = PageRequest.of(0, 5, Sort.Direction.DESC, "timestamp"),
			    secondPage = PageRequest.of(1, 5, Sort.Direction.DESC, "timestamp"),
			    thirdPage = PageRequest.of(2, 5, Sort.Direction.DESC, "timestamp");
		
		List<TruckData> trucks1 = truckDao.findByTransporterId("transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69", firstPage);
		assertThat(listTruckData.subList(0, 5)).isEqualTo(trucks1);
		List<TruckData> trucks2 = truckDao.findByTransporterId("transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69", secondPage);
		assertThat(listTruckData.subList(5, 9)).isEqualTo(trucks2);
		List<TruckData> trucks3 = truckDao.findByTransporterId("transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69", thirdPage);
		assertThat(listTruckData.subList(9, 9)).isEqualTo(trucks3);

	}

	@Test
	public void testFindByTruckApproved() {

		List<TruckData> listTruckData = new ArrayList<TruckData>();

		for(int i=1; i<=9; i++) {
					
					TruckData savedindb = entityManager.persist(new TruckData("truckId:1"+i, "transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69",
							"AP 32 AD 2220"+i , true, "alpha", (long) 50, "driver:0de885e0-5f43-4c68-8dde-b25464747865", 16,
							(long) 40, TruckData.TruckType.OPEN_HALF_BODY, Timestamp.valueOf("2021-07-28 23:28:50.134")));
					entityManager.flush();
					listTruckData.add(savedindb);
					wait(1);			
					 
				}
				
				Collections.reverse(listTruckData);
				System.err.println(listTruckData);

				
				PageRequest firstPage = PageRequest.of(0, 5, Sort.Direction.DESC, "timestamp"),
					    secondPage = PageRequest.of(1, 5, Sort.Direction.DESC, "timestamp"),
					    thirdPage = PageRequest.of(2, 5, Sort.Direction.DESC, "timestamp");
				
				List<TruckData> trucks1 = truckDao.findByTruckApproved(true, firstPage);
				assertThat(listTruckData.subList(0, 5)).isEqualTo(trucks1);
				List<TruckData> trucks2 = truckDao.findByTruckApproved(true, secondPage);
				assertThat(listTruckData.subList(5, 9)).isEqualTo(trucks2);
				List<TruckData> trucks3 = truckDao.findByTruckApproved(true, thirdPage);
				assertThat(listTruckData.subList(9, 9)).isEqualTo(trucks3);

			}

	@Test
	public void testFindByTransporterIdAndTruckApproved() {
		List<TruckData> listTruckData = new ArrayList<TruckData>();

		for(int i=1; i<=9; i++) {
			
			TruckData savedindb = entityManager.persist(new TruckData("truckId:1"+i, "transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69",
					"AP 32 AD 2220"+i , true, "alpha", (long) 50, "driver:0de885e0-5f43-4c68-8dde-b25464747865", 16,
					(long) 40, TruckData.TruckType.OPEN_HALF_BODY, Timestamp.valueOf("2021-07-28 23:28:50.134")));
			entityManager.flush();
			listTruckData.add(savedindb);
			wait(1);			
			 
		}
		
		System.err.println(listTruckData.toString());
 
		Collections.reverse(listTruckData);

		
		PageRequest firstPage = PageRequest.of(0, 5, Sort.Direction.DESC, "timestamp"),
			    secondPage = PageRequest.of(1, 5, Sort.Direction.DESC, "timestamp"),
			    thirdPage = PageRequest.of(2, 5, Sort.Direction.DESC, "timestamp");
		
		List<TruckData> trucks1 = truckDao.findByTransporterIdAndTruckApproved("transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69", true, firstPage);
		assertThat(listTruckData.subList(0, 5)).isEqualTo(trucks1);
		List<TruckData> trucks2 = truckDao.findByTransporterIdAndTruckApproved("transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69",true, secondPage);
		assertThat(listTruckData.subList(5, 9)).isEqualTo(trucks2);
		List<TruckData> trucks3 = truckDao.findByTransporterIdAndTruckApproved("transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69", true, thirdPage);
		assertThat(listTruckData.subList(9, 9)).isEqualTo(trucks3);

	}

	@Test
	public void testFindByTransporterIdAndTruckNo() {

		List<TruckData> listTruckData = createTruckData();

		TruckData savedInDb =  entityManager.persist(listTruckData.get(0));
		List<TruckData> getFromDb = truckDao.findByTransporterIdAndTruckNo(TruckConstants.TRANSPORTER_ID,"AP32AD2220");
	
		System.err.println(savedInDb);
		System.err.println(getFromDb);
		

		assertThat(getFromDb).isEqualTo(Arrays.asList(savedInDb)
);
	}
	
	@Test
	public void testgetALL() {
		List<TruckData> listTruckData = new ArrayList<TruckData>();

		for(int i=1; i<=9; i++) {
			
			TruckData savedindb = entityManager.persist(new TruckData("truckId:1"+i, "transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69"+i,
					"AP 32 AD 2220"+i , true, "alpha", (long) 50, "driver:0de885e0-5f43-4c68-8dde-b25464747865", 16,
					(long) 40, TruckData.TruckType.OPEN_HALF_BODY, Timestamp.valueOf("2021-07-28 23:28:50.134")));
			entityManager.flush();
			listTruckData.add(savedindb);
			wait(1);			
			 
		}
		
		System.err.println(listTruckData.toString());
 
		Collections.reverse(listTruckData);

		
		PageRequest firstPage = PageRequest.of(0, 5, Sort.Direction.DESC, "timestamp"),
			    secondPage = PageRequest.of(1, 5, Sort.Direction.DESC, "timestamp"),
			    thirdPage = PageRequest.of(2, 5, Sort.Direction.DESC, "timestamp");
		
		List<TruckData> trucks1 = truckDao.getAll( firstPage);
		assertThat(listTruckData.subList(0, 5)).isEqualTo(trucks1);
		List<TruckData> trucks2 = truckDao.getAll( secondPage);
		assertThat(listTruckData.subList(5, 9)).isEqualTo(trucks2);
		List<TruckData> trucks3 = truckDao.getAll( thirdPage);
		assertThat(listTruckData.subList(9, 9)).isEqualTo(trucks3);

	}
	
	


	public List<TruckData> createTruckData() {
		List<TruckData> truckList = Arrays.asList(
				new TruckData(TruckConstants.TRUCK_ID, TruckConstants.TRANSPORTER_ID,
						"AP32AD2220", true, "alpha", (long) 50, "driver:0de885e0-5f43-4c68-8dde-b25464747865", 16,
						(long) 40, TruckData.TruckType.OPEN_HALF_BODY, Timestamp.valueOf("2021-07-28 23:28:50.134")),
				new TruckData("id1", null, "AP 32 AD 2226", true, null, (long) 0, null, null, null, null,null),
				new TruckData("id2", "transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb69", null, true, null, (long) 0,
						null, null, null, null,null),
				new TruckData("id3", TruckConstants.TRANSPORTER_ID, "AP 32 AD 2220", true, null, (long) 0, null, null,
						null, null,null),
				new TruckData("id4", TruckConstants.TRANSPORTER_ID, "Ap32ad2219", true, null, (long) 0, null, null,
						null, null,null),
				new TruckData("id5", "transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb68", "A32ad2219", false, null,
						(long) 0, null, null, (long) 30, null,null),
				new TruckData("id6", "transporterId:0de885e0-5f43-4c68-8dde-b0f9ff81cb68", "Ap32ad221", false, null,
						(long) 0, null, null, (long) 40, null,null));

		return truckList;
	}
}
