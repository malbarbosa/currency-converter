package com.demo.currencyconverter.service.impl;

import com.demo.currencyconverter.exception.EntityExistsException;
import com.demo.currencyconverter.exception.EntityNotFoundException;
import com.demo.currencyconverter.model.User;
import com.demo.currencyconverter.repository.UserRepository;
import com.demo.currencyconverter.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@Log4j2
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository repository;

	@Override
	public Mono<User> findById(String id) {
		log.info(String.format("Start method findById, id=%s",id));
		final Mono<User> userMono = repository.findById(id)
				.log(log.getName())
				.switchIfEmpty(Mono.error(new EntityNotFoundException("user.notfound")));
		log.info("Finish method findById");
		return userMono;
	}

	@Override
	public Mono<User> save(User user){
		log.info(String.format("Start method save, user=%s",user.toString()));
		final Mono<Object> newUser = repository.findByEmail(user.getEmail()).timeout(Duration.ofSeconds(10))
				.log(log.getName())
				.flatMap(userFound -> Mono.error(new EntityExistsException("user.exists")))
				.switchIfEmpty(repository.save(user))
				.log(log.getName());
		final Mono<User> userMono = newUser.cast(User.class);
		log.info("Finish method save");
		return userMono;
	}

	@Override
	public Mono<User> findByName(String name) {
		log.info(String.format("Start method findByName, name=%s",name));
		final Mono<User> userMono = repository.findByNameIgnoreCase(name).log(UserServiceImpl.log.getName());
		log.info("Finish method findByName");
		return userMono;
	}


}
