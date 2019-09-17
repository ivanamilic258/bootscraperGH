package com.bootscrape.bootscraper.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FlightDto {

    private String arrivalStation;
    private String departureStation;
    private String classOfService;
    private String priceType;
    private String hasMacFlight;
    private String departureDate;
    private List<String> departureDates;
    private PriceDto price;
}
