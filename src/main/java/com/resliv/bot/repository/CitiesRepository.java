package com.resliv.bot.repository;

import com.resliv.bot.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CitiesRepository extends JpaRepository<City, Long> {
    List<String> findAllCitiesNames();

    boolean existsByName(String name);
    City findByName(String name);
    void deleteByName(String name);
}
