package com.bootscrape.bootscraper.controllers;

import com.bootscrape.bootscraper.dto.UserDto;
import com.bootscrape.bootscraper.service.ResultsService;
import com.bootscrape.bootscraper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UIController {


	@Autowired
	UserService    userService;
	@Autowired
	ResultsService resultsService;

	@RequestMapping(value = "isAlive", method = RequestMethod.GET)
	@ResponseBody
	public String isAlive(){
		return "ALIVE";
	}

	@RequestMapping(value = "getUserByEmail", method = RequestMethod.GET)
	@ResponseBody
	public UserDto getUserByEmail(@RequestParam("email") String email){
		return userService.findUserByEmail(email);
	}


}
