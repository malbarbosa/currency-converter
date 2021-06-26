package com.demo.currencyconverter.service;

import com.demo.currencyconverter.model.User;
import com.demo.currencyconverter.repository.UserRepository;
import com.demo.currencyconverter.service.impl.UserServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static com.demo.currencyconverter.util.DataBuilder.createNewUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = UserServiceImpl.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	@InjectMocks
	private UserServiceImpl userServiceImpl;

	@MockBean
	private UserRepository userRepository;

	@Test
	@DisplayName("Should not find user by ID")
	void shouldNotFindById() {
		when(userRepository.findById(any(String.class))).thenReturn(Mono.empty());
		var result = userServiceImpl.findById("123");
		assertEquals(Mono.empty(), result);
	}
	
	@Test
	@DisplayName("Should find user by ID")
	void shouldFindById() {
		var expected = createNewUser();
		when(userRepository.findById(any(String.class))).thenReturn(Mono.just(expected));
		var result = userServiceImpl.findById("123");
		var found = ((User)result.block());	
		assertEquals(expected, found);
	}

	@Test
	@SneakyThrows
	@DisplayName("Do not save when the user exists")
	void shouldNotSaveWhenTheUserExists() {
		var user = createNewUser();
		when(userRepository.findById(any(String.class))).thenReturn(Mono.just(user));
		Mockito.when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));
		final Mono<User> userMono = userServiceImpl.save(user);
		StepVerifier.create(userMono).expectError().verify();
	}

	@Test
	@SneakyThrows
	@DisplayName("Save with success when the user not exists")
	void shouldSaveUser() {
		var user = createNewUser();
		when(userRepository.findById(any(String.class))).thenReturn(Mono.empty());
		Mockito.when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));
		Mono<User> userMono = userServiceImpl.save(user);
		StepVerifier.create(userMono).expectNext(user).verifyComplete();
	}

}
