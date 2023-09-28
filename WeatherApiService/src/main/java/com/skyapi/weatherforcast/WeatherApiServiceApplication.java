package com.skyapi.weatherforcast;

import com.skyapi.weatherforcast.common.HourlyWeather;
import com.skyapi.weatherforcast.hourly.HourlyWeatherDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeatherApiServiceApplication {

    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        var typeMap1 = modelMapper.typeMap(HourlyWeather.class, HourlyWeatherDto.class);
        typeMap1.addMapping(src -> src.getId().getHourOfDay(), HourlyWeatherDto::setHourOfDay);

        var typeMap2 = modelMapper.typeMap(HourlyWeatherDto.class, HourlyWeather.class);
        typeMap2.addMapping(HourlyWeatherDto::getHourOfDay,
                (dest, value) -> dest.getId().setHourOfDay(value != null ? (int) value : 0));

        return modelMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiServiceApplication.class, args);
    }
}