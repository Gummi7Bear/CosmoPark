package com.space.service;

import com.space.controller.ShipOrder;
import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.*;

/**
В методе findAll используются JdbcTemplate и DAO для изучения темы. Вся остальная работа с данными через Spring Data.
 */
@Component
public class ShipDaoService implements ShipDao{

    //Класс JdbcTemplate выполняет SQL-запросы, выполняет итерации по ResultSet и извлекает вызываемые значения, обновляет инструкции и вызовы процедур, “ловит” исключения и транслирует их в исключения, определённые в пакете org.springframwork.dao
    private JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    //DataSource -  sql интерфейс, его объекты предоставляют нам connection.
    @Autowired //чтобы бин dataSource автоматически вставился
    public ShipDaoService(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    private Map<Long, Ship> shipsMap = new HashMap<>();

    //RowMapper - это специальный объект, который отображает строки ResultSet,
    //который пришел к нам из бд, в объекты джава.
    private RowMapper<Ship> shipRowMapper
            = (ResultSet resultSet, int i) -> {
        Long id = resultSet.getLong("id");

        if (!shipsMap.containsKey(id)) {
            String name = resultSet.getString("name");
            String planet = resultSet.getString("planet");
            ShipType shipType = ShipType.valueOf(resultSet.getString("shipType"));
            Long prodDate = resultSet.getDate("prodDate").getTime();
            Boolean isUsed = resultSet.getBoolean("isUsed");
            Double speed = resultSet.getDouble("speed");
            Integer crewSize = resultSet.getInt("crewSize");
            Double rating = resultSet.getDouble("rating");

            Ship ship = new Ship(id, name, planet, shipType, prodDate, isUsed, speed, crewSize, rating);
            shipsMap.put(id, ship);
        }
        return shipsMap.get(id);
    };

    @Override
    public List<Ship> findAll(@RequestParam(value = "name", required = false) Optional<String> name,
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
                              @RequestParam(value = "maxRating", required = false) Optional<Double> maxRating) throws Exception{

        String sqlSelectByFilters = " SELECT * FROM ship where name like :name and planet like :planet " +
                                    " and prodDate between :after and :before " +
                                    " and speed between :minSpeed and :maxSpeed " +
                                    " and crewSize between :minCrewSize and :maxCrewSize " +
                                    " and rating between :minRating and :maxRating ";

        Date dateAfter = new GregorianCalendar(2800, 0 , 1).getTime();
        Date dateBefore = new GregorianCalendar(3019, 11 , 31).getTime();
        Date beforeNew = before.isPresent() ? new Date(before.get() -3600001L) : new Date(dateBefore.getTime());

        MapSqlParameterSource mapFilters = new MapSqlParameterSource();
        mapFilters.addValue("name", "%" + name.orElse("") + "%");
        mapFilters.addValue("planet", "%" + planet.orElse("") + "%");
        mapFilters.addValue("after", new Date(after.orElse(dateAfter.getTime())));
        mapFilters.addValue("before", beforeNew);
        mapFilters.addValue("minSpeed", minSpeed.orElse(0.01));
        mapFilters.addValue("maxSpeed", maxSpeed.orElse(0.99));
        mapFilters.addValue("minCrewSize", minCrewSize.orElse(1));
        mapFilters.addValue("maxCrewSize", maxCrewSize.orElse(9999));
        mapFilters.addValue("minRating", minRating.orElse(0.0));
        mapFilters.addValue("maxRating", maxRating.orElse(80.0));

        List<Ship> ships =  namedParameterJdbcTemplate.query(sqlSelectByFilters, mapFilters, shipRowMapper);
        shipsMap.clear();

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
        return ships;
    }

    public void getOrderShipList (List<Ship> list, ShipOrder order) {
        if (order.equals(ShipOrder.ID)) {
            list.sort(new Comparator<Ship>() {
                @Override
                public int compare(Ship ship1, Ship ship2) {
                    if(ship1.getId() - ship2.getId() > 0) return 1;
                    if(ship1.getId() - ship2.getId() < 0) return -1;
                    else return 0;
                }
            });
        }
        else if (order.equals(ShipOrder.SPEED)) {
            list.sort(new Comparator<Ship>() {
                @Override
                public int compare(Ship ship1, Ship ship2) {
                    if(ship1.getSpeed() - ship2.getSpeed() > 0) return 1;
                    if(ship1.getSpeed() - ship2.getSpeed() < 0) return -1;
                    else return 0;
                }
            });
        }
        else if (order.equals(ShipOrder.DATE)) {
            list.sort(new Comparator<Ship>() {
                @Override
                public int compare(Ship ship1, Ship ship2) {
                    if(ship1.getProdDate() - ship2.getProdDate() > 0) return 1;
                    if(ship1.getProdDate() - ship2.getProdDate() < 0) return -1;
                    else return 0;
                }
            });
        }
        else if (order.equals(ShipOrder.RATING)){
            list.sort(new Comparator<Ship>() {
                @Override
                public int compare(Ship ship1, Ship ship2) {
                    if(ship1.getRating() - ship2.getRating() > 0) return 1;
                    if(ship1.getRating() - ship2.getRating() < 0) return -1;
                    else return 0;
                }
            });
        }
    }

    @Override
    public Optional<Ship> find(Integer id) {

        return Optional.empty();
    }

    @Override
    public void save(Ship model) {

    }

    @Override
    public void update(Ship model) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public List<Ship> findAll() {
        return null;
    }

    @Override
    public List<Ship> findAllOnPage(List<Ship> ships, Integer pageNumber, Integer pageSize) {
        List<Ship> result = new ArrayList<>();
        int skip = pageNumber * pageSize;

        for (int i = skip; i < Math.min(skip + pageSize, ships.size()); i++) {
            result.add(ships.get(i));
        }
        return result;
    }
}
