package com.skyapi.weatherforcast.hourly;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class HourlyWeatherListDto {
    private String location;
    @JsonProperty("hourly_forecast")
    private List<HourlyWeatherDto> hourlyForecast = new ArrayList<>();

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<HourlyWeatherDto> getHourlyForecast() {
        return hourlyForecast;
    }

    public void setHourlyForecast(List<HourlyWeatherDto> hourlyForecast) {
        this.hourlyForecast = hourlyForecast;
    }

    public void addHourlyWeatherDto(HourlyWeatherDto dto) {
        this.hourlyForecast.add(dto);
    }
}
