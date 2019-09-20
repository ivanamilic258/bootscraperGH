package com.bootscrape.bootscraper.repository;

import com.bootscrape.bootscraper.model.wizz.Airport;
import com.bootscrape.bootscraper.model.wizz.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface AirportRepository extends CrudRepository<Airport, Long> {

	Airport findAirportByCode(String code);

}
