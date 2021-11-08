package com.tesodev.gateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tesodev.gateway.model.ClientRequestModel;
import com.tesodev.gateway.service.IClientService;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Service
public class ClientService implements IClientService {

	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public String getRestResponse(ClientRequestModel clientRequest){
		return restTemplate.getForObject(clientRequest.getUrl(), String.class);
	}

}
