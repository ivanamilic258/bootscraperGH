package com.bootscrape.bootscraper.engine;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateEngine {

     static  List<Interval> splitDateIntoMonths(Date from, Date to) throws IllegalArgumentException {

        List<Interval> dates =new ArrayList<>();

        DateTime dFrom = new DateTime(from);
        DateTime dTo = new DateTime(to);

        if (dFrom.compareTo(dTo) >= 0) {

            throw new IllegalArgumentException("Please provide a to-date greater than the from-date");

        }

        while (dFrom.compareTo(dTo) < 0) {
//
//            DateTime[] dar = new DateTime[2];
//            dar[0] = dFrom;
//            dar[1] = dFrom
//                    .withDayOfMonth(dFrom.getMonthOfYear() == dTo.getMonthOfYear() && dFrom.getYear() == dTo.getYear()
//                            ? dTo.getDayOfMonth() : dFrom.dayOfMonth().getMaximumValue());

            dates.add(new Interval(dFrom, dFrom .withDayOfMonth(dFrom.getMonthOfYear() == dTo.getMonthOfYear() && dFrom.getYear() == dTo.getYear()
                    ? dTo.getDayOfMonth() : dFrom.dayOfMonth().getMaximumValue())));

            dFrom = dFrom.plusMonths(1).withDayOfMonth(1);

        }

        return dates;

    }
}
