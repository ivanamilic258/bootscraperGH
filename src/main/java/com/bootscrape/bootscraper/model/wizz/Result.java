package com.bootscrape.bootscraper.model.wizz;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "wizz_result")
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String departure;
    private String arrival;
    private double price;
    private String currency;
    private Date datetime;


    public Result(String departure, String arrival, double price, String currency, Date datetime) {
        this.departure = departure;
        this.arrival = arrival;
        this.price = price;
        this.currency = currency;
        this.datetime = datetime;
    }
}
