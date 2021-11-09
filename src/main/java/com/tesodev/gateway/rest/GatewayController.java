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
		String customerId = customerService.createCustomer(customer);
		long endTime = System.currentTimeMillis();
		
		response.setResult(new MessageResponseModel(true, "New Customer created with " + customerId));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@PutMapping("/updateCustomer")
    public RestResponse<MessageResponseModel> updateCustomer(@RequestBody CustomerModel customer) {
		RestResponse<MessageResponseModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		boolean result = customerService.updateCustomer(customer);
		long endTime = System.currentTimeMillis();
		
		response.setResult(new MessageResponseModel(result, "Customer update status " + result));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@DeleteMapping("/deleteCustomer")
    public RestResponse<MessageResponseModel> deleteCustomer(@PathVariable(value = "id", required = true) String customerId) {
		RestResponse<MessageResponseModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		boolean result = customerService.deleteCustomer(customerId);
		long endTime = System.currentTimeMillis();
		
		response.setResult(new MessageResponseModel(result, "Customer delete status " + result));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@GetMapping("/listCustomers")
    public RestResponse<CustomersResultModel> getAllCustomers() {
		RestResponse<CustomersResultModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		List<CustomerModel> customers = customerService.listAllCustomers();
		long endTime = System.currentTimeMillis();
		
		response.setResult(new CustomersResultModel(customers));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@GetMapping("/getCustomer/{id}")
    public RestResponse<CustomerModel> getCustomer(@PathVariable(value = "id", required = true) String customerId) {
		RestResponse<CustomerModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		CustomerModel customer = customerService.getCustomer(customerId);
		long endTime = System.currentTimeMillis();
		
		response.setResult(customer);
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@PostMapping("/createOrder")
    public RestResponse<MessageResponseModel> createOrder(@RequestBody OrderModel order) {
		RestResponse<MessageResponseModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		String orderId = orderService.createOrder(order);
		long endTime = System.currentTimeMillis();
		
		response.setResult(new MessageResponseModel(true, "New Order created with " + orderId));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@PutMapping("/updateOrder")
    public RestResponse<MessageResponseModel> updateOrder(@RequestBody OrderModel order) {
		RestResponse<MessageResponseModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		boolean result = orderService.updateOrder(order);
		long endTime = System.currentTimeMillis();
		
		response.setResult(new MessageResponseModel(result, "Order update status " + result));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@DeleteMapping("/deleteOrder")
    public RestResponse<MessageResponseModel> deleteOrder(@PathVariable(value = "id", required = true) String orderId) {
		RestResponse<MessageResponseModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		boolean result = orderService.deleteOrder(orderId);
		long endTime = System.currentTimeMillis();
		
		response.setResult(new MessageResponseModel(result, "Order delete status " + result));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@GetMapping("/listOrders")
    public RestResponse<OrdersResultModel> getAllOrders() {
		RestResponse<OrdersResultModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		List<OrderModel> orders = orderService.listAllOrders();
		long endTime = System.currentTimeMillis();
		
		response.setResult(new OrdersResultModel(orders));
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
	
	@GetMapping("/getOrder/{id}")
    public RestResponse<OrderModel> getOrder(@PathVariable(value = "id", required = true) String orderId) {
		RestResponse<OrderModel> response = new RestResponse<>();
		
		long startTime = System.currentTimeMillis();
		OrderModel order = orderService.getOrder(orderId);
		long endTime = System.currentTimeMillis();
		
		response.setResult(order);
		response.setResponseTime(endTime - startTime);
		
        return response;
    }
}
