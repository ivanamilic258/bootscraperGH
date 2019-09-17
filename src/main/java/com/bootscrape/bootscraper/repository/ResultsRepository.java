package com.bootscrape.bootscraper.repository;

import com.bootscrape.bootscraper.model.wizz.DeparturesArrivals;
import com.bootscrape.bootscraper.model.wizz.Result;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface ResultsRepository extends CrudRepository<Result, Long> {


}
