package com.demo.currencyconverter.service;

import com.demo.currencyconverter.exception.NotFoundException;
import com.demo.currencyconverter.model.User;
import com.demo.currencyconverter.repository.UserRepository;
import com.demo.currencyconverter.service.impl.UserServiceImpl;
import com.demo.currencyconverter.util.DataBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import static com.demo.currencyconverter.util.DataBuilder.*;
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
	void shouldNotFindById() {
		when(userRepository.findById(any(Long.class))).thenReturn(Mono.empty());
		var result = userServiceImpl.findById(1L);
		assertEquals(Mono.empty(), result);
	}
	
	@Test
	void shouldFindById() {
		var expected = createNewUser();
		when(userRepository.findById(any(Long.class))).thenReturn(Mono.just(expected));
		var result = userServiceImpl.findById(1L);
		var found = ((User)result.block());	
		assertEquals(expected, found);
	}

	@Test
	void shouldNotSaveUserIfUserNotFound() {
		var user = createNewUser();
		when(userRepository.findById(any(Long.class))).thenReturn(Mono.empty());
		NotFoundException notFoundException = Assertions.assertThrows(NotFoundException.class, () -> {
			userServiceImpl.save(user);
		});
		assertEquals(1,notFoundException.getCode());
	}

	@Test
	@SneakyThrows
	void shouldSaveUser() {
		var user = createNewUser();
		when(userRepository.findById(any(Long.class))).thenReturn(Mono.just(user));
		Mono<User> userMono = userServiceImpl.save(user);
		Mockito.verify(userRepository,times(1)).save(any(User.class));
	}

}
