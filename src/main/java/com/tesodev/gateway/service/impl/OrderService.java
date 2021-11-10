package com.tesodev.gateway.service.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tesodev.gateway.exception.ErrorCodes;
import com.tesodev.gateway.exception.ServiceException;
import com.tesodev.gateway.model.BaseResponseModel;
import com.tesodev.gateway.model.ClientRequestModel;
import com.tesodev.gateway.model.CustomerModel;
import com.tesodev.gateway.model.OrderModel;
import com.tesodev.gateway.model.OrdersResultModel;
import com.tesodev.gateway.model.IdResultModel;
import com.tesodev.gateway.model.StatusResultModel;
import com.tesodev.gateway.service.IClientService;
import com.tesodev.gateway.service.ICustomerService;
import com.tesodev.gateway.service.IOrderService;

@Service
public class OrderService implements IOrderService {

	@Value("${serviceurl.order}")
	private String serviceUrl;
	
	private static final String API_BASE = "/api/order/";
	private static final String CREATE_ORDER = "create";
	private static final String UPDATE_ORDER = "update";
	private static final String DELETE_ORDER = "delete";
	private static final String LIST_ALL_ORDER = "list";
	private static final String GET_ORDER = "get";
	
	private final IClientService clientService;
	private final ICustomerService customerService;
	
	private final Gson gson = new Gson();
	
	public OrderService(IClientService clientService, ICustomerService customerService) {
		this.clientService = clientService;
		this.customerService = customerService;
	}
	
	@Override
	public String createOrder(OrderModel order) throws ServiceException {
		ClientRequestModel request = ClientRequestModel.builder()
				.setUrl(serviceUrl + API_BASE + CREATE_ORDER)
				.setRequestBody(gson.toJson(order))
				.setHttpMethod(HttpMethod.POST)
				.build();
		
		String response = clientService.getRestResponse(request);
		
		var typeToken = TypeToken.getParameterized(BaseResponseModel.class, IdResultModel.class);
		var fromJson = gson.fromJson(response, typeToken.getType());
		
		@SuppressWarnings("unchecked")
		var baseResponse = (BaseResponseModel<IdResultModel>) fromJson;
		
		if(baseResponse.getError() != null) {
			throw new ServiceException(ErrorCodes.CREATION_FAIL, "Order create fail : " + baseResponse.getError());
		}
		
		return baseResponse.getResult().getId().toString();
	}

	@Override
	public boolean updateOrder(OrderModel order) throws ServiceException {
		ClientRequestModel request = ClientRequestModel.builder()
				.setUrl(serviceUrl + API_BASE + UPDATE_ORDER)
				.setRequestBody(gson.toJson(order))
				.setHttpMethod(HttpMethod.PUT)
				.build();
		
		String response = clientService.getRestResponse(request);
		
		var typeToken = TypeToken.getParameterized(BaseResponseModel.class, StatusResultModel.class);
		var fromJson = gson.fromJson(response, typeToken.getType());
		
		@SuppressWarnings("unchecked")
		var baseResponse = (BaseResponseModel<StatusResultModel>) fromJson;
		
		if(baseResponse.getError() != null) {
			throw new ServiceException(ErrorCodes.UPDATE_FAIL, "Order update fail : " + baseResponse.getError());
		}
		
		return baseResponse.getResult().isStatus();
	}

	@Override
	public boolean deleteOrder(String orderId) throws ServiceException {
		ClientRequestModel request = ClientRequestModel.builder()
				.setUrl(serviceUrl + API_BASE + DELETE_ORDER + "/" + UUID.fromString(orderId))
				.setHttpMethod(HttpMethod.DELETE)
				.build();
		
		String response = clientService.getRestResponse(request);
		
		var typeToken = TypeToken.getParameterized(BaseResponseModel.class, StatusResultModel.class);
		var fromJson = gson.fromJson(response, typeToken.getType());
		
		@SuppressWarnings("unchecked")
		var baseResponse = (BaseResponseModel<StatusResultModel>) fromJson;
		
		if(baseResponse.getError() != null) {
			throw new ServiceException(ErrorCodes.DELETE_FAIL, "Order delete fail : " + baseResponse.getError());
		}
		
		return baseResponse.getResult().isStatus();
	}
	
	@Override
	public List<OrderModel> listAllOrders() throws ServiceException {
		ClientRequestModel request = ClientRequestModel.builder()
				.setUrl(serviceUrl + API_BASE + LIST_ALL_ORDER)
				.setHttpMethod(HttpMethod.GET)
				.build();
		
		String response = clientService.getRestResponse(request);
		
		var typeToken = TypeToken.getParameterized(BaseResponseModel.class, OrdersResultModel.class);
		var fromJson = gson.fromJson(response, typeToken.getType());
		
		@SuppressWarnings("unchecked")
		var baseResponse = (BaseResponseModel<OrdersResultModel>) fromJson;
		
		if(baseResponse.getError() != null) {
			throw new ServiceException(ErrorCodes.DATA_RETREIVE_FAIL, "Order list fail : " + baseResponse.getError());
		}
		
		List<OrderModel> orders = baseResponse.getResult().getOrders();
		List<String> customerIds = orders.stream().map(order -> order.getCustomerId().toString()).collect(Collectors.toList());
		
		List<CustomerModel> customerList = customerService.listCustomersByCustomerIds(customerIds);
		Map<UUID, CustomerModel> customerMap = customerList.stream()
			      .collect(Collectors.toMap(CustomerModel::getId, Function.identity()));
		
		for(OrderModel order : orders) {
			order.setCustomer(customerMap.get(order.getCustomerId()));
		}
		
		return orders;
	}

	@Override
	public OrderModel getOrder(String orderId) throws ServiceException {
		ClientRequestModel request = ClientRequestModel.builder()
				.setUrl(serviceUrl + API_BASE + GET_ORDER + "/" + UUID.fromString(orderId))
				.setHttpMethod(HttpMethod.GET)
				.build();
		
		String response = clientService.getRestResponse(request);
		
		var typeToken = TypeToken.getParameterized(BaseResponseModel.class, OrderModel.class);
		var fromJson = gson.fromJson(response, typeToken.getType());
		
		@SuppressWarnings("unchecked")
		var baseResponse = (BaseResponseModel<OrderModel>) fromJson;
		
		if(baseResponse.getError() != null) {
			throw new ServiceException(ErrorCodes.DATA_RETREIVE_FAIL, "Order get fail : " + baseResponse.getError());
		}
		
		OrderModel order = baseResponse.getResult();
		CustomerModel customer = customerService.getCustomer(order.getCustomerId().toString());
		order.setCustomer(customer);
		
		return order;
	}
}
