# Riviera Dev 2024 - Demo

## Paris Weather

```bash
http http://localhost:8080/weather/paris
http http://localhost:8080/weather/paris?daysInFuture=100
```

Configure Expiration on the client

```properties
quarkus.cache.infinispan.paris.lifespan=10s
```

## Caching Use Case

```properties
quarkus.infinispan-client.dev-services.enabled=false
quarkus.infinispan-client.hosts=localhost:11222
quarkus.infinispan-client.username=admin
quarkus.infinispan-client.password=password
quarkus.infinispan-client.client-intelligence=BASIC
quarkus.cache.infinispan.expiration-cache.lifespan=15s
```

```bash
podman compose up 
```

## Surveillance

Console Jaeger

http://localhost:16686/

## Composite Key simple

```java
    @GET
    public WeatherForecast getForecast(@RestQuery String city, @RestQuery long daysInFuture) {
        long executionStart = System.currentTimeMillis();
        LocalDate nowPlusDays = LocalDate.now().plusDays(daysInFuture);
        LocalDate nowPlusDaysPlus1 = LocalDate.now().plusDays(daysInFuture + 1L);
        LocalDate nowPlusDaysPlus2 = LocalDate.now().plusDays(daysInFuture + 2L);
        List<String> dailyForecasts = Arrays.asList(
                service.getDailyForecastDayMonth(nowPlusDays.toEpochDay(), city),
                service.getDailyForecastDayMonth(nowPlusDaysPlus1.toEpochDay(), city),
                service.getDailyForecastDayMonth(nowPlusDaysPlus2.toEpochDay(), city));
        long executionEnd = System.currentTimeMillis();
        return new WeatherForecast(dailyForecasts, executionEnd - executionStart);
    }
```

```java
    @CacheResult(cacheName = "allcities")
    public String getDailyForecastDayMonth(long epoch, String city) {
        LocalDate localDate = LocalDate.ofEpochDay(epoch);
        return localDate.getDayOfWeek() + " will be " + getDailyResult(localDate.getDayOfMonth() % 4) + " in " + city;
    }
```


## Schema for weather

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
@CacheResult(cacheName = "allcities")
public Weather getDaily(long epoch, String city) {
    LocalDate localDate = LocalDate.ofEpochDay(epoch);
    String dailyResult = getDailyResult(localDate.getDayOfMonth() % 4);
    return new Weather(dailyResult, localDate.getDayOfWeek().name(), city);
}
```

```java
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
```

## Query


```java
@Proto
@Indexed
public record Weather(@Text String weather, String day, String city) {
}
```

```java
@Inject
@Remote("allcities")
private RemoteCache<String, Weather> weatherRemoteCache;

public List<String> searchCityByWeather(String weather) {
        Query<Object[]> query = weatherRemoteCache.query("select w.city from riviera.Weather w where w.weather:':p1'~2");
//        Query<Object[]> query = weatherRemoteCache.query("select w.city from riviera.Weather w where w.weather=':p1'");
        query.setParameter("p1", weather);
        Log.info(query.getQueryString());
        query.execute().list().stream().forEach(s -> System.out.println(s));
        return query.execute().list().stream().map(r -> r[0].toString()).collect(Collectors.toList());
}
```

```java
 @GET
    @Path("travel")
    public List<String> getTravelCity(@RestQuery String type) {
        Log.info(type);
        return service.searchCityByWeather(type);
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