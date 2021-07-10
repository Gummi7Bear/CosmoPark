package com.space.service;

import java.util.List;
import java.util.Optional;

//Базовые операции под любую сущность.
public interface CrudDao<T> {
    Optional<T> find(Integer id);
    void save(T model);
    void update(T model);
    void delete(Integer id);
    List<T> findAll();

}
