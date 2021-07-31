package com.space.service;

import java.util.List;

//Базовые операции под любую сущность.
public interface CrudDao<T> {
    void save(T model);
    void update(T model);
    void delete(Integer id);
    List<T> findAll();
}
