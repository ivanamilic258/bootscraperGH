package com.bootscrape.bootscraper.controllers;

import com.bootscrape.bootscraper.dto.ImportResultsRequest;
import com.bootscrape.bootscraper.dto.UserDto;
import com.bootscrape.bootscraper.exception.StringException;
import com.bootscrape.bootscraper.repository.DeparturesArrivalsRepository;
import com.bootscrape.bootscraper.service.ResultsService;
import com.bootscrape.bootscraper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Arrays;
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

    //results for routes
    @RequestMapping(value = "importResults",method = RequestMethod.POST)
    @ResponseBody
    public void importResults(@RequestBody ImportResultsRequest request){
        try {
            resultsService.importSelectedDeparturesInDateRange( request.getRoutes(), request.getDateFrom(), request.getDateTo());
        } catch (ParseException e) {
            throw new StringException("Parsing exception");
        }
    }
    @RequestMapping(value = "getRoutes",method = RequestMethod.GET)
    @ResponseBody
    public List<DeparturesArrivalsRepository.DepArrCurrDto> getRoutes(){
      return resultsService.getNonDuplicatedRoutes();
    }

//only dates
    @RequestMapping(value = "importAllResults",method = RequestMethod.POST)
    @ResponseBody
    public void importAllResults(@RequestBody ImportResultsRequest request) {
        try {
            resultsService.importForDateRange( request.getDateFrom(), request.getDateTo());
        } catch (ParseException e) {
            throw new StringException("Parsing exception");
        }
    }

    //best value is in approx. 7 weeks
    @RequestMapping(value = "importResults7WeeksFromNow",method = RequestMethod.POST)
    @ResponseBody
    public void importResults7WeeksFromNow() {
        try {
            resultsService.importResults7WeeksFromNow();
        } catch (ParseException e) {
            throw new StringException("Parsing exception");
        }
    }


    //every possible combination between those destinations
   @RequestMapping(value = "importResultsForDestinationsOfInterest",method = RequestMethod.POST)
   @ResponseBody
   public void importResultsForDestinationsOfInterest(@RequestBody ImportResultsRequest request) {
       try {
           resultsService.importForDestinations(request.getDestinations(), request.getDateFrom(), request.getDateTo());
       } catch (ParseException e) {
           throw new StringException("Parsing exception");
       }
   }


   //every possible combination for user's desired destinations
   @RequestMapping(value = "importDestinationsForUser",method = RequestMethod.POST)
   @ResponseBody
   public void importDestinationsForUser(@RequestBody ImportResultsRequest request) {
       try {
           List<String> departures = Arrays.asList( "BEG", "BUD", "TSR" );
           resultsService.importDestinationsForUser(request.getUsername(), request.getDateFrom(), request.getDateTo(),departures);
       } catch (ParseException e) {
           throw new StringException("Parsing exception");
       }
   }

 //every possible combination for user's desired destinations
   @RequestMapping(value = "importDestinationsForUserFromBEG",method = RequestMethod.POST)
   @ResponseBody
   public void importDestinationsForUserFromBEG(@RequestBody ImportResultsRequest request) {
       try {
           List<String> departures = Arrays.asList( "BEG" );
           resultsService.importDestinationsForUser(request.getUsername(), request.getDateFrom(), request.getDateTo(), departures);
       } catch (ParseException e) {
           throw new StringException("Parsing exception");
       }
   }



 //every possible combination for user's desired destinations
   @RequestMapping(value = "importDestinationsForUserFromTSR",method = RequestMethod.POST)
   @ResponseBody
   public void importDestinationsForUserFromTSR(@RequestBody ImportResultsRequest request) {
       try {
           List<String> departures = Arrays.asList( "TSR" );
           resultsService.importDestinationsForUser(request.getUsername(), request.getDateFrom(), request.getDateTo(), departures);
       } catch (ParseException e) {
           throw new StringException("Parsing exception");
       }
   }


 //every possible combination for user's desired destinations
   @RequestMapping(value = "importDestinationsForUserFromBUD",method = RequestMethod.POST)
   @ResponseBody
   public void importDestinationsForUserFromBUD(@RequestBody ImportResultsRequest request) {
       try {
           List<String> departures = Arrays.asList( "BUD" );
           resultsService.importDestinationsForUser(request.getUsername(), request.getDateFrom(), request.getDateTo(), departures);
       } catch (ParseException e) {
           throw new StringException("Parsing exception");
       }
   }

    //every possible combination from beg
    @RequestMapping(value = "importAllFromBEG",method = RequestMethod.POST)
    @ResponseBody
    public void importAllFromBEG(@RequestBody ImportResultsRequest request) {
        try {
            resultsService.importAllFromAirportInDateRange(request.getDateFrom(), request.getDateTo(), "BEG");
        } catch (ParseException e) {
            throw new StringException("Parsing exception");
        }
    }

    @RequestMapping(value = "test",method = RequestMethod.POST)
    @ResponseBody
    public void test() {
       resultsService.test();
    }



}
