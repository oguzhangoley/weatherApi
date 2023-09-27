package com.skyapi.weatherforcast.realtime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforcast.GeoLocationException;
import com.skyapi.weatherforcast.GeoLocationService;
import com.skyapi.weatherforcast.common.Location;
import com.skyapi.weatherforcast.location.LocationNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RealtimeWeatherApiController.class)
public class RealtimeWeatherApiControllerTests {

    private static final String END_POINT_PATH = "/v1/realtime";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    RealtimeWeatherService realtimeWeatherService;
    @MockBean
    GeoLocationService locationService;

    @Test
    public void testGetShouldReturn400() throws Exception {
        Mockito.when(locationService.getLocation(Mockito.anyString())).thenThrow(GeoLocationException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void testGetShouldReturn404() throws Exception {

        Location location = new Location();

        Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
        Mockito.when(realtimeWeatherService.getByLocation(location)).thenThrow(LocationNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(status().isNotFound()).andDo(print());
    }
}
