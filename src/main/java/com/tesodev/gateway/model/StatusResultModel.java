package com.tesodev.gateway.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusResultModel implements Serializable {

	private static final long serialVersionUID = 8603145781531957377L;

	private boolean status;
}
