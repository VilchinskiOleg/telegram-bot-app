package com.resliv.bot.repository;

import com.resliv.bot.entity.City;
import com.resliv.bot.entity.Description;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CitiesRepository extends JpaRepository<City, Long> {
    List<String> findAllCitiesNames();
//    @Query(value = "select c.descriptions from City c where c.name = :name")
//    List<Description> findAllDescriptionsByCityName(String name);

    boolean existsByName(String name);
    City findByName(String name);
    void deleteByName(String name);
}
