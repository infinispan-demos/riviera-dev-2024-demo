package org.riviera.cache;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.jboss.resteasy.reactive.RestQuery;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Path("/weather")
public class WeatherForecastResource {

    @Inject
    WeatherForecastService service;

    // http localhost:8080/weather/paris
    @GET
    @Path("paris")
    public WeatherForecast getForecastParis(@RestQuery long daysInFuture) {
        long executionStart = System.currentTimeMillis();
        LocalDate nowPlusDays = LocalDate.now().plusDays(daysInFuture);
        LocalDate nowPlusDaysPlus1 = LocalDate.now().plusDays(daysInFuture + 1L);
        LocalDate nowPlusDaysPlus2 = LocalDate.now().plusDays(daysInFuture + 2L);
        List<String> dailyForecasts = Arrays.asList(
                service.getDailyForecastParis(nowPlusDays.toEpochDay()),
                service.getDailyForecastParis(nowPlusDaysPlus1.toEpochDay()),
                service.getDailyForecastParis(nowPlusDaysPlus2.toEpochDay()));
        long executionEnd = System.currentTimeMillis();
        return new WeatherForecast(dailyForecasts, executionEnd - executionStart);
    }

}
