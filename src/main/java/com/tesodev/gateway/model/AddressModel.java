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
public class AddressModel implements Serializable {

	private static final long serialVersionUID = -5304352763631736509L;

	private UUID id;
	private String addressLine;
    private String city;
    private String country;
    private int cityCode;
    
}
