package com.tesodev.gateway.model;

import lombok.Data;

@Data
public class RestResponse<T> {

	private T result;
	private String error;
	private long responseTime;
}
