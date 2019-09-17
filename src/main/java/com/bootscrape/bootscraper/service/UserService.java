package com.bootscrape.bootscraper.service;

import com.bootscrape.bootscraper.dto.UserDto;
import com.bootscrape.bootscraper.exception.StringException;
import com.bootscrape.bootscraper.model.wizz.User;
import com.bootscrape.bootscraper.repository.UserRepository;
import com.bootscrape.bootscraper.service.assembler.UserAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

@Component
public class UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAssembler userAssembler;

    public UserDto findUserByEmail(String email) {
        if(email == null || email.equals("")){
            throw new StringException("Email cannot be blank!");
        }
        return userAssembler.assembleUserDtoFromEntity(userRepository.findUserByEmail(email));
    }

    public void addUser(UserDto userDto) {
        if(userDto.getEmail() == null || userDto.getEmail().equals("")){
            throw new StringException("Email address is mandatory!");
        }
        if(userRepository.findUserByEmail(userDto.getEmail()) != null){
            throw new StringException(MessageFormat.format("A user with email {0} already exists in our database!", userDto.getEmail()));
        }

        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setUsername(userDto.getUsername());
        userRepository.save(user);
    }
}
