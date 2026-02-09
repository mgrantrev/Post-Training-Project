package com.revastudio.media.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "track")
public class Track {

    @Id
    @Column(name = "track_id")
    private Integer trackId;

    private String name;

    @Column(name = "album_id")
    private Integer albumId;

    @Column(name = "composer")
    private String composer;

    @Column(name = "milliseconds")
    private Integer milliseconds;

    @Column(name = "bytes")
    private Integer bytes;

    @Column(name = "unit_price")
    private Double unitPrice;
}
