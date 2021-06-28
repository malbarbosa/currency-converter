package com.demo.currencyconverter.controller;

import com.demo.currencyconverter.controller.request.UserRequest;
import com.demo.currencyconverter.controller.response.ErrorResponse;
import com.demo.currencyconverter.controller.response.UserResponse;
import com.demo.currencyconverter.exception.EntityNotFoundException;
import com.demo.currencyconverter.model.User;
import com.demo.currencyconverter.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@Log4j2
public class UserController implements BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper mapper;

    @Operation(summary = "Create new user", operationId = "createUser",
            description = "Endpoint use to create a new user",
            tags = "user", responses = {
            @ApiResponse(responseCode = "201",
                    description = "successful operation",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "400",
                    description = "bad request",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "User not found.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "422",
                    description = "User already exists.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "Something did not go well, try later.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping(value = "/users", consumes = "application/json", produces = "application/json")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "successful operation")
    public Mono<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        log.info("Start method createUser");
        var user = new User();
        mapper.map(request,user);
        return userService.save(user)
                .map(newUser -> mapper.map(newUser, UserResponse.class))
                .log(UserController.log.getName());
    }
    @Operation(summary = "Find an user by Id", operationId = "findUserById",
            description = "Method use to find a user by Id",
            tags = "user", responses = {
            @ApiResponse(responseCode = "201",
                    description = "successful operation.",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "User not found.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "Something did not go well, try later.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping(value = "/users/{userId}", consumes = "application/json", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK, reason = "successful operation")
    public Mono<UserResponse> findUserById(@PathVariable String userId) {
        log.info("Start method findUserById");
        final Mono<User> userMono = userService.findById(userId);
        return userMono.
                map(newUser -> mapper.map(newUser, UserResponse.class))
                .switchIfEmpty(Mono.error(new EntityNotFoundException("user.notfound")))
                .log(UserController.log.getName());
    }


    @Operation(summary = "Find an user by name", operationId = "findUserByName",
            description = "Endpoint use to find a user by name",
            tags = "user", responses = {
            @ApiResponse(responseCode = "201",
                    description = "successful operation.",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404",
                    description = "User not found.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "Something did not go well, try later.",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping(value = "/users", consumes = "application/json", produces = "application/json")
    @ResponseStatus(value = HttpStatus.OK, reason = "successful operation")
    public Flux<UserResponse> findUserByName(@RequestParam(value = "name", required = true) String name) {
        log.info("Start method findUserByName");
        final Flux<User> responseFlux = userService.findByName(name);
        return responseFlux.map(newUser -> mapper.map(newUser, UserResponse.class)).
                switchIfEmpty(Flux.error(new EntityNotFoundException("user.notfound")))
                .log(UserController.log.getName());
    }
}
