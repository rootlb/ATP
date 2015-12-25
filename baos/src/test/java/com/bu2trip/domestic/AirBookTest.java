package com.bu2trip.domestic;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.utils.DateUtils;
import org.junit.Test;

import com.baosheng.bs3.interfaces.client.http.Bs3ApiHttpClient;
import com.baosheng.bs3.interfaces.client.http.Bs3ApiHttpClientImpl;
import com.baosheng.bs3.interfaces.client.http.Bs3ApiHttpResponse;
import com.baosheng.bs3.interfaces.client.json.JsonConverter;
import com.baosheng.bs3.interfaces.client.security.SecurityDES;
import com.baosheng.bs3.interfaces.client.vo.domestic.common.AirFlightSegmentInfo;
import com.baosheng.bs3.interfaces.client.vo.domestic.common.AirPriceInfo;
import com.baosheng.bs3.interfaces.client.vo.domestic.common.CabinPrice;
import com.baosheng.bs3.interfaces.client.vo.domestic.common.airbook.AirFlightBookRQ;
import com.baosheng.bs3.interfaces.client.vo.domestic.common.airbook.AirFlightBookRS;
import com.baosheng.bs3.interfaces.client.vo.domestic.common.airbook.AirPassengerInfo;
import com.baosheng.bs3.interfaces.client.vo.domestic.fightsearch.AirFlightShopDRQ;
import com.baosheng.bs3.interfaces.client.vo.domestic.fightsearch.AirFlightShopDRS;
import com.baosheng.bs3.interfaces.client.vo.domestic.fightsearch.AvailableJourney;
import com.baosheng.bs3.interfaces.client.vo.domestic.fightsearch.AvailableJourneyWithPrices;

import com.bu2trip.config.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class AirBookTest extends BaseTest{
	
	//用于请求
	public Object client(String url, Object req, Object resp){
		ObjectMapper mapper = new ObjectMapper();
		String json;
		try {
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(req);
			Bs3ApiHttpClient client = new Bs3ApiHttpClientImpl(username, password, url);
			String data = SecurityDES.des3EncodeECB(deskey, json, null);
			Bs3ApiHttpResponse response = client.sendHttpPostMethod(data, agencyCode);
			resp =JsonConverter.parseObject(response.getResponseBody(), resp.getClass());
			return resp;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public AirFlightShopDRS airFlightShopDRQ(){
		AirFlightShopDRQ airFlightShopDRQ = new AirFlightShopDRQ();
		AirFlightShopDRS airFlightShopDRS = new AirFlightShopDRS();
		airFlightShopDRQ.setOriCode("CTU");
		airFlightShopDRQ.setDesCode("CGO");
		airFlightShopDRQ.setDepartureDate("19DEC15");
		//AirFlightShopDRQ.setReturnDate("");返程时间
		airFlightShopDRQ.setCarrierAirline("3U");
		airFlightShopDRQ.setJourneyType("OW");
		airFlightShopDRQ.setCabin("Y");
		//AirFlightShopDRQ.setAvJourneys(null);
		
		try {
			String url = "http://124.205.15.148:9080/api/domestic/airflightshop.json";	
			airFlightShopDRS =(AirFlightShopDRS)client(url,airFlightShopDRQ,airFlightShopDRS);
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
	
	public AirFlightBookRS airFlightBook(){
		//查询结果
		AirFlightShopDRS airFlightShopDRS = airFlightShopDRQ();
		AirFlightBookRS bookrs = new AirFlightBookRS();
		
		AvailableJourneyWithPrices resultss = airFlightShopDRS.getFlightshopResults().get(0);
		AvailableJourney avJourneys = resultss.getAvJourneys().get(0);
		AirFlightSegmentInfo airFlights = avJourneys.getAirFlights().get(0);
		CabinPrice cabinPrice = resultss.getCabinGroups().get(0);
		AirFlightBookRQ bookrq = new AirFlightBookRQ();
		log.debug("@#@"+Integer.toString(cabinPrice.getAirPriceList().size()));
		AirPriceInfo airPriceInfo = cabinPrice.getAirPriceList().get(0);
		List<AirFlightSegmentInfo> flightInfo= new ArrayList<AirFlightSegmentInfo>();
		flightInfo.add(airFlights);
		
		List<AirPassengerInfo> AirPassengerInfos = new ArrayList<AirPassengerInfo>();
		AirPassengerInfo AirPassengerInfo = new AirPassengerInfo();
		AirPassengerInfo.setPassengerName("肖伟林");
		AirPassengerInfo.setPassengerType("0");
		AirPassengerInfo.setCentificateType("0");
		AirPassengerInfo.setCentificateCode("411381199009304812");
		AirPassengerInfo.setPassengerMobile("18603834398");
		
		AirPassengerInfo.setBirthday(DateUtils.parseDate("1990-09-30"));
		AirPassengerInfo.setGender("Male");
		AirPassengerInfo.setExpirdDate("2019-09-12");
		AirPassengerInfo.setPassengerTitle("0");
		AirPassengerInfo.setRph("1");
		AirPassengerInfos.add(AirPassengerInfo);
		
		
		//查询时间戳
		bookrq.setRequestTime(airFlightShopDRS.getRequestTime());
		//预定回调密串
		bookrq.setSecretKey(cabinPrice.getSecretKey());
		//订单总价
		bookrq.setTotalAmount(resultss.getSettlementPriceAmount());
		//总税费
		bookrq.setTotalTax(airPriceInfo.getTaxAmount());
		//退订规则
		bookrq.setTicketRuleRes("");
		//出票航空公司
		bookrq.setTicketingCarrier(resultss.getTicketingCarrier());
		//票总价
		bookrq.setTotalTicketPrice(resultss.getSettlementPriceAmount());
		//价格信息
		bookrq.setCabinPrice(cabinPrice);
		//旅客信息
		bookrq.setAirPassengerInfos(AirPassengerInfos);
		//航段信息
		bookrq.setFlightInfo(flightInfo);
		//联系人姓名
		bookrq.setContactName("肖伟林");
		//联系人电话
		bookrq.setContactPhone("18603834398");
		
		ObjectMapper mapper = new ObjectMapper();
		String url = "http://124.205.15.148:9080/api/domestic/airflightbook.json";
		String json;
		try {
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookrq);
			Bs3ApiHttpClient client = new Bs3ApiHttpClientImpl(username, password, url);
			String data = SecurityDES.des3EncodeECB(deskey, json, null);
			Bs3ApiHttpResponse response = client.sendHttpPostMethod(data, agencyCode);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
		
		
		bookrs = (AirFlightBookRS)client(url,bookrq,bookrs);
		return null;
	}
	@Test
	public void airFlightShopTest(){
		airFlightShopDRQ();
		
	}
	
	@Test
	public void airFlightBookTest(){
		airFlightBook();
	}
}
