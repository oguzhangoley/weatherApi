package com.skyapi.weatherforcast.location;

import com.skyapi.weatherforcast.common.Location;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/v1/location")
public class LocationApiController {

    private LocationService locationService;

    public LocationApiController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping
    public ResponseEntity<Location> addLocation(@RequestBody Location location)
    {
        Location addedLocation = locationService.add(location);
        URI uri = URI.create("v1/locations"+location.getCode());
        return ResponseEntity.created(uri).body(addedLocation);
    }
}
