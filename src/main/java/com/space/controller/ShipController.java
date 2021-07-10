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

import java.util.List;
import java.util.Optional;

@Controller
public class ShipController {

    @Autowired
    private ShipDao shipDao;


    @RequestMapping(path = "/rest/ships", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Ship>> getAllShips(@RequestParam(value = "name", required = false) String name,
                                    @RequestParam(value = "planet", required = false) String planet,
                                    @RequestParam(value = "shipType", required = false) ShipType shipType,
                                    @RequestParam(value = "after", required = false) Long after,
                                    @RequestParam(value = "before", required = false) Long before,
                                    @RequestParam(value = "isUsed", required = false) Boolean isUsed,
                                    @RequestParam(value = "minSpeed", required = false) Double minSpeed,
                                    @RequestParam(value = "maxSpeed", required = false) Double maxSpeed,
                                    @RequestParam(value = "minCrewSize", required = false) Integer minCrewSize,
                                    @RequestParam(value = "maxCrewSize", required = false) Integer maxCrewSize,
                                    @RequestParam(value = "minRating", required = false) Double minRating,
                                    @RequestParam(value = "maxRating", required = false) Double maxRating,
                                    @RequestParam(value = "order", required = false) ShipOrder order,
                                    @RequestParam(value = "pageNumber", required = false) Optional<Integer> pageNumber,
                                    @RequestParam(value = "pageSize", required = false) Optional<Integer> pageSize) throws Exception {

        List<Ship> ships = null;

        if (name != null) {
            ships = shipDao.findAllByFirstName(name);

        } else {
            ships = shipDao.findAll();
        }

        if (ships.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<Ship> shipsOnPage = shipDao.findAllOnPage(ships, pageNumber, pageSize);
        return new ResponseEntity<>(shipsOnPage, HttpStatus.OK);
    }

}
