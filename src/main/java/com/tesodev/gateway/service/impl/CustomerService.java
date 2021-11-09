package com.tesodev.gateway.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tesodev.gateway.exception.ErrorCodes;
import com.tesodev.gateway.exception.ServiceException;
import com.tesodev.gateway.model.BaseResponseModel;
import com.tesodev.gateway.model.ClientRequestModel;
import com.tesodev.gateway.model.CustomerIdListRequestModel;
import com.tesodev.gateway.model.CustomerModel;
import com.tesodev.gateway.model.CustomersResultModel;
import com.tesodev.gateway.model.IdResultModel;
import com.tesodev.gateway.model.StatusResultModel;
import com.tesodev.gateway.service.IClientService;
import com.tesodev.gateway.service.ICustomerService;

@Service
public class CustomerService implements ICustomerService{

	@Value("${serviceurl.customer}")
	private String serviceUrl;
	
	private static final String API_BASE = "/api/customer/";
	private static final String CREATE_CUSTOMER = "create";
	private static final String UPDATE_CUSTOMER = "update";
	private static final String DELETE_CUSTOMER = "delete";
	private static final String LIST_ALL_CUSTOMER = "list";
	private static final String LIST_BY_CUSTOMERIDS = "listByIds";
	private static final String GET_CUSTOMER = "get";
	
	private final IClientService clientService;
	private final Gson gson = new Gson();
	
	public CustomerService(IClientService clientService) {
		this.clientService = clientService;
	}
	
	@Override
	public String createCustomer(CustomerModel customer) throws ServiceException {
		ClientRequestModel request = ClientRequestModel.builder()
				.setUrl(serviceUrl + API_BASE + CREATE_CUSTOMER)
				.setRequestBody(gson.toJson(customer))
				.setHttpMethod(HttpMethod.POST)
				.build();
		
		String response = clientService.getRestResponse(request);
		
		var typeToken = TypeToken.getParameterized(BaseResponseModel.class, IdResultModel.class);
		var fromJson = gson.fromJson(response, typeToken.getType());
		
		@SuppressWarnings("unchecked")
		var baseResponse = (BaseResponseModel<IdResultModel>) fromJson;
		
		if(baseResponse.getError() != null) {
			throw new ServiceException(ErrorCodes.CREATION_FAIL, "Customer create fail : " + baseResponse.getError());
		}
		
		return baseResponse.getResult().getId().toString();
	}

	@Override
	public boolean updateCustomer(CustomerModel customer) throws ServiceException {
		ClientRequestModel request = ClientRequestModel.builder()
				.setUrl(serviceUrl + API_BASE + UPDATE_CUSTOMER)
				.setRequestBody(gson.toJson(customer))
				.setHttpMethod(HttpMethod.PUT)
				.build();
		
		String response = clientService.getRestResponse(request);
		
		var typeToken = TypeToken.getParameterized(BaseResponseModel.class, StatusResultModel.class);
		var fromJson = gson.fromJson(response, typeToken.getType());
		
		@SuppressWarnings("unchecked")
		var baseResponse = (BaseResponseModel<StatusResultModel>) fromJson;
		
		if(baseResponse.getError() != null) {
			throw new ServiceException(ErrorCodes.UPDATE_FAIL, "Customer update fail : " + baseResponse.getError());
		}
		
		return baseResponse.getResult().isStatus();
	}

	@Override
	public boolean deleteCustomer(String customerId) throws ServiceException {
		ClientRequestModel request = ClientRequestModel.builder()
				.setUrl(serviceUrl + API_BASE + DELETE_CUSTOMER + "/" + UUID.fromString(customerId))
				.setHttpMethod(HttpMethod.DELETE)
				.build();
		
		String response = clientService.getRestResponse(request);
		
		var typeToken = TypeToken.getParameterized(BaseResponseModel.class, StatusResultModel.class);
		var fromJson = gson.fromJson(response, typeToken.getType());
		
		@SuppressWarnings("unchecked")
		var baseResponse = (BaseResponseModel<StatusResultModel>) fromJson;
		
		if(baseResponse.getError() != null) {
			throw new ServiceException(ErrorCodes.DELETE_FAIL, "Customer delete fail : " + baseResponse.getError());
		}
		
		return baseResponse.getResult().isStatus();
	}
	
	@Override
	public List<CustomerModel> listAllCustomers() throws ServiceException {
		ClientRequestModel request = ClientRequestModel.builder()
				.setUrl(serviceUrl + API_BASE + LIST_ALL_CUSTOMER)
				.setHttpMethod(HttpMethod.GET)
				.build();
		
		String response = clientService.getRestResponse(request);
		
		var typeToken = TypeToken.getParameterized(BaseResponseModel.class, CustomersResultModel.class);
		var fromJson = gson.fromJson(response, typeToken.getType());
		
		@SuppressWarnings("unchecked")
		var baseResponse = (BaseResponseModel<CustomersResultModel>) fromJson;
		
		if(baseResponse.getError() != null) {
			throw new ServiceException(ErrorCodes.DATA_RETREIVE_FAIL, "Customer get all fail : " + baseResponse.getError());
		}
		
		return baseResponse.getResult().getCustomers();
	}
	
	@Override
	public List<CustomerModel> listCustomersByCustomerIds(List<String> customerIds) throws ServiceException {
		ClientRequestModel request = ClientRequestModel.builder()
				.setUrl(serviceUrl + API_BASE + LIST_BY_CUSTOMERIDS)
				.setRequestBody(gson.toJson(new CustomerIdListRequestModel(customerIds)))
				.setHttpMethod(HttpMethod.POST)
				.build();
		
		String response = clientService.getRestResponse(request);
		
		var typeToken = TypeToken.getParameterized(BaseResponseModel.class, CustomersResultModel.class);
		var fromJson = gson.fromJson(response, typeToken.getType());
		
		@SuppressWarnings("unchecked")
		var baseResponse = (BaseResponseModel<CustomersResultModel>) fromJson;
		
		if(baseResponse.getError() != null) {
			throw new ServiceException(ErrorCodes.DATA_RETREIVE_FAIL, "Customer list by ids fail : " + baseResponse.getError());
		}
		
		return baseResponse.getResult().getCustomers();
	}

	@Override
	public CustomerModel getCustomer(String customerId) throws ServiceException {
		ClientRequestModel request = ClientRequestModel.builder()
				.setUrl(serviceUrl + API_BASE + GET_CUSTOMER + "/" + UUID.fromString(customerId))
				.setHttpMethod(HttpMethod.GET)
				.build();
		
		String response = clientService.getRestResponse(request);
		
		var typeToken = TypeToken.getParameterized(BaseResponseModel.class, CustomerModel.class);
		var fromJson = gson.fromJson(response, typeToken.getType());
		
		@SuppressWarnings("unchecked")
		var baseResponse = (BaseResponseModel<CustomerModel>) fromJson;
		
		if(baseResponse.getError() != null) {
			throw new ServiceException(ErrorCodes.DATA_RETREIVE_FAIL, "Customer get fail : " + baseResponse.getError());
		}
		
		return baseResponse.getResult();
	}

}
