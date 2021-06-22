package com.demo.currencyconverter.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class CurrencyRateDTO implements Serializable {

	private String success;
	private String base;
	private Map<String,Double> rates;
	
}
