package com.tesodev.gateway.model;

import org.springframework.http.HttpMethod;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "set")
public class ClientRequestModel {

	private String url;
	private String requestBody;
	private HttpMethod httpMethod;
}
