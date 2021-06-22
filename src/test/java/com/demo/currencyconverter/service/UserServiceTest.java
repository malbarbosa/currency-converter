package com.demo.currencyconverter.service;

import com.demo.currencyconverter.exception.NotFoundException;
import com.demo.currencyconverter.model.User;
import com.demo.currencyconverter.repository.UserRepository;
import com.demo.currencyconverter.service.impl.UserServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import static com.demo.currencyconverter.util.DataBuilder.createNewUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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

		final Mono<User> userMono = userServiceImpl.save(user);
		Mockito.verify(userRepository,times(0)).save(any(User.class));
	}

	@Test
	@SneakyThrows
	@DisplayName("Save with success when the user not exists")
	void shouldSaveUser() {
		var user = createNewUser();
		when(userRepository.findById(any(String.class))).thenReturn(Mono.empty());
		Mono<User> userMono = userServiceImpl.save(user);
		Mockito.verify(userRepository,times(1)).save(any(User.class));
	}

}
