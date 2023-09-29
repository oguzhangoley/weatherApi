package com.skyapi.weatherforcast.realtime;

import com.skyapi.weatherforcast.common.Location;
import com.skyapi.weatherforcast.common.RealtimeWeather;
import com.skyapi.weatherforcast.location.LocationNotFoundException;
import com.skyapi.weatherforcast.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RealtimeWeatherService {

    private final RealtimeWeatherRepository realtimeWeatherRepository;
    private final LocationRepository locationRepository;

    public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepository, LocationRepository locationRepository) {
        this.realtimeWeatherRepository = realtimeWeatherRepository;
        this.locationRepository = locationRepository;
    }

    public RealtimeWeather getByLocation(Location location) {
        String countryCode = location.getCountryCode();
        String city = location.getCityName();

        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, city);
        if (realtimeWeather == null) {
            throw new LocationNotFoundException(countryCode, city);
        }
        return realtimeWeather;
    }

    public RealtimeWeather getByLocationCode(String locationCode) {
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);

        if (realtimeWeather == null) {
            throw new LocationNotFoundException(locationCode);
        }
        return realtimeWeather;
    }

    public RealtimeWeather update(String locationCode, RealtimeWeather realtimeWeather) {
        Location location = locationRepository.findByCode(locationCode);
        if (location == null) {
            throw new LocationNotFoundException(locationCode);
        }

        realtimeWeather.setLocation(location);
        realtimeWeather.setLastUpdated(new Date());

        return realtimeWeatherRepository.save(realtimeWeather);
    }
}
