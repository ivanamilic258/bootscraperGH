package com.bootscrape.bootscraper.dto;

import com.bootscrape.bootscraper.dto.request.DepArrDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImportResultsRequest {

    private List<DepArrDto> routes;
}
