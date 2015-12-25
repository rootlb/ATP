package com.bu2trip.internation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.baosheng.bs3.interfaces.client.http.Bs3ApiHttpClient;
import com.baosheng.bs3.interfaces.client.http.Bs3ApiHttpClientImpl;
import com.baosheng.bs3.interfaces.client.http.Bs3ApiHttpResponse;
import com.baosheng.bs3.interfaces.client.security.SecurityDES;
import com.baosheng.bs3.interfaces.client.vo.international.common.FlightJourney;
import com.baosheng.bs3.interfaces.client.vo.international.flightbook.AirPriceInfo;
import com.baosheng.bs3.interfaces.client.vo.international.flightbook.BookTicketRequest;
import com.baosheng.bs3.interfaces.client.vo.international.flightbook.BookTicketResponse;
import com.baosheng.bs3.interfaces.client.vo.international.flightbook.ContactInfo;
import com.baosheng.bs3.interfaces.client.vo.international.flightbook.InvoiceDeliveryInfo;
import com.baosheng.bs3.interfaces.client.vo.international.flightbook.Passenger;
import com.baosheng.bs3.interfaces.client.vo.international.flightbook.PnrInfo;
import com.baosheng.bs3.interfaces.client.vo.international.flightbook.SegmentInfo;
import com.baosheng.bs3.interfaces.client.vo.international.flightsearch.AirFlight;
import com.baosheng.bs3.interfaces.client.vo.international.flightsearch.AirFlightShopIRQ;
import com.baosheng.bs3.interfaces.client.vo.international.flightsearch.AirFlightShopIRS;
import com.baosheng.bs3.interfaces.client.vo.international.flightsearch.AvailableCondition;
import com.baosheng.bs3.interfaces.client.vo.international.flightsearch.AvailableJourneyWithPrices;
import com.baosheng.bs3.interfaces.client.vo.international.flightsearch.CabinPrice;
import com.baosheng.bs3.interfaces.client.vo.international.flightsearch.PassengerModel;
import com.baosheng.bs3.interfaces.client.vo.international.verifyseat.IntlPATRequest;
import com.baosheng.bs3.interfaces.client.vo.international.verifyseat.IntlPATResponse;
import com.bu2trip.config.BaseTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AirFlightShopIRQTest extends BaseTest{
	

	private String requestTime;
	@Test
	public void airFlinghtShopTest(){
	    AirFlightShopIRQ flightShop = new AirFlightShopIRQ();
	    AirFlightShopIRS fligheShops = new AirFlightShopIRS();
		// AvailableCondition
		AvailableCondition availableCondition = new AvailableCondition();
		availableCondition.setDepCityName("CTU");
		availableCondition.setArrCityName("ROM");
		availableCondition.setDepDate("2015-12-19");
		List<AvailableCondition> queryAvailableConditionList = new ArrayList<AvailableCondition>();
		queryAvailableConditionList.add(availableCondition);
		flightShop.setQueryAvailableConditionList(queryAvailableConditionList);

		// PassengerModel
		PassengerModel passengerModel = new PassengerModel();
		passengerModel.setPassgerIdentity("0");
		passengerModel.setPassgerNumber(1);
		passengerModel.setPassgerType("ADT");
		List<PassengerModel> passengerType = new ArrayList<PassengerModel>();
		passengerType.add(passengerModel);
		flightShop.setPassengerType(passengerType);

		flightShop.setJourneyType("OW");// 
		flightShop.setPhysicalCabin("ECONOMY");// 
		flightShop.setDirectOnly(false);
		//flightShop.setCarrier("3U");
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			String url = "http://124.205.15.148:9080/api/international/airflightshop.json";
			
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(flightShop);
			Bs3ApiHttpClient client = new Bs3ApiHttpClientImpl(username, password, url);
			String data = SecurityDES.des3EncodeECB(deskey, json, null);
			Bs3ApiHttpResponse response = client.sendHttpPostMethod(data, agencyCode);
			
			fligheShops = mapper.readValue(response.getResponseBody(), fligheShops.getClass());
			
			for( AvailableJourneyWithPrices resultss :fligheShops.getResults()){
				
				for(CabinPrice cabinPrice : resultss.getCabinGroups()){
					log.info("    航空公司："+resultss.getTicketingCarrier()+
							 "       原价:"+resultss.getBasePriceAmount().toString()+
							 "      结算价:"+resultss.getSettlementPriceAmount().toString()+
							 "预定回调密码串:"+cabinPrice.getSecretKey());
				}
				
			}
			requestTime = fligheShops.getRequestTime();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void IntlPATRequestTest(){
		IntlPATRequest intlPATRequest = new IntlPATRequest();
		IntlPATResponse IntlPATResponse = new IntlPATResponse();
		List<FlightJourney> segments = new ArrayList<FlightJourney>();
		FlightJourney flightJourney= new FlightJourney();
		List<SegmentInfo> segmentList = new ArrayList<SegmentInfo>();
		SegmentInfo segmentInfo = new SegmentInfo();
		segmentInfo.setFlightNumber("8903");
		segmentInfo.setDepDate("2015-12-19");
		segmentInfo.setDepTime("15:20:00");
		segmentInfo.setDepCode("CTU");
		segmentInfo.setAirwayCode("3U");
		segmentInfo.setArrCode("ICN");
		segmentInfo.setArrDate("2015-12-19");
		segmentInfo.setArrTime("19:35:00");
		segmentInfo.setClassCode("Y");
		
		segmentList.add(segmentInfo);
		flightJourney.setSegmentList(segmentList);
		segments.add(flightJourney);
		intlPATRequest.setSegments(segments);
		intlPATRequest.setUuid("550e8400-e29b-41d4-a716-446655440000");
		ObjectMapper mapper = new ObjectMapper();
		String url = "http://124.205.15.148:9080/api/international/checkcabin.json";
		
		try {
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(intlPATRequest);
			Bs3ApiHttpClient client = new Bs3ApiHttpClientImpl(username, password, url);
			String data = SecurityDES.des3EncodeECB(deskey, json, null);
			Bs3ApiHttpResponse response = client.sendHttpPostMethod(data, agencyCode);
			IntlPATResponse = mapper.readValue(response.getResponseBody(), IntlPATResponse.getClass());
			log.info(IntlPATResponse.getRestClass());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void airBookTest(){
		BookTicketRequest bookTicketRequest = new BookTicketRequest();
		BookTicketResponse bookTicketResponse = new BookTicketResponse();
		PnrInfo pnrInfo = new PnrInfo();
		InvoiceDeliveryInfo invoiceDeliveryInfo = new InvoiceDeliveryInfo();
		ContactInfo contactInfo = new ContactInfo();
		AirFlightShopIRS fligheShops = airFlinghtShop();
		AirFlight airFlight = fligheShops.getResults().get(0).getAvailableJourneys().get(0).getAvailableJourneyOptions().get(0).getAirFlightSearchResults().get(0);
		//航段信息
		List<FlightJourney> segments = new ArrayList<FlightJourney>();
		FlightJourney flightJourney= new FlightJourney();
		List<SegmentInfo> segmentList = new ArrayList<SegmentInfo>();
		SegmentInfo segmentInfo = new SegmentInfo();
		segmentInfo.setFlightNumber(airFlight.getAirFlightNo());
		segmentInfo.setDepDate("2015-12-19");
		segmentInfo.setDepTime("15:20:00");
		segmentInfo.setDepCode("CTU");
		segmentInfo.setAirwayCode("3U");
		segmentInfo.setArrCode("ICN");
		segmentInfo.setArrDate("2015-12-19");
		segmentInfo.setArrTime("19:35:00");
		segmentInfo.setClassCode("Y");
		segmentList.add(segmentInfo);
		flightJourney.setSegmentList(segmentList);
		segments.add(flightJourney);
		pnrInfo.setFlightInfo(segments);
		
		//航程信息
		pnrInfo.setJourneyType("OW");
		
		//乘机人信息
		List<Passenger>passengers = new ArrayList<Passenger>();
		Passenger passenger = new Passenger();
		passenger.setName("肖伟林");
		passenger.setPassengerType("ADT");
		passenger.setIdentityType("0");
		passenger.setIdentity("CN");
		passenger.setIdentityNo("G37974392");
		passenger.setPassengerMobile("13551155269");
		passenger.setBirth("1980-04-11");
		passenger.setSex("MALE");
		passenger.setIdentityVaildDate("2019-04-11");
		passenger.setNationality("CN");
		passenger.setIdentity("0");
		passenger.setSurName("xiao");
		passenger.setGivenName("weilin");
		passenger.setCertificateCountry("CN");
		passengers.add(passenger);
		pnrInfo.setPassenger(passengers);
		
		
		//价格信息
		CabinPrice cabinPrice =fligheShops.getResults().get(0).getCabinGroups().get(0);
		com.baosheng.bs3.interfaces.client.vo.international.flightbook.CabinPrice cabinPrices = new com.baosheng.bs3.interfaces.client.vo.international.flightbook.CabinPrice();
		cabinPrices.setPhysicalCabins(cabinPrice.getPhysicalCabins());
		cabinPrices.setCabinGroup(cabinPrice.getCabinGroup());
		cabinPrices.setFlightCabinGroup(cabinPrice.getFlightCabinGroup());
		cabinPrices.setSeats(cabinPrice.getSeats());
		cabinPrices.setTicketingCarrier(cabinPrice.getTicketingCarrier());
		cabinPrices.setTotalAmount(new BigDecimal(cabinPrice.getTotalAmount()));
		List<AirPriceInfo> airPriceInfos = new ArrayList<AirPriceInfo>();
		
		for(com.baosheng.bs3.interfaces.client.vo.international.flightsearch.AirPriceInfo s :cabinPrice.getAirPriceList()){
			AirPriceInfo airPriceInfo = new AirPriceInfo();
			BigDecimal BigDecimal =new BigDecimal(s.getTicketPriceAmount());
			airPriceInfo.setTicketPriceAmount(BigDecimal);
			airPriceInfo.setSupplierPrice(new BigDecimal(s.getSupplierPrice()));
			airPriceInfo.setTaxAmount(new BigDecimal(s.getTaxAmount()));
			log.info("@#@:"+s.getPassengerType());
			airPriceInfo.setPassengerType(s.getPassengerType());
			log.info("@#@:"+s.getPassengerIdentity());
			airPriceInfo.setPassengerIdentity(s.getPassengerIdentity());
			log.info("@#@:"+s.getTicketRuleRQ());
			airPriceInfo.setTicketRuleRQ(s.getTicketRuleRQ());
			airPriceInfo.setPolicyInfo(s.getPolicyInfo());
			airPriceInfo.setBaggages(s.getBaggages());
			airPriceInfo.setPlatformPrice(new BigDecimal("1816.0"));
			airPriceInfos.add(airPriceInfo);
		}
		cabinPrices.setAirPriceList(airPriceInfos);
		cabinPrices.setSecretKey(fligheShops.getResults().get(0).getCabinGroups().get(0).getSecretKey());
		pnrInfo.setCabinPrice(cabinPrices);
		//预定回调密码串
		pnrInfo.setSecretKey(fligheShops.getResults().get(0).getCabinGroups().get(0).getSecretKey());
		//订单总价
		
		pnrInfo.setTotalAmount(new BigDecimal(cabinPrice.getTotalAmount()));
		//退改签规定
		//pnrInfo.setTicketRuleRes();
		//出票航空公司
		pnrInfo.setTicketingCarrier("3U");
		//总票价面值
		pnrInfo.setTotalTicketPrice(airPriceInfos.get(0).getTicketPriceAmount());
		//总税费
		pnrInfo.setTotalTax(airPriceInfos.get(0).getTaxAmount());
		//备注信息
		
		
		
		//预定信息
		bookTicketRequest.setPnrInfo(pnrInfo);
		
		
		//行程单邮寄地址
		invoiceDeliveryInfo.setDeliveryType(2);
		invoiceDeliveryInfo.setName("肖伟林");
		invoiceDeliveryInfo.setMobile("13551155269");
		bookTicketRequest.setInvoiceDeliveryInfo(invoiceDeliveryInfo);
		
		//联系人信息
		contactInfo.setCreatorCn("肖伟林");
		contactInfo.setCreatorPhone("13551155269");
		bookTicketRequest.setContactInfo(contactInfo);
		
		//时间戳
		bookTicketRequest.setRequestTime(fligheShops.getRequestTime());
		
		ObjectMapper mapper = new ObjectMapper();
		String url = "http://124.205.15.148:9080/api/international/airbook.json";
		
		try {
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bookTicketRequest);
			Bs3ApiHttpClient client = new Bs3ApiHttpClientImpl(username, password, url);
			String data = SecurityDES.des3EncodeECB(deskey, json, null);
			Bs3ApiHttpResponse response = client.sendHttpPostMethod(data, agencyCode);
			bookTicketResponse = mapper.readValue(response.getResponseBody(), bookTicketResponse.getClass());
			log.info("@@#@@"+bookTicketResponse.toString());
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	
	public AirFlightShopIRS airFlinghtShop(){
	    AirFlightShopIRQ flightShop = new AirFlightShopIRQ();
	    AirFlightShopIRS fligheShops = new AirFlightShopIRS();
		// AvailableCondition
		AvailableCondition availableCondition = new AvailableCondition();
		availableCondition.setDepCityName("CTU");
		availableCondition.setArrCityName("ICN");
		availableCondition.setDepDate("2015-12-19");
		List<AvailableCondition> queryAvailableConditionList = new ArrayList<AvailableCondition>();
		queryAvailableConditionList.add(availableCondition);
		flightShop.setQueryAvailableConditionList(queryAvailableConditionList);

		// PassengerModel
		PassengerModel passengerModel = new PassengerModel();
		passengerModel.setPassgerIdentity("0");
		passengerModel.setPassgerNumber(1);
		passengerModel.setPassgerType("ADT");
		List<PassengerModel> passengerType = new ArrayList<PassengerModel>();
		passengerType.add(passengerModel);
		flightShop.setPassengerType(passengerType);

		flightShop.setJourneyType("OW");
		flightShop.setPhysicalCabin("ECONOMY");
		flightShop.setDirectOnly(false);
		flightShop.setCarrier("3U");
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			String url = "http://124.205.15.148:9080/api/international/airflightshop.json";
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(flightShop);
			Bs3ApiHttpClient client = new Bs3ApiHttpClientImpl(username, password, url);
			String data = SecurityDES.des3EncodeECB(deskey, json, null);
			Bs3ApiHttpResponse response = client.sendHttpPostMethod(data, agencyCode);
			
			fligheShops = mapper.readValue(response.getResponseBody(), fligheShops.getClass());
			
			for( AvailableJourneyWithPrices resultss :fligheShops.getResults()){
				
				for(CabinPrice cabinPrice : resultss.getCabinGroups()){
					log.info("    航空公司："+resultss.getTicketingCarrier()+
							 "       原价:"+resultss.getBasePriceAmount().toString()+
							 "      结算价:"+resultss.getSettlementPriceAmount().toString()+
							 "预定回调密码串:"+cabinPrice.getSecretKey());
				}
				
			}
			requestTime = fligheShops.getRequestTime();
			return fligheShops;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
