package com.bootscrape.bootscraper.model.wizz;

import com.bootscrape.bootscraper.dto.request.DepArrDto;
import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Table(name = "wizz_departures_arrivals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeparturesArrivals {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Airport departureAirport;

    @ManyToOne
    private Airport arrivalAirport;

    @ManyToOne
    private Currency currency;







}
