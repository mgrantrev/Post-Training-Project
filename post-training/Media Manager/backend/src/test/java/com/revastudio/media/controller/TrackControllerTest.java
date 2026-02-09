package com.revastudio.media.controller;

import com.revastudio.media.dto.TrackDto;
import com.revastudio.media.service.TrackService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrackControllerTest {

    private MockMvc mockMvc;

    private TrackService trackService;

    @org.junit.jupiter.api.BeforeEach
    void setup() {
        TrackDto dto = new TrackDto();
        dto.setTrackId(1);
        dto.setName("Test Track");
        dto.setAlbumTitle("Album");
        dto.setArtistName("Artist");
        dto.setUnitPrice(0.99);

        // simple stubbed TrackService using anonymous subclass to avoid Mockito in this standalone test
        this.trackService = new TrackService(null, null, null) {
            @Override
            public java.util.List<TrackDto> getTracksForCustomer(Integer customerId) {
                return List.of(dto);
            }
        };

        TrackController controller = new TrackController(trackService);
        this.mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetTracksForCustomer() throws Exception {
        mockMvc.perform(get("/api/customers/1/tracks").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].trackId").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Track"))
                .andExpect(jsonPath("$[0].albumTitle").value("Album"))
                .andExpect(jsonPath("$[0].artistName").value("Artist"));
    }
}

