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
    public void testAddShouldReturn201Created() throws Exception
    {
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

}
