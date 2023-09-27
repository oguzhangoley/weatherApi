package com.skyapi.weatherforcast.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "locations")
public class Location {
    @Column(length = 12, nullable = false, unique = true)
    @Id
    @NotNull(message = "Location code cannot be null")
    @Length(min = 3, max = 12, message = "Location code must have 3-12 characters")
    private String code;
    @Column(length = 128, nullable = false)
    @JsonProperty("city_name")
    @NotBlank(message = "City cannot be left blank")
    private String cityName;

    @Column(length = 64, nullable = false)
    @JsonProperty("country_name")
    @NotBlank(message = "Country name cannot be left blank")
    private String countryName;


    @Column(length = 128, nullable = true)
    @JsonProperty("region_name")
    private String regionName;
    @Column(length = 2, nullable = false)
    @JsonProperty("country_code")
    @NotBlank(message = "Country code cannot be left blank")
    private String countryCode;

    private boolean enabled;
    @JsonIgnore
    private boolean trashed;

    @OneToMany(mappedBy = "id.location",cascade = CascadeType.ALL)
    private List<HourlyWeather> listHourlyWeather = new ArrayList<>();

    public Location() {
    }

    public Location(String cityName, String countryName, String regionName, String countryCode) {
        this.cityName = cityName;
        this.countryName = countryName;
        this.regionName = regionName;
        this.countryCode = countryCode;
    }

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private RealtimeWeather realtimeWeather;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isTrashed() {
        return trashed;
    }

    public void setTrashed(boolean trashed) {
        this.trashed = trashed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(code, location.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return cityName+", "+(regionName != null ? regionName:"")+", "+countryName;
    }

    public RealtimeWeather getRealtimeWeather() {
        return realtimeWeather;
    }

    public void setRealtimeWeather(RealtimeWeather realtimeWeather) {
        this.realtimeWeather = realtimeWeather;
    }

    public List<HourlyWeather> getListHourlyWeather() {
        return listHourlyWeather;
    }

    public void setListHourlyWeather(List<HourlyWeather> listHourlyWeather) {
        this.listHourlyWeather = listHourlyWeather;
    }
}
