package com.tesodev.gateway.service;

import java.util.List;

import com.tesodev.gateway.exception.ServiceException;
import com.tesodev.gateway.model.OrderModel;

public interface IOrderService {

	String createOrder(OrderModel order) throws ServiceException;
	
	boolean updateOrder(OrderModel order) throws ServiceException;
	
	boolean deleteOrder(String orderId) throws ServiceException;
	
	List<OrderModel> listAllOrders() throws ServiceException;
	
	OrderModel getOrder(String orderId) throws ServiceException;
}
