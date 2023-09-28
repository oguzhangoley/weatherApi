package com.skyapi.weatherforcast.hourly;

import com.skyapi.weatherforcast.common.HourlyWeather;
import com.skyapi.weatherforcast.common.HourlyWeatherId;
import com.skyapi.weatherforcast.common.Location;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class HourlyWeatherRepositoryTests {
    @Autowired
    private HourlyWeatherRepository repository;

    @Test
    public void testAdd() {
        String locationCode = "DELHI_IN";
        int hourOfDay = 12;

        Location location = new Location().code(locationCode);
        HourlyWeather forecast = new HourlyWeather().location(location)
                .hourOfDay(hourOfDay)
                .temperature(13)
                .precipitation(70)
                .status("Cloudy");

        HourlyWeather updatedHourlyWeather = repository.save(forecast);

        assertThat(updatedHourlyWeather.getId().getLocation().getCode()).isEqualTo(locationCode);
        assertThat(updatedHourlyWeather.getId().getHourOfDay()).isEqualTo(hourOfDay);
    }

    @Test
    public void testDelete() {
        Location location = new Location().code("DELHI_IN");
        HourlyWeatherId hourlyWeatherId = new HourlyWeatherId(10, location);
        repository.deleteById(hourlyWeatherId);

        Optional<HourlyWeather> result = repository.findById(hourlyWeatherId);
        assertThat(result).isNotPresent();
    }

    @Test
    public void findByLocationCodeFound() {
        String locationCode = "DELHI_IN";
        int currentHour = 10;

        List<HourlyWeather> hourlyWeathers = repository.findByLocationCode(locationCode, currentHour);
        assertThat(hourlyWeathers).isNotEmpty();
    }

    @Test
    public void findByLocationCodeNotFound() {
        String locationCode = "DELHI_IN";
        int currentHour = 15;

        List<HourlyWeather> hourlyWeathers = repository.findByLocationCode(locationCode, currentHour);
        assertThat(hourlyWeathers).isEmpty();
    }


}
