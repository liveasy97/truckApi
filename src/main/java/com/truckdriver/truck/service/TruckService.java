package com.truckdriver.truck.service;

import java.util.List;

import com.truckdriver.truck.Model.TruckCreateResponse;
import com.truckdriver.truck.Model.TruckDeleteResponse;
import com.truckdriver.truck.Model.TruckRequest;
import com.truckdriver.truck.Model.TruckUpdateRequest;
import com.truckdriver.truck.Model.TruckUpdateResponse;

import sharedEntity.TruckData;


public interface TruckService {
	public TruckCreateResponse addData(TruckRequest truckRequest);

	public TruckUpdateResponse updateData(String id, TruckUpdateRequest truckUpdateRequest);

	public TruckDeleteResponse deleteData(String id);

	public TruckData getDataById(String Id);

	public List<TruckData> getTruckDataPagableService(Integer pageNo, String transporterId, Boolean truckApproved,
			String truckId);

}
