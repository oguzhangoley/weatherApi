package com.skyapi.weatherforcast.hourly;

import com.skyapi.weatherforcast.common.HourlyWeather;
import com.skyapi.weatherforcast.common.Location;
import com.skyapi.weatherforcast.location.LocationNotFoundException;
import com.skyapi.weatherforcast.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class HourlyWeatherService {
    private HourlyWeatherRepository hourlyWeatherRepository;
    private LocationRepository locationRepository;

    public HourlyWeatherService(HourlyWeatherRepository hourlyWeatherRepository, LocationRepository locationRepository) {
        this.hourlyWeatherRepository = hourlyWeatherRepository;
        this.locationRepository = locationRepository;
    }

    public List<HourlyWeather> getByLocation(Location location, int currentHour) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        Location locationDb = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);

        if (locationDb == null) {
            throw new LocationNotFoundException("No location found with the given country code and city name");
        }
        return hourlyWeatherRepository.findByLocationCode(locationDb.getCode(), currentHour);
    }

    public List<HourlyWeather> getByLocationCode(String locationCode, int currentHour) throws LocationNotFoundException {
        Location location = locationRepository.findByCode(locationCode);
        if (location == null) {
            throw new LocationNotFoundException("No location found with the given code: " + locationCode);
        }
        return hourlyWeatherRepository.findByLocationCode(locationCode, currentHour);
    }

    public List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> list) throws LocationNotFoundException {
        Location location = locationRepository.findByCode(locationCode);
        if (location == null) {
            throw new LocationNotFoundException("No location found with the given code: " + locationCode);
        }
        return (List<HourlyWeather>) hourlyWeatherRepository.saveAll(list);
    }

}
