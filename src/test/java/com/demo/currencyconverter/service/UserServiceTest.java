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
import static org.mockito.ArgumentMatchers.anyString;
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
		StepVerifier.create(result).expectError().verify();
	}
	
	@Test
	@DisplayName("Should find user by ID")
	void shouldFindById() {
		var expected = createNewUser();
		when(userRepository.findById(any(String.class))).thenReturn(Mono.just(expected));
		var result = userServiceImpl.findById("123");
		StepVerifier.create(result).expectNext(expected).verifyComplete();
	}

	@Test
	@SneakyThrows
	@DisplayName("Do not save when the user exists")
	void shouldNotSaveWhenTheUserExists() {
		var user = createNewUser();
		when(userRepository.findByEmail(any(String.class))).thenReturn(Mono.just(user));
		Mockito.when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));
		final Mono<User> userMono = userServiceImpl.save(user);
		StepVerifier.create(userMono).expectError().verify();
	}

	@Test
	@SneakyThrows
	@DisplayName("Save with success when the user not exists")
	void shouldSaveUser() {
		var user = createNewUser();
		when(userRepository.findByEmail(any(String.class))).thenReturn(Mono.empty());
		Mockito.when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));
		Mono<User> userMono = userServiceImpl.save(user);
		StepVerifier.create(userMono).expectNext(user).verifyComplete();
	}

	@Test
	@SneakyThrows
	@DisplayName("Should find user by name")
	void shouldFindUserByName() {
		var user = createNewUser();
		Mockito.when(userRepository.findByNameIgnoreCase(anyString())).thenReturn(Mono.just(user));
		final Mono<User> userMono = userServiceImpl.findByName("test");
		StepVerifier.create(userMono).expectNext(user).verifyComplete();
	}
}
