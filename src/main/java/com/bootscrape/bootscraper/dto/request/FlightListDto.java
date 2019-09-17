package com.bootscrape.bootscraper.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FlightListDto {

    private String arrivalStation;
    private String departureStation;
    private String from;
    private String to;
}
