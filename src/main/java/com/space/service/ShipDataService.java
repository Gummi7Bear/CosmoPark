package com.space.service;

import com.space.model.DataParams;
import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

/**
 Работа с данными через Spring Data.
 */
@Component
public class ShipDataService implements ShipData {

    @Autowired
    ShipRepository shipRepository;

    public Optional<Ship> find(Integer id) {
        try {
            return shipRepository.findById((long) id);
        }
        catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public ResponseEntity<Ship> save(DataParams dataParams) {

        int shipYear = getYearFromDate(new Date(dataParams.getProdDate()));

        if (dataParams.getName().length() > 50 || dataParams.getPlanet().length() > 50 || dataParams.getName() == "" || dataParams.getPlanet() == "" ||
                dataParams.getProdDate() < 0 || dataParams.getSpeed() < 0.01 || dataParams.getSpeed() > 0.99 ||
                dataParams.getCrewSize() < 1 || dataParams.getCrewSize() > 9999 || shipYear < 2800 || shipYear > 3019) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Boolean isUsed = dataParams.getIsUsed() != null ? dataParams.getIsUsed() : false;
        Double index = getIndex(isUsed);
        Double rating = getRating(dataParams.getSpeed(), index, shipYear);

        Ship ship = new Ship(dataParams.getName(), dataParams.getPlanet(), dataParams.getShipType(), new Date(dataParams.getProdDate()), isUsed, dataParams.getSpeed(), dataParams.getCrewSize(), rating);
        shipRepository.save(ship);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    public void delete(Integer id) {
        shipRepository.deleteById((long) id);
    }

    private int getYearFromDate(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate.getYear();
    }

    private double getIndex(Boolean isUsed) {
        if (!isUsed)
           return 1;
        else
           return 0.5;
    }

    private double getRating (Double speed, Double index, int shipYear) {
        BigDecimal result = new BigDecimal((80 * speed * index) / (3019 - shipYear + 1));
        return result.setScale(2, RoundingMode.DOWN).doubleValue();
    }
}
