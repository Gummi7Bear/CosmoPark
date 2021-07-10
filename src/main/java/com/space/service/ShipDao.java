package com.space.service;

import com.space.model.Ship;

import java.util.List;
import java.util.Optional;

public interface ShipDao extends CrudDao<Ship>{
    //получать отфильтрованный список кораблей в соответствии с переданными фильтрами
    List<Ship> findAllByFilters(List<String> filters);
    //получать количество кораблей, которые соответствуют фильтрам
    int getNumberByFilters(List<String> filters);
    List<Ship> findAllOnPage(List<Ship> ships, Optional<Integer> pageNumber, Optional<Integer> pageSize);
}
