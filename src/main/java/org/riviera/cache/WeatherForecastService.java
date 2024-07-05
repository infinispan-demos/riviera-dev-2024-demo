package org.riviera.cache;

import java.time.LocalDate;

import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.cache.CacheResult;

@ApplicationScoped
public class WeatherForecastService {

    @CacheResult(cacheName = "mycache")
    public String getDailyForecastDayMonth(long epoch, String city) {
//        try {
//            //Thread.sleep(1000L);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
        LocalDate localDate = LocalDate.ofEpochDay(epoch);
        return localDate.getDayOfWeek() + " will be " + getDailyResult(localDate.getDayOfMonth() % 4) + " in " + city;
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
