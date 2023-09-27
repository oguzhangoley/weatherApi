package com.skyapi.weatherforcast.realtime;

import com.skyapi.weatherforcast.common.Location;
import com.skyapi.weatherforcast.common.RealtimeWeather;
import com.skyapi.weatherforcast.location.LocationNotFoundException;
import com.skyapi.weatherforcast.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RealtimeWeatherService {

    private RealtimeWeatherRepository realtimeWeatherRepository;
    private LocationRepository locationRepository;

    public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepository, LocationRepository locationRepository) {
        this.realtimeWeatherRepository = realtimeWeatherRepository;
        this.locationRepository = locationRepository;
    }

    public RealtimeWeather getByLocation(Location location) throws LocationNotFoundException {
        String countryCode = location.getCountryCode();
        String city = location.getCityName();

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, city);
        if (realtimeWeather == null) {
            throw new LocationNotFoundException("No Location found with the country code and city name");
        }
        return realtimeWeather;
    }

    public RealtimeWeather getByLocationCode(String locationCode) throws LocationNotFoundException {
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);

        if (realtimeWeather == null) {
            throw new LocationNotFoundException("No location found with the given code: " + locationCode);
        }
        return realtimeWeather;
    }

    public RealtimeWeather update(String locationCode, RealtimeWeather realtimeWeather) throws LocationNotFoundException {
        Location location = locationRepository.findByCode(locationCode);
        if (location == null) {
            throw new LocationNotFoundException("No location found with the given code: " + locationCode);
        }

        realtimeWeather.setLocation(location);
        realtimeWeather.setLastUpdated(new Date());

        return realtimeWeatherRepository.save(realtimeWeather);
    }
}
