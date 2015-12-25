package com.bu2trip.enums;

import lombok.AllArgsConstructor;

/**
 * 保盛机票接口响应结果
 * <p>
 * code：响应代码<br>
 * message：响应消息
 * <p>
 * @author johnson
 *
 */
@AllArgsConstructor
public enum ResponseEnum {
	SUCCESS("0000","成功"),
	AGENCY_CODE_ERROR("0001","无效的agencyCode"),
	NOT_AUTHORITY_IP("0002","未授权的IP地址"),
	SIGNATURE_ERROR("0003","签名错误"),
	AUTHORITY_EXPIRATION("0004","授权已经过期"),
	NOT_AUTHORITY_ACCESS("0005","未授权接口访问"),
	INTERFACE_ERROR("0009","接口异常"),
	PAYMENTS_FAILURE("0010","支付失败"),
	NO_AIRLINES_DATA("0011","保盛没有航班数据返回"),
	NO_AIRLINES("0012","没有航线"),
	REQUEST_PARAM_ERROR("0013","请求参数异常"),
	BOOKING_ERROR("0014","预定失败"),
	BOOKING("0026","预定中"),
	PNR_ERROR("0027","调用取消pnr接口失败"),
	PNR_NOT_EXIST("0028","pnr不存在"),
	ORDER_STATUS_ERROR("0029","取消订单的状态不正确"),
	NO_ORDER("0030","该机构无此订单");
	
	private String code;
	private String message;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
