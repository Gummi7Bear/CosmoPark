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
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.util.*;

@Component
public class ShipDaoService implements ShipDao{

    //Класс JdbcTemplate выполняет SQL-запросы, выполняет итерации по ResultSet и извлекает вызываемые значения, обновляет инструкции и вызовы процедур, “ловит” исключения и транслирует их в исключения, определённые в пакете org.springframwork.dao
    private JdbcTemplate template;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final String SQL_SELECT_ALL = "SELECT * FROM ship";
    private final String SQL_SELECT_BY_ID = "SELECT * FROM ship WHERE id = ?";
    private final String SQL_SELECT_BY_Name = "SELECT * FROM ship WHERE name like :name";
    private final String SQL_SELECT_BY_Planet = "SELECT * FROM ship WHERE planet like :planet";

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
    public List<Ship> findAll(String name, String planet, Date after, Date before, Boolean isUsed, Double minSpeed, Double maxSpeed, Integer minCrewSize, Integer maxCrewSize, Double minRating, Double maxRating) {

        String sqlSelectByFilters = " SELECT * FROM ship where name like :name and planet like :planet " +
                                    " and prodDate between :after and :before " +
                                    " and speed between :minSpeed and :maxSpeed " +
                                    " and crewSize between :minCrewSize and :maxCrewSize " +
                                    " and rating between :minRating and :maxRating ";
        MapSqlParameterSource mapFilters = new MapSqlParameterSource();
        mapFilters.addValue("name", "%" + name+ "%");
        mapFilters.addValue("planet", "%" + planet+ "%");
        mapFilters.addValue("after", after);
        mapFilters.addValue("before", before);
        mapFilters.addValue("minSpeed", minSpeed);
        mapFilters.addValue("maxSpeed", maxSpeed);
        mapFilters.addValue("minCrewSize", minCrewSize);
        mapFilters.addValue("maxCrewSize", maxCrewSize);
        mapFilters.addValue("minRating", minRating);
        mapFilters.addValue("maxRating", maxRating);

        List<Ship> result =  namedParameterJdbcTemplate.query(sqlSelectByFilters, mapFilters, shipRowMapper);
        shipsMap.clear();
        return result;
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
    public int getNumberByFilters(List<String> filters) {

        return 0;
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
