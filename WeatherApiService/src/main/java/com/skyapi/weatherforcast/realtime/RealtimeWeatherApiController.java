package com.skyapi.weatherforcast.realtime;

import com.skyapi.weatherforcast.CommonUtility;
import com.skyapi.weatherforcast.GeoLocationException;
import com.skyapi.weatherforcast.GeoLocationService;
import com.skyapi.weatherforcast.common.Location;
import com.skyapi.weatherforcast.common.RealtimeWeather;
import com.skyapi.weatherforcast.location.LocationNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/realtime")
public class RealtimeWeatherApiController {
    private static final Logger logger = LoggerFactory.getLogger(RealtimeWeatherApiController.class);
    private GeoLocationService locationService;
    private final RealtimeWeatherService realtimeWeatherService;
    private final ModelMapper modelMapper;

    public RealtimeWeatherApiController(GeoLocationService locationService, RealtimeWeatherService realtimeWeatherService, ModelMapper modelMapper) {
        this.locationService = locationService;
        this.realtimeWeatherService = realtimeWeatherService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);
        Location locationFromIP = locationService.getLocation(ipAddress);
        RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIP);

        RealtimeWeatherDto realtimeWeatherDto = entity2Dto(realtimeWeather);

        return ResponseEntity.ok(realtimeWeatherDto);

    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> getRealtimeWeatherByLocationCode(@PathVariable("locationCode") String locationCode) {

        RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(locationCode);
        RealtimeWeatherDto realtimeWeatherDto = entity2Dto(realtimeWeather);
        return ResponseEntity.ok(realtimeWeatherDto);

    }

    @PutMapping("/{locationCode}")
    public ResponseEntity<?> updateRealtimeWeather(@PathVariable("locationCode") String locationCode, @RequestBody @Valid RealtimeWeather realtimeWeather) {

        RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode, realtimeWeather);
        RealtimeWeatherDto realtimeWeatherDto = entity2Dto(updatedRealtimeWeather);
        return ResponseEntity.ok(realtimeWeatherDto);

    }

    private RealtimeWeatherDto entity2Dto(RealtimeWeather realtimeWeather) {
        return modelMapper.map(realtimeWeather, RealtimeWeatherDto.class);
    }
}
