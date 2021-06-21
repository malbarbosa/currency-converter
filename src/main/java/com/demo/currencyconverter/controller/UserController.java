package com.demo.currencyconverter.controller;

import com.demo.currencyconverter.api.UserApi;
import com.demo.currencyconverter.api.model.UserRequest;
import com.demo.currencyconverter.api.model.UserResponse;
import com.demo.currencyconverter.model.User;
import com.demo.currencyconverter.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
public class UserController implements BaseController, UserApi {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @Override
    @PostMapping(value = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserResponse> createUser(@Valid UserRequest request) {
        User user = new User();
        mapper.map(request,user);
        final Mono<User> newUser = userService.save(user);
        user = newUser.block();
        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setName(user.getName());
        return created(URI.create("/users")).body(response);
    }

    @Override
    @GetMapping(value = "/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<UserResponse> findUserById(@PathVariable Long userId) {
        final Mono<User> userFlux = userService.findById(userId);
        final User user = userFlux.block();
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setName(user.getName());
        return ok(userResponse);
    }
}
