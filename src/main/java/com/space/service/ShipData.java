package com.space.service;

import com.space.model.DataParams;
import com.space.model.Ship;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface ShipData {
    ResponseEntity<Ship> save(DataParams dataParams);
    ResponseEntity<Ship> update(Ship ship, DataParams dataParams);
    Optional<Ship> find(Integer id);
    void delete(Integer id);


}