package com.resliv.bot.service;

import com.resliv.bot.entity.City;
import com.resliv.bot.entity.Description;
import com.resliv.bot.repository.CitiesRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class CitiesServiceImpl implements CitiesService {

    private CitiesRepository citiesRepository;
    private final static Map<String, BinaryOperator<List<Description>>> UPDATE_RULES = new HashMap<>();

    static {
        UPDATE_RULES.put("-new", (cur, ext) -> {
            cur.clear();
            cur.addAll(ext);
            return cur;
        });
        UPDATE_RULES.put("-add", (cur, ext) -> {
            cur.addAll(ext);
            return cur;
        });
    }

    public CitiesServiceImpl(CitiesRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
    }



    @Override
    public void createCity(City city) {
        if (city == null) {
            throw new IllegalArgumentException("city is null");
        }
        if (citiesRepository.existsByName(city.getName())) {
            throw new RuntimeException("such city already created");
        }
        citiesRepository.save(city);
    }

    @Override
    public void createCity(String name) {
        if (name == null) {
            throw new IllegalArgumentException("city name is null");
        }
        if (citiesRepository.existsByName(name)) {
            throw new RuntimeException("such city already created");
        }
        City city = new City(0L, name, Collections.emptyList());
        citiesRepository.save(city);
    }

    @Override
    public void createCity(String name, String description) {
        if (name == null) {
            throw new IllegalArgumentException("city name is null");
        } else if (description == null) {
            throw new IllegalArgumentException("city description is null!");
        }
        if (citiesRepository.existsByName(name)) {
            throw new RuntimeException("such city already created");
        }
        List<Description> descriptions = new ArrayList<>();
        descriptions.add(new Description(0L, description));
        City city = new City(0L, name, descriptions);
        citiesRepository.save(city);
    }

    @Override
    public void updateCityByName(String rul, String name, String... descriptions) {
        if (name == null) {
            throw new IllegalArgumentException("city name is null");
        } else if (rul == null) {
            throw new IllegalArgumentException("rul is null");
        }
        if (!citiesRepository.existsByName(name)) {
            throw new RuntimeException("city with such name does not exist");
        }
        City city = citiesRepository.findByName(name);
        List<Description> extraList = Stream.of(descriptions)
                .map(str -> new Description(0L, str))
                .collect(Collectors.toList());
        List<Description> currentList;
        if ((currentList = city.getDescriptions()) == null) {
            city.setDescriptions(extraList);
        } else {
            BinaryOperator<List<Description>> handler = UPDATE_RULES.get(rul);
            if (handler == null) throw new IllegalArgumentException("such rul does not exist");
            city.setDescriptions(handler.apply(currentList, extraList));
        }
        citiesRepository.save(city);
    }


    @Override
    public void deleteCityByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("city name is null!");
        }
        if (!citiesRepository.existsByName(name)) {
            throw new RuntimeException("city with such name does not exist");
        }
        citiesRepository.deleteByName(name);
    }



    @Override
    public boolean existCityByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("city name is null!");
        }
        return citiesRepository.existsByName(name);
    }

    @Override
    public List<String> getDescriptionsByCityName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("city name is null!");
        }
        City city = citiesRepository.findByName(name);
        if (city != null) {
            return city.getDescriptions()
                    .stream()
                    .map(Description::getText)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<String> getAllCitiesNames() {
        return citiesRepository.findAllCitiesNames();
    }
}
