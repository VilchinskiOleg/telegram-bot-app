package com.resliv.bot.service;

import com.resliv.bot.entity.City;
import com.resliv.bot.repository.CitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class CitiesServiceImpl implements CitiesService {

    @Autowired
    private CitiesRepository citiesRepository;

    @Override
    public void createCity(City city) {
        if (city == null) {
            throw new IllegalArgumentException("City is null!");
        }
        citiesRepository.save(city);
    }

    @Override
    public void createCity(String name) {
        if (name == null) {
            throw new IllegalArgumentException("City is null!");
        }
        City city = new City(0L, name, null);
        citiesRepository.save(city);
    }

    // Один арг. descr.
    @Override
    public void createCity(String name, String... description) {
        if (name == null) {
            throw new IllegalArgumentException("City is null!");
        }
        City city = new City(0L, name, Arrays.asList(description.clone()));
        citiesRepository.save(city);
    }

    @Override
    public void updateCityByNameNewDescription(String name, String... description) {
        if (name == null) {
            throw new IllegalArgumentException("City is null!");
        }
        City city = citiesRepository.findByName(name);
        city.setDescriptions(Arrays.asList(description.clone()));
        citiesRepository.save(city);
    }

    @Override
    public void updateCityByNameAddDescription(String name, String... description) {
        if (name == null) {
            throw new IllegalArgumentException("City is null!");
        }
        City city = citiesRepository.findByName(name);
        List<String> extraDescriptions = Arrays.asList(description.clone());
        city.getDescriptions().addAll(extraDescriptions);
        citiesRepository.save(city);
    }

    @Override
    public void deleteCityByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("City is null!");
        }
        citiesRepository.deleteByName(name);
    }



    @Override
    public boolean existCityByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("City is null!");
        }
        return citiesRepository.existsByName(name);
    }

    @Override
    public List<String> getDescriptionsByCityName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("City is null!");
        }
        City city = citiesRepository.findByName(name);
        if (city != null) {
            return city.getDescriptions();
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAllCitiesNames() {
        return citiesRepository.findAllCitiesNames();
    }
}
