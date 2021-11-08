package com.tesodev.gateway.http;

import com.tesodev.gateway.model.HttpRequestModel;
import com.tesodev.gateway.model.HttpResponseModel;

public interface IHttpClientService {

	HttpResponseModel execute(HttpRequestModel params) throws Exception;

}
