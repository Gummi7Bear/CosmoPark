package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 Работа с данными через Spring Data.
 */
@Component
public class ShipDataService implements ShipData{

    @Autowired
    ShipRepository shipRepository;

    @Override
    public Optional<Ship> find(Integer id) {
        try {
            return shipRepository.findById((long) id);
        }
        catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    @Override
    public void save(Ship model) {

    }

    @Override
    public void update(Ship model) {

    }

    @Override
    public void delete(Integer id) {
             shipRepository.deleteById((long) id);
    }

    @Override
    public List<Ship> findAll() {
        return null;
    }
}
