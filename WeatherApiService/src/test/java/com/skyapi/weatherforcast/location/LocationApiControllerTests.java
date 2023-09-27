package com.skyapi.weatherforcast.location;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforcast.common.Location;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LocationApiController.class)
public class LocationApiControllerTests {

    private static final String END_POINT_PATH = "/v1/locations";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    LocationService locationService;

    @Test
    public void testAddShouldReturn400BadRequest() throws Exception {
        Location location = new Location();

        String body = mapper.writeValueAsString(location);
        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH).contentType("application/json").content(body))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testAddShouldReturn201Created() throws Exception {
        Location location = new Location();
        location.setCode("NYC_USA");
        location.setCityName("New York City");
        location.setRegionName("New York");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnabled(true);

        Mockito.when(locationService.add(location)).thenReturn(location);

        String body = mapper.writeValueAsString(location);

        mockMvc.perform(MockMvcRequestBuilders.post(END_POINT_PATH).contentType(MediaType.APPLICATION_JSON_VALUE).content(body))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.code").value("NYC_USA"))
                .andDo(print());

    }

    @Test
    public void testListShouldReturn204NoContent() throws Exception {
        Mockito.when(locationService.list()).thenReturn(Collections.emptyList());
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(status().isNoContent())
                .andDo(print());
    }
    @Test
    public void testListShouldReturn200Ok() throws Exception {
        Location location1 = new Location();
        location1.setCode("NYC_USA");
        location1.setCityName("New York City");
        location1.setRegionName("New York");
        location1.setCountryCode("US");
        location1.setCountryName("United States of America");
        location1.setEnabled(true);

        Location location2 = new Location();
        location2.setCode("LACA_USA");
        location2.setCityName("Los Angeles");
        location2.setRegionName("California");
        location2.setCountryCode("US");
        location2.setCountryName("United States of America");
        location2.setEnabled(true);

        Location location3 = new Location();
        location3.setCode("DELHI_IN");
        location3.setCityName("New Delhi");
        location3.setRegionName("Delhi");
        location3.setCountryCode("IN");
        location3.setCountryName("India");
        location3.setEnabled(true);
        location3.setTrashed(true);

        Mockito.when(locationService.list()).thenReturn(List.of(location1,location2,location3));
        mockMvc.perform(MockMvcRequestBuilders.get(END_POINT_PATH))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn405MethodNotAllowed() throws Exception {
        String requestUri = END_POINT_PATH+"/ABCDE";

        mockMvc.perform(MockMvcRequestBuilders.post(requestUri))
                .andExpect(status().isMethodNotAllowed())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn404NotFound() throws Exception {
        String requestUri = END_POINT_PATH+"/ABCDE";

        mockMvc.perform(MockMvcRequestBuilders.get(requestUri))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturn200Ok() throws Exception {
        String code = "NYC_USA";
        String requestUri = END_POINT_PATH+"/"+code;

        Location location1 = new Location();
        location1.setCode("NYC_USA");
        location1.setCityName("New York City");
        location1.setRegionName("New York");
        location1.setCountryCode("US");
        location1.setCountryName("United States of America");
        location1.setEnabled(true);
        Mockito.when(locationService.get(code)).thenReturn(location1);

        mockMvc.perform(MockMvcRequestBuilders.get(requestUri))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(code))
                .andDo(print());

    }

}
