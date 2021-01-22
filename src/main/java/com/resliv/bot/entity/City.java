package com.resliv.bot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "cities")
@Data
@NoArgsConstructor
@AllArgsConstructor

@NamedQuery(
        name = "City.findAllCitiesNames",
        query = "select c.name from City c"
)
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    String name;
    @ElementCollection
    List<String> descriptions;
}
