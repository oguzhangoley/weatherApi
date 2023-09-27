package com.skyapi.weatherforcast;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.skyapi.weatherforcast.common.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeoLocationService {
    private static final Logger logger = LoggerFactory.getLogger(GeoLocationService.class);
    private String DBPath = "C:\\Users\\Monster\\Desktop\\Java\\wheatherapi\\WeatherApiProject\\WeatherApiService\\ip2locdb\\IP2LOCATION-LITE-DB3.BIN";
    private IP2Location ipLocator = new IP2Location();

    public GeoLocationService() {
        try {
            ipLocator.Open(DBPath);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public Location getLocation(String ipAddress) throws GeoLocationException {
        try {
            IPResult ipResult = ipLocator.IPQuery(ipAddress);
            if (!"OK".equals(ipResult.getStatus())) {
                throw new GeoLocationException("GeoLocation failed with status : " + ipResult.getStatus());
            }
            logger.info(ipResult.toString());
            return new Location(ipResult.getCity(),ipResult.getRegion(), ipResult.getCountryLong(),ipResult.getCountryShort());
        } catch (Exception e) {
            throw new GeoLocationException("Error querying IP database", e);
        }
    }
}
