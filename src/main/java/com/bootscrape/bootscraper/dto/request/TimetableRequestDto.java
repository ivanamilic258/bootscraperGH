package com.bootscrape.bootscraper.dto.request;

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
public class TimetableRequestDto {

//    {"flightList":[{"departureStation":"BEG","arrivalStation":"BVA","from":"2019-06-23","to":"2019-06-30"},{"departureStation":"BVA","arrivalStation":"BEG","from":"2019-07-01","to":"2019-08-04"}],"priceType":"wdc","adultCount":1,"childCount":0,"infantCount":0}


    private int adultCount;
    private int childCount;
    private int infantCount;
    private String priceType; //wdc = Wizz Discount Club
    private List<FlightListDto> flightList = new ArrayList<>();

}
