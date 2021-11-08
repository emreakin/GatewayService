package com.tesodev.gateway.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tesodev.gateway.model.CustomerModel;
import com.tesodev.gateway.model.CustomersResultModel;
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
}
