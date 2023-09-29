package com.skyapi.weatherforcast.hourly;

import com.skyapi.weatherforcast.common.HourlyWeather;
import com.skyapi.weatherforcast.common.HourlyWeatherId;
import com.skyapi.weatherforcast.common.Location;
import com.skyapi.weatherforcast.location.LocationNotFoundException;
import com.skyapi.weatherforcast.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class HourlyWeatherService {
    private final HourlyWeatherRepository hourlyWeatherRepository;
    private final LocationRepository locationRepository;

    public HourlyWeatherService(HourlyWeatherRepository hourlyWeatherRepository, LocationRepository locationRepository) {
        this.hourlyWeatherRepository = hourlyWeatherRepository;
        this.locationRepository = locationRepository;
    }

    public List<HourlyWeather> getByLocation(Location location, int currentHour) {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        Location locationDb = locationRepository.findByCountryCodeAndCityName(countryCode, cityName);

        if (locationDb == null) {
            throw new LocationNotFoundException(countryCode, cityName);
        }
        return hourlyWeatherRepository.findByLocationCode(locationDb.getCode(), currentHour);
    }

    public List<HourlyWeather> getByLocationCode(String locationCode, int currentHour) {
        Location location = locationRepository.findByCode(locationCode);
        if (location == null) {
            throw new LocationNotFoundException(locationCode);
        }
        return hourlyWeatherRepository.findByLocationCode(locationCode, currentHour);
    }

    public List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> list) {
        Location location = locationRepository.findByCode(locationCode);
        if (location == null) {
            throw new LocationNotFoundException(locationCode);
        }

        for (HourlyWeather item : list) {
            item.getId().setLocation(location);
        }

        List<HourlyWeather> listInDb = location.getListHourlyWeather();
        List<HourlyWeather> listTobeRemoved = new ArrayList<>();
        for (HourlyWeather item : listInDb) {
            if (!list.contains(item)) {
                listTobeRemoved.add(item.getShallowCopy());
            }
        }
        for (HourlyWeather item : listTobeRemoved) {
            listInDb.remove(item);
        }

        return (List<HourlyWeather>) hourlyWeatherRepository.saveAll(list);
    }

}
