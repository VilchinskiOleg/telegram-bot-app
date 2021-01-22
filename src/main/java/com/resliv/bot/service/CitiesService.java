package com.resliv.bot.service;

import com.resliv.bot.entity.City;
import java.util.List;

public interface CitiesService {
    void createCity(City city);
    void createCity(String name);
    void createCity(String name, String ... description);
    void updateCityByNameAddDescription(String name, String ... description);
    void updateCityByNameNewDescription(String name, String ... description);
    void deleteCityByName(String name);

    List<String> getDescriptionsByCityName(String name);
    List<String> getAllCitiesNames();
    boolean existCityByName(String name);
}
