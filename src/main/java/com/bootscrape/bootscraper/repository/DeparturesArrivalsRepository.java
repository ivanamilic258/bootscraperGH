package com.bootscrape.bootscraper.repository;

import com.bootscrape.bootscraper.dto.request.DepArrDto;
import com.bootscrape.bootscraper.model.wizz.DeparturesArrivals;
import com.bootscrape.bootscraper.model.wizz.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DeparturesArrivalsRepository extends CrudRepository<DeparturesArrivals, Long> {
//
//    @Query(value =  "select new com.bootscrape.bootscraper.dto.request.DepArrDto(ai.code as departure, air.code as arrival ) from wizz_departures_arrivals a \n" +
//            "join wizz_airport ai on ai.id=a.arrivalAirport_id\n" +
//            "join wizz_airport air on air.id =a.departureAirport_id", nativeQuery = true)
//    @Query(value =  "select  new com.bootscrape.bootscraper.dto.request.DepArrDto(ai.code, air.code) from DeparturesArrivals a \n" +
//            "join Airport ai on ai.id=a.arrivalAirport.id\n" +
//            "join Airport air on air.id =a.departureAirport.id", nativeQuery = false)
//    List<DepArrDto> findAllNonDuplicated();
//
//
//



    @Query(value =  "select  distinct \n"
            + "                      case \n"
            + "                       when da.departureAirport_id < da.arrivalAirport_id then a1.`code`\n"
            + "                       else a.`code`\n"
            + "                     end as departure,\n"
            + "                    case \n"
            + "                       when da.departureAirport_id > da.arrivalAirport_id then a1.`code`\n"
            + "                      else a.`code` \n"
            + "                     end as  arrival,\n"
            + "   case \n"
            + "                       when da.departureAirport_id < da.arrivalAirport_id then a1.currency_id\n"
            + "                       else a.currency_id\n"
            + "                     end as currencyId\n"
            + "      from wizz_departures_arrivals da\n"
            + "join wizz_airport a on a.id = da.arrivalAirport_id\n"
            + "join wizz_airport a1 on a1.id = da.departureAirport_id", nativeQuery = true)
    List<DepArrCurrDto> findAllNonDuplicated();


    @Query(value =  "select  distinct \n"
            + "                      case \n"
            + "                       when da.departureAirport_id < da.arrivalAirport_id then a1.`code`\n"
            + "                       else a.`code`\n"
            + "                     end as departure,\n"
            + "                    case \n"
            + "                       when da.departureAirport_id > da.arrivalAirport_id then a1.`code`\n"
            + "                      else a.`code` \n"
            + "                     end as  arrival,\n"
            + "   case \n"
            + "                       when da.departureAirport_id < da.arrivalAirport_id then a1.currency_id\n"
            + "                       else a.currency_id\n"
            + "                     end as currencyId\n"
            + "      from wizz_departures_arrivals da\n"
            + "join wizz_airport a on a.id = da.arrivalAirport_id\n"
            + "join wizz_airport a1 on a1.id = da.departureAirport_id\n"
            + "where da.departureAirport_id in :ids", nativeQuery = true)
    List<DepArrCurrDto> findNonDuplicatedForAirportIds(@Param("ids")List<Long> ids);

 @Query(value =  "select  distinct \n"
            + "                      case \n"
            + "                       when da.departureAirport_id < da.arrivalAirport_id then a1.`code`\n"
            + "                       else a.`code`\n"
            + "                     end as departure,\n"
            + "                    case \n"
            + "                       when da.departureAirport_id > da.arrivalAirport_id then a1.`code`\n"
            + "                      else a.`code` \n"
            + "                     end as  arrival,\n"
            + "   case \n"
            + "                       when da.departureAirport_id < da.arrivalAirport_id then a1.currency_id\n"
            + "                       else a.currency_id\n"
            + "                     end as currencyId\n"
            + "      from wizz_departures_arrivals da\n"
            + "join wizz_airport a on a.id = da.arrivalAirport_id\n"
            + "join wizz_airport a1 on a1.id = da.departureAirport_id\n"
            + "where da.departureAirport_id in :ids and da.arrivalAirport_id in :ids", nativeQuery = true)
    List<DepArrCurrDto> findNonDuplicatedCombinationsForAirportIds(@Param("ids")List<Long> ids);


    public static  interface DepArrCurrDto{
        String getDeparture();
        String getArrival();
        Long getCurrencyId();

    }
}
