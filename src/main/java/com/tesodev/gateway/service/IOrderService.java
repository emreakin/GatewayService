package com.tesodev.gateway.service;

import java.util.List;

import com.tesodev.gateway.model.OrderModel;

public interface IOrderService {

	String createOrder(OrderModel order);
	
	boolean updateOrder(OrderModel order);
	
	boolean deleteOrder(String orderId);
	
	List<OrderModel> listAllOrders();
	
	OrderModel getOrder(String orderId);
}
