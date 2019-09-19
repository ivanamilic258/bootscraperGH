package com.bootscrape.bootscraper.dto.request;

import ch.qos.logback.core.joran.spi.NoAutoStart;
import lombok.*;

@Builder
@AllArgsConstructor
@NoAutoStart
@Getter
@Setter
public class DepArrDto {
private String departure;
private String arrival;

}
