package com.tesodev.gateway.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomersResultModel {

	private List<CustomerModel> customers;
}
