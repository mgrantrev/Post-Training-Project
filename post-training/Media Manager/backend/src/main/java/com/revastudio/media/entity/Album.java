package com.revastudio.media.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "album")
public class Album {

    @Id
    @Column(name = "album_id")
    private Integer albumId;

    private String title;

    @Column(name = "artist_id")
    private Integer artistId;
}
