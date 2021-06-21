package com.demo.currencyconverter.service;

import com.demo.currencyconverter.model.User;
import reactor.core.publisher.Mono;

public interface UserService {
	
	Mono<User> findById(final Long id);
	
	Mono<User> save(final User user);

}
