package com.demo.currencyconverter.repository;

import com.demo.currencyconverter.model.Conversion;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversionRepository extends ReactiveCrudRepository<Conversion, Long>{

}
