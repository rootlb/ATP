package com.bu2trip.service;

import org.springframework.stereotype.Service;

import com.baosheng.bs3.interfaces.client.vo.domestic.fightsearch.AirFlightShopDRQ;
import com.baosheng.bs3.interfaces.client.vo.domestic.fightsearch.AirFlightShopDRS;

public interface AirFlightShopIRQService {
	public AirFlightShopDRS airFlightShopDRQ(AirFlightShopDRQ airFlightShopDRQ);
}
