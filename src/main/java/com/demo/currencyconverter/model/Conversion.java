package com.demo.currencyconverter.model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Document(collection = "conversion")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
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
	private BigDecimal sourceValue;
	@Getter
	@Setter
	private String targetCurrency;
	@Getter
	private BigDecimal targetValue;
	@Getter
	private BigDecimal rateValue;
	@Getter
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private OffsetDateTime dateTimeConversion;

	public static Conversion of(String id, String userId, String sourceCurrency, BigDecimal sourceValue, String targetCurrency){
		var conversion = new Conversion();
		conversion.id = id;
		conversion.userId = userId;
		conversion.sourceCurrency = sourceCurrency;
		conversion.sourceValue = sourceValue;
		conversion.targetCurrency = targetCurrency;
		conversion.dateTimeConversion = OffsetDateTime.now();
		return conversion;
	}
	public Conversion calculateTargetValue(BigDecimal rateValue){
		this.rateValue = rateValue;
		this.targetValue = this.sourceValue.divide(rateValue);
		return this;
	}

	public static Conversion getInstance(){
		return new Conversion();
	}

}
