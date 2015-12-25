package com.bu2trip.service.impl;

import org.springframework.stereotype.Service;

import com.baosheng.bs3.interfaces.client.vo.domestic.common.CabinPrice;
import com.baosheng.bs3.interfaces.client.vo.domestic.fightsearch.AirFlightShopDRQ;
import com.baosheng.bs3.interfaces.client.vo.domestic.fightsearch.AirFlightShopDRS;
import com.baosheng.bs3.interfaces.client.vo.domestic.fightsearch.AvailableJourneyWithPrices;
import com.bu2trip.service.AirFlightShopIRQService;
import com.bu2trip.utils.ClientUitls;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class AirFlightShopIRQImpl implements AirFlightShopIRQService{
	@Override
	public AirFlightShopDRS airFlightShopDRQ(AirFlightShopDRQ airFlightShopDRQ) {
		// TODO Auto-generated method stub
		AirFlightShopDRS airFlightShopDRS = new AirFlightShopDRS();
		try {
			String url = "http://124.205.15.148:9080/api/domestic/airflightshop.json";	
			airFlightShopDRS =(AirFlightShopDRS)ClientUitls.client(url,airFlightShopDRQ,airFlightShopDRS);
			for( AvailableJourneyWithPrices resultss :airFlightShopDRS.getFlightshopResults()){
				
				for(CabinPrice cabinPrice : resultss.getCabinGroups()){
				log.info("    航空公司："+resultss.getTicketingCarrier()+
						 "       原价:"+resultss.getTktPrice().toString()+
						 "      结算价:"+resultss.getSettlementPriceAmount().toString()+
						 "        机建:"+resultss.getTaxCN()+
						 "预定回调密码串:"+cabinPrice.getSecretKey());
				}

			}
			return airFlightShopDRS;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
