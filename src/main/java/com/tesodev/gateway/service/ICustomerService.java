package com.tesodev.gateway.service;

import java.util.List;

import com.tesodev.gateway.model.CustomerModel;

public interface ICustomerService {

	List<CustomerModel> listAllCustomers();
	
	CustomerModel getCustomer(String customerId);
}
