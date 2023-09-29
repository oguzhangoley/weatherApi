package com.skyapi.weatherforcast.hourly;

import com.skyapi.weatherforcast.BadRequestException;
import com.skyapi.weatherforcast.CommonUtility;
import com.skyapi.weatherforcast.GeoLocationException;
import com.skyapi.weatherforcast.GeoLocationService;
import com.skyapi.weatherforcast.common.HourlyWeather;
import com.skyapi.weatherforcast.common.Location;
import com.skyapi.weatherforcast.location.LocationNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/hourly")
@Validated
public class HourlyWeatherApiController {
    private final HourlyWeatherService hourlyWeatherService;
    private final GeoLocationService locationService;
    private final ModelMapper mapper;

    public HourlyWeatherApiController(HourlyWeatherService hourlyWeatherService, GeoLocationService locationService, ModelMapper mapper) {
        this.hourlyWeatherService = hourlyWeatherService;
        this.locationService = locationService;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtility.getIPAddress(request);

        try {
            int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

            Location location = locationService.getLocation(ipAddress);
            List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocation(location, currentHour);

            if (hourlyForecast.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(listEntity2Dto(hourlyForecast));
        } catch (NumberFormatException | GeoLocationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{locationCode}")
    public ResponseEntity<?> listHourlyForecastByLocationCode(@PathVariable String locationCode, HttpServletRequest request) {

        try {
            int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

            List<HourlyWeather> hourlyForecast = hourlyWeatherService.getByLocationCode(locationCode, currentHour);
            if (hourlyForecast.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(listEntity2Dto(hourlyForecast));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> update(@PathVariable("code") String locationCode, @RequestBody @Valid List<HourlyWeather> listDto) {
        if (listDto.isEmpty()) {
            throw new BadRequestException("Hourly forecast data cannot be empty");
        }
        List<HourlyWeather> list = hourlyWeatherService.updateByLocationCode(locationCode, listDto);
        return ResponseEntity.accepted().build();
    }

    private List<HourlyWeather> listDto2ListEntity(List<HourlyWeatherDto> dtoList) {
        List<HourlyWeather> list = new ArrayList<>();
        dtoList.forEach(dto -> {
            list.add(mapper.map(dto, HourlyWeather.class));
        });
        return list;
    }

    private HourlyWeatherListDto listEntity2Dto(List<HourlyWeather> hourlyForecast) {
        Location location = hourlyForecast.get(0).getId().getLocation();
        HourlyWeatherListDto listDto = new HourlyWeatherListDto();
        listDto.setLocation(location.toString());

        hourlyForecast.forEach(item -> {
            HourlyWeatherDto hourlyWeatherDto = mapper.map(item, HourlyWeatherDto.class);
            listDto.addHourlyWeatherDto(hourlyWeatherDto);
        });
        return listDto;
    }
}
