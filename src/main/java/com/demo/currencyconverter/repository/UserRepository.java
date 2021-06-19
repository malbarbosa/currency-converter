package com.demo.currencyconverter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.currencyconverter.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

}
