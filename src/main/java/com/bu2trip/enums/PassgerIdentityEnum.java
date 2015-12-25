package com.bu2trip.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum PassgerIdentityEnum {
	COMMON("0","普通"),
	STUDENT("2","学生"),
	VISITRE_LATIVES("5","探亲"),
	IMMIGRANT("6","移民");
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
