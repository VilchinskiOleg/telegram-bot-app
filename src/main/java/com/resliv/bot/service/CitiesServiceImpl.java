package com.resliv.bot.service;

import com.resliv.bot.entity.City;
import com.resliv.bot.repository.CitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
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
            throw new IllegalArgumentException("City name is null!");
        }
        City city = new City(0L, name, Collections.emptyList());
        citiesRepository.save(city);
    }

    @Override
    public void createCity(String name, String description) {
        if (name == null) {
            throw new IllegalArgumentException("City name is null!");
        } else if (description == null) {
            throw new IllegalArgumentException("City description is null!");
        }
        List<String> descriptions = new ArrayList<>();
        descriptions.add(description);
        City city = new City(0L, name, descriptions);
        citiesRepository.save(city);
    }

    @Override
    public void updateCityByNameNewDescription(String name, String... descriptions) {
        if (name == null) {
            throw new IllegalArgumentException("City name is null!");
        }
        if (!citiesRepository.existsByName(name)) {
            throw new RuntimeException("City with such name does not exist");
        }
        City city = citiesRepository.findByName(name);
        city.setDescriptions(Arrays.asList(descriptions.clone()));
        // Выдает ошибку
        citiesRepository.save(city);
    }

    @Override
    public void updateCityByNameAddDescription(String name, String... descriptions) {
        if (name == null) {
            throw new IllegalArgumentException("City name is null!");
        }
        if (!citiesRepository.existsByName(name)) {
            throw new RuntimeException("City with such name does not exist");
        }
        City city = citiesRepository.findByName(name);
        List<String> extraList = Arrays.asList(descriptions.clone());
        List<String> currentList;
        if ((currentList = city.getDescriptions()) == null) {
            city.setDescriptions(extraList);
        } else {
            currentList.addAll(extraList);
            city.setDescriptions(currentList);
        }
        citiesRepository.save(city);
    }

    @Override
    public void deleteCityByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("City name is null!");
        }
        if (!citiesRepository.existsByName(name)) {
            throw new RuntimeException("City with such name does not exist");
        }
        citiesRepository.deleteByName(name);
    }



    @Override
    public boolean existCityByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("City name is null!");
        }
        return citiesRepository.existsByName(name);
    }

    @Override
    public List<String> getDescriptionsByCityName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("City name is null!");
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
