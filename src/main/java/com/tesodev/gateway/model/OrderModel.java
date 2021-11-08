package com.tesodev.gateway.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class OrderModel implements Serializable {

	private static final long serialVersionUID = 8430452469267447141L;

	private UUID id;
    private UUID customerId;
    private int quantity;
    private double price;
    private String status;
    private AddressModel address;
    private ProductModel product;
}
