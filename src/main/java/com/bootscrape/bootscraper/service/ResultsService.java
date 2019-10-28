package com.bootscrape.bootscraper.service;

import com.bootscrape.bootscraper.dto.request.DepArrDto;
import com.bootscrape.bootscraper.dto.response.FlightDto;
import com.bootscrape.bootscraper.dto.response.TimetableResponseDto;
import com.bootscrape.bootscraper.engine.HttpRequestEngine;
import com.bootscrape.bootscraper.engine.PdfEngine;
import com.bootscrape.bootscraper.model.wizz.Airport;
import com.bootscrape.bootscraper.model.wizz.Currency;
import com.bootscrape.bootscraper.model.wizz.DeparturesArrivals;
import com.bootscrape.bootscraper.model.wizz.Result;
import com.bootscrape.bootscraper.model.wizz.User;
import com.bootscrape.bootscraper.repository.*;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.io.ByteArrayInputStream;
import java.text.DateFormat;
import java.text.MessageFormat;
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
	@Autowired
	MailService                  mailService;
	@Autowired
	CurrencyRepository           currencyRepository;

	@Autowired
	PdfEngine pdfEngine;

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
			if (departures != null && !departures.isEmpty()) {
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

	public void importDestinationsForUser(String username, Date dateFrom, Date dateTo, List<String> departures) throws ParseException {
		resultsRepository.deleteAll();

		List<Result> results = getResultsFromEndpoint( getRoutesForUserWithAddedDepartures( username, departures ), dateFrom, dateTo );

		resultsRepository.saveAll( results );
		sendPdfToUser(userRepository.findUserByUsername(username).getEmail());
	}

	public void importAllFromAirportInDateRange(Date dateFrom, Date dateTo, List<String> departures, String emailTo) throws ParseException {
		resultsRepository.deleteAll();
		List<Result> results = getResultsFromEndpoint( getAllRoutesFromAirports( departures ), dateFrom, dateTo );
		resultsRepository.saveAll( results );
		if(emailTo != null && !emailTo.equals( "" )) {
			sendPdfToUser( emailTo);
		}
	}


	private List<DepArrDto> getAllRoutesFromAirports(List<String> departuresList) {
		List<DeparturesArrivalsRepository.DepArrCurrDto> allRoutes = getNonDuplicatedRoutes();
		List<DepArrDto> resultList = new ArrayList<>();
		for (DeparturesArrivalsRepository.DepArrCurrDto route : allRoutes) {
			if (departuresList.contains( route.getArrival() ) || departuresList.contains( route.getDeparture() )) {
				resultList.add( new DepArrDto( route.getDeparture(), route.getArrival() ) );
			}
		}
		return resultList;
	}

	public void test() {
//		ByteArrayInputStream bis = new ByteArrayInputStream( pdfEngine.generatePdfFromList( Arrays.asList( "fds", "fasd", "dscf" ) ) );
//		try {
//			mailService.sendEmailWithAttachment( Arrays.asList( "ivanamilic258@gmail.com" ), "ivanaxyz123@gmail.com", "subj", "content", bis, "" );
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		}
	}

	public void sendPdfToUser(String mailTo) {
		DateFormat df = new SimpleDateFormat( "dd/MM/yyyy" );
		Iterable<Result> allResults = resultsRepository.findAll();
		List<String> rows = new ArrayList<>();
		Iterator<Result> iter = allResults.iterator();
		while (iter.hasNext()) {
			Result result = iter.next();
			Currency currency = currencyRepository.findByName( result.getCurrency() );
			rows.add( MessageFormat.format( "{0} - {1} ({2}): {3}", result.getDeparture(), result.getArrival(), df.format( result.getDatetime() ),
											result.getPrice() / currency.getEurConversionRate() ) );
		}

		ByteArrayInputStream bis = new ByteArrayInputStream( pdfEngine.generatePdfFromList( rows) );
		try {
			mailService.sendEmailWithAttachment( Arrays.asList( mailTo ), "ivanaxyz123@gmail.com", "Your Wizz search results", "Please find your search results attached.", bis, "results.pdf" );
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
