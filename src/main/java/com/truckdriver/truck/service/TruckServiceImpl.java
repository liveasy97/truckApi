package com.truckdriver.truck.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.truckdriver.truck.Constants.TruckConstants;
import com.truckdriver.truck.Exception.BusinessException;
import com.truckdriver.truck.Exception.EntityNotFoundException;
import com.truckdriver.truck.Model.TruckCreateResponse;
import com.truckdriver.truck.Model.TruckDeleteResponse;
import com.truckdriver.truck.Model.TruckRequest;
import com.truckdriver.truck.Model.TruckUpdateRequest;
import com.truckdriver.truck.Model.TruckUpdateResponse;

import lombok.extern.slf4j.Slf4j;
import sharedDao.SecondTruckDao;
import sharedDao.TruckDao;
import sharedEntity.TruckData;
import sharedEntity.TruckData.RcStatus;
import sharedEntity.TruckTransporterData;

@Service
@Slf4j
public class TruckServiceImpl implements TruckService {

	@Autowired
	TruckDao truckDao;
	@Autowired
	SecondTruckDao sTruckDao;

	private TruckConstants truckConstants;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public TruckCreateResponse addData(TruckRequest truckRequest) {

		TruckCreateResponse truckCreateResponse = new TruckCreateResponse();

		String truckNo = truckRequest.getTruckNo();

		String checkTruckNo = TruckConstants.CHECK_TRUCK_NO;

		if (!truckNo.matches(checkTruckNo)) {
			log.error(truckConstants.TRUCK_NO_IS_INVALID);
			throw new BusinessException(truckConstants.TRUCK_NO_IS_INVALID);

		}

		String truckNoUpdated = generateTruckNumber(truckNo);

		// sending truckData to TruckData Table.
		TruckData truckData = new TruckData();

		String truckId_temp = "truck:" + UUID.randomUUID().toString();
		truckData.setTruckId(truckId_temp);
		truckData.setTransporterId(truckRequest.getTransporterId());
		truckData.setTruckNo(truckNoUpdated);
		if (truckRequest.getImei() != null) {
			truckData.setImei(truckRequest.getImei());
		}
		if (truckRequest.getDeviceId() != null) {
			truckData.setDeviceId(truckRequest.getDeviceId());
		}
		if (truckRequest.getPassingWeight() != 0) {
			truckData.setPassingWeight(truckRequest.getPassingWeight());
		}
		if (truckRequest.getDriverId() != null) {
			truckData.setDriverId(truckRequest.getDriverId());
		}

		if (truckRequest.getTruckLength() != null) {
			truckData.setTruckLength(truckRequest.getTruckLength());
		}
		if (truckRequest.getRcStatus() != null) {
			if ("pending".equals(String.valueOf(truckRequest.getRcStatus())))
				truckData.setRcStatus(RcStatus.pending);
			else if ("verified".equals(String.valueOf(truckRequest.getRcStatus())))
				truckData.setRcStatus(RcStatus.verified);
			else if ("rejected".equals(String.valueOf(truckRequest.getRcStatus())))
				truckData.setRcStatus(RcStatus.rejected);
			else {
				log.error(truckConstants.INVALID_RC_STATUS_ERROR);
				throw new BusinessException(truckConstants.INVALID_RC_STATUS_ERROR);
			}
		} else {
			truckData.setRcStatus(RcStatus.notAdded);
		}

		if (truckRequest.getTruckType() != null) {

			truckData.setTruckType(truckRequest.getTruckType());
		}

		if (truckRequest.getTyres() != null) {
			if (truckRequest.getTyres() >= 4 && truckRequest.getTyres() <= 26 && truckRequest.getTyres() % 2 == 0) {
				truckData.setTyres(truckRequest.getTyres());
			} else {
				log.error(truckConstants.INVALID_NUMBER_OF_TYRES_ERROR);
				throw new BusinessException(truckConstants.INVALID_NUMBER_OF_TYRES_ERROR);
			}
		}

		truckData.setTruckApproved(false);

		truckDao.save(truckData);

		// sending truckData to truckId - TransporterId table.
		TruckTransporterData sData = new TruckTransporterData();
		sData.setTransporterId(truckRequest.getTransporterId());
		sData.setTruckId(truckId_temp);

		sTruckDao.save(sData);
		log.info("Truck Data is saved");
		// Sending success postResponse
		truckCreateResponse.setStatus(truckConstants.ADD_SUCCESS);
		truckCreateResponse.setTransporterId(truckData.getTransporterId());
		truckCreateResponse.setTruckId(truckId_temp);
		truckCreateResponse.setDriverId(truckData.getDriverId());
		truckCreateResponse.setImei(truckData.getImei());
		truckCreateResponse.setDeviceId(truckData.getDeviceId());
		truckCreateResponse.setPassingWeight(truckData.getPassingWeight());
		truckCreateResponse.setTruckApproved(false);
		truckCreateResponse.setTruckLength(truckData.getTruckLength());
		truckCreateResponse.setTruckNo(truckData.getTruckNo());
		truckCreateResponse.setTruckType(truckData.getTruckType());
		truckCreateResponse.setTyres(truckData.getTyres());
		truckCreateResponse.setRcStatus(truckData.getRcStatus());
		log.info("Post Service Response returned");

		return truckCreateResponse;

	}

