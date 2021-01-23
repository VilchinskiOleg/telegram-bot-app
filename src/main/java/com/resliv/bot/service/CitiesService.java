package com.resliv.bot.service;

import com.resliv.bot.entity.City;
import java.util.List;

public interface CitiesService {
    void createCity(City city);
    void createCity(String name);
    void createCity(String name, String description);
    void updateCityByName(String rul, String name, String ... descriptions);
    void deleteCityByName(String name);

    List<String> getDescriptionsByCityName(String name);
    List<String> getAllCitiesNames();
    boolean existCityByName(String name);
}
