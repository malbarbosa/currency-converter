package com.demo.currencyconverter.controller;

import com.demo.currencyconverter.api.model.UserResponse;
import com.demo.currencyconverter.model.User;
import com.demo.currencyconverter.service.UserService;
import com.demo.currencyconverter.util.DataBuilder;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(UserController.class)
class UserControllerTest extends BaseControllerTest{

    @MockBean
    private UserService userService;

    @Test
    @SneakyThrows
    void createUser() {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("test");
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

    @Test
    @DisplayName("Should return 200 when GET /users/{userId} receives a valid id with param")
    void findUserById() throws Exception {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setName("test");

        final User newUser = DataBuilder.createNewUser();
        Mockito.when(userService.findById(Mockito.any(Long.class))).thenReturn(Mono.just(newUser));
        final WebTestClient.BodySpec<UserResponse, ?> value = webTestClient
                .get().uri(BASE_PATH+"/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody(UserResponse.class)
                .isEqualTo(userResponse);
    }
}