package com.bootscrape.bootscraper.service;

import com.bootscrape.bootscraper.dto.request.DepArrDto;
import com.bootscrape.bootscraper.dto.response.FlightDto;
import com.bootscrape.bootscraper.dto.response.TimetableResponseDto;
import com.bootscrape.bootscraper.engine.HttpRequestEngine;
import com.bootscrape.bootscraper.model.wizz.Airport;
import com.bootscrape.bootscraper.model.wizz.Result;
import com.bootscrape.bootscraper.model.wizz.User;
import com.bootscrape.bootscraper.repository.AirportRepository;
import com.bootscrape.bootscraper.repository.DeparturesArrivalsRepository;
import com.bootscrape.bootscraper.repository.ResultsRepository;
import com.bootscrape.bootscraper.repository.UserRepository;
import com.bootscrape.bootscraper.util.ConstantManager;
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
	ResultsRepository            resultsRepository;
	@Autowired
	HttpRequestEngine            httpRequestEngine;
	@Autowired
	AirportRepository            airportRepository;
	@Autowired
	UserRepository               userRepository;

	private static final int year = 2019;

	public void importSelectedDeparturesInDateRange(List<DepArrDto> routes, Date dateFrom, Date dateTo) throws ParseException {

		resultsRepository.deleteAll();

		List<Result> results = getResultsFromEndpoint( routes, dateFrom, dateTo );
		resultsRepository.saveAll( results );

	}

	private List<Result> getResultsFromEndpoint(List<DepArrDto> routes, Date dateFrom, Date dateTo) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
		List<TimetableResponseDto> response = httpRequestEngine.requestTimetableResults( routes, dateFrom, dateTo );
		List<Result> results = new ArrayList<>();
		if (!response.isEmpty()) {
			for (TimetableResponseDto responseDto : response) {
				if (!responseDto.getOutboundFlights().isEmpty()) {
					for (FlightDto of : responseDto.getOutboundFlights()) {
						results.add( new Result( of.getDepartureStation(), of.getArrivalStation(), of.getPrice().getAmount(), of.getPrice().getCurrencyCode(),
												 sdf.parse( of.getDepartureDate() ) ) );
					}
				}
				if (!responseDto.getReturnFlights().isEmpty()) {
					for (FlightDto rf : responseDto.getReturnFlights()) {
						results.add( new Result( rf.getDepartureStation(), rf.getArrivalStation(), rf.getPrice().getAmount(), rf.getPrice().getCurrencyCode(),
												 sdf.parse( rf.getDepartureDate() ) ) );
					}
				}
			}
		}
		return results;
	}

	public List<DeparturesArrivalsRepository.DepArrCurrDto> getNonDuplicatedRoutes() {
		return departuresArrivalsRepository.findAllNonDuplicated();
	}

	public void importForDateRange(Date dateFrom, Date dateTo) throws ParseException {
		resultsRepository.deleteAll();

		List<Result> results = getResultsFromEndpoint( getAllRoutes(), dateFrom, dateTo );

		resultsRepository.saveAll( results );
	}

	List<DepArrDto> getAllRoutes() {
		List<DeparturesArrivalsRepository.DepArrCurrDto> routesList = getNonDuplicatedRoutes();
		List<DepArrDto> routes = new ArrayList<>();
		routesList.forEach( r -> routes.add( new DepArrDto( r.getDeparture(), r.getArrival() ) ) );
		return routes;
	}

	List<DepArrDto> getRoutesForAirportIds(List<Long> ids) {
		List<DeparturesArrivalsRepository.DepArrCurrDto> routesList = departuresArrivalsRepository.findNonDuplicatedForAirportIds( ids );
		List<DepArrDto> routes = new ArrayList<>();
		routesList.forEach( r -> routes.add( new DepArrDto( r.getDeparture(), r.getArrival() ) ) );
		return routes;
	}

	List<DepArrDto> getRoutesForUserWithAddedDepartures(String username, List<String> departures) {
		User user = userRepository.findUserByUsername( username );
		Set<Airport> destinationsOfInterest = user.getAirports();

		List<DepArrDto> routes = new ArrayList<>();
		if (destinationsOfInterest != null && !destinationsOfInterest.isEmpty()) {
			List<Long> ids = new ArrayList<>();
			destinationsOfInterest.forEach( d -> ids.add( d.getId() ) );
			if(departures !=  null && !departures.isEmpty()){
				for (String departure : departures) {
					ids.add( airportRepository.findAirportByCode( departure ).getId() );
				}
			}
			List<DeparturesArrivalsRepository.DepArrCurrDto> routesList = departuresArrivalsRepository.findNonDuplicatedCombinationsForAirportIds( ids );
			routesList.forEach( r -> routes.add( new DepArrDto( r.getDeparture(), r.getArrival() ) ) );
		}
		return routes;
	}

	public void importResults7WeeksFromNow() throws ParseException {
		LocalDate startDate = LocalDate.now().plusDays( 35 );
		LocalDate endDate = LocalDate.now().plusDays( 65 );
		resultsRepository.deleteAll();

		List<Result> results = getResultsFromEndpoint( getAllRoutes(), startDate.toDate(), endDate.toDate() );

		resultsRepository.saveAll( results );
	}

	public void importForDestinations(List<String> destinations, Date dateFrom, Date dateTo) throws ParseException {
		List<Long> airportIds = new ArrayList<>();
		destinations.forEach( d -> airportIds.add( airportRepository.findAirportByCode( d ).getId() ) );

		resultsRepository.deleteAll();

		List<Result> results = getResultsFromEndpoint( getRoutesForAirportIds( airportIds ), dateFrom, dateTo );

		resultsRepository.saveAll( results );
	}

	public void importDestinationsForUser(String username, Date dateFrom, Date dateTo,List<String> departures) throws ParseException {
		resultsRepository.deleteAll();

		List<Result> results = getResultsFromEndpoint( getRoutesForUserWithAddedDepartures( username, departures ), dateFrom, dateTo );

		resultsRepository.saveAll( results );
	}

}
