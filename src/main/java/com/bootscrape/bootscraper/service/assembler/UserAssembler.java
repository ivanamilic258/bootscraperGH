package com.bootscrape.bootscraper.service.assembler;

import com.bootscrape.bootscraper.dto.UserDto;
import com.bootscrape.bootscraper.model.wizz.User;
import org.springframework.stereotype.Component;

@Component
public class UserAssembler {

public UserDto assembleUserDtoFromEntity(User user){
    UserDto dto = new UserDto();
    dto.setId(user.getId());
    dto.setEmail(user.getEmail());
    dto.setName(user.getName());
    dto.setUsername(user.getUsername());
    return dto;
}

}
