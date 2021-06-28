package com.demo.currencyconverter.repository;

import com.demo.currencyconverter.model.User;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, String>{

    @Query("{'name': {$regex:?0, $options: 'i'}}")
    Flux<User> findByNameIgnoreCase(String name);

    Mono<User> findByEmail(String email);
}
