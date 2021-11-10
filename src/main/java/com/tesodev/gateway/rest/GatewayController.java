package com.tesodev.gateway.rest;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tesodev.gateway.exception.ServiceException;
import com.tesodev.gateway.model.CustomerModel;
import com.tesodev.gateway.model.CustomersResultModel;
import com.tesodev.gateway.model.MessageResponseModel;
import com.tesodev.gateway.model.OrderModel;
import com.tesodev.gateway.model.OrdersResultModel;
import com.tesodev.gateway.model.RestResponse;
import com.tesodev.gateway.service.ICustomerService;
import com.tesodev.gateway.service.IOrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class GatewayController {

	private final ICustomerService customerService;
	private final IOrderService orderService;
	
	@PostMapping("/createCustomer")
    public RestResponse<MessageResponseModel> createCustomer(@RequestBody CustomerModel customer) {
		RestResponse<MessageResponseModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		long endTime;
		String customerId;
		try {
			customerId = customerService.createCustomer(customer);
			endTime = System.currentTimeMillis();
		} catch (ServiceException e) {
			response.setError(e.getErrorMessage());
			return response;
		}
		
		response.setResult(new MessageResponseModel(true, "New Customer created with " + customerId));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@PutMapping("/updateCustomer")
    public RestResponse<MessageResponseModel> updateCustomer(@RequestBody CustomerModel customer) {
		RestResponse<MessageResponseModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		boolean result;
		long endTime;
		try {
			result = customerService.updateCustomer(customer);
			endTime = System.currentTimeMillis();
		} catch (ServiceException e) {
			response.setError(e.getErrorMessage());
			return response;
		}
		
		response.setResult(new MessageResponseModel(result, "Customer update status " + result));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@DeleteMapping("/deleteCustomer/{id}")
    public RestResponse<MessageResponseModel> deleteCustomer(@PathVariable(value = "id", required = true) String customerId) {
		RestResponse<MessageResponseModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		boolean result;
		long endTime;
		try {
			result = customerService.deleteCustomer(customerId);
			endTime = System.currentTimeMillis();
		} catch (ServiceException e) {
			response.setError(e.getErrorMessage());
			return response;
		}
		
		response.setResult(new MessageResponseModel(result, "Customer delete status " + result));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@GetMapping("/listCustomers")
    public RestResponse<CustomersResultModel> getAllCustomers() {
		RestResponse<CustomersResultModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		List<CustomerModel> customers;
		long endTime;
		try {
			customers = customerService.listAllCustomers();
			endTime = System.currentTimeMillis();
		} catch (ServiceException e) {
			response.setError(e.getErrorMessage());
			return response;
		}
		
		response.setResult(new CustomersResultModel(customers));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@GetMapping("/getCustomer/{id}")
    public RestResponse<CustomerModel> getCustomer(@PathVariable(value = "id", required = true) String customerId) {
		RestResponse<CustomerModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		CustomerModel customer;
		long endTime;
		try {
			customer = customerService.getCustomer(customerId);
			endTime = System.currentTimeMillis();
		} catch (ServiceException e) {
			response.setError(e.getErrorMessage());
			return response;
		}
		
		response.setResult(customer);
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@PostMapping("/createOrder")
    public RestResponse<MessageResponseModel> createOrder(@RequestBody OrderModel order) {
		RestResponse<MessageResponseModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		String orderId;
		long endTime;
		try {
			orderId = orderService.createOrder(order);
			endTime = System.currentTimeMillis();
		} catch (ServiceException e) {
			response.setError(e.getErrorMessage());
			return response;
		}
		
		response.setResult(new MessageResponseModel(true, "New Order created with " + orderId));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@PutMapping("/updateOrder")
    public RestResponse<MessageResponseModel> updateOrder(@RequestBody OrderModel order) {
		RestResponse<MessageResponseModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		boolean result;
		long endTime;
		try {
			result = orderService.updateOrder(order);
			endTime = System.currentTimeMillis();
		} catch (ServiceException e) {
			response.setError(e.getErrorMessage());
			return response;
		}
		
		response.setResult(new MessageResponseModel(result, "Order update status " + result));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@DeleteMapping("/deleteOrder/{id}")
    public RestResponse<MessageResponseModel> deleteOrder(@PathVariable(value = "id", required = true) String orderId) {
		RestResponse<MessageResponseModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		boolean result;
		long endTime;
		try {
			result = orderService.deleteOrder(orderId);
			endTime = System.currentTimeMillis();
		} catch (ServiceException e) {
			response.setError(e.getErrorMessage());
			return response;
		}
		
		response.setResult(new MessageResponseModel(result, "Order delete status " + result));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@GetMapping("/listOrders")
    public RestResponse<OrdersResultModel> getAllOrders() {
		RestResponse<OrdersResultModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		List<OrderModel> orders;
		long endTime;
		try {
			orders = orderService.listAllOrders();
			endTime = System.currentTimeMillis();
		} catch (ServiceException e) {
			response.setError(e.getErrorMessage());
			return response;
		}
		
		response.setResult(new OrdersResultModel(orders));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@GetMapping("/getOrder/{id}")
    public RestResponse<OrderModel> getOrder(@PathVariable(value = "id", required = true) String orderId) {
		RestResponse<OrderModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		OrderModel order;
		long endTime;
		try {
			order = orderService.getOrder(orderId);
			endTime = System.currentTimeMillis();
		} catch (ServiceException e) {
			response.setError(e.getErrorMessage());
			return response;
		}
		
		response.setResult(order);
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
}
