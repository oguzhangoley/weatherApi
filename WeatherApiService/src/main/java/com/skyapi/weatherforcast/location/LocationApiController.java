package com.skyapi.weatherforcast.location;

import com.skyapi.weatherforcast.common.Location;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/locations")
public class LocationApiController {

    private final LocationService locationService;
    private final ModelMapper mapper;

    public LocationApiController(LocationService locationService, ModelMapper mapper) {
        this.locationService = locationService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<?> addLocation(@RequestBody @Valid Location location) {
        Location addedLocation = locationService.add(location);
        URI uri = URI.create("v1/locations/" + addedLocation.getCode());
        return ResponseEntity.created(uri).body(entity2Dto(addedLocation));
    }

    @GetMapping
    public ResponseEntity<?> listLocations() {
        List<Location> locations = locationService.list();
        if (locations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listEntity2ListDto(locations));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getLocation(@PathVariable String code) {
        Location location = locationService.get(code);
        return ResponseEntity.ok(entity2Dto(location));
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@RequestBody @Valid Location location) {

        Location updatedLocation = locationService.update(location);
        return ResponseEntity.ok(entity2Dto(updatedLocation));

    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable String code) {

        locationService.delete(code);
        return ResponseEntity.noContent().build();

    }

    private List<LocationDto> listEntity2ListDto(List<Location> listEntity) {
        return listEntity.stream().map(this::entity2Dto).toList();
    }

    private LocationDto entity2Dto(Location entity) {
        return mapper.map(entity, LocationDto.class);
    }

    private Location Dto2entity(LocationDto dto) {
        return mapper.map(dto, Location.class);
    }
}
