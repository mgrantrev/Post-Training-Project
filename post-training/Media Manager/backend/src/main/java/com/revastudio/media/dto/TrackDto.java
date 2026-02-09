package com.revastudio.media.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TrackDto {
    private Integer trackId;
    private String name;
    private String albumTitle;
    private String artistName;
    private Double unitPrice;
}
