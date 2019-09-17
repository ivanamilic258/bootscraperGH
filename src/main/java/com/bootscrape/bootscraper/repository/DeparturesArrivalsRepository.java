package com.bootscrape.bootscraper.repository;

import com.bootscrape.bootscraper.dto.request.DepArrDto;
import com.bootscrape.bootscraper.model.wizz.DeparturesArrivals;
import com.bootscrape.bootscraper.model.wizz.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DeparturesArrivalsRepository extends CrudRepository<DeparturesArrivals, Long> {
//
//    @Query(value =  "select new com.bootscrape.bootscraper.dto.request.DepArrDto(ai.code as departure, air.code as arrival ) from wizz_departures_arrivals a \n" +
//            "join wizz_airport ai on ai.id=a.arrivalAirport_id\n" +
//            "join wizz_airport air on air.id =a.departureAirport_id", nativeQuery = true)
    @Query(value =  "select  new com.bootscrape.bootscraper.dto.request.DepArrDto(ai.code, air.code) from DeparturesArrivals a \n" +
            "join Airport ai on ai.id=a.arrivalAirport.id\n" +
            "join Airport air on air.id =a.departureAirport.id", nativeQuery = false)
    List<DepArrDto> findAllNonDuplicated();





//
//    @Query(value =  "select new com.bootscrape.bootscraper.dto.request.DepArrDto( distinct " +
//            "          case " +
//            "            when departureAirport_id < arrivalAirport_id then departureAirport_id " +
//            "            else arrivalAirport_id " +
//            "          end as departure," +
//            "          case " +
//            "            when departureAirport_id > arrivalAirport_id then departureAirport_id " +
//            "            else arrivalAirport_id " +
//            "          end as  arrival) " +
//            "from wizz_departures_arrivals", nativeQuery = true)
//    List<DepArrDto> findAllNonDuplicated();
}
