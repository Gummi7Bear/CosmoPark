package com.space.model;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ship")
public class Ship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** Название корабля (до 50 знаков включительно)
     */
    @Column(name = "name")
    private String name;

    /** Планета пребывания (до 50 знаков включительно
     */
    @Column(name = "planet")
    private String planet;

    /**  Тип корабля
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "shipType")
    private ShipType shipType;

    /**  Дата выпуска. Диапазон значений года 2800..3019
     */
    @Column(name = "prodDate")
    private Date prodDate;

    /**  Использованный / новый
     */
    @Column(name = "isUsed")
    private Boolean isUsed;

    /** Максимальная скорость корабля. Диапазон значений 0,01..0,99 включительно.
     */
    @Column(name = "speed")
    private Double speed;

    /**Количество членов экипажа. Диапазон значений 1..9999 включительно.
     */
    @Column(name = "crewSize")
    private Integer crewSize;

    /**Рейтинг корабля
     */
    @Column(name = "rating")
    private Double rating;

    public Ship() {
    }

    public Ship(Long id, String name, String planet, ShipType shipType, Date prodDate, Boolean isUsed, Double speed, Integer crewSize, Double rating) {
        this.id = id;
        this.name = name;
        this.planet = planet;
        this.shipType = shipType;
        this.prodDate = prodDate;
        this.isUsed = isUsed;
        this.speed = speed;
        this.crewSize = crewSize;
        this.rating = rating;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlanet(String planet) {
        this.planet = planet;
    }

    public void setShipType(ShipType shipType) {
        this.shipType = shipType;
    }

    public void setProdDate(Date prodDate) {
        this.prodDate = prodDate;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public void setCrewSize(Integer crewSize) {
        this.crewSize = crewSize;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPlanet() {
        return planet;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public Date getProdDate() {
        return prodDate;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public Double getSpeed() {
        return speed;
    }

    public Integer getCrewSize() {
        return crewSize;
    }

    public Double getRating() {
        return rating;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, planet, shipType, prodDate, isUsed, speed, crewSize, rating);
    }
}
