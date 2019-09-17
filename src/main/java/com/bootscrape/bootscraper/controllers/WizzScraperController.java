package com.bootscrape.bootscraper.controllers;

import com.bootscrape.bootscraper.dto.ImportResultsRequest;
import com.bootscrape.bootscraper.dto.UserDto;
import com.bootscrape.bootscraper.dto.request.DepArrDto;
import com.bootscrape.bootscraper.exception.StringException;
import com.bootscrape.bootscraper.service.ResultsService;
import com.bootscrape.bootscraper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;


@Controller
@RequestMapping("/wizz")
public class WizzScraperController {

    @Autowired
    UserService userService;
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

    @RequestMapping(value = "addUser", method = RequestMethod.POST)
    @ResponseBody
    public void addUser(@RequestBody UserDto userDto){
        userService.addUser(userDto);
    }

    @RequestMapping(value = "importResults",method = RequestMethod.POST)
    @ResponseBody
    public void importResults(@RequestBody ImportResultsRequest request){
        try {
            resultsService.importDeparturesUntilEndOfTheYear( request.getRoutes());
        } catch (ParseException e) {
            throw new StringException("Parsing exception");
        }
    }

}
