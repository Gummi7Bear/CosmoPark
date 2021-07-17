package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;

import java.util.Date;
import java.util.List;

public interface ShipDao extends CrudDao<Ship>{

    List<Ship> findAll(String name, String planet, Date after, Date before, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating);
    int getNumberByFilters(List<String> filters);
    List<Ship> findAllOnPage(List<Ship> ships,  Integer pageNumber, Integer pageSize);
    void getOrderShipList (List<Ship> list, ShipOrder order);
}
