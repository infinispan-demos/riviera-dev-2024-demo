package org.riviera.cache;

import io.quarkus.cache.CacheResult;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;

@ApplicationScoped
public class WeatherForecastService {

    @CacheResult(cacheName = "mycache")
    public Weather getDailyForecastParis(long epoch, String city) {
        LocalDate localDate = LocalDate.ofEpochDay(epoch);
        return new Weather(getDailyResult(localDate.getDayOfMonth() % 4),
                localDate.getDayOfWeek().name(), city);
    }

    private String getDailyResult(int dayOfMonthModuloFour) {
        switch (dayOfMonthModuloFour) {
            case 0:
                return "sunny";
            case 1:
                return "cloudy";
            case 2:
                return "chilly";
            case 3:
                return "rainy";
            default:
                throw new IllegalArgumentException();
        }
    }
}
