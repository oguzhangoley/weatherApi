package com.skyapi.weatherforcast.common;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "realtime_weather")
public class RealtimeWeather {

    @Id
    @Column(name = "location_code")
    private String locationCode;
    @Range(min = -50,max = 50,message = "Temperature must be in range of -50 to 50 Celsius degree")
    private int temperature;
    @Range(min = 0,max = 100,message = "Humidity must be in range of 0 to 100 percentage")
    private int humidity;
    @Range(min = 0,max = 100,message = "Precipitation must be in range of 0 to 100 percentage")
    private int precipitation;
    @Range(min = 0,max = 200,message = "Wind speed must be in range of 0 to 200 km/h")
    private int windSpeed;
    @Column(length = 50)
    @NotBlank(message = "Status must not be empty")
    private String status;
    private Date lastUpdated;
    @OneToOne
    @JoinColumn(name = "location_code")
    @MapsId
    private Location location;

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(int precipitation) {
        this.precipitation = precipitation;
    }

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RealtimeWeather that = (RealtimeWeather) o;
        return Objects.equals(locationCode, that.locationCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationCode);
    }

    public void setLocation(Location location) {
        this.locationCode = location.getCode();
        this.location = location;
    }
}
