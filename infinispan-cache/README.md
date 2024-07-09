# Riviera Dev 2024 - Demo

## Paris Weather

```bash
http http://localhost:8080/weather/paris
http http://localhost:8080/weather/paris?daysInFuture=100
```

## Paris Infinispan Cache

```xml
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-infinispan-cache</artifactId>
    </dependency>
```

## Composite Key simple

```java

@GET
@Path('{city}')
public WeatherForecast getForecast(@RestPath String city, @RestQuery long daysInFuture){
 // 
}
        
```

```java
    @CacheResult(cacheName = "mycache")
    public String getDailyForecastDayMonth(long epoch, String city) {
        LocalDate localDate = LocalDate.ofEpochDay(epoch);
        return localDate.getDayOfWeek() + " will be " + getDailyResult(localDate.getDayOfMonth() % 4) + " in " + city;
    }
```


## Retourner Weather

```java
@Proto
public record Weather(String weather, String day, String city) {
}
```

```java
@ProtoSchema(includeClasses = Weather.class,
        schemaPackageName = "riviera")
public interface WeatherSchema extends GeneratedSchema {
}
```

```java
@CacheResult(cacheName = "mycache")
public Weather getDaily(long epoch, String city) {
    LocalDate localDate = LocalDate.ofEpochDay(epoch);
    String dailyResult = getDailyResult(localDate.getDayOfMonth() % 4);
    return new Weather(dailyResult, localDate.getDayOfWeek().name(), city);
}
```

```java
    @GET
    public WeatherForecast getForecast(@RestQuery String city, @RestQuery long daysInFuture) {
        List<Weather> dailyForecasts = Arrays.asList(
                service.getDaily(nowPlusDays.toEpochDay(), city),
                service.getDaily(nowPlusDaysPlus1.toEpochDay(), city),
                service.getDaily(nowPlusDaysPlus2.toEpochDay(), city));
        long executionEnd = System.currentTimeMillis();
        return new WeatherForecast(dailyForecasts, executionEnd - executionStart);
    }
```

## Faire une recherche non indexé

```java
    @GET
    @Path("travel")
    public List<String> getTravelCity(@RestQuery String type) {
        Log.info(type);
        return service.searchCityByWeather(type);
    }
```

```java
@Inject
@Remote("mycache")
private RemoteCache<WeatherKey, Weather> weatherRemoteCache;

public List<String> searchCityByWeather(String weather) {
        Query<Object[]> query = weatherRemoteCache.query("select w.city from riviera.Weather w where w.weather=':p1'");
        query.setParameter("p1", weather);
        Log.info(query.getQueryString());
        query.execute().list().stream().forEach(s -> System.out.println(s));
        return query.execute().list().stream().map(r -> r[0].toString()).collect(Collectors.toList());
}
```

```java
public static class ExceptionMappers {
        @ServerExceptionMapper
        public RestResponse<String> mapException(HotRodClientException ex) {
            return RestResponse.status(Response.Status.BAD_REQUEST,
                    Json.createObjectBuilder().add("infinispan-error", ex.getMessage()).build().toString());
        }
    }
```
