package com.demo.currencyconverter.controller;

import com.demo.currencyconverter.controller.request.UserRequest;
import com.demo.currencyconverter.controller.response.UserResponse;
import com.demo.currencyconverter.model.User;
import com.demo.currencyconverter.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class UserController implements BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @PostMapping(value = "/users")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<UserResponse> createUser(@Valid UserRequest request) {
        User user = new User();
        mapper.map(request,user);
        final Mono<User> userMono = userService.save(user);
        return userMono.map(newUser -> mapper.map(newUser, UserResponse.class));
    }

    @GetMapping(value = "/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<UserResponse> findUserById(@PathVariable String userId) {
        final Mono<User> userMono = userService.findById(userId);
        return userMono.map(newUser -> mapper.map(newUser,UserResponse.class));
    }
}
