package com.bu2trip;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baosheng.bs3.interfaces.client.vo.domestic.fightsearch.AirFlightShopDRQ;
import com.baosheng.bs3.interfaces.client.vo.domestic.fightsearch.AirFlightShopDRS;
import com.bu2trip.request.AirFlightRequest;
import com.bu2trip.service.AirFlightShopIRQService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/AirFlightShop")
//@Scope("request")
@Slf4j
public class AirFlightShopIRQ {
	@Autowired
	AirFlightShopIRQService airFlightShopIRQService;
	
	@RequestMapping(method = RequestMethod.POST)
	public AirFlightShopDRS updatePermission(@RequestBody AirFlightRequest airFlightRequest) {
		try {
			AirFlightShopDRQ airFlightShopDRQ = new AirFlightShopDRQ();
			AirFlightShopDRS airFlightShopDRS = new AirFlightShopDRS();
			airFlightShopDRQ.setOriCode(airFlightRequest.getOriCode());
			airFlightShopDRQ.setDesCode(airFlightRequest.getDesCode());
			airFlightShopDRQ.setDepartureDate(airFlightRequest.getDepartureDate());
			//AirFlightShopDRQ.setReturnDate("");返程时间
			airFlightShopDRQ.setCarrierAirline(airFlightRequest.getCarrierAirline());
			airFlightShopDRQ.setJourneyType(airFlightRequest.getJourneyType());
			airFlightShopDRQ.setCabin(airFlightRequest.getCabin());
			airFlightShopDRS = airFlightShopIRQService.airFlightShopDRQ(airFlightShopDRQ);
		return airFlightShopDRS ;
		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return null ;
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String updatePermission() {
		try {
			log.info("success");;
		return "success";

		} catch (Exception e) {
			log.error(e.toString(), e);
		}
		return null ;
	}
}
