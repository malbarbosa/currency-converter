package com.demo.currencyconverter.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.List;

@Document(collection = "user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@MongoId
	private String id;
	private String name;
	private List<Conversion> conversions;
	
	public User(final String id,final String name) {
		this.id = id;
		this.name = name;
		
	}
	
}
