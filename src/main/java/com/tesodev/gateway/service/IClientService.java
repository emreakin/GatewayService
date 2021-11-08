package com.tesodev.gateway.service;

import com.tesodev.gateway.model.ClientRequestModel;

public interface IClientService {

	String getRestResponse(ClientRequestModel clientRequest);
}
