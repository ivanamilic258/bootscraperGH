package com.bootscrape.bootscraper.service;

import com.bootscrape.bootscraper.dto.request.DepArrDto;
import com.bootscrape.bootscraper.dto.response.FlightDto;
import com.bootscrape.bootscraper.dto.response.TimetableResponseDto;
import com.bootscrape.bootscraper.engine.HttpRequestEngine;
import com.bootscrape.bootscraper.model.wizz.Result;
import com.bootscrape.bootscraper.repository.DeparturesArrivalsRepository;
import com.bootscrape.bootscraper.repository.ResultsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ResultsService {


    @Autowired
    DeparturesArrivalsRepository departuresArrivalsRepository;
    @Autowired
    ResultsRepository resultsRepository;
    @Autowired
    HttpRequestEngine httpRequestEngine;

    private static  final int year = 2019;
    public void importDeparturesUntilEndOfTheYear(List<DepArrDto> routes) throws ParseException {

        resultsRepository.deleteAll();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
       Date dateFrom = new Date();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH,9 );
        cal.set(Calendar.DAY_OF_MONTH, 31);

        Date dateTo = cal.getTime();


        List<TimetableResponseDto> response = httpRequestEngine.requestTimetableResults(routes,dateFrom, dateTo);
        List<Result> results = new ArrayList<>();
        if(!response.isEmpty()){
            for (TimetableResponseDto responseDto : response) {
                if(!responseDto.getOutboundFlights().isEmpty()){
                    for (FlightDto of : responseDto.getOutboundFlights()) {
                        results.add(new Result(of.getDepartureStation(), of.getArrivalStation(), of.getPrice().getAmount(), of.getPrice().getCurrencyCode(),
                                sdf.parse(of.getDepartureDate())));
                    }
                }
                if(!responseDto.getReturnFlights().isEmpty()){
                    for (FlightDto rf : responseDto.getReturnFlights()) {
                        results.add(new Result(rf.getDepartureStation(), rf.getArrivalStation(), rf.getPrice().getAmount(), rf.getPrice().getCurrencyCode(),
                                sdf.parse(rf.getDepartureDate())));
                    }
                }
            }
        }

        resultsRepository.saveAll(results);

    }
}
