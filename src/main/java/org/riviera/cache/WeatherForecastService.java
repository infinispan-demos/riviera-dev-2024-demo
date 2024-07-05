package org.riviera.cache;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import io.quarkus.infinispan.client.Remote;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import io.quarkus.cache.CacheResult;
import jakarta.inject.Inject;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.commons.api.query.Query;

@ApplicationScoped
public class WeatherForecastService {

    @CacheResult(cacheName = "parisbackups")
    public String getDailyForecastParis(long epoch) {
//        try {
//            Thread.sleep(1000L);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
        LocalDate localDate = LocalDate.ofEpochDay(epoch);
        return localDate.getDayOfWeek() + " will be " + getDailyResult(localDate.getDayOfMonth() % 4);
    }

    @CacheResult(cacheName = "allcities")
    public String getDailyForecastDayMonth(long epoch, String city) {
        LocalDate localDate = LocalDate.ofEpochDay(epoch);
        return localDate.getDayOfWeek() + " will be " + getDailyResult(localDate.getDayOfMonth() % 4) + " in " + city;
    }

    @CacheResult(cacheName = "allcities")
    public Weather getDaily(long epoch, String city) {
        LocalDate localDate = LocalDate.ofEpochDay(epoch);
        String dailyResult = getDailyResult(localDate.getDayOfMonth() % 4);
        return new Weather(dailyResult, localDate.getDayOfWeek().name(), city);
    }

    @Inject
    @Remote("allcities")
    private RemoteCache<String, Weather> weatherRemoteCache;

    public List<String> searchCityByWeather(String weather) {
        Query<Object[]> query = weatherRemoteCache.query("select w.city from riviera.Weather w where w.weather=':p1'");
        query.setParameter("p1", weather);
        Log.info(query.getQueryString());
        query.execute().list().stream().forEach(s -> System.out.println(s));
        return query.execute().list().stream().map(r -> r[0].toString()).collect(Collectors.toList());
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
