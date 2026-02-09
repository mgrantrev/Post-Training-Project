package com.revastudio.media.service;

import com.revastudio.media.dto.TrackDto;
import com.revastudio.media.entity.Album;
import com.revastudio.media.entity.Artist;
import com.revastudio.media.entity.Track;
import com.revastudio.media.repository.AlbumRepository;
import com.revastudio.media.repository.ArtistRepository;
import com.revastudio.media.repository.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TrackServiceTest {

    @Mock
    private TrackRepository trackRepository;

    @Mock
    private AlbumRepository albumRepository;

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private TrackService trackService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTracksForCustomer_mapsToDto_correctly() {
        Track t = new Track();
        t.setTrackId(1);
        t.setName("Test Track");
        t.setAlbumId(10);
        t.setUnitPrice(1.99);

        when(trackRepository.findTracksPurchasedByCustomer(1)).thenReturn(List.of(t));

        Album album = new Album();
        album.setAlbumId(10);
        album.setTitle("Test Album");
        album.setArtistId(100);
        when(albumRepository.findById(10)).thenReturn(Optional.of(album));

        Artist artist = new Artist();
        artist.setArtistId(100);
        artist.setName("Test Artist");
        when(artistRepository.findById(100)).thenReturn(Optional.of(artist));

        List<TrackDto> dtos = trackService.getTracksForCustomer(1);

        assertEquals(1, dtos.size());
        TrackDto dto = dtos.get(0);
        assertEquals(1, dto.getTrackId());
        assertEquals("Test Track", dto.getName());
        assertEquals("Test Album", dto.getAlbumTitle());
        assertEquals("Test Artist", dto.getArtistName());
        assertEquals(1.99, dto.getUnitPrice());

        verify(trackRepository).findTracksPurchasedByCustomer(1);
    }
}
