package com.demo.currencyconverter.service.impl;

import com.demo.currencyconverter.exception.EntityExistsException;
import com.demo.currencyconverter.model.User;
import com.demo.currencyconverter.repository.UserRepository;
import com.demo.currencyconverter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository repository;

	@Override
	public Mono<User> findById(String id) {
		Mono<User> user = repository.findById(id);
		return user;
	}

	@Override
	public Mono<User> save(User user){
		final Mono<Object> newUser = findById(user.getId()).timeout(Duration.ofSeconds(10))
				.flatMap((userFound) -> Mono.error(new EntityExistsException(2, "User exists")))
				.switchIfEmpty(repository.save(user));

		return newUser.cast(User.class);
	}
	

}
