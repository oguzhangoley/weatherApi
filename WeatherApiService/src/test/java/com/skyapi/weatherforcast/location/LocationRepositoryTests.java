package com.skyapi.weatherforcast.location;

import com.skyapi.weatherforcast.common.HourlyWeather;
import com.skyapi.weatherforcast.common.HourlyWeatherId;
import com.skyapi.weatherforcast.common.Location;
import com.skyapi.weatherforcast.common.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
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
        location.setCode("LACA_USA");
        location.setCityName("Los Angeles");
        location.setRegionName("California");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Location savedLocation = repository.save(location);

        assertThat(savedLocation).isNotNull();
        assertThat(savedLocation.getCode()).isEqualTo("LACA_USA");
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

    @Test
    public void testAddHourlyWeatherData(){
        Location location = repository.findById("DELHI_IN").get();
        List<HourlyWeather> listHourlyWeather = location.getListHourlyWeather();
        HourlyWeather forecast1 = new HourlyWeather().id(location,10)
                .temperature(15)
                .precipitation(40)
                .status("Sunny");

        HourlyWeather forecast2 = new HourlyWeather().location(location)
                .hourOfDay(11)
                .temperature(16)
                .precipitation(50)
                .status("Cloudy");

        listHourlyWeather.add(forecast1);
        listHourlyWeather.add(forecast2);

        Location updatedLocation = repository.save(location);

        assertThat(updatedLocation.getListHourlyWeather()).isNotEmpty();
    }

    @Test
    public void testFindBtCountryCodeAndCityNotFound(){
        String countryCode = "US";
        String cityName = "New York City";

        Location location = repository.findByCountryCodeAndCityName(countryCode,cityName);
        assertThat(location).isNull();
    }

    @Test
    public void testFindBtCountryCodeAndCityFound(){
        String countryCode = "IN";
        String cityName = "New Delhi";

        Location location = repository.findByCountryCodeAndCityName(countryCode,cityName);
        System.out.println(location);
        assertThat(location).isNotNull();
        assertThat(location.getCountryCode()).isEqualTo(countryCode);
        assertThat(location.getCityName()).isEqualTo(cityName);

    }

}
