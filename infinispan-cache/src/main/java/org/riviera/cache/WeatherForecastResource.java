package org.riviera.cache;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.jboss.resteasy.reactive.RestPath;
import org.jboss.resteasy.reactive.RestQuery;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Path("/weather")
public class WeatherForecastResource {

    @Inject
    WeatherForecastService service;

    @GET
    @Path("{city}")
    public WeatherForecast getForecastParis(@RestQuery long daysInFuture, @RestPath("city") String city) {
        long executionStart = System.currentTimeMillis();
        LocalDate nowPlusDays = LocalDate.now().plusDays(daysInFuture);
        LocalDate nowPlusDaysPlus1 = LocalDate.now().plusDays(daysInFuture + 1L);
        LocalDate nowPlusDaysPlus2 = LocalDate.now().plusDays(daysInFuture + 2L);
        List<Weather> dailyForecasts = Arrays.asList(
                service.getDailyForecastParis(nowPlusDays.toEpochDay(), city),
                service.getDailyForecastParis(nowPlusDaysPlus1.toEpochDay(), city),
                service.getDailyForecastParis(nowPlusDaysPlus2.toEpochDay(), city));
        long executionEnd = System.currentTimeMillis();
        return new WeatherForecast(dailyForecasts, executionEnd - executionStart);
    }
}
