package com.tesodev.gateway.service;

import java.util.List;

import com.tesodev.gateway.model.CustomerModel;

public interface ICustomerService {

	String createCustomer(CustomerModel customer);
	
	boolean updateCustomer(CustomerModel customer);
	
	boolean deleteCustomer(String customerId);
	
	List<CustomerModel> listAllCustomers();
	
	List<CustomerModel> listCustomersByCustomerIds(List<String> customerIds);
	
	CustomerModel getCustomer(String customerId);
}
