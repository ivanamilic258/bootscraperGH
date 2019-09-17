package com.bootscrape.bootscraper.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class DepArrDto {
private String departure;
private String arrival;

    public DepArrDto() {
    }

    public DepArrDto(String departure, String arrival) {
        this.departure = departure;
        this.arrival = arrival;
    }
}
