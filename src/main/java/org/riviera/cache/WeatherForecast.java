package org.riviera.cache;

import java.util.List;

public class WeatherForecast {

    private List dailyForecasts;

    private long executionTimeInMs;

    public WeatherForecast(List dailyForecasts, long executionTimeInMs) {
        this.dailyForecasts = dailyForecasts;
        this.executionTimeInMs = executionTimeInMs;
    }

    public List getDailyForecasts() {
        return dailyForecasts;
    }

    public long getExecutionTimeInMs() {
        return executionTimeInMs;
    }
}