	@Transactional(rollbackFor = Exception.class)
	public TruckUpdateResponse updateData(String id, TruckUpdateRequest truckUpdateRequest) {

		TruckUpdateResponse response = new TruckUpdateResponse();
		TruckData truckData = truckDao.findByTruckId(id);

		if (truckData == null) {
			EntityNotFoundException ex = new EntityNotFoundException(TruckData.class, "truckId", id.toString());

			log.error(String.valueOf(ex));
			throw ex;
		}

		if (truckUpdateRequest.getImei() != null) {
			truckData.setImei(truckUpdateRequest.getImei());
		}
		if (truckUpdateRequest.getDeviceId() != null) {
			truckData.setDeviceId(truckUpdateRequest.getDeviceId());
		}

		if (truckUpdateRequest.getPassingWeight() != 0) {
			truckData.setPassingWeight(truckUpdateRequest.getPassingWeight());
		}

		if (truckUpdateRequest.getDriverId() != null) {
			truckData.setDriverId(truckUpdateRequest.getDriverId());
		}

		if (truckUpdateRequest.getTruckApproved() != null) {
			truckData.setTruckApproved(truckUpdateRequest.getTruckApproved());

		}

		if (truckUpdateRequest.getTruckLength() != null) {
			truckData.setTruckLength(truckUpdateRequest.getTruckLength());
		}

		if (truckUpdateRequest.getTruckType() != null) {
			truckData.setTruckType(truckUpdateRequest.getTruckType());
		}

		if (truckUpdateRequest.getRcStatus() != null) {
			if ("pending".equals(String.valueOf(truckUpdateRequest.getRcStatus())))
				truckData.setRcStatus(RcStatus.pending);
			else if ("verified".equals(String.valueOf(truckUpdateRequest.getRcStatus())))
				truckData.setRcStatus(RcStatus.verified);
			else if ("rejected".equals(String.valueOf(truckUpdateRequest.getRcStatus())))
				truckData.setRcStatus(RcStatus.rejected);
			else {
				log.error(truckConstants.INVALID_RC_STATUS_ERROR);
				throw new BusinessException(truckConstants.INVALID_RC_STATUS_ERROR);
			}
		} else {
			truckData.setRcStatus(RcStatus.notAdded);
		}

		if (truckUpdateRequest.getTyres() != null) {
			if (truckUpdateRequest.getTyres() >= 4 && truckUpdateRequest.getTyres() <= 26
					&& truckUpdateRequest.getTyres() % 2 == 0) {
				truckData.setTyres(truckUpdateRequest.getTyres());
			} else {
				log.error(truckConstants.INVALID_NUMBER_OF_TYRES_ERROR);
				throw new BusinessException(truckConstants.INVALID_NUMBER_OF_TYRES_ERROR);

			}
		}

		truckDao.save(truckData);
		log.info("Truck Data is updated");
		response.setStatus(truckConstants.UPDATE_SUCCESS);
		response.setTransporterId(truckData.getTransporterId());
		response.setTruckId(id);
		response.setDriverId(truckData.getDriverId());
		response.setImei(truckData.getImei());
		response.setDeviceId(truckData.getDeviceId());
		response.setPassingWeight(truckData.getPassingWeight());
		response.setTruckApproved(truckData.getTruckApproved());
		response.setTruckLength(truckData.getTruckLength());
		response.setTruckNo(truckData.getTruckNo());
		response.setTruckType(truckData.getTruckType());
		response.setTyres(truckData.getTyres());
		response.setRcStatus(truckData.getRcStatus());
		log.info("Put Service Response returned");
		return response;
	}

