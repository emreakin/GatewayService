package com.tesodev.gateway.service;

import java.util.List;

import com.tesodev.gateway.exception.ServiceException;
import com.tesodev.gateway.model.CustomerModel;

public interface ICustomerService {

	String createCustomer(CustomerModel customer) throws ServiceException;
	
	boolean updateCustomer(CustomerModel customer) throws ServiceException;
	
	boolean deleteCustomer(String customerId) throws ServiceException;
	
	List<CustomerModel> listAllCustomers() throws ServiceException;
	
	List<CustomerModel> listCustomersByCustomerIds(List<String> customerIds) throws ServiceException;
	
	CustomerModel getCustomer(String customerId) throws ServiceException;
}
