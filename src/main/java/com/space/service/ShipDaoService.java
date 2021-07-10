package com.space.service;

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
    public List<Ship> findAllByName(String name) {
        List<Ship> result =  namedParameterJdbcTemplate.query(SQL_SELECT_BY_Name, new MapSqlParameterSource().addValue("name", "%" + name+ "%"), shipRowMapper);
        shipsMap.clear();
        return result;
    }

    @Override
    public List<Ship> findAllByPlanet(String planet) {
        List<Ship> result =  namedParameterJdbcTemplate.query(SQL_SELECT_BY_Planet, new MapSqlParameterSource().addValue("planet", "%" + planet+ "%"), shipRowMapper);
        shipsMap.clear();
        return result;
    }

    @Override
    public List<Ship> findAllByFilters(List<String> filters) {
        return null;
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
        List<Ship> result =  template.query(SQL_SELECT_ALL, shipRowMapper);
        shipsMap.clear();
        return result;
    }

    @Override
    public List<Ship> findAllOnPage(List<Ship> ships, Optional<Integer> pageNumber, Optional<Integer> pageSize) {
        int pNumber = pageNumber.orElse(0);
        int pSize = pageSize.orElse(3);

        List<Ship> result = new ArrayList<>();
        int skip = pNumber * pSize;

        for (int i = skip; i < Math.min(skip + pSize, ships.size()); i++) {
            result.add(ships.get(i));
        }

        return result;
    }

}
