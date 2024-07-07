package org.riviera.cache;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import jakarta.ws.rs.core.Response;
import org.infinispan.client.hotrod.exceptions.HotRodClientException;
import org.jboss.resteasy.reactive.RestQuery;
import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

@Path("/weather")
public class WeatherForecastResource {

    @Inject
    WeatherForecastService service;

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

//    @GET
//    public WeatherForecast getForecast(@RestQuery String city, @RestQuery long daysInFuture) {
//        long executionStart = System.currentTimeMillis();
//        LocalDate nowPlusDays = LocalDate.now().plusDays(daysInFuture);
//        LocalDate nowPlusDaysPlus1 = LocalDate.now().plusDays(daysInFuture + 1L);
//        LocalDate nowPlusDaysPlus2 = LocalDate.now().plusDays(daysInFuture + 2L);
//        List<String> dailyForecasts = Arrays.asList(
//                service.getDailyForecastDayMonth(nowPlusDays.toEpochDay(), city),
//                service.getDailyForecastDayMonth(nowPlusDaysPlus1.toEpochDay(), city),
//                service.getDailyForecastDayMonth(nowPlusDaysPlus2.toEpochDay(), city));
//        long executionEnd = System.currentTimeMillis();
//        return new WeatherForecast(dailyForecasts, executionEnd - executionStart);
//    }

    @GET
    public WeatherForecast getForecast(@RestQuery String city, @RestQuery long daysInFuture) {
        long executionStart = System.currentTimeMillis();
        LocalDate nowPlusDays = LocalDate.now().plusDays(daysInFuture);
        LocalDate nowPlusDaysPlus1 = LocalDate.now().plusDays(daysInFuture + 1L);
        LocalDate nowPlusDaysPlus2 = LocalDate.now().plusDays(daysInFuture + 2L);
        List<Weather> dailyForecasts = Arrays.asList(
                service.getDaily(nowPlusDays.toEpochDay(), city),
                service.getDaily(nowPlusDaysPlus1.toEpochDay(), city),
                service.getDaily(nowPlusDaysPlus2.toEpochDay(), city));
        long executionEnd = System.currentTimeMillis();
        return new WeatherForecast(dailyForecasts, executionEnd - executionStart);
    }

    @GET
    @Path("travel")
    public List<String> getTravelCity(@RestQuery String type) {
        Log.info(type);
        return service.searchCityByWeather(type);
    }

    public static class ExceptionMappers {
        @ServerExceptionMapper
        public RestResponse<String> mapException(HotRodClientException ex) {
            return RestResponse.status(Response.Status.BAD_REQUEST,
                    Json.createObjectBuilder().add("infinispan-error", ex.getMessage()).build().toString());
        }
    }
}
