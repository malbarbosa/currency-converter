package com.demo.currencyconverter.service;

import com.demo.currencyconverter.model.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
	
	Mono<User> findById(final String id);
	
	Mono<User> save(final User user);

    Flux<User> findByName(String name);
}
