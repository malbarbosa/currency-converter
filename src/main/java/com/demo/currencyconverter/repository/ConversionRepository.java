package com.demo.currencyconverter.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.currencyconverter.model.Conversion;

@Repository
public interface ConversionRepository extends CrudRepository<Conversion, Long>{

}
