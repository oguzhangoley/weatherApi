package com.skyapi.weatherforcast;

import java.io.IOException;

public class GeoLocationException extends RuntimeException {
    public GeoLocationException(String message) {
        super(message);
    }

    public GeoLocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
