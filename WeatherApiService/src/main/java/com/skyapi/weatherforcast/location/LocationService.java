package com.skyapi.weatherforcast.location;

import com.skyapi.weatherforcast.common.Location;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    private LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    public Location add(Location location) {
        return repository.save(location);
    }
}
