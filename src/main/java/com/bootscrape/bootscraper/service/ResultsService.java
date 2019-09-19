package com.bootscrape.bootscraper.service;

import com.bootscrape.bootscraper.dto.request.DepArrDto;
import com.bootscrape.bootscraper.dto.response.FlightDto;
import com.bootscrape.bootscraper.dto.response.TimetableResponseDto;
import com.bootscrape.bootscraper.engine.HttpRequestEngine;
import com.bootscrape.bootscraper.model.wizz.Result;
import com.bootscrape.bootscraper.repository.DeparturesArrivalsRepository;
import com.bootscrape.bootscraper.repository.ResultsRepository;
import org.joda.time.LocalDate;
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

    public void importSelectedDeparturesInDateRange(List<DepArrDto> routes, Date dateFrom, Date dateTo) throws ParseException {

        resultsRepository.deleteAll();

        List<Result> results = getResultsFromEndpoint( routes,dateFrom,dateTo );
        resultsRepository.saveAll(results);

    }


    private List<Result> getResultsFromEndpoint(List<DepArrDto> routes, Date dateFrom, Date dateTo) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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
        return results;
    }

    public  List<DeparturesArrivalsRepository.DepArrCurrDto> getNonDuplicatedRoutes(){
      return  departuresArrivalsRepository.findAllNonDuplicated();
    }

    public void importForDateRange(Date dateFrom, Date dateTo) throws ParseException {
        resultsRepository.deleteAll();


        List<Result> results = getResultsFromEndpoint( getAllRoutes(),dateFrom,dateTo );

        resultsRepository.saveAll(results);
    }

    List<DepArrDto> getAllRoutes(){
        List<DeparturesArrivalsRepository.DepArrCurrDto> routesList = getNonDuplicatedRoutes();
        List<DepArrDto> routes = new ArrayList<>(  );
        routesList.forEach( r -> routes.add( new DepArrDto( r.getDeparture(), r.getArrival() ) ) );
        return routes;
    }

    public void importResults7WeeksFromNow() throws ParseException {
        LocalDate startDate = LocalDate.now().plusDays( 35 );
        LocalDate endDate = LocalDate.now().plusDays( 65 );
        resultsRepository.deleteAll();


        List<Result> results = getResultsFromEndpoint( getAllRoutes(),startDate.toDate(),endDate.toDate());

        resultsRepository.saveAll(results);
    }
}
