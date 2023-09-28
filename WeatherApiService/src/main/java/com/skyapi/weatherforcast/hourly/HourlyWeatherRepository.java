package com.skyapi.weatherforcast.hourly;

import com.skyapi.weatherforcast.common.HourlyWeather;
import com.skyapi.weatherforcast.common.HourlyWeatherId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, HourlyWeatherId> {

    @Query("""
            SELECT h FROM HourlyWeather h WHERE
            h.id.location.code = ?1 AND h.id.hourOfDay > ?2
            AND h.id.location.trashed = false
            """)
    public List<HourlyWeather> findByLocationCode(String locationCode,int currentHour);
}
