package com.demo.currencyconverter.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Document(collection = "conversion")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class Conversion {

	@Id
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
	private LocalDateTime dateTimeConversion;

	public static Conversion of(String id, String userId, String sourceCurrency, BigDecimal sourceValue, String targetCurrency){
		var conversion = new Conversion();
		conversion.id = id;
		conversion.userId = userId;
		conversion.sourceCurrency = sourceCurrency;
		conversion.sourceValue = sourceValue;
		conversion.targetCurrency = targetCurrency;

		return conversion;
	}
	public Conversion calculateTargetValue(BigDecimal rateValue){
		this.rateValue = rateValue;
		this.targetValue = this.sourceValue.multiply(rateValue).setScale(2,RoundingMode.HALF_EVEN);
		this.dateTimeConversion = LocalDateTime.now();
		return this;
	}

	public static Conversion getInstance(){
		return new Conversion();
	}

}
