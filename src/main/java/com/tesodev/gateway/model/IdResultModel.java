package com.tesodev.gateway.model;

import java.io.Serializable;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class IdResultModel implements Serializable {

	private static final long serialVersionUID = -7554887740325346994L;
	
	private UUID id;
}
