package com.truckdriver.gpsApiData.service;

import java.util.List;
import java.util.stream.Stream;

import sharedEntity.historyData;

import sharedEntity.gpsData;


public interface gpsDataService {

	public List<historyData> getgpsData(String imei) throws Exception;
	public String savegpsData(gpsData data);
	public List<historyData> getHistoryData(String imei, String startTime, String endTime);
	public String addImei(String imei);
	public String saveHistoryData(historyData data);
}
