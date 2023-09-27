package com.skyapi.weatherforcast.realtime;

import com.skyapi.weatherforcast.common.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RealtimeWeatherRepositoryTest {
    @Autowired
    private RealtimeWeatherRepository repository;

    @Test
    public void testUpdate() {
        String locationCode = "NYC_USA";
        RealtimeWeather realtimeWeather = repository.findById(locationCode).get();
        realtimeWeather.setTemperature(-2);
        realtimeWeather.setHumidity(32);
        realtimeWeather.setPrecipitation(42);
        realtimeWeather.setStatus("Snowy");
        realtimeWeather.setWindSpeed(12);
        realtimeWeather.setLastUpdated(new Date());

        RealtimeWeather updatedRealtime = repository.save(realtimeWeather);
        assertThat(updatedRealtime.getHumidity()).isEqualTo(32);
    }

    @Test
    public void testFindByCountryCodeAndCityNotFound() {
        String countryCode = "JP";
        String cityNme = "Tokyo";

        RealtimeWeather realtimeWeather = repository.findByCountryCodeAndCity(countryCode, cityNme);

        assertThat(realtimeWeather).isNull();
    }

    @Test
    public void testFindByCountryCodeAndCityOk() {
        String countryCode = "US";
        String cityNme = "New York City";

        RealtimeWeather realtimeWeather = repository.findByCountryCodeAndCity(countryCode, cityNme);

        assertThat(realtimeWeather).isNotNull();
        assertThat(realtimeWeather.getLocation().getCityName()).isEqualTo(cityNme);
    }

    @Test
    public void testFindByLocationNotFound(){
        String locationCode = "ABCXYZ";
        RealtimeWeather realtimeWeather = repository.findByLocationCode(locationCode);

        assertThat(realtimeWeather).isNull();
    }

    @Test
    public void testFindByLocationFound(){
        String locationCode = "DELHI_IN";
        RealtimeWeather realtimeWeather = repository.findByLocationCode(locationCode);

        assertThat(realtimeWeather).isNotNull();
        assertThat(realtimeWeather.getLocationCode()).isEqualTo(locationCode);
    }


}
