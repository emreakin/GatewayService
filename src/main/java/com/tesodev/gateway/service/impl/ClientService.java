package com.tesodev.gateway.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    
	    HttpEntity<String> request = new HttpEntity<>(clientRequest.getRequestBody(), headers);
	    
	    ResponseEntity<String> response = this.restTemplate.exchange(clientRequest.getUrl(), clientRequest.getHttpMethod(), request, String.class);
	    if(response.getStatusCode() == HttpStatus.OK) {
	        return response.getBody();
	    } else {
	        return null;
	    }
	}
}
