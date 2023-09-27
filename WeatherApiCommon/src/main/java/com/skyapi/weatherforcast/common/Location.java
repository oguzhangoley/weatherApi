package com.skyapi.weatherforcast.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "locations")
public class Location {
    @Column(length = 12, nullable = false, unique = true)
    @Id
    @NotBlank
    private String code;
    @Column(length = 128, nullable = false)
    @JsonProperty("city_name")
    @NotBlank
    private String cityName;

    @Column(length = 64, nullable = false)
    @JsonProperty("country_name")
    @NotBlank
    private String countryName;


    @Column(length = 128, nullable = false)
    @JsonProperty("region_name")
    @NotNull
    private String regionName;
    @Column(length = 2, nullable = false)
    @JsonProperty("country_code")
    @NotBlank
    private String countryCode;

    private boolean enabled;
    @JsonIgnore
    private boolean trashed;

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
        return "Location{" +
                "code='" + code + '\'' +
                ", cityName='" + cityName + '\'' +
                ", countryName='" + countryName + '\'' +
                ", regionName='" + regionName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", enabled=" + enabled +
                ", trashed=" + trashed +
                '}';
    }
}
