package com.truckdriver.buyGPS.Service;

import java.util.List;

import sharedEntity.BuyGPS;

import com.truckdriver.buyGPS.Model.BuyGPSPostRequest;
import com.truckdriver.buyGPS.Model.BuyGPSPutRequest;
import com.truckdriver.buyGPS.Response.CreateBuyGPSResponse;
import com.truckdriver.buyGPS.Response.DeleteBuyGPSResponse;
import com.truckdriver.buyGPS.Response.UpdateBuyGPSResponse;

public interface BuyGPSService 
{
	public CreateBuyGPSResponse addBuyGPS (BuyGPSPostRequest buygpsrequest);
	
	public BuyGPS getBuyGPS (String gpsId);
	
	public List<BuyGPS> getBuyGPS (String truckId, String transporterId, String purchaseDate, 
								   Boolean installedStatus);
	
	public UpdateBuyGPSResponse updateBuyGPS (String gpsId, BuyGPSPutRequest buygpsrequest);
	
	public DeleteBuyGPSResponse DeleteBuyGPS (String gpsId);
	

}
