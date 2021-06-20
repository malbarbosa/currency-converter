package com.demo.currencyconverter.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document(collection = "conversion")
@Getter
@Setter
public class Conversion {

	@MongoId
	private Long id;
	private User user;
}
