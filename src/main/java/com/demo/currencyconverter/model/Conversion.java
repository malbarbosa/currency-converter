package com.demo.currencyconverter.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Document(collection = "conversion")

@AllArgsConstructor
@NoArgsConstructor
public class Conversion {

	@MongoId
	@Getter
	@Setter
	private String id;
	@Getter
	@Setter
	private String userId;
	@Getter
	@Setter
	private String sourceCurrency;
	@Getter
	@Setter
	private Double sourceValue;
	@Getter
	@Setter
	private String targetCurrency;
	@Getter
	private Double targetValue;
	@Getter
	private Double rateValue;
	@Getter
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private OffsetDateTime dateTimeConversion;


	public static Conversion of(String id, String userId, String sourceCurrency, Double sourceValue, String targetCurrency){
		Conversion conversion = new Conversion();
		conversion.id = id;
		conversion.userId = userId;
		conversion.sourceCurrency = sourceCurrency;
		conversion.sourceValue = sourceValue;
		conversion.targetCurrency = targetCurrency;
		conversion.dateTimeConversion = OffsetDateTime.now();
		return conversion;
	}
	public Conversion calculateTargetValue(Double rateValue){
		this.rateValue = rateValue;
		this.targetValue = this.sourceValue/rateValue;
		return this;
	}

}
