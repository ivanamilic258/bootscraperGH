package com.bootscrape.bootscraper.repository;

import com.bootscrape.bootscraper.model.wizz.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserRepository extends CrudRepository<User, Long> {

User findUserByEmail(String email);

}
