package com.tesodev.gateway.service.impl;

import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;

import com.tesodev.gateway.http.IHttpClientService;
import com.tesodev.gateway.model.ClientRequestModel;
import com.tesodev.gateway.model.HttpRequestModel;
import com.tesodev.gateway.model.HttpRequestModel.HttpRequestModelBuilder;
import com.tesodev.gateway.model.HttpResponseModel;
import com.tesodev.gateway.service.IClientService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ClientService implements IClientService {
	
	private final IHttpClientService httpClientService;
	
	public String getRestResponse(ClientRequestModel clientRequest){

		HttpRequestModel params = createHttpRequestParams(clientRequest);

		String body = null;
		try {
			HttpResponseModel execute = httpClientService.execute(params);
			body = execute.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return body;
	}
	
	private HttpRequestModel createHttpRequestParams(ClientRequestModel clientRequest) {
		HttpRequestModelBuilder httpReqBuilder = HttpRequestModel.builder()
				.setCharset(StandardCharsets.UTF_8.name())
				.setUseGzip(false)
				.setMethod(clientRequest.getHttpMethod())
				.setUrl(clientRequest.getUrl())
				.setContentType(MediaType.APPLICATION_JSON_VALUE)
				.setPostData(clientRequest.getRequestBody())
				.setAuthorizationToken("Bearer " + clientRequest.getJwtToken());

		return httpReqBuilder.build();
	}
}
