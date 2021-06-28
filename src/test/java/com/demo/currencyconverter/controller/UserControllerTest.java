package com.demo.currencyconverter.controller;

import com.demo.currencyconverter.controller.request.ConversionRequest;
import com.demo.currencyconverter.controller.request.UserRequest;
import com.demo.currencyconverter.controller.response.ErrorResponse;
import com.demo.currencyconverter.controller.response.UserResponse;
import com.demo.currencyconverter.exception.EntityExistsException;
import com.demo.currencyconverter.model.User;
import com.demo.currencyconverter.service.UserService;
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
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.demo.currencyconverter.util.DataBuilder.*;

@WebFluxTest(UserController.class)
class UserControllerTest extends BaseControllerTest{

    @MockBean
    private UserService userService;

    @Test
    @SneakyThrows
    @DisplayName("Should return 201 when POST /users and the user not exists")
    void createUser() {
        UserResponse userResponse = getUserResponse();
        final UserRequest newUser = createNewUserRequest();
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(Mono.just(createNewUser()));
        final WebTestClient.BodySpec<UserResponse, ?> value = webTestClient
                .post().uri(BASE_PATH+"/users")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange().expectStatus().isCreated()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);
    }

    @Test
    @SneakyThrows
    @DisplayName("Should return 400 when POST /users and body invalid")
    void shouldNotCreateUser() {
        Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(Mono.just(createNewUser()));
        final WebTestClient.BodySpec<UserResponse, ?> value = webTestClient
                .post().uri(BASE_PATH+"/users")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new ConversionRequest())
                .exchange().expectStatus().isBadRequest()
                .expectBody(UserResponse.class);
    }



    @Test
    @DisplayName("Should return 422 when POST /users return one user ")
    void shouldReturnOneUserExists() throws Exception {

        final UserRequest newUser = createNewUserRequest();
        Mockito.when(userService.save(Mockito.any(User.class)))
                .thenThrow(new EntityExistsException("user.exists"));

        final ErrorResponse responseBody = webTestClient
                .post().uri(BASE_PATH + "/users")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(newUser)
                .exchange().expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody(ErrorResponse.class)
                .returnResult().getResponseBody();

        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY.value(), responseBody.getCode());
        Assertions.assertEquals("User already exists.", responseBody.getMessage());
    }

    @Test
    @DisplayName("Should return 200 when GET /users/{userId} receives a valid id with param")
    void findUserById() throws Exception {
        UserResponse userResponse = getUserResponse();

        final User newUser = createNewUser();
        Mockito.when(userService.findById(Mockito.any(String.class))).thenReturn(Mono.just(newUser));
        final WebTestClient.BodySpec<UserResponse, ?> value = webTestClient
                .get().uri(BASE_PATH+"/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);
    }

    @Test
    @DisplayName("Should return 200 when GET /users receives a valid name with query parameter")
    void findUserByName() throws Exception {
        UserResponse userResponse = getUserResponse();

        final User newUser = createNewUser();
        Mockito.when(userService.findByName(Mockito.any(String.class))).thenReturn(Flux.just(newUser));
        final WebTestClient.BodySpec<UserResponse, ?> value = webTestClient
                .get().uri(UriComponentsBuilder.fromPath(BASE_PATH+"/users")
                        .queryParam("name","Test")
                        .build().toUri())
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);
    }

    @Test
    @DisplayName("Should return 400 when GET /users and name is null ")
    void shouldNotFindUserWhenNameIsNull() throws Exception {
        webTestClient
                .get().uri(UriComponentsBuilder.fromPath(BASE_PATH+"/users")
                        .build().toUri())
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isBadRequest();

    }
}