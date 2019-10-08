package com.bootscrape.bootscraper.engine;

import com.bootscrape.bootscraper.dto.request.DepArrDto;
import com.bootscrape.bootscraper.dto.request.FlightListDto;
import com.bootscrape.bootscraper.dto.request.TimetableRequestDto;
import com.bootscrape.bootscraper.dto.response.FlightDto;
import com.bootscrape.bootscraper.dto.response.TimetableResponseDto;
import com.bootscrape.bootscraper.exception.StringException;
import com.bootscrape.bootscraper.model.wizz.DeparturesArrivals;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.joda.time.Interval;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class HttpRequestEngine {

    private static final String timetableUrl = "https://be.wizzair.com/9.18.0/Api/search/timetable";
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String DISCOUNTED_PRICE = "wdc";

    public List<TimetableResponseDto> requestTimetableResults(List<DepArrDto> routes, Date from, Date to) {
        if (routes.isEmpty()) {
            throw new StringException("No routes requested!");
        }
        List<TimetableResponseDto> resultList = new ArrayList<>();
        List<Interval> dateIntervals = DateEngine.splitDateIntoMonths(from, to);
        for (DepArrDto route : routes) {


            TimetableRequestDto jsonRequest = new TimetableRequestDto();
            jsonRequest.setAdultCount(1);
            jsonRequest.setChildCount(0);
            jsonRequest.setInfantCount(0);
            jsonRequest.setPriceType(DISCOUNTED_PRICE);
            for (Interval interval : dateIntervals) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String dateFrom = df.format(interval.getStart().toDate());
                String dateTo = df.format(interval.getEnd().toDate());
                jsonRequest.getFlightList().clear();
                jsonRequest.getFlightList().add(new FlightListDto(route.getArrival(), route.getDeparture(), dateFrom, dateTo));
                jsonRequest.getFlightList().add(new FlightListDto(route.getDeparture(), route.getArrival(), dateFrom, dateTo));

                try {
                    HttpClient client = HttpClientBuilder.create().build();
                    HttpPost request = new HttpPost(timetableUrl);
                    request.setHeader("User-Agent", USER_AGENT);
                    Gson gson = new Gson();
                    StringEntity postingString = new StringEntity(gson.toJson(jsonRequest), ContentType.APPLICATION_JSON);
                    request.setEntity(postingString);
                    request.setHeader("Content-type", "application/json");
                    HttpResponse response = client.execute(request);

                    String jsonResponse = EntityUtils.toString(response.getEntity(), "UTF-8");

                    TimetableResponseDto responseDto = gson.fromJson(jsonResponse, TimetableResponseDto.class);
                    resultList.add(responseDto);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return resultList;
    }

}
