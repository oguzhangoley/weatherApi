package com.skyapi.weatherforcast.location;

import com.skyapi.weatherforcast.common.Location;
import com.skyapi.weatherforcast.common.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class LocationRepositoryTests {

    @Autowired
    private LocationRepository repository;

    @Test
    public void testAddSuccess() {
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Location savedLocation = repository.save(location);

        assertThat(savedLocation).isNotNull();
        assertThat(savedLocation.getCode()).isEqualTo("NYC_USA");
    }

    @Test
    public void testListSuccess() {
        List<Location> locations = repository.findUntrashed();

        assertThat(locations).isNotEmpty();
        locations.forEach(System.out::println);
    }

    @Test
    public void testGetNotFound() {
        String code = "ABCD";
        Location location = repository.findByCode(code);

        assertThat(location).isNull();
    }

    @Test
    public void testGetOk() {
        String code = "NYC_USA";
        Location location = repository.findByCode(code);

        assertThat(location).isNotNull();
        assertThat(location.getCode()).isEqualTo(code);
    }

    @Test
    public void testTrashSuccess() {
        String code = "DELHI_IN";
        repository.trashByCode(code);

        Location location = repository.findByCode(code);
        assertThat(location).isNull();
    }

    @Test
    public void testAddRealtimeWeatherData() {
        String code = "NYC_USA";
        Location location = repository.findByCode(code);
        RealtimeWeather realtimeWeather = location.getRealtimeWeather();
        if (realtimeWeather == null) {
            realtimeWeather = new RealtimeWeather();
            realtimeWeather.setLocation(location);
            location.setRealtimeWeather(realtimeWeather);
        }
        realtimeWeather.setTemperature(-1);
        realtimeWeather.setHumidity(30);
        realtimeWeather.setPrecipitation(40);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(15);
        realtimeWeather.setLastUpdated(new Date());

        Location updatedLocation = repository.save(location);
        assertThat(updatedLocation.getRealtimeWeather().getLocationCode()).isEqualTo(code);
    }

}
