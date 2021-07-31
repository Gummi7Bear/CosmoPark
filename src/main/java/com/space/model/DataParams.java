package com.space.model;

import java.util.Optional;

public class DataParams {
    String name;
    String planet;
    ShipType shipType;
    Long prodDate;
    Boolean isUsed;
    Double speed;
    Integer crewSize;

    public DataParams() {
    }

    public DataParams(String name, String planet, ShipType shipType, Long prodDate, Optional<Boolean> isUsed, Double speed, Integer crewSize) {
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed.get();
        this.speed = speed;
        this.crewSize = crewSize;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlanet() {
        return planet;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public Long getProdDate() {
        return prodDate;
    }

    public void setProdDate(Long prodDate) {
        this.prodDate = prodDate;
    }

    public Boolean getIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Optional<Boolean> isUsed) {
        this.isUsed = isUsed.get();
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public boolean isValid() {
        return this.name != null && this.planet != null && this.shipType != null && this.crewSize != null && this.prodDate != null && this.prodDate != null && this.speed != null;
    }
}
