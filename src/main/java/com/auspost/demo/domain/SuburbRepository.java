package com.auspost.demo.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SuburbRepository extends CrudRepository<Suburb, Integer> {
    List<Suburb> findByName(String name);
    List<Suburb> findByPostCode(Integer postCode);
}
