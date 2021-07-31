package com.space.controller;

import com.space.model.DataParams;
import com.space.model.Ship;
import com.space.service.ShipData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.*;

@Controller
public class ShipDataController {

    @Autowired
    private ShipData shipData;

    @ResponseBody
    @RequestMapping(path = "/rest/ships/{id}", method = RequestMethod.GET)
    public ResponseEntity<Ship> getShip(@PathVariable("id") int id) {

        if (id <= 0)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        Optional<Ship> ship = shipData.find(id);

        if (ship.isPresent())
            return new ResponseEntity<>(ship.get(), HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @RequestMapping(path = "/rest/ships/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<HttpStatus> deleteShip(@PathVariable("id") int id) {

        ResponseEntity<HttpStatus> responseEntity;

        if (id <= 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            try {
                shipData.delete(id);
                responseEntity = new ResponseEntity<>(HttpStatus.OK);
            } catch (EmptyResultDataAccessException e) {
                responseEntity = new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
        return responseEntity;
    }

    @RequestMapping(path = "/rest/ships/", method = RequestMethod.POST)
    public ResponseEntity<Ship> creatShip(@RequestBody DataParams dataParams) throws ParseException {
        if (!dataParams.isValid())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        else {
           return shipData.save(dataParams);
        }
    }
}
