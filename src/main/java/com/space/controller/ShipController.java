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

        Date dateAfter = new GregorianCalendar(2800, 0 , 1).getTime();
        Date dateBefore = new GregorianCalendar(3019, 11 , 31).getTime();
        Date beforeNew = before.isPresent() ? new Date(before.get() -3600001L) : new Date(dateBefore.getTime());

        List<Ship> ships = shipDao.findAll(name.orElse(""), planet.orElse(""), new Date(after.orElse(dateAfter.getTime())), beforeNew,
                                           isUsed.orElse(false), minSpeed.orElse(0.01), maxSpeed.orElse(0.99), minCrewSize.orElse(1), maxCrewSize.orElse(9999),
                                           minRating.orElse(0.0), maxRating.orElse(80.0));

        if (ships.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(shipType.isPresent())
            for (int i = 0; i < ships.size(); i++) {
                if(!ships.get(i).getShipType().equals(shipType.get())) {
                    ships.remove(i);
                    i--;
                }
            }

        if(isUsed.isPresent())
            for (int i = 0; i < ships.size(); i++) {
                if(!ships.get(i).getUsed().equals(isUsed.get())) {
                    ships.remove(i);
                    i--;
                }
            }

        if (order.isPresent()) {
            shipDao.getOrderShipList(ships, order.get());
        }

        List<Ship> shipsOnPage = shipDao.findAllOnPage(ships, pageNumber.orElse(0), pageSize.orElse(3));
        return new ResponseEntity<>(shipsOnPage, HttpStatus.OK);
    }

}
