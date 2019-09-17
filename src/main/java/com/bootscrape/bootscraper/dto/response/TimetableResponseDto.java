package com.bootscrape.bootscraper.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimetableResponseDto {

    private List<FlightDto> outboundFlights = new ArrayList<>();
    private List<FlightDto> returnFlights = new ArrayList<>();
}
