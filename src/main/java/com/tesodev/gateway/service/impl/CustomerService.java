package com.tesodev.gateway.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tesodev.gateway.model.BaseResponseModel;
import com.tesodev.gateway.model.ClientRequestModel;
import com.tesodev.gateway.model.CustomerModel;
import com.tesodev.gateway.model.CustomersResultModel;
import com.tesodev.gateway.service.IClientService;
import com.tesodev.gateway.service.ICustomerService;

@Service
public class CustomerService implements ICustomerService{

	@Value("${serviceurl.customer}")
	private String serviceUrl;
	
	private static final String LIST_ALL = "list";
	private static final String GET_CUSTOMER = "get";
	
	private final IClientService clientService;
	private final Gson gson = new Gson();
	
	public CustomerService(IClientService clientService) {
		this.clientService = clientService;
	}
	
	@Override
	public List<CustomerModel> listAllCustomers() {
		ClientRequestModel request = ClientRequestModel.builder()
				.setUrl(serviceUrl + "/api/customer/" + LIST_ALL)
				.setHttpMethod(HttpMethod.GET)
				.build();
		
		String response = clientService.getRestResponse(request);
		
		var typeToken = TypeToken.getParameterized(BaseResponseModel.class, CustomersResultModel.class);
		var fromJson = gson.fromJson(response, typeToken.getType());
		
		@SuppressWarnings("unchecked")
		var baseResponse = (BaseResponseModel<CustomersResultModel>) fromJson;
		
		return baseResponse.getResult().getCustomers();
	}

	@Override
	public CustomerModel getCustomer(String customerId) {
		ClientRequestModel request = ClientRequestModel.builder()
				.setUrl(serviceUrl + "/api/customer/" + GET_CUSTOMER + "/" + UUID.fromString(customerId))
				.setHttpMethod(HttpMethod.GET)
				.build();
		
		String response = clientService.getRestResponse(request);
		
		var typeToken = TypeToken.getParameterized(BaseResponseModel.class, CustomerModel.class);
		var fromJson = gson.fromJson(response, typeToken.getType());
		
		@SuppressWarnings("unchecked")
		var baseResponse = (BaseResponseModel<CustomerModel>) fromJson;
		
		return baseResponse.getResult();
	}

}
