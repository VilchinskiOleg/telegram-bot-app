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



    /**
     * Method that creates a copy of the input object and saves it to the database.
     * @param city a new city object.
     * @throws IllegalArgumentException if the city is null.
     * @throws RuntimeException if such city already exist in database.
     * */
    @Override
    public synchronized void createCity(City city) {
        if (city == null) {
            throw new IllegalArgumentException("city is null");
        }
        if (citiesRepository.existsByName(city.getName())) {
            throw new RuntimeException("such city already created");
        }
        citiesRepository.save(city);
    }

    /**
     * Method that creates new city with out any description and saves it to the database.
     * @param name a name for new city object.
     * @throws IllegalArgumentException if the name is null.
     * @throws RuntimeException if such city already exist in database.
     * */
    @Override
    public synchronized void createCity(String name) {
        if (name == null) {
            throw new IllegalArgumentException("city name is null");
        }
        if (citiesRepository.existsByName(name)) {
            throw new RuntimeException("such city already created");
        }
        City city = new City(0L, name, Collections.emptyList());
        citiesRepository.save(city);
    }

    /**
     * Method that creates new city with some description and saves it to the database.
     * @param name a name for new city object.
     * @param description a description for new city object.
     * @throws IllegalArgumentException if the name or description is null.
     * @throws RuntimeException if such city already exist in database.
     * */
    @Override
    public synchronized void createCity(String name, String description) {
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

    /**
     * Method that finds and updates city by input name, and saves changes to the database.
     * @param rul a specific marker (property) for definition the details of the updating.
     * @param name a name for looking for a necessary city.
     * @param descriptions a array of descriptions which you wont to add in current city.
     * @throws IllegalArgumentException if the name or rul is null.
     * @throws RuntimeException if such city does not exist in database.
     * */
    @Override
    public synchronized void updateCityByName(String rul, String name, String... descriptions) {
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

    /**
     * Method that finds and deletes city from database by input name.
     * @param name a name for looking for a necessary city.
     * @throws IllegalArgumentException if the name is null.
     * @throws RuntimeException if such city does not exist in database.
     * */
    @Override
    public synchronized void deleteCityByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("city name is null!");
        }
        if (!citiesRepository.existsByName(name)) {
            throw new RuntimeException("city with such name does not exist");
        }
        citiesRepository.deleteByName(name);
    }



    /**
     * Method that checks if the required city is in the database or not, by the input name
     * @param name a name for looking for a necessary city.
     * @throws IllegalArgumentException if the name is null.
     * @return true if such city in the database, or false if not.
     * */
    @Override
    public boolean existCityByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("city name is null!");
        }
        return citiesRepository.existsByName(name);
    }

    /**
     * Method that finds and gets descriptions by cities name.
     * @param name a name for looking for a necessary city.
     * @throws IllegalArgumentException if the name is null.
     * @return list of descriptions values as Strings.
     * */
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

    /**
     * Method that gets list of names of all active cities.
     * @return list of names of all active cities as Strings.
     * */
    @Override
    public List<String> getAllCitiesNames() {
        return citiesRepository.findAllCitiesNames();
    }
}
