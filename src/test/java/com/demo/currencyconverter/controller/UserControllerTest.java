package com.demo.currencyconverter.controller;

import com.demo.currencyconverter.api.model.ErrorResponse;
import com.demo.currencyconverter.api.model.UserResponse;
import com.demo.currencyconverter.exception.EntityExistsException;
import com.demo.currencyconverter.model.User;
import com.demo.currencyconverter.service.UserService;
import com.demo.currencyconverter.util.DataBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(UserController.class)
class UserControllerTest extends BaseControllerTest{

    @MockBean
    private UserService userService;

    @Test
    @SneakyThrows
    @DisplayName("Should return 201 when POST /users and the user not exists")
    void createUser() {
        UserResponse userResponse = getUserResponse();
        final User newUser = DataBuilder.createNewUser();
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(Mono.just(newUser));
        final WebTestClient.BodySpec<UserResponse, ?> value = webTestClient
                .post().uri(BASE_PATH+"/users")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange().expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);
    }

    private UserResponse getUserResponse() {
        UserResponse userResponse = new UserResponse();
        userResponse.setId("123");
        userResponse.setName("test");
        return userResponse;
    }

    @Test
    @DisplayName("Should return 422 when POST /users return one user ")
    void shouldReturnOneUserExists() throws Exception {

        final User newUser = DataBuilder.createNewUser();
        Mockito.when(userService.save(Mockito.any(User.class)))
                .thenThrow(new EntityExistsException(HttpStatus.UNPROCESSABLE_ENTITY.value(), "User exists"));

        final ErrorResponse responseBody = webTestClient
                .post().uri(BASE_PATH + "/users")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange().expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), responseBody.getCode());
        Assertions.assertEquals("User exists", responseBody.getMessage());
    }

    @Test
    @DisplayName("Should return 200 when GET /users/{userId} receives a valid id with param")
    void findUserById() throws Exception {
        UserResponse userResponse = getUserResponse();

        final User newUser = DataBuilder.createNewUser();
        Mockito.when(userService.findById(Mockito.any(String.class))).thenReturn(Mono.just(newUser));
        final WebTestClient.BodySpec<UserResponse, ?> value = webTestClient
                .get().uri(BASE_PATH+"/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);
    }

    @Test
    @DisplayName("Should return 400 when GET /users/{userId} and invalid id with param")
    void shouldNotFindUserById() throws Exception {

        final User newUser = DataBuilder.createNewUser();
        Mockito.when(userService.findById(Mockito.any(String.class))).thenReturn(Mono.just(newUser));

        final ErrorResponse responseBody = webTestClient
                .get().uri(BASE_PATH + "/users/a")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isBadRequest()
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();
    }


}