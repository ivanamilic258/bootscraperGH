package com.bootscrape.bootscraper.model.wizz;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Data
@Table(name = "wizz_currency")
@Getter
@Setter
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String name;

    private double eurConversionRate;

    public Currency() {
    }
}
