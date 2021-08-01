package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class ShipController {

    @Autowired
    private ShipDao shipDao;

    @RequestMapping(path = "/rest/ships", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Ship>> getAllShips(@RequestParam(value = "name", required = false) Optional<String> name,
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
                                    @RequestParam(value = "maxRating", required = false) Optional<Double> maxRating,
                                    @RequestParam(value = "order", required = false) Optional<ShipOrder> order,
                                    @RequestParam(value = "pageNumber", required = false) Optional<Integer> pageNumber,
                                    @RequestParam(value = "pageSize", required = false) Optional<Integer> pageSize) throws Exception {

        List<Ship> ships = shipDao.findAll(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

        if (ships.size() == 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        if (order.isPresent())
            shipDao.getOrderShipList(ships, order.get());

        List<Ship> shipsOnPage = shipDao.findAllOnPage(ships, pageNumber.orElse(0), pageSize.orElse(3));
        return new ResponseEntity<>(shipsOnPage, HttpStatus.OK);
    }

    @RequestMapping(path = "/rest/ships/count", method = RequestMethod.GET)
    public ResponseEntity<Integer> getShipsCount (@RequestParam(value = "name", required = false) Optional<String> name,
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
                                                  @RequestParam(value = "maxRating", required = false) Optional<Double> maxRating) throws Exception {

        List<Ship> ships = shipDao.findAll(name, planet, shipType, after, before, isUsed, minSpeed, maxSpeed, minCrewSize, maxCrewSize, minRating, maxRating);

        if (ships.size() == 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(ships.size(), HttpStatus.OK);
    }
}
