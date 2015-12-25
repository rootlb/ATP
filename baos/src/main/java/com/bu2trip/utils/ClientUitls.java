package com.bu2trip.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.baosheng.bs3.interfaces.client.http.Bs3ApiHttpClient;
import com.baosheng.bs3.interfaces.client.http.Bs3ApiHttpClientImpl;
import com.baosheng.bs3.interfaces.client.http.Bs3ApiHttpResponse;
import com.baosheng.bs3.interfaces.client.json.JsonConverter;
import com.baosheng.bs3.interfaces.client.security.SecurityDES;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ClientUitls {
	@Value("${boas.login.name}")
	private static String username  = "nick";
	@Value("${boas.login.password}")
	private static String password= "nick";
	@Value("${boas.login.deskey}")
	private static String deskey = "nick_baosheng_international_test";
	@Value("${boas.login.agencyCode}")
	private static String agencyCode = "4561";
	
	public static Object client(String url, Object req, Object resp){
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
}
