package com.bu2trip.request;

import lombok.Data;

@Data
public class AirFlightRequest {
	private String OriCode;
	private String DesCode;
	private String DepartureDate;
	private String CarrierAirline;
	private String JourneyType;
	private String Cabin;
}
