package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface ShipDao extends CrudDao<Ship>{

    List<Ship> findAll(@RequestParam(value = "name", required = false) Optional<String> name,
                       @RequestParam(value = "planet", required = false) Optional<String> planet,
                       @RequestParam(value = "shipType", required = false) Optional<ShipType> shipType,
                       @RequestParam(value = "after", required = false) Optional<Long> after,
                       @RequestParam(value = "before", required = false) Optional<Long> before,
                       @RequestParam(value = "isUsed", required = false) Optional<Boolean> isUsed,
                       @RequestParam(value = "minSpeed", required = false) Optional<Double> minSpeed,
                       @RequestParam(value = "maxSpeed", required = false) Optional<Double> maxSpeed,
                       @RequestParam(value = "minCrewSize", required = false) Optional<Integer> minCrewSize,
                       @RequestParam(value = "maxCrewSize", required = false) Optional<Integer> maxCrewSize,
                       @RequestParam(value = "minRating", required = false) Optional<Double> minRating,
                       @RequestParam(value = "maxRating", required = false) Optional<Double> maxRating) throws Exception;

    List<Ship> findAllOnPage(List<Ship> ships,  Integer pageNumber, Integer pageSize);
    void getOrderShipList (List<Ship> list, ShipOrder order);
}
