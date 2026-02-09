package com.revastudio.media.service;

import com.revastudio.media.dto.TrackDto;
import com.revastudio.media.entity.Album;
import com.revastudio.media.entity.Artist;
import com.revastudio.media.entity.Track;
import com.revastudio.media.repository.AlbumRepository;
import com.revastudio.media.repository.ArtistRepository;
import com.revastudio.media.repository.TrackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public TrackService(TrackRepository trackRepository, AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.trackRepository = trackRepository;
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    public List<TrackDto> getTracksForCustomer(Integer customerId) {
        List<Track> tracks = trackRepository.findTracksPurchasedByCustomer(customerId);
        return tracks.stream().map(t -> {
            TrackDto dto = new TrackDto();
            dto.setTrackId(t.getTrackId());
            dto.setName(t.getName());
            dto.setUnitPrice(t.getUnitPrice());

            Album album = null;
            if (t.getAlbumId() != null) album = albumRepository.findById(t.getAlbumId()).orElse(null);
            if (album != null) dto.setAlbumTitle(album.getTitle());

            if (album != null && album.getArtistId() != null) {
                Artist artist = artistRepository.findById(album.getArtistId()).orElse(null);
                if (artist != null) dto.setArtistName(artist.getName());
            }

            return dto;
        }).collect(Collectors.toList());
    }
}
