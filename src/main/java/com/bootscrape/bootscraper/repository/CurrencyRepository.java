package com.bootscrape.bootscraper.repository;

import com.bootscrape.bootscraper.model.wizz.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Component
public interface CurrencyRepository extends CrudRepository<Currency,Long> {

	Currency findByName(String name);
}
