package com.skyapi.weatherforcast.location;

import com.skyapi.weatherforcast.common.Location;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LocationService {
    private final LocationRepository repository;

    public LocationService(LocationRepository repository) {
        this.repository = repository;
    }

    public Location add(Location location) {
        return repository.save(location);
    }

    public List<Location> list() {
        return repository.findUntrashed();
    }

    public Location get(String code) {
        Location location = repository.findByCode(code);
        if (location == null) {
            throw new LocationNotFoundException(code);
        }
        return location;
    }

    public Location update(Location locationInRequest) {
        String code = locationInRequest.getCode();

        Location locationInDb = repository.findByCode(code);
        if (locationInDb == null) {
            throw new LocationNotFoundException(code);
        }

        locationInDb.setTrashed(locationInRequest.isTrashed());
        locationInDb.setEnabled(locationInRequest.isEnabled());
        locationInDb.setCityName(locationInRequest.getCityName());
        locationInDb.setCountryName(locationInRequest.getCountryName());
        locationInDb.setRegionName(locationInRequest.getRegionName());
        locationInDb.setCountryCode(locationInRequest.getCountryCode());

        return repository.save(locationInDb);
    }

    public void delete(String code) {

        Location location = repository.findByCode(code);
        if (location == null) {
            throw new LocationNotFoundException(code);
        }
        repository.trashByCode(code);
        repository.save(location);
    }
}
