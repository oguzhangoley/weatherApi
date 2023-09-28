package com.skyapi.weatherforcast.hourly;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforcast.GeoLocationException;
import com.skyapi.weatherforcast.GeoLocationService;
import com.skyapi.weatherforcast.common.Location;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HourlyWeatherApiController.class)
public class HourlyWeatherAPiControllerTests {
    private static final String END_POINT_PATH = "/v1/hourly";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    HourlyWeatherService hourlyWeatherService;
    @MockBean
    GeoLocationService locationService;

    @Test
    public void testGetBtIPShouldReturn400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGetBtIPShouldReturn400GeolocationEx() throws Exception {

        Mockito.when(locationService.getLocation(Mockito.anyString())).thenThrow(GeoLocationException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH).header("X-Current-Hour", "9"))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGetBtIPShouldReturn204() throws Exception {
        int currentHour = 9;
        Location location = new Location().code("DELHI_IN");


        Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(hourlyWeatherService.getByLocation(location,currentHour)).thenReturn(new ArrayList<>());

        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH).header("X-Current-Hour", "9"))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
}
