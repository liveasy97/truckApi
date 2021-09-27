package com.truckdriver.buyGPS.Service;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.truckdriver.buyGPS.Constants.CommonConstants;
import sharedDao.BuyGPSDao;
import sharedEntity.BuyGPS;
import com.truckdriver.buyGPS.Exception.BusinessException;
import com.truckdriver.buyGPS.Exception.EntityNotFoundException;
import com.truckdriver.buyGPS.Model.BuyGPSPostRequest;
import com.truckdriver.buyGPS.Model.BuyGPSPutRequest;
import com.truckdriver.buyGPS.Response.CreateBuyGPSResponse;
import com.truckdriver.buyGPS.Response.DeleteBuyGPSResponse;
import com.truckdriver.buyGPS.Response.UpdateBuyGPSResponse;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class BuyGPSServiceImpl implements BuyGPSService
{
	@Autowired 
	private BuyGPSDao buygpsdao;

	@Override
	public CreateBuyGPSResponse addBuyGPS(BuyGPSPostRequest buygpsrequest) 
	{
	
		log.info("add BuyGPS service has started");
		
		String temp ="";
		BuyGPS buygps = new BuyGPS();
		CreateBuyGPSResponse response = new CreateBuyGPSResponse();
		
		temp = "gps:" + UUID.randomUUID();
		buygps.setGpsId(temp);
		response.setGpsId(temp);
		
		temp = buygpsrequest.getTransporterId();
		buygps.setTransporterId(temp);
		response.setTransporterId(temp);
		
		temp = buygpsrequest.getTruckId();
		buygps.setTruckId(temp);
		response.setTruckId(temp);
		
		buygps.setRate(buygpsrequest.getRate());
		
		temp = buygpsrequest.getDuration();
		buygps.setDuration(temp);
		response.setDuration(temp);
		
		temp = buygpsrequest.getAddress();
		buygps.setAddress(temp);
		response.setAddress(temp);
		
		buygps.setInstalledStatus(false);
		response.setInstalledStatus(false);
		
		buygps.setImei(null);
		response.setImei(null);
		
		buygpsdao.save(buygps);
		log.info("Buy gps information has been added");
		response.setTimestamp(buygps.getTimestamp());
		response.setPurchaseDate(buygps.getPurchaseDate());
		
		return response;
	}

	@Override
	public BuyGPS getBuyGPS(String gpsId) 
	{
		
		log.info("get by gps id has been initiated");
		Optional<BuyGPS> G = buygpsdao.findById(gpsId);
		if (G.isEmpty())
		{
			throw new EntityNotFoundException(BuyGPS.class, "id", gpsId.toString());
		}
		log.info("get by gpsId service has returned its reponse");
		return G.get();
	}

	@Override
	public List<BuyGPS> getBuyGPS(String truckId, String transporterId, String purchaseDate, Boolean installedStatus) {
		
		log.info("get GPS with params has started");
		
		if (truckId!= null)
		{
			try
			{
				return buygpsdao.findByTruckId(truckId);
			}
			catch(Exception ex)
			{
				log.error("BuyGPS data with params not returned" + String.valueOf(ex));
				throw ex;
			}
		}
		
		if (transporterId!=null)
		{
			try
			{
				return buygpsdao.findByTransporterId(transporterId);
			}
			catch (Exception ex)
			{
				log.error("BuyGPS data with params not returned" + String.valueOf(ex));
				throw ex;
			}
		}
		
		if(purchaseDate!=null)
		{
			try
			{
				return buygpsdao.findByPurchaseDate(purchaseDate);
			}
			catch (Exception ex)
			{
				log.error("BuyGPS data with params not returned" + String.valueOf(ex));
				throw ex;
			}
		}
		
		if(installedStatus != null)
		{
			try
			{
				return buygpsdao.findByInstalledStatus(installedStatus);
			}
			catch(Exception ex)
			{
				log.error("BuyGPS data with params not returned" + String.valueOf(ex));
				throw ex;
			}
		}
		try
		{
			log.info("BuyGPS data with params returned");
			return buygpsdao.findAll();
		}
		catch(Exception ex)
		{
			log.error("BuyGPS data with params not returned" + String.valueOf(ex));
			throw ex;
		}
	}

	@Override
	public UpdateBuyGPSResponse updateBuyGPS(String gpsId, BuyGPSPutRequest buygpsrequest) 
	{
		log.info("update BuyGps has started");
		
		UpdateBuyGPSResponse response = new UpdateBuyGPSResponse();
		BuyGPS buygps = buygpsdao.findById(gpsId).orElse(null);
		
		if(buygps==null)
		{
			EntityNotFoundException ex = new EntityNotFoundException(BuyGPS.class,"gpsId", gpsId.toString());
			throw ex;
		}
		if(buygpsrequest.getRate()!=0)
		{
			if(buygpsrequest.getDuration() == null)
			
				throw new BusinessException("Duration cannot be null when Rate is provided");
			buygps.setDuration(buygpsrequest.getDuration());
			buygps.setRate(buygpsrequest.getRate());
		}
		if(buygpsrequest.getAddress()!=null)
		{
			buygps.setAddress(buygpsrequest.getAddress());
		}
		if(String.valueOf(buygpsrequest.getInstalledStatus()).equals("true"))
		{
			buygps.setInstalledStatus(buygpsrequest.getInstalledStatus());
		}
		if(buygpsrequest.getImei()!=null)
		{
		    buygps.setImei(buygpsrequest.getImei());	
		}
		
		buygpsdao.save(buygps);
		log.info("updated");
		response.setMessage(CommonConstants.updateSuccess);
		response.setGpsId(buygps.getGpsId());
		response.setTransporterId(buygps.getTransporterId());
		response.setTruckId(buygps.getTruckId());
		response.setRate(buygps.getRate());
		response.setDuration(buygps.getDuration());
		response.setAddress(buygps.getAddress());
		response.setPurchaseDate(buygps.getPurchaseDate());
		response.setInstalledStatus(buygps.isInstalledStatus());
		response.setImei(buygps.getImei());
		response.setTimestamp(buygps.getTimestamp());
		
		log.info("Update service response returned");
		return response;
		
	}

	@Override
	public DeleteBuyGPSResponse DeleteBuyGPS(String gpsId)
	{
		log.info("deleteBuyGPS has started");

		DeleteBuyGPSResponse deleteGps = new DeleteBuyGPSResponse();
		
		Optional<BuyGPS> D = buygpsdao.findById(gpsId);
		if (D.isEmpty())
		{
			EntityNotFoundException ex = new EntityNotFoundException(BuyGPS.class, "gpsIs", gpsId.toString());
			log.error(String.valueOf(ex));
			throw ex;
		}
		else if(D.isPresent())
		{
			buygpsdao.deleteById(gpsId);
			deleteGps.setMessage(CommonConstants.deleteSuccess);
			log.info("delete BuyGPS service has been executed");
			return deleteGps;
		}
		else
		{
			deleteGps.setMessage(CommonConstants.AccountDoesntExist);
			return deleteGps;
		}
		
	}

}
