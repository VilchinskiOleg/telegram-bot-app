package com.resliv.bot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.ArrayList;
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
    @ElementCollection(fetch = FetchType.EAGER)
    @OrderColumn(name = "index", nullable = false)
    @Column(nullable = false)
    List<String> descriptions;

    public List<String> getDescriptions() {
        return descriptions == null ? null : new ArrayList<>(descriptions);
    }
}