	@Transactional(rollbackFor = Exception.class)
	public TruckDeleteResponse deleteData(String id) {

		TruckDeleteResponse truckDeleteResponse = new TruckDeleteResponse();

		// TruckData findTruckData = truckDao.findByTruckId(id);
		// TruckTransporterData findTruckTransporterData = sTruckDao.findByTruckId(id);

		TruckData truckData = truckDao.findByTruckId(id);
		TruckTransporterData findTruckTransporterData = sTruckDao.findByTruckId(id);

		if (truckData == null || findTruckTransporterData == null) {
			if (truckData == null) {
				EntityNotFoundException ex = new EntityNotFoundException(TruckData.class, "truckId", id.toString());
				log.error(String.valueOf(ex));
				throw ex;
			} else {
				EntityNotFoundException ex = new EntityNotFoundException(TruckTransporterData.class, "truckId",
						id.toString());
				log.error(String.valueOf(ex));
				throw ex;

			}
		}

		truckDao.delete(truckData);
		sTruckDao.delete(findTruckTransporterData);
		log.info("Deleted");

		truckDeleteResponse.setStatus(truckConstants.DELETE_SUCCESS);
		log.info("Deleted Service Response returned");
		return truckDeleteResponse;
	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	@Override
	public TruckData getDataById(String Id) {

		TruckData truckData = truckDao.findByTruckId(Id);

		if (truckData == null) {
			EntityNotFoundException ex = new EntityNotFoundException(TruckData.class, "truckId", Id.toString());

			log.error(String.valueOf(ex));
			throw ex;
		}

		log.info("Truck Data returned");
		return truckData;

	}

	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public List<TruckData> getTruckDataPagableService(Integer pageNo, String transporterId, Boolean truckApproved,
			String truckId) {

		if (pageNo == null)
			pageNo = 0;

		Pageable currentPage = PageRequest.of(pageNo, TruckConstants.pageSize, Sort.Direction.DESC, "timestamp");

		if (truckId != null) {
			return truckDao.findByTruckId(truckId, currentPage);
		}

		else {
			if (transporterId != null && truckApproved == null) {
				log.info("Truck Data with params returned");
				return truckDao.findByTransporterId(transporterId, currentPage);
			}

			else if (transporterId == null && truckApproved != null) {
				log.info("Truck Data with params returned");
				return truckDao.findByTruckApproved(truckApproved, currentPage);
			}

			else if (transporterId != null && truckApproved != null) {
				log.info("Truck Data with params returned");
				return truckDao.findByTransporterIdAndTruckApproved(transporterId, truckApproved, currentPage);
			}

		}
		log.info("Truck Data get all method called");
		return truckDao.getAll(currentPage);
	}

	public String generateTruckNumber(String truckNo) {

		// removing all unnecessary sign's or spaces.
		// Example: converts "AP-31-RT-4555" to "AP31RT4555" i.e removes all extra
		// spaces and character
		String str = "";

		// dividing string based on continuous sequence of integers or character.
		// to increase readability
		String truckNoUpdated = "";

		for (int i = 0; i < truckNo.length(); i++) {

			if (truckNo.charAt(i) != ' ' && truckNo.charAt(i) != '-' && truckNo.charAt(i) != '/') {

				str += truckNo.charAt(i);
			}
		}

		// iterate till last 4th index
		for (int i = 0; i < str.length() - 4; i++) {

			truckNoUpdated += str.charAt(i);

			int l1 = Integer.valueOf(str.charAt(i));
			int l2 = Integer.valueOf(str.charAt(i + 1));

			// compares present and next character having same type or not, if different
			// type, add's extra space
			// Example: converts "AP32EEE4444" to "AP 32 EEE" i.e all integers and
			// characters separate
			if ((l1 >= 48 && l1 <= 57 && (l2 < 48 || l2 > 57)) || (l2 >= 48 && l2 <= 57 && (l1 < 48 || l1 > 57))) {

				truckNoUpdated += ' ';
			}
		}

		if (truckNoUpdated.charAt(truckNoUpdated.length() - 1) != ' ') {
			truckNoUpdated += ' ';
		}

		// add's the remaining last 4digits of truckNumber
		truckNoUpdated += str.substring(str.length() - 4, str.length());

		return truckNoUpdated;
	}

}
