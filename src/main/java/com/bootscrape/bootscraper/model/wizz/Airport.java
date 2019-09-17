package com.bootscrape.bootscraper.model.wizz;

import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Data
@Table(name = "wizz_airport")
@Getter
@Setter
public class Airport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String code;

    private String alias;

    @NonNull
    @ManyToOne
    private Currency currency;

    private double longitude;
    private double latitude;

    private String countryCode;

    private String countryName;
    public Airport() {
    }
}
